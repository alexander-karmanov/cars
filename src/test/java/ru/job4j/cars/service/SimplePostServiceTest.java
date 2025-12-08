package ru.job4j.cars.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.job4j.cars.dto.ImageDto;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SimplePostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private CarModelService carModelService;

    @Mock
    private CarService carService;

    @Mock
    private EngineService engineService;

    @Mock
    private ImageService imageService;

    @Mock
    private PriceHistoryService priceHistoryService;

    private SimplePostService simplePostService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        simplePostService = new SimplePostService(
                postRepository, carModelService, carService, engineService,
                imageService, priceHistoryService
        );
    }

    @Test
    void whenSavePostWithImageThenSavesImageAndPostSuccessfully() {
        Post post = new Post();
        Car car = new Car();
        CarModel carModel = new CarModel();
        carModel.setId(1);
        carModel.setName("Test Model");
        carModel.setBrandId(1);
        Engine engine = new Engine();
        engine.setId(1);
        car.setCarModel(carModel);
        car.setEngine(engine);
        post.setCar(car);
        ImageDto imageDto = new ImageDto();
        imageDto.setContent(new byte[]{1, 2, 3});
        Image newImage = new Image();
        when(imageService.saveImage(imageDto)).thenReturn(newImage);
        Optional<Post> savedPost = Optional.of(post);
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);
        when(carModelService.getById(1)).thenReturn(carModel);

        Optional<Post> result = simplePostService.save(post, imageDto);

        assertEquals(savedPost, result);
        verify(imageService).saveImage(imageDto);
        verify(postRepository).save(argThat(saved -> saved.getImage() == newImage));
        verify(imageService, never()).deleteImage(any(Image.class));
    }

    @Test
    void whenSavePostWithImageButSaveFailsThenDeletesImage() {
        Post post = new Post();
        Car car = new Car();
        CarModel carModel = new CarModel();
        carModel.setId(1);
        carModel.setName("Test Model");
        carModel.setBrandId(1);
        Engine engine = new Engine();
        engine.setId(1);
        car.setCarModel(carModel);
        car.setEngine(engine);
        post.setCar(car);
        ImageDto imageDto = new ImageDto();
        imageDto.setContent(new byte[]{1, 2, 3});
        Image newImage = new Image();
        when(imageService.saveImage(imageDto)).thenReturn(newImage);
        Optional<Post> emptyPost = Optional.empty();
        when(postRepository.save(any(Post.class))).thenReturn(emptyPost);
        when(carModelService.getById(1)).thenReturn(carModel);

        Optional<Post> result = simplePostService.save(post, imageDto);

        assertEquals(emptyPost, result);
        verify(imageService).saveImage(imageDto);
        verify(postRepository).save(argThat(saved -> saved.getImage() == newImage));
        verify(imageService).deleteImage(newImage);
    }

    @Test
    void whenUpdatePostWithImageThenUpdatesImageAndPostSuccessfully() {
        Post post = new Post();
        post.setId(1);
        post.setPrice(1000);
        Car car = new Car();
        CarModel carModel = new CarModel();
        carModel.setId(1);
        carModel.setName("Test Model");
        carModel.setBrandId(1);
        Engine engine = new Engine();
        engine.setId(1);
        car.setCarModel(carModel);
        car.setEngine(engine);
        post.setCar(car);
        ImageDto imageDto = new ImageDto();
        imageDto.setContent(new byte[]{1, 2, 3});
        Image newImage = new Image();
        Set<PriceHistory> priceHistories = new HashSet<>(Set.of(new PriceHistory()));
        when(priceHistoryService.getPriceHistoriesByPostId(1)).thenReturn(priceHistories);
        when(imageService.saveImage(imageDto)).thenReturn(newImage);
        when(carModelService.getById(1)).thenReturn(carModel);
        when(postRepository.update(any(Post.class))).thenReturn(true);

        boolean result = simplePostService.update(post, imageDto);

        assertTrue(result);
        verify(priceHistoryService).getPriceHistoriesByPostId(1);
        verify(imageService).saveImage(imageDto);
        verify(postRepository).update(argThat(updated -> updated.getImage() == newImage));
        verify(imageService, never()).deleteImage(any(Image.class));
    }

    @Test
    void whenUpdatePostWithImageButUpdateFailsThenDeletesNewImage() {
        Post post = new Post();
        post.setId(1);
        post.setPrice(1000);
        Car car = new Car();
        CarModel carModel = new CarModel();
        carModel.setId(1);
        carModel.setName("Test Model");
        carModel.setBrandId(1);
        Engine engine = new Engine();
        engine.setId(1);
        car.setCarModel(carModel);
        car.setEngine(engine);
        post.setCar(car);
        ImageDto imageDto = new ImageDto();
        imageDto.setContent(new byte[]{1, 2, 3});
        Image newImage = new Image();
        Set<PriceHistory> priceHistories = new HashSet<>(Set.of(new PriceHistory()));
        when(priceHistoryService.getPriceHistoriesByPostId(1)).thenReturn(priceHistories);
        when(imageService.saveImage(imageDto)).thenReturn(newImage);
        when(carModelService.getById(1)).thenReturn(carModel);
        when(postRepository.update(any(Post.class))).thenReturn(false);

        boolean result = simplePostService.update(post, imageDto);

        assertFalse(result);
        verify(priceHistoryService).getPriceHistoriesByPostId(1);
        verify(imageService).saveImage(imageDto);
        verify(postRepository).update(argThat(updated -> updated.getImage() == newImage));
        verify(imageService).deleteImage(newImage);
    }

    @Test
    void whenUpdatePostWithoutImageButPriceChangedThenUpdatesSuccessfully() {
        Post post = new Post();
        post.setId(1);
        post.setPrice(1500);
        Car car = new Car();
        CarModel carModel = new CarModel();
        carModel.setId(1);
        carModel.setName("Test Model");
        carModel.setBrandId(1);
        Engine engine = new Engine();
        engine.setId(1);
        car.setCarModel(carModel);
        car.setEngine(engine);
        post.setCar(car);
        ImageDto imageDto = new ImageDto();
        imageDto.setContent(new byte[0]);
        PriceHistory oldPriceHistory = new PriceHistory();
        oldPriceHistory.setPrice(1000);
        oldPriceHistory.setDate(LocalDateTime.now().minusSeconds(1));
        Set<PriceHistory> priceHistories = new HashSet<>(Set.of(oldPriceHistory));
        when(priceHistoryService.getPriceHistoriesByPostId(1)).thenReturn(priceHistories);
        when(carModelService.getById(1)).thenReturn(carModel);
        when(postRepository.update(post)).thenReturn(true);

        boolean result = simplePostService.update(post, imageDto);

        assertTrue(result);
        verify(priceHistoryService).getPriceHistoriesByPostId(1);
        verify(imageService, never()).saveImage(any(ImageDto.class));
        verify(postRepository).update(post);
        verify(imageService, never()).deleteImage(any(Image.class));
    }
}
