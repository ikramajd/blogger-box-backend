package com.dauphine.blogger.controllers;

import com.dauphine.blogger.dto.CategoryRequest;
import com.dauphine.blogger.models.Category;
import com.dauphine.blogger.models.Post;
import com.dauphine.blogger.services.CategoryService;
import com.dauphine.blogger.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/categories")
@Tag(name = "Categories", description = "Blogger Box category endpoints")
public class CategoryController {
    private final CategoryService categoryService;
    private final PostService postService;

    public CategoryController(CategoryService categoryService, PostService postService) {
        this.categoryService = categoryService;
        this.postService = postService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all categories")
    public ResponseEntity<List<Category>> getAll(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(categoryService.getAll(name));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a category by id")
    public ResponseEntity<Category> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @GetMapping("/{id}/posts")
    @Operation(summary = "Retrieve posts for a category")
    public ResponseEntity<List<Post>> getPostsByCategory(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.getPostsByCategoryId(id));
    }

    @PostMapping
    @Operation(summary = "Create a category")
    public ResponseEntity<Category> create(@Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.create(request);
        return ResponseEntity.created(URI.create("/v1/categories/" + category.getId())).body(category);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a category")
    public ResponseEntity<Category> update(@PathVariable UUID id, @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.update(id, request));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a category")
    public ResponseEntity<Category> patch(@PathVariable UUID id, @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.patch(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        postService.deletePostsByCategoryId(id);
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
