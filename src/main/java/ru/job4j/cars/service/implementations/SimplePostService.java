package ru.job4j.cars.service.implementations;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.ImageDto;
import ru.job4j.cars.dto.PostSearchDto;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.interfaces.PostRepository;
import ru.job4j.cars.service.interfaces.*;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class SimplePostService implements PostService {

    private final PostRepository postRepository;
    private final CarModelService carModelService;
    private final CarService carService;
    private final EngineService engineService;
    private final ImageService imageService;
    private final PriceHistoryService priceHistoryService;

    private final SessionFactory sessionFactory;

    @Override
    public Optional<Post> save(Post post, ImageDto imageDto) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Image newImage = null;
        try {
            setBrand(post.getCar());
            setEngine(post.getCar());
            addPriceHistory(post);
            if (imageDto.getContent().length != 0) {
                newImage = imageService.saveImage(imageDto);
                post.setImage(newImage);
            }
            Post savedPost = (Post) session.merge(post);
            transaction.commit();
            return Optional.of(savedPost);
        } catch (Exception e) {
            transaction.rollback();
            if (newImage != null) {
                imageService.deleteImage(newImage);
            }
            throw e;
        }
    }

    @Override
    public Optional<Post> findById(int id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> findAllNotSold() {
        return postRepository.findAllNotSold();
    }

    @Override
    public List<Post> findAllByUserId(int id) {
        return postRepository.findAllByUserId(id);
    }

    @Override
    public boolean update(Post post, ImageDto imageDto) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Image newImage = null;
        try {
            setBrand(post.getCar());
            setEngine(post.getCar());
            Set<PriceHistory> priceHistories = priceHistoryService.getPriceHistoriesByPostId(post.getId());
            PriceHistory lastPrice = priceHistories.stream()
                    .max(Comparator.comparing(PriceHistory::getDate))
                    .orElse(null);

            if (!Objects.equals(post.getPrice(), lastPrice != null ? lastPrice.getPrice() : null)) {
                addPriceHistory(post);
            }
            if (hasValidImage(imageDto)) {
                newImage = imageService.saveImage(imageDto);
                post.setImage(newImage);
            } else if (post.getImage() == null) {
                post.setImage(null);
            }
            session.merge(post);
            transaction.commit();
            if (newImage != null && post.getImage() != null && !post.getImage().equals(newImage)) {
                imageService.deleteImage(post.getImage());
            }
            return true;
        } catch (Exception e) {
            transaction.rollback();
            if (newImage != null) {
                imageService.deleteImage(newImage);
            }
            throw new RuntimeException("Ошибка при обновлении поста с ID: " + post.getId(), e);
        }
    }

    private boolean hasValidImage(ImageDto imageDto) {
        return imageDto != null && imageDto.getContent() != null && imageDto.getContent().length > 0;
    }

    @Override
    public void deleteAllByUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        try {
            List<Post> posts = postRepository.findAllByUserId(user.getId());
            postRepository.deleteAllByUser(user);
            posts.forEach(this::deletePostsImage);
            posts.forEach(p -> carService.delete(p.getCar()));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<Post> findAllByCriteria(PostSearchDto searchDto) {
        return postRepository.findAllByCriteria(
                searchDto.getCar(),
                searchDto.isImageExists(),
                searchDto.getPostCreatedBeforeDays(),
                searchDto.getLowestPrice(),
                searchDto.getHighestPrice());
    }

    private Car setBrand(Car car) {
        CarModel carModel = carModelService.getById(car.getCarModel().getId());
        Brand brand = new Brand();
        brand.setId(carModel.getBrandId());
        car.setBrand(brand);
        return car;
    }

    private Car setEngine(Car car) {
        Optional<Engine> engineOptional = engineService.findByFuelTypeAndSize(
                car.getEngine().getFuelType(), car.getEngine().getEngineSize());
        engineOptional.ifPresentOrElse(
                car::setEngine,
                () -> {
                    Engine engine = engineService.save(car.getEngine());
                    car.setEngine(engine);
                });
        return car;
    }

    private void deletePostsImage(Post post) {
        Optional<Image> postImage = post.getImage() != null
                ? imageService.getImageById(post.getImage().getId()) : Optional.empty();
        postImage.ifPresent(imageService::deleteImage);
    }

    private Post addPriceHistory(Post post) {
        PriceHistory newPriceHistory = new PriceHistory();
        newPriceHistory.setPrice(post.getPrice());
        post.getPriceHistories().add(newPriceHistory);
        return post;
    }
}
