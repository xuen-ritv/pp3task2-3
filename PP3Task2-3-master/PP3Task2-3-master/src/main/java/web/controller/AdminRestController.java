package web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.Role;
import web.model.User;
import web.service.RoleServiceImpl;
import web.service.UserServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminRestController {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    @Autowired
    public AdminRestController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@ModelAttribute("user") User user) {
        return user != null
                ? new ResponseEntity<>(user, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Отображение списка пользователей
    @GetMapping(value = "/admin")
    public ResponseEntity<List<User>> listUsers() {
        final List<User> users = userService.getAllUsers();

        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


//    @PostMapping(value = "/admin/new")
//    public ResponseEntity<?> addUser(@RequestBody User user, @RequestParam String roleIds) {
//        List<Role> roles = Arrays.stream(roleIds.split(","))
//                .map(String::trim)
//                .map(Long::parseLong)
//                .map(roleService::getRoleById)
//                .collect(Collectors.toList());
//
//        user.setRoles(roles);
//
//        userService.createUser(user);
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }

    @PostMapping(value = "/admin/new", consumes = "multipart/form-data")
    public ResponseEntity<?> addUser(
            @RequestParam("roleIds") String roleIds,   // Роли как строка
            @ModelAttribute User user)
    {
        List<Role> roles = Arrays.stream(roleIds.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .map(roleService::getRoleById)
                .collect(Collectors.toList());

        user.setRoles(roles);

        userService.createUser(user);

        return ResponseEntity.status(201).body("User created successfully");
    }

    // Обновление пользователя
    @PutMapping("/admin/edit/{id}")
    public ResponseEntity<User> updateUser(@ModelAttribute User user, @RequestParam String roleIds) {
        List<Role> roles = Arrays.stream(roleIds.split(","))
                .map(String::trim)
                .map(Long::parseLong) // Преобразуем строку в long blyat
                .map(roleService::getRoleById) // Получаем роль из базы данных
                .collect(Collectors.toList());

        user.setRoles(roles);

        userService.updateUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Удаление пользователя
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK); // Обратите внимание на путь
    }
}