package ru.job4j.cars.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.ImageDto;
import ru.job4j.cars.dto.PostSearchDto;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.service.interfaces.*;
import ru.job4j.cars.utilities.SearchValidator;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PostControllerTest {
    private PostService postService;
    private BodyService bodyService;
    private BrandService brandService;
    private CarModelService carModelService;
    private CategoryService categoryService;
    private ColorService colorService;
    private EngineSizeService engineSizeService;
    private FuelTypeService fuelTypeService;
    private TransmissionService transmissionService;
    private SearchValidator searchValidator;
    private PostController postController;

    @BeforeEach
    public void setUp() {
        postService = Mockito.mock(PostService.class);
        bodyService = Mockito.mock(BodyService.class);
        brandService = Mockito.mock(BrandService.class);
        carModelService = Mockito.mock(CarModelService.class);
        categoryService = Mockito.mock(CategoryService.class);
        colorService = Mockito.mock(ColorService.class);
        engineSizeService = Mockito.mock(EngineSizeService.class);
        fuelTypeService = Mockito.mock(FuelTypeService.class);
        transmissionService = Mockito.mock(TransmissionService.class);
        searchValidator = Mockito.mock(SearchValidator.class);
        postController = new PostController(
                postService, bodyService, brandService, carModelService, categoryService,
                colorService, engineSizeService, fuelTypeService, transmissionService, searchValidator
        );
    }

    @Test
    void whenGetCreationPageThenSuccess() {
        Model model = Mockito.mock(Model.class);
        String viewName = postController.getCreationPage(model);
        assertEquals("posts/create", viewName);
    }

    @Test
    void whenGetAllPostsWithSearchActiveThenSuccess() {
        Model model = Mockito.mock(Model.class);
        PostSearchDto searchDto = new PostSearchDto();
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        List<Post> filteredPosts = List.of(new Post());
        when(postService.findAllByCriteria(searchDto)).thenReturn(filteredPosts);

        String viewName = postController.getAllPosts(model, searchDto, bindingResult);

        assertEquals("posts/list", viewName);
        verify(searchValidator).validate(searchDto, bindingResult);
        verify(postService).findAllByCriteria(searchDto);
        verify(model).addAttribute("posts", filteredPosts);
    }

    @Test
    void whenGetByIdWithPostFoundThenSuccess() {
        Model model = Mockito.mock(Model.class);
        int postId = 1;
        Post testPost = new Post();
        when(postService.findById(postId)).thenReturn(Optional.of(testPost));

        String viewName = postController.getById(model, postId);

        assertEquals("posts/one", viewName);
        verify(postService).findById(postId);
        verify(model).addAttribute("post", testPost);
        verify(model, never()).addAttribute(eq("message"), anyString());
    }

    @Test
    void whenCreatePostWithSuccessThenRedirect() throws IOException {
        Model model = Mockito.mock(Model.class);
        Post post = new Post();
        MultipartFile file = Mockito.mock(MultipartFile.class);
        String filename = "test.jpg";
        byte[] fileBytes = "test data".getBytes();
        when(file.getOriginalFilename()).thenReturn(filename);
        when(file.getBytes()).thenReturn(fileBytes);
        Post savedPost = new Post();
        when(postService.save(post, new ImageDto(filename, fileBytes))).thenReturn(Optional.of(savedPost));

        String viewName = postController.createPost(post, file, model);

        assertEquals("redirect:/posts/all", viewName);
        verify(postService).save(post, new ImageDto(filename, fileBytes));
        verifyNoInteractions(model);
    }

    @Test
    void whenCreatePostWithErrorThenErrorPage() throws IOException {
        Model model = Mockito.mock(Model.class);
        Post post = new Post();
        MultipartFile file = Mockito.mock(MultipartFile.class);
        String filename = "test.jpg";
        byte[] fileBytes = "test data".getBytes();
        when(file.getOriginalFilename()).thenReturn(filename);
        when(file.getBytes()).thenReturn(fileBytes);
        when(postService.save(post, new ImageDto(filename, fileBytes))).thenReturn(Optional.empty());
        String viewName = postController.createPost(post, file, model);
        assertEquals("errors/404", viewName);
        verify(postService).save(post, new ImageDto(filename, fileBytes));
        verify(model).addAttribute(eq("message"), anyString());
    }
}
