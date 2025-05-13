package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entityes.Role;

import java.util.List;

public interface RoleService {

    List<Role> getRoles();

    Role findById(Long id);

    Role findByName(String name);

    void addRole(Role role);

    void deleteRole(Long id);
}