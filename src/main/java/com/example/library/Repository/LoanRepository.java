package com.example.library.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.library.Entity.LoanEntity;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Integer> {
    Optional<LoanEntity> findByIdLoan(Integer idLoan);
}
