package ru.job4j.cars.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.PostService;
import ru.job4j.cars.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

public class UserControllerTest {
    private UserService userService;
    private PostService postService;
    private UserController userController;

    @BeforeEach
    public void setUp() {
        userService = Mockito.mock(UserService.class);
        postService = Mockito.mock(PostService.class);
        userController = new UserController(userService, postService);
    }

    @Test
    void whenGetRegistrationPageThenSuccess() {
        UserController userController = new UserController(userService, postService);
        String viewName = userController.getRegistrationPage();
        assertEquals("users/register", viewName);
    }

    @Test
    void whenLogoutThenSuccess() {
        HttpSession session = Mockito.mock(HttpSession.class);
        UserController userController = new UserController(userService, postService);
        String result = userController.logout(session);
        Mockito.verify(session).invalidate();
        assertEquals("redirect:/", result);
    }

    @Test
    void whenGetUpdatePageThenModelContainsUserAndReturnsCorrectView() {
        HttpSession session = Mockito.mock(HttpSession.class);
        User user = new User();
        Mockito.when(session.getAttribute("user")).thenReturn(user);
        UserController userController = new UserController(userService, postService);
        Model model = Mockito.mock(Model.class);
        String viewName = userController.getUpdatePage(model, session);
        Mockito.verify(model).addAttribute("user", user);
        assertEquals("users/update", viewName);
    }

    @Test
    void whenInvalidLoginThenReturnLoginPageWithError() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(userService.findByEmailAndPassword(anyString(), anyString()))
                .thenReturn(Optional.empty());
        Model model = Mockito.mock(Model.class);
        User userInput = new User();
        userInput.setEmail("test@example.com");
        userInput.setPassword("password");
        UserController userController = new UserController(userService, postService);
        String result = userController.loginUser(userInput, model, request);
        Mockito.verify(model).addAttribute(Mockito.eq("error"), Mockito.anyString());
        assertEquals("users/login", result);
    }

    @Test
    void whenDeleteUserWhenPasswordMatchesThenDeleteAndRedirect() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("correctPassword");
        HttpSession session = Mockito.mock(HttpSession.class);
        Mockito.when(session.getAttribute("user")).thenReturn(user);
        Model model = Mockito.mock(Model.class);
        String password = "correctPassword";
        String result = userController.deleteUser(password, model, session);
        Mockito.verify(session).invalidate();
        Mockito.verify(postService).deleteAllByUser(user);
        Mockito.verify(userService).deleteByEmailAndPassword(user.getEmail(), user.getPassword());
        assertEquals("redirect:/", result);
    }
}
