import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { forkJoin } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { AccountService } from '../../core/services/account.service';
import { TransactionService } from '../../core/services/transaction.service';
import { Transaction } from '../../core/models/transaction.model';
import { extractErrorMessage } from '../../core/utils/error.util';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {
  totalAccounts = 0;
  totalBalance = 0;
  successfulTransactions = 0;
  failedTransactions = 0;
  recentTransactions: Transaction[] = [];

  loading = true;
  errorMessage = '';

  constructor(
    private accountService: AccountService,
    private transactionService: TransactionService
  ) {}

  ngOnInit(): void {
    this.loading = true;
    this.errorMessage = '';

    forkJoin({
      accounts: this.accountService.getAllAccounts(),
      transactionsPage: this.transactionService.getAllTransactions(0, 200, 'createdAt', 'DESC')
    }).subscribe({
      next: ({ accounts, transactionsPage }) => {
        this.totalAccounts = accounts.length;
        this.totalBalance = accounts.reduce((sum, acc) => sum + Number(acc.balance), 0);

        const transactions = transactionsPage.content;
        this.successfulTransactions = transactions.filter(t => t.status === 'SUCCESS').length;
        this.failedTransactions = transactions.filter(t => t.status === 'FAILED').length;
        this.recentTransactions = transactions.slice(0, 5);

        this.loading = false;
      },
      error: (err: HttpErrorResponse) => {
        this.loading = false;
        this.errorMessage = extractErrorMessage(err);
      }
    });
  }
}