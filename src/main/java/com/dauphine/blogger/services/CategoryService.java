package com.dauphine.blogger.services;

import com.dauphine.blogger.dto.CategoryRequest;
import com.dauphine.blogger.exceptions.BadRequestException;
import com.dauphine.blogger.exceptions.ResourceNotFoundException;
import com.dauphine.blogger.models.Category;
import com.dauphine.blogger.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Category> getAll(String name) {
        if (name == null || name.isBlank()) {
            return categoryRepository.findAllByOrderByNameAsc();
        }
        return categoryRepository.findAllByNameContaining(name.trim());
    }

    @Transactional(readOnly = true)
    public Category getById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
    }

    public Category create(CategoryRequest request) {
        validateRequest(request);
        Category category = new Category();
        category.setName(request.getName().trim());
        return categoryRepository.save(category);
    }

    public Category update(UUID id, CategoryRequest request) {
        validateRequest(request);
        Category category = getById(id);
        category.setName(request.getName().trim());
        return categoryRepository.save(category);
    }

    public Category patch(UUID id, CategoryRequest request) {
        Category category = getById(id);
        if (request == null || request.getName() == null) {
            return category;
        }
        validateRequest(request);
        category.setName(request.getName().trim());
        return categoryRepository.save(category);
    }

    public void delete(UUID id) {
        Category category = getById(id);
        categoryRepository.delete(category);
    }

    private void validateRequest(CategoryRequest request) {
        if (request == null || request.getName() == null || request.getName().isBlank()) {
            throw new BadRequestException("Category name is required");
        }
        if (request.getName().length() > 100) {
            throw new BadRequestException("Category name must be less than 100 characters");
        }
    }
}
