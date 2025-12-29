package com.example.library.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.library.Entity.BookEntity;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {
    Optional<BookEntity> findByIdBook(Integer idBook);
}
