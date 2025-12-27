package ru.job4j.cars.service.interfaces;

import ru.job4j.cars.dto.ImageDto;
import ru.job4j.cars.model.Image;

import java.util.Optional;

public interface ImageService {

    ImageDto getDefaultImageDto();

    ImageDto getImageDtoById(int id);

    Optional<Image> getImageById(int id);

    Image saveImage(ImageDto imageDto);

    void deleteImage(Image image);

}