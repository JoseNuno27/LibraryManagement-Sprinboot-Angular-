package com.inn.library.repositories;

import com.inn.library.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Custom query method to find a category by name
    Optional<Category> findByName(String name);

    // Find categories by part of their name
    List<Category> findByNameContaining(String keyword);

    // Custom JPQL query to find categories in alphabetical order
    @Query("SELECT c FROM Category c ORDER BY c.name ASC")
    List<Category> findAllCategoriesSortedByName();
}
