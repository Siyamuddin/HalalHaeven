package com.pm.unitalk.Controller;

import com.pm.unitalk.Service.CategoryService;
import com.pm.unitalk.Service.ProductService;
import com.pm.unitalk.Service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserServices userServices;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ProductService productService;
    
    @GetMapping
    public String adminDashboard(Model model) {
        // Add categories for the navigation
        model.addAttribute("categories", categoryService.getCategories(0, 10, "categoryTitle", "asc"));
        
        // Add user count
        model.addAttribute("userCount", userServices.getAllUser(0, 1000, "id", "asc").size());
        
        // Add product count
        model.addAttribute("productCount", productService.getAllPost(0, 1, "id", "asc").getTotalElements());
        
        // Add category count
        model.addAttribute("categoryCount", categoryService.getCategories(0, 1000, "categoryId", "asc").size());
        
        return "admin/dashboard";
    }
}