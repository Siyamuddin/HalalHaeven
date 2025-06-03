package com.pm.unitalk.Controller;

import com.pm.unitalk.Configurations.AppConstants;
import com.pm.unitalk.DTOs.CategoryDto;
import com.pm.unitalk.DTOs.ProductDTO;
import com.pm.unitalk.Service.CategoryService;
import com.pm.unitalk.Service.ProductService;
import com.pm.unitalk.Service.UserServices;
import com.pm.unitalk.Utility.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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

    // Product Management
    @GetMapping("/products")
    public String listProducts(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDirec", defaultValue = "desc", required = false) String sortDirec,
            Model model) {

        // Get all products with pagination
        PostResponse postResponse = productService.getAllPost(pageNumber, pageSize, sortBy, sortDirec);

        // Add categories for the navigation
        model.addAttribute("categories", categoryService.getCategories(0, 10, "categoryTitle", "asc"));
        model.addAttribute("products", postResponse.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", postResponse.getTotalPages());
        model.addAttribute("totalItems", postResponse.getTotalElements());
        model.addAttribute("pageSize", pageSize);

        return "admin/products";
    }

    @GetMapping("/products/new")
    public String showProductForm(Model model) {
        // Add categories for the navigation and dropdown
        List<CategoryDto> categories = categoryService.getCategories(0, 100, "categoryTitle", "asc");

        model.addAttribute("categories", categories);
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("isEdit", false);

        return "admin/product-form";
    }

    @PostMapping("/products")
    public String createProduct(
            @ModelAttribute ProductDTO productDTO,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("productImage") MultipartFile productImage,
            RedirectAttributes redirectAttributes) {

        try {
            // Get current user (admin)
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            Long userId = userServices.findUserByEmail(email).getId();

            // Create the product
            ProductDTO createdProduct = productService.createPost(productDTO, userId, categoryId);

            // Upload image if provided
            if (!productImage.isEmpty()) {
                productService.uploadImage(createdProduct.getId(), productImage);
            }

            redirectAttributes.addFlashAttribute("success", "Product created successfully!");
            return "redirect:/admin/products";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating product: " + e.getMessage());
            return "redirect:/admin/products/new";
        }
    }

    @GetMapping("/products/{productId}/edit")
    public String showEditProductForm(@PathVariable Long productId, Model model) {
        // Get the product
        ProductDTO product = productService.getPost(productId);

        // Add categories for the navigation and dropdown
        List<CategoryDto> categories = categoryService.getCategories(0, 100, "categoryTitle", "asc");

        model.addAttribute("categories", categories);
        model.addAttribute("product", product);
        model.addAttribute("isEdit", true);

        return "admin/product-form";
    }

    @PostMapping("/products/{productId}")
    public String updateProduct(
            @PathVariable Long productId,
            @ModelAttribute ProductDTO productDTO,
            @RequestParam(value = "productImage", required = false) MultipartFile productImage,
            RedirectAttributes redirectAttributes) {

        try {
            // Set the ID to ensure we're updating the right product
            productDTO.setId(productId);

            // Update the product
            ProductDTO updatedProduct = productService.updatePost(productDTO, productId);

            // Update image if provided
            if (productImage != null && !productImage.isEmpty()) {
                productService.changeImage(productId, productImage);
            }

            redirectAttributes.addFlashAttribute("success", "Product updated successfully!");
            return "redirect:/admin/products";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating product: " + e.getMessage());
            return "redirect:/admin/products/" + productId + "/edit";
        }
    }

    @GetMapping("/products/{productId}/delete")
    public String deleteProduct(@PathVariable Long productId, RedirectAttributes redirectAttributes) {
        try {
            productService.deletePost(productId);
            redirectAttributes.addFlashAttribute("success", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting product: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }
}
