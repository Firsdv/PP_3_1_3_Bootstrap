package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entityes.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.security.Principal;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User findByPrincipal(Principal principal) {
        return findByEmail(principal.getName());
    }

    @Override
    @Transactional
    public User findByAuthentication(Authentication auth) {
        return findByEmail(auth.getName());
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return user;
    }

    @Override
    public boolean isEmailAlreadyExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    @Transactional
    public void createUserWithEncodedPassword(User user) {
        if (isEmailAlreadyExists(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUserWithPasswordHandling(User user) {
        User existingUser = findById(user.getId());
        if (existingUser == null) return;

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(existingUser.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(user);
    }

    @Override
    public User getByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    @Override
    public User findByPrincipalOrThrow(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new UsernameNotFoundException("No authenticated user");
        }
        User user = findByEmail(principal.getName());
        if (user == null) {
            throw new UsernameNotFoundException("User not found with name: " + principal.getName());
        }
        return user;
    }


}
