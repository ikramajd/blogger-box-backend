package com.dauphine.blogger.services;

import com.dauphine.blogger.dto.PostRequest;
import com.dauphine.blogger.exceptions.BadRequestException;
import com.dauphine.blogger.exceptions.ResourceNotFoundException;
import com.dauphine.blogger.models.Category;
import com.dauphine.blogger.models.Post;
import com.dauphine.blogger.repositories.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional
public class PostService {
    private final CategoryService categoryService;
    private final PostRepository postRepository;

    public PostService(CategoryService categoryService, PostRepository postRepository) {
        this.categoryService = categoryService;
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public List<Post> getAll(String value, String date) {
        boolean hasValue = value != null && !value.isBlank();
        boolean hasDate = date != null && !date.isBlank();

        if (hasDate) {
            LocalDate localDate = parseDate(date);
            LocalDateTime start = localDate.atStartOfDay();
            LocalDateTime end = localDate.plusDays(1).atStartOfDay();
            return postRepository.findAllByCreatedDateBetweenOrderByCreatedDateDesc(start, end).stream()
                    .filter(post -> !hasValue || matchesValue(post, value))
                    .toList();
        }

        if (hasValue) {
            return postRepository.findAllByTitleOrContentContaining(value.trim());
        }

        return postRepository.findAllByOrderByCreatedDateDesc();
    }

    @Transactional(readOnly = true)
    public Post getById(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByCategoryId(UUID categoryId) {
        categoryService.getById(categoryId);
        return postRepository.findAllByCategory_IdOrderByCreatedDateDesc(categoryId);
    }

    public Post create(PostRequest request) {
        validateRequest(request);
        Category category = categoryService.getById(request.getCategoryId());
        Post post = new Post();
        post.setTitle(request.getTitle().trim());
        post.setContent(request.getContent().trim());
        post.setCategory(category);
        post.setCreatedDate(LocalDateTime.now(ZoneOffset.UTC));
        return postRepository.save(post);
    }

    public Post update(UUID id, PostRequest request) {
        validateRequest(request);
        Post post = getById(id);
        Category category = categoryService.getById(request.getCategoryId());
        post.setTitle(request.getTitle().trim());
        post.setContent(request.getContent().trim());
        post.setCategory(category);
        return postRepository.save(post);
    }

    public void delete(UUID id) {
        Post post = getById(id);
        postRepository.delete(post);
    }

    public void deletePostsByCategoryId(UUID categoryId) {
        postRepository.deleteAllByCategoryId(categoryId);
    }

    private void validateRequest(PostRequest request) {
        if (request == null) {
            throw new BadRequestException("Post body is required");
        }
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new BadRequestException("Post title is required");
        }
        if (request.getTitle().length() > 150) {
            throw new BadRequestException("Post title must be less than 150 characters");
        }
        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new BadRequestException("Post content is required");
        }
        if (request.getContent().length() > 2500) {
            throw new BadRequestException("Post content must be less than 2500 characters");
        }
        if (request.getCategoryId() == null) {
            throw new BadRequestException("Post categoryId is required");
        }
    }

    private boolean matchesValue(Post post, String value) {
        String normalizedValue = value.toLowerCase(Locale.ROOT);
        return post.getTitle().toLowerCase(Locale.ROOT).contains(normalizedValue)
                || post.getContent().toLowerCase(Locale.ROOT).contains(normalizedValue);
    }

    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date.trim());
        } catch (DateTimeException ignored) {
            try {
                return LocalDate.parse(date.trim(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (DateTimeException exception) {
                throw new BadRequestException("Post date must use yyyy-MM-dd or dd-MM-yyyy format");
            }
        }
    }
}
