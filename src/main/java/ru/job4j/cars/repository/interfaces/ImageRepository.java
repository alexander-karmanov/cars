package ru.job4j.cars.repository.interfaces;

import ru.job4j.cars.model.Image;

import java.util.Optional;

public interface ImageRepository {

    Image getDefaultImage();

    Optional<Image> findById(int id);

    void delete(Image image);

}