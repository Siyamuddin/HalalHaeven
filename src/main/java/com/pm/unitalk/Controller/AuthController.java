package com.pm.unitalk.Controller;

import com.pm.unitalk.DTOs.UserDTO;
import com.pm.unitalk.Exceptions.ResourceAlreadyExist;
import com.pm.unitalk.Service.CategoryService;
import com.pm.unitalk.Service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserServices userServices;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("categories", categoryService.getCategories(0, 10, "categoryTitle", "asc"));
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("categories", categoryService.getCategories(0, 10, "categoryTitle", "asc"));
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDTO userDTO, RedirectAttributes redirectAttributes) {
        try {
            // Set role to ADMIN for the first user, otherwise USER
            if (userServices.getAllUser(0, 1, "id", "asc").isEmpty()) {
                userDTO.setRole("ADMIN");
            } else {
                userDTO.setRole("USER");
            }

            UserDTO registeredUser = userServices.registerUser(userDTO);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (ResourceAlreadyExist e) {
            redirectAttributes.addFlashAttribute("error", "Email already exists. Please use a different email.");
            return "redirect:/register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred during registration. Please try again.");
            return "redirect:/register";
        }
    }

    // Add a method to create an admin user for testing
    @GetMapping("/create-admin")
    public String createAdminUser(RedirectAttributes redirectAttributes) {
        try {
            UserDTO adminUser = new UserDTO();
            adminUser.setFirstName("Halal");
            adminUser.setLastName("Haven");
            adminUser.setEmail("siyamuddin177@gmail.com");
            adminUser.setPassword("halalhaven638");
            adminUser.setRole("ADMIN");

            userServices.registerUser(adminUser);
            redirectAttributes.addFlashAttribute("success", "Admin user created successfully!");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create admin user: " + e.getMessage());
            return "redirect:/login";
        }
    }
}
