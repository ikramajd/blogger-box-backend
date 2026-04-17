package com.dauphine.blogger.controllers;

import com.dauphine.blogger.dto.PostRequest;
import com.dauphine.blogger.models.Post;
import com.dauphine.blogger.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/v1/posts")
@Tag(name = "Posts", description = "Blogger Box post endpoints")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all posts ordered by creation date")
    public ResponseEntity<List<Post>> getAll(
            @RequestParam(required = false) String value,
            @RequestParam(required = false) String date
    ) {
        return ResponseEntity.ok(postService.getAll(value, date));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a post by id")
    public ResponseEntity<Post> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create a post")
    public ResponseEntity<Post> create(@Valid @RequestBody PostRequest request) {
        Post post = postService.create(request);
        return ResponseEntity.created(URI.create("/v1/posts/" + post.getId())).body(post);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a post")
    public ResponseEntity<Post> update(@PathVariable UUID id, @Valid @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a post")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
