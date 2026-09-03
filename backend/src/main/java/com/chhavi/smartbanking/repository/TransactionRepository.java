package com.chhavi.smartbanking.repository;

import com.chhavi.smartbanking.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByTransactionId(String transactionId);

    boolean existsByTransactionId(String transactionId);

    // findAll(Pageable) is already inherited from JpaRepository — no need to redeclare
    Page<Transaction> findAll(Pageable pageable);
}