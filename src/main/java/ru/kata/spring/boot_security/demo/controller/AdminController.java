package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entityes.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showUserList(@RequestParam(value = "editId", required = false) Long editId,
                               @RequestParam(value = "deleteId", required = false) Long deleteId,
                               Model model, Principal principal) {

        User authUser = userService.findByPrincipalOrThrow(principal);
        model.addAttribute("authUser", authUser);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("allRoles", roleService.getRoles());

        if (editId != null) {
            model.addAttribute("editUser", userService.getByIdOrThrow(editId));
        }

        if (deleteId != null) {
            model.addAttribute("deleteUser", userService.getByIdOrThrow(deleteId));
        }

        return "admin";
    }

    @GetMapping("/new")
    public String createUser(Model model, Principal principal) {
        User authUser = userService.findByPrincipalOrThrow(principal);
        model.addAttribute("authUser", authUser);
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getRoles());
        return "new";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model, Principal principal) {
        User authUser = userService.findByPrincipalOrThrow(principal);
        model.addAttribute("authUser", authUser);
        model.addAttribute("editUser", userService.getByIdOrThrow(id));
        model.addAttribute("allRoles", roleService.getRoles());
        return "admin";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult result) {
        if (userService.isEmailAlreadyExists(user.getEmail())) {
            result.rejectValue("email", "error.user", "Email already exists");
            return "new";
        }
        if (result.hasErrors()) {
            return "new";
        }
        userService.createUserWithEncodedPassword(user);
        return "redirect:/admin";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("editUser") User user) {
        userService.updateUserWithPasswordHandling(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}