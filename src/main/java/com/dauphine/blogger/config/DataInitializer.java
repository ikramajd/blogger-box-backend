package com.dauphine.blogger.config;

import com.dauphine.blogger.models.Category;
import com.dauphine.blogger.models.Post;
import com.dauphine.blogger.repositories.CategoryRepository;
import com.dauphine.blogger.repositories.PostRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
            "Lifestyle",
            "Programming",
            "Finance",
            "Music",
            "Movies",
            "Books",
            "Gaming",
            "Environment",
            "Productivity",
            "Career",
            "News"
    );

    @Bean
    public ApplicationRunner seedData(CategoryRepository categoryRepository, PostRepository postRepository) {
        return args -> {
            DEFAULT_CATEGORIES.stream()
                    .filter(name -> !categoryRepository.existsByNameIgnoreCase(name))
                    .map(this::createCategory)
                    .forEach(categoryRepository::save);

            seedPosts(categoryRepository, postRepository);
        };
    }

    private Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }

    private void seedPosts(CategoryRepository categoryRepository, PostRepository postRepository) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        List<SeedPost> posts = List.of(
                new SeedPost("Technology", "How Angular connects to Spring Boot", "A practical overview of how the Angular frontend sends requests to the Spring Boot REST API through the local proxy configuration."),
                new SeedPost("Business", "Building a simple product roadmap", "A roadmap helps teams choose priorities, plan releases, and explain the next steps clearly to users and stakeholders."),
                new SeedPost("Education", "Study notes for REST APIs", "REST APIs use resources, HTTP methods, status codes, and JSON payloads to exchange data between applications."),
                new SeedPost("Food", "Quick pasta for busy students", "A fast pasta recipe can be prepared with tomato sauce, garlic, olive oil, and a few fresh herbs after class."),
                new SeedPost("Travel", "A weekend guide to Paris", "Paris is easy to explore over a weekend with a walk along the Seine, a museum visit, and time in a local cafe."),
                new SeedPost("Health", "Small habits for better focus", "Short breaks, hydration, regular sleep, and a clear task list can improve focus during long coding sessions."),
                new SeedPost("Science", "Why databases need indexes", "Indexes help databases find rows faster, especially when filtering or sorting large collections of records."),
                new SeedPost("Sports", "What teamwork teaches developers", "Good teams communicate clearly, share responsibility, review mistakes, and keep improving after each sprint."),
                new SeedPost("Arts", "Design basics for a blog page", "Readable typography, balanced spacing, and strong image choices make a blog page easier and more pleasant to use."),
                new SeedPost("Lifestyle", "Organizing a productive morning", "A calm morning routine can include planning the day, checking deadlines, and starting with one focused task."),
                new SeedPost("Technology", "Understanding JPA repositories", "Spring Data JPA repositories reduce boilerplate by generating common database operations from interfaces and method names."),
                new SeedPost("Business", "Writing clearer project updates", "A clear update explains what changed, what is blocked, what is next, and what decision is needed from the team."),
                new SeedPost("Education", "How to prepare for a demo", "A good demo starts with a working app, realistic data, and a short explanation of the user flow being presented."),
                new SeedPost("Science", "The role of validation in APIs", "Validation protects the application by rejecting missing, invalid, or oversized values before they reach business logic."),
                new SeedPost("Travel", "Packing light for a short trip", "Packing light means choosing flexible clothes, keeping documents accessible, and carrying only the tools you really need.")
        );

        for (int i = 0; i < posts.size(); i++) {
            SeedPost seedPost = posts.get(i);
            if (postRepository.existsByTitleIgnoreCase(seedPost.title())) {
                continue;
            }

            Category category = categoryRepository.findByNameIgnoreCase(seedPost.categoryName())
                    .orElseThrow(() -> new IllegalStateException("Missing category: " + seedPost.categoryName()));

            Post post = new Post();
            post.setTitle(seedPost.title());
            post.setContent(seedPost.content());
            post.setCategory(category);
            post.setCreatedDate(now.minusDays(i));
            postRepository.save(post);
        }
    }

    private record SeedPost(String categoryName, String title, String content) {
    }
}
