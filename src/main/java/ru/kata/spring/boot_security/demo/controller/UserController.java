package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.entityes.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;


@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showUser (Model model, Principal principal) {
        // Получаем пользователя по principal
        User user = userService.findByPrincipal(principal);

        // Если пользователь не найден, перенаправляем на страницу входа
        if (user == null) {
            return "redirect:/login"; // Или на другую страницу, если это необходимо
        }

        // Добавляем пользователя в модель
        model.addAttribute("user", user);
        return "user"; // Возвращаем имя шаблона для страницы пользователя
    }
}

