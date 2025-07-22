package com.pm.unitalk.Controller;

import com.pm.unitalk.Configurations.AppConstants;
import com.pm.unitalk.DTOs.ProductDTO;
import com.pm.unitalk.Exceptions.ResourceNotFoundException;
import com.pm.unitalk.Service.CategoryService;
import com.pm.unitalk.Service.ProductService;
import com.pm.unitalk.Utility.PostResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
@Slf4j
@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDirec", defaultValue = "dsc", required = false) String sortDirec,
            Model model) {

        // Get all products with pagination
        PostResponse postResponse = productService.getAllPost(pageNumber, pageSize, sortBy, sortDirec);

        // Get all categories for the sidebar
        model.addAttribute("categories", categoryService.getCategories(0, 100, "categoryId", "dsc"));
        model.addAttribute("products", postResponse.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", postResponse.getTotalPages());
        model.addAttribute("totalItems", postResponse.getTotalElements());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("title", "All Products");

        return "index";
    }

    @GetMapping("/category/{categoryId}")
    public String getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDirec", defaultValue = "dsc", required = false) String sortDirec,
            Model model) {

        // Get products by category with pagination
        PostResponse postResponse = productService.getPostByCategory(categoryId, pageNumber, pageSize, sortBy, sortDirec);

        // Get category name
        String categoryTitle = categoryService.getCategory(categoryId).getCategoryTitle();

        // Get all categories for the sidebar
        model.addAttribute("categories", categoryService.getCategories(0, 100, "categoryId", "asc"));
        model.addAttribute("products", postResponse.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", postResponse.getTotalPages());
        model.addAttribute("totalItems", postResponse.getTotalElements());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("title", categoryTitle + " Products");

        return "index";
    }

    @GetMapping("/search")
    public String searchProducts(
            @RequestParam("keyword") String keyword,
            Model model) {

        // Search products by keyword
        model.addAttribute("products", productService.searchPost(keyword));

        // Get all categories for the sidebar
        model.addAttribute("categories", categoryService.getCategories(0, 100, "categoryId", "asc"));
        model.addAttribute("title", "Search Results for: " + keyword);

        return "index";
    }

    @GetMapping("/product/{productId}")
    public String getProductDetail(
            @PathVariable Long productId,
            Model model) {

        try {
            // Get product details
            ProductDTO product = productService.getPost(productId);

            // Get all categories for the sidebar
            model.addAttribute("categories", categoryService.getCategories(0, 100, "categoryId", "asc"));
            model.addAttribute("product", product);
            model.addAttribute("title", product.getProductName());

            return "product-detail";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("errorMessage", "Product not found");
            return "redirect:/";
        }
    }
}
