import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { TransactionService } from '../../core/services/transaction.service';
import { extractErrorMessage } from '../../core/utils/error.util';

@Component({
  selector: 'app-deposit-withdraw',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './deposit-withdraw.html',
  styleUrl: './deposit-withdraw.css'
})
export class DepositWithdraw {
  form: FormGroup;
  submitting = false;
  successMessage = '';
  errorMessage = '';

  constructor(private fb: FormBuilder, private transactionService: TransactionService) {
    this.form = this.fb.group({
      accountNumber: ['', Validators.required],
      amount: [null, [Validators.required, Validators.min(0.01)]],
      operationType: ['DEPOSIT', Validators.required]
    });
  }

  get f() {
    return this.form.controls;
  }

  onSubmit(): void {
    this.successMessage = '';
    this.errorMessage = '';

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { operationType, ...request } = this.form.value;
    this.submitting = true;

    // The backend has separate POST /deposit and POST /withdraw endpoints
    // (not one combined endpoint), so the form's operationType picks which
    // service method — and which URL — actually gets called.
    const call = operationType === 'DEPOSIT'
      ? this.transactionService.deposit(request)
      : this.transactionService.withdraw(request);

    call.subscribe({
      next: (txn) => {
        this.submitting = false;
        const label = operationType === 'DEPOSIT' ? 'Deposit' : 'Withdrawal';
        this.successMessage = `${label} successful. Transaction ID: ${txn.transactionId}`;
        this.form.reset({ operationType: 'DEPOSIT' });
      },
      error: (err: HttpErrorResponse) => {
        this.submitting = false;
        this.errorMessage = extractErrorMessage(err);
      }
    });
  }
}