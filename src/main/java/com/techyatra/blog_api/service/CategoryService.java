package com.techyatra.blog_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techyatra.blog_api.dto.CategoryDto;
import com.techyatra.blog_api.model.Category;
import com.techyatra.blog_api.repository.CategoryRepository;

import ch.qos.logback.core.joran.util.beans.BeanUtil;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDto saveCategory(CategoryDto dto) throws Exception {
        Category category = Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .build();
        Category categoryObj = categoryRepository.save(category);

        BeanUtils.copyProperties(categoryObj, dto);
        return dto;
    }

    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> dtos = new ArrayList<>();
        for (Category category : categories) {
            CategoryDto dto = new CategoryDto();
            dto.setId(category.getId());
            dto.setName(category.getName());
            dto.setDescription(category.getDescription());
            dto.setImageUrl(category.getImageUrl());
            dtos.add(dto);
        }
        return dtos;
    }
    
}
