package com.dauphine.blogger.repositories;

import com.dauphine.blogger.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByOrderByCreatedDateDesc();

    boolean existsByTitleIgnoreCase(String title);

    List<Post> findAllByCategory_IdOrderByCreatedDateDesc(UUID categoryId);

    List<Post> findAllByCreatedDateBetweenOrderByCreatedDateDesc(LocalDateTime start, LocalDateTime end);

    @Query("""
            select post
            from Post post
            where lower(post.title) like lower(concat('%', :value, '%'))
               or lower(post.content) like lower(concat('%', :value, '%'))
            order by post.createdDate desc
            """)
    List<Post> findAllByTitleOrContentContaining(@Param("value") String value);

    @Modifying
    @Query("delete from Post post where post.category.id = :categoryId")
    void deleteAllByCategoryId(@Param("categoryId") UUID categoryId);
}
