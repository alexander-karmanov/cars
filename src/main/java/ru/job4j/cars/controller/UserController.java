package ru.job4j.cars.controller;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.interfaces.PostService;
import ru.job4j.cars.service.interfaces.UserService;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final PostService postService;

    @GetMapping("/register")
    public String getRegistrationPage() {
        return "users/register";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/update")
    public String getUpdatePage(Model model, HttpSession session) {
        model.addAttribute("user", session.getAttribute("user"));
        return "users/update";
    }

    @GetMapping("/posts")
    public String getUsersPosts(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("posts", postService.findAllByUserId(user.getId()));
        return "users/posts";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        Optional<User> savedUser = userService.save(user);
        if (savedUser.isEmpty()) {
            model.addAttribute("error", "Пользователь с таким email уже существует");
            return "users/register";
        }
        return "redirect:/users/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model, HttpServletRequest request) {
        Optional<User> foundUser = userService.findByEmailAndPassword(
                user.getEmail(), user.getPassword());
        if (foundUser.isEmpty()) {
            model.addAttribute("error", "Email или пароль введены не верно!");
            return "users/login";
        }
        HttpSession session = request.getSession();
        session.setAttribute("user", foundUser.get());
        return "redirect:/";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user, Model model, HttpSession session) {
        User logonUser = (User) session.getAttribute("user");
        user.setEmail(logonUser.getEmail());
        user.setPassword(logonUser.getPassword());
        boolean updated =  userService.update(user);
        if (!updated) {
            model.addAttribute("error", "Ошибка при обновлении данных пользователя.");
            model.addAttribute("user", session.getAttribute("user"));
            return "users/update";
        }
        session.invalidate();
        return "redirect:/users/login";
    }

    @PostMapping("/delete")
    public String deleteUser(Model model, HttpSession session) {
        User actualUser = (User) session.getAttribute("user");
        session.invalidate();
        postService.deleteAllByUser(actualUser);
        userService.deleteByEmailAndPassword(
                actualUser.getEmail(),
                actualUser.getPassword()
        );
        return "redirect:/";
    }
}
