import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { TransactionService } from '../../core/services/transaction.service';
import { extractErrorMessage } from '../../core/utils/error.util';

@Component({
  selector: 'app-transfer',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './transfer.html',
  styleUrl: './transfer.css'
})
export class Transfer {
  transferForm: FormGroup;
  submitting = false;
  successMessage = '';
  errorMessage = '';

  constructor(private fb: FormBuilder, private transactionService: TransactionService) {
    this.transferForm = this.fb.group({
      senderAccountNumber: ['', Validators.required],
      receiverAccountNumber: ['', Validators.required],
      amount: [null, [Validators.required, Validators.min(0.01)]]
    });
  }

  get f() {
    return this.transferForm.controls;
  }

  onSubmit(): void {
    this.successMessage = '';
    this.errorMessage = '';

    if (this.transferForm.invalid) {
      this.transferForm.markAllAsTouched();
      return;
    }

    if (this.f['senderAccountNumber'].value === this.f['receiverAccountNumber'].value) {
      this.errorMessage = 'Sender and receiver account cannot be the same.';
      return;
    }

    this.submitting = true;
    this.transactionService.transfer(this.transferForm.value).subscribe({
      next: (txn) => {
        this.submitting = false;
        this.successMessage = `Transfer successful. Transaction ID: ${txn.transactionId}`;
        this.transferForm.reset();
      },
      error: (err: HttpErrorResponse) => {
        this.submitting = false;
        // No client-side balance rollback needed on error: the backend's
        // @Transactional transfer() never applies the debit/credit unless
        // the whole operation succeeds. We just surface what the server said.
        this.errorMessage = extractErrorMessage(err);
      }
    });
  }
}