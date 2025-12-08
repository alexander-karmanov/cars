package ru.job4j.cars.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.job4j.cars.dto.ImageDto;
import ru.job4j.cars.model.Image;
import ru.job4j.cars.repository.ImageRepository;
import ru.job4j.cars.utilities.ImageUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SimpleImageServiceTest {
    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImageUtil imageUtil;

    private SimpleImageService simpleImageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        simpleImageService = new SimpleImageService(imageRepository, imageUtil);
    }

    @Test
    void whenGetDefaultImageDtoThenReturnsCorrectDto() {
        Image defaultImage = new Image();
        ImageDto expectedImageDto = new ImageDto();
        when(imageRepository.getDefaultImage()).thenReturn(defaultImage);
        when(imageUtil.getImageDto(defaultImage)).thenReturn(expectedImageDto);
        ImageDto result = simpleImageService.getDefaultImageDto();
        assertEquals(expectedImageDto, result);
    }

    @Test
    void whenSaveImageThenReturnsSavedImage() {
        ImageDto imageDto = new ImageDto();
        Image expectedImage = new Image();
        when(imageUtil.saveImage(imageDto)).thenReturn(expectedImage);
        Image result = simpleImageService.saveImage(imageDto);
        assertEquals(expectedImage, result);
        verify(imageUtil).saveImage(imageDto);
    }

    @Test
    void whenDeleteImageThenDeletesFromRepositoryAndUtil() {
        Image image = new Image();
        simpleImageService.deleteImage(image);
        verify(imageRepository).delete(image);
        verify(imageUtil).deleteImage(image);
    }
}
