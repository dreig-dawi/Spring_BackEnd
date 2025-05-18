package com.example.cheffin.repository;

import com.example.cheffin.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.role = 'CHEF' AND u.featured = true")
    List<User> findFeaturedChefs();
    
    @Query("SELECT u FROM User u WHERE u.role = 'CHEF'")
    List<User> findAllChefs();
    
    @Query("SELECT u FROM User u WHERE u.role = 'CHEF'")
    Page<User> findAllChefsPageable(Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.role = 'CHEF' AND (LOWER(u.username) LIKE %:keyword% OR LOWER(u.specialty) LIKE %:keyword% OR LOWER(u.bio) LIKE %:keyword%)")
    Page<User> searchChefsByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
