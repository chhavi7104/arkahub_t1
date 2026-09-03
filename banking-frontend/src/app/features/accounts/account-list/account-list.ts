import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AccountService } from '../../../core/services/account.service';
import { Account } from '../../../core/models/account.model';
import { extractErrorMessage } from '../../../core/utils/error.util';

@Component({
  selector: 'app-account-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './account-list.html',
  styleUrl: './account-list.css'
})
export class AccountList implements OnInit {
  allAccounts: Account[] = [];
  filteredAccounts: Account[] = [];
  searchQuery = '';

  loading = true;
  errorMessage = '';
  successMessage = '';

  constructor(private accountService: AccountService) {}

  ngOnInit(): void {
    this.loadAccounts();
  }

  loadAccounts(): void {
    this.loading = true;
    this.errorMessage = '';
    this.accountService.getAllAccounts().subscribe({
      next: (data) => {
        this.allAccounts = data;
        this.applyFilter();
        this.loading = false;
      },
      error: (err: HttpErrorResponse) => {
        this.loading = false;
        this.errorMessage = extractErrorMessage(err);
      }
    });
  }

  // Backend has no /search endpoint, so filtering happens here against the
  // already-loaded list rather than round-tripping to the server per keystroke.
  applyFilter(): void {
    const q = this.searchQuery.trim().toLowerCase();
    this.filteredAccounts = !q
      ? this.allAccounts
      : this.allAccounts.filter(acc =>
          acc.accountNumber.toLowerCase().includes(q) ||
          acc.accountHolderName.toLowerCase().includes(q)
        );
  }

  confirmClose(account: Account): void {
    const confirmed = window.confirm(
      `Close account ${account.accountNumber} (${account.accountHolderName})? It will no longer be able to perform transactions.`
    );
    if (!confirmed) return;

    this.successMessage = '';
    this.errorMessage = '';
    this.accountService.updateAccount(account.accountNumber, {
      accountHolderName: account.accountHolderName,
      status: 'INACTIVE'
    }).subscribe({
      next: () => {
        this.successMessage = `Account ${account.accountNumber} closed.`;
        this.loadAccounts();
      },
      error: (err: HttpErrorResponse) => (this.errorMessage = extractErrorMessage(err))
    });
  }

  confirmDelete(account: Account): void {
    const confirmed = window.confirm(
      `Permanently delete account ${account.accountNumber} (${account.accountHolderName})? This cannot be undone.`
    );
    if (!confirmed) return;

    this.successMessage = '';
    this.errorMessage = '';
    this.accountService.deleteAccount(account.accountNumber).subscribe({
      next: () => {
        this.successMessage = `Account ${account.accountNumber} deleted.`;
        this.loadAccounts();
      },
      error: (err: HttpErrorResponse) => (this.errorMessage = extractErrorMessage(err))
    });
  }
}