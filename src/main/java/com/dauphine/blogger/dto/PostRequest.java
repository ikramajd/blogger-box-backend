package com.dauphine.blogger.dto;

import java.util.UUID;

public class PostRequest {
    private String title;
    private String content;
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
