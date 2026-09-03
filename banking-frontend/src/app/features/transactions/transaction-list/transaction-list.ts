import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { TransactionService } from '../../../core/services/transaction.service';
import { Transaction } from '../../../core/models/transaction.model';
import { extractErrorMessage } from '../../../core/utils/error.util';

@Component({
  selector: 'app-transaction-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './transaction-list.html',
  styleUrl: './transaction-list.css'
})
export class TransactionList implements OnInit {
  transactions: Transaction[] = [];
  currentPage = 0;
  totalPages = 0;
  pageSize = 20;

  loading = true;
  errorMessage = '';

  constructor(private transactionService: TransactionService) {}

  ngOnInit(): void {
    this.loadPage(0);
  }

  loadPage(page: number): void {
    this.loading = true;
    this.errorMessage = '';
    this.transactionService.getAllTransactions(page, this.pageSize, 'createdAt', 'DESC').subscribe({
      next: (result) => {
        this.transactions = result.content;
        this.currentPage = result.number;
        this.totalPages = result.totalPages;
        this.loading = false;
      },
      error: (err: HttpErrorResponse) => {
        this.loading = false;
        this.errorMessage = extractErrorMessage(err);
      }
    });
  }

  nextPage(): void {
    if (this.currentPage + 1 < this.totalPages) this.loadPage(this.currentPage + 1);
  }

  previousPage(): void {
    if (this.currentPage > 0) this.loadPage(this.currentPage - 1);
  }
}