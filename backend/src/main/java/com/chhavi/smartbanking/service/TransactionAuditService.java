package com.chhavi.smartbanking.service;

import com.chhavi.smartbanking.entity.Transaction;
import com.chhavi.smartbanking.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionAuditService {

    private final TransactionRepository transactionRepository;

    public TransactionAuditService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Persists a FAILED transaction record in its own independent transaction.
     * REQUIRES_NEW is essential here: the calling method throws immediately after
     * this call to trigger a rollback of the main business transaction. Without
     * REQUIRES_NEW, that rollback would also undo this audit insert, and we'd lose
     * the record of the failed attempt.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFailedTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}