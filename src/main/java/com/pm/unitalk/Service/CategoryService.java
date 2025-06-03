package com.pm.unitalk.Service;

import com.pm.unitalk.DTOs.CategoryDto;

import java.util.List;

public interface CategoryService
{
    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);
    public void deleteCategory(Long id);
    CategoryDto getCategory(Long categoryId);
    List<CategoryDto> getCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortDirec);

}
