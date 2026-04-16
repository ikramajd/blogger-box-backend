package com.dauphine.blogger.config;

import com.dauphine.blogger.models.Category;
import com.dauphine.blogger.repositories.CategoryRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {
    private static final List<String> DEFAULT_CATEGORIES = List.of(
            "Technology",
            "Travel",
            "Food",
            "Sports",
            "Arts",
            "Education",
            "Health",
            "Business",
            "Science",
            "Lifestyle"
    );

    @Bean
    public ApplicationRunner seedCategories(CategoryRepository categoryRepository) {
        return args -> DEFAULT_CATEGORIES.stream()
                .filter(name -> !categoryRepository.existsByNameIgnoreCase(name))
                .map(this::createCategory)
                .forEach(categoryRepository::save);
    }

    private Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }
}
