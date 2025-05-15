package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.Authentication;
import ru.kata.spring.boot_security.demo.entityes.User;

import java.security.Principal;
import java.util.List;

public interface UserService {

    void save(User user);

    User findById(Long userId);

    List<User> findAll();

    void update(User user);

    void delete(Long userId);

    User findByEmail(String email);

    User findByPrincipal(Principal principal);

    User findByAuthentication(Authentication auth);

    boolean isEmailAlreadyExists(String email);

    void createUserWithEncodedPassword(User user);

    void updateUserWithPasswordHandling(User user);

    User findByPrincipalOrThrow(Principal principal);

    User getByIdOrThrow(Long id);
}
