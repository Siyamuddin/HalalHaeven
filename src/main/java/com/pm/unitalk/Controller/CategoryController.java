package com.pm.unitalk.Controller;
import com.pm.unitalk.Configurations.AppConstants;
import com.pm.unitalk.DTOs.CategoryDto;
import com.pm.unitalk.Service.CategoryService;
import com.pm.unitalk.Utility.APIResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto)
    {
        CategoryDto createCategory=this.categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(createCategory, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Long id)
    {
        CategoryDto updatedCategory=this.categoryService.updateCategory(categoryDto,id);
        return new ResponseEntity<>(updatedCategory,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteCategory(@PathVariable Long id)
    {
        this.categoryService.deleteCategory(id);
        return new ResponseEntity<>(new APIResponse("The category has been deleted",true),HttpStatus.OK);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long id)
    {
        CategoryDto categoryDto=this.categoryService.getCategory(id);
        return new ResponseEntity<>(categoryDto,HttpStatus.OK);
    }
    @GetMapping("/get/all")
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
    @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
    @RequestParam(value = "sortBy",defaultValue = "categoryId",required = false) String sortBy,
    @RequestParam(value = "sortDirec",defaultValue = "dsc",required = false) String sortDirec)
    {
        log.info("The loading data recieved successfully");
        List<CategoryDto> categoryDtos=this.categoryService.getCategories(pageNumber,pageSize,sortBy,sortDirec);
        log.info("Data retrived successfully.");
        return new ResponseEntity<>(categoryDtos,HttpStatus.OK);
    }

}
