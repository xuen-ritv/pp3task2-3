package web.controller;


import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    @Autowired
    public AdminController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    // Регистрация нового пользователя
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // register.html
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        if(userService.getUserByUsername(user.getUsername()) != null) {
            System.out.println("Admin already exists");
            return "redirect:/login";
        }
        else {
            userService.createAdmin(user);
            return "redirect:/login";
        }
    }

    // Отображение списка пользователей
    @GetMapping("/admin")
    public String listUsers(Model model, @AuthenticationPrincipal User user) {
        if (user.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("users", userService.getAllUsers());
        } else {
            model.addAttribute("user", user);
        }
        return "admin"; // admin.html
    }
    @PostMapping("/admin")
    public String addUser(@ModelAttribute User user, @RequestParam String roleIds) {
        List<Role> roles = Arrays.stream(roleIds.split(","))
                .map(String::trim)
                .map(Long::parseLong) // Преобразуем строку в long
                .map(roleService::getRoleById) // Получаем роль из базы данных
                .collect(Collectors.toList());

        user.setRoles(roles);

        userService.createUser(user);
        return "redirect:/admin";
    }

    // Отображение формы добавления пользователя
    @GetMapping("/admin/new")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "addUser"; // addUser.html
    }

    // Отображение формы редактирования пользователя
    @GetMapping("/admin/edit/{id}")
    public String showEditUserForm(@PathVariable Integer id, Model model) {
        User user = userService.getById(Long.valueOf(id));
        List<String> roles = user.getRoles().stream().map(Role::getName).toList();
        model.addAttribute("user", user);
        model.addAttribute("roles", String.join(", ", roles));
        return "editUser"; // editUser.html
    }
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }



    // Обновление пользователя
    @PostMapping("/admin/edit/{id}")
    public String updateUser(@PathVariable Integer id, @ModelAttribute User user, @RequestParam String roleIds) {
        List<Role> roles = Arrays.stream(roleIds.split(","))
                .map(String::trim)
                .map(Long::parseLong) // Преобразуем строку в long blyat
                .map(roleService::getRoleById) // Получаем роль из базы данных
                .collect(Collectors.toList());

        user.setRoles(roles);

        userService.updateUser(user);
        return "redirect:/admin";
    }

    // Удаление пользователя
    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return "redirect:/admin"; // Обратите внимание на путь
    }
}