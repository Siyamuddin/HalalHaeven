package com.pm.unitalk.Service.ServiceImpl;
import com.pm.unitalk.DTOs.CategoryDto;
import com.pm.unitalk.Exceptions.ResourceNotFoundException;
import com.pm.unitalk.Model.Category;
import com.pm.unitalk.Repository.CategoryRepo;
import com.pm.unitalk.Service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category cat=this.modelMapper.map(categoryDto,Category.class);
        Category addedCat=this.categoryRepo.save(cat);
        return this.modelMapper.map(addedCat,CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {
        Category cat=this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","Category Id",categoryId));
        cat.setCategoryTitle(categoryDto.getCategoryTitle());
        Category addedCat=this.categoryRepo.save(cat);
        return this.modelMapper.map(addedCat,CategoryDto.class);
    }

    @Override
    public void deleteCategory(Long id) {
        Category cat=this.categoryRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",id));
        this.categoryRepo.delete(cat);

    }

    @Override
    public CategoryDto getCategory(Long categoryId) {
        Category cat=this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));


        return this.modelMapper.map(cat,CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getCategories(Integer pageNumber, Integer pageSize,String sortBy, String sortDirec) {
        Sort sort=null;
        if(sortDirec.equalsIgnoreCase("asc"))
        {
            sort=Sort.by(sortBy).ascending();
        }
        else
        {
            sort=Sort.by(sortBy).descending();
        }
        log.info("Loading data in service page ricived"+pageNumber+pageSize+sortBy+sortDirec);
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> categories=categoryRepo.findAll(pageable);
        log.info("data successfully fetched from db.");

        List<CategoryDto> categoryDtos=categories.stream().map((category)-> modelMapper.map(category, CategoryDto.class)).collect(Collectors.toUnmodifiableList());
        log.info("Data delivered successfully.");
        return categoryDtos;
    }
}
