package com.dauphine.blogger.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class PostRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 150, message = "Title must contain between 5 and 150 characters")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(max = 2500, message = "Content cannot exceed 2500 characters")
    private String content;

    @NotNull(message = "Category is required")
    private UUID categoryId;

    public PostRequest() {
    }

    public PostRequest(String title, String content, UUID categoryId) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }
}
