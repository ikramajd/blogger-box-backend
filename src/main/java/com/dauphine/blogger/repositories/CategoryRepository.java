package com.dauphine.blogger.repositories;

import com.dauphine.blogger.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findAllByOrderByNameAsc();

    boolean existsByNameIgnoreCase(String name);

    Optional<Category> findByNameIgnoreCase(String name);

    @Query("""
            select category
            from Category category
            where lower(category.name) like lower(concat('%', :name, '%'))
            order by category.name asc
            """)
    List<Category> findAllByNameContaining(@Param("name") String name);
}
