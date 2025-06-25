package com.algonet.algonetapi.repositories;

import com.algonet.algonetapi.models.entities.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProblemRepository extends JpaRepository<Problem, Integer> {
      @Query("""
            SELECT p FROM Problem p ORDER BY p.createdAt DESC
            """)
    Page<Problem> findAllSorted(Pageable pageable);
    
    @Query("""
            SELECT p FROM Problem p JOIN FETCH p.author ORDER BY p.createdAt DESC
            """)
    Page<Problem> findAllSortedWithAuthor(Pageable pageable);
      @Query("""
            SELECT p FROM Problem p WHERE p.title LIKE %:title% ORDER BY p.createdAt DESC
            """)
    Page<Problem> findByTitleContaining(String title, Pageable pageable);
    
    @Query("""
            SELECT p FROM Problem p JOIN FETCH p.author WHERE p.title LIKE %:title% ORDER BY p.createdAt DESC
            """)
    Page<Problem> findByTitleContainingWithAuthor(String title, Pageable pageable);
      @Query("""
            SELECT p FROM Problem p WHERE p.author.username = :username ORDER BY p.createdAt DESC
            """)
    Page<Problem> findByAuthorUsername(String username, Pageable pageable);    @Query("""
            SELECT p FROM Problem p 
            LEFT JOIN FETCH p.author 
            LEFT JOIN FETCH p.tags 
            WHERE p.id = :id
            """)
    Problem findByIdWithDetails(Integer id);
}
