import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AccountService } from '../../../core/services/account.service';
import { extractErrorMessage } from '../../../core/utils/error.util';

@Component({
  selector: 'app-account-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './account-create.html',
  styleUrl: './account-create.css'
})
export class AccountCreate {
  accountForm: FormGroup;
  submitting = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private accountService: AccountService,
    private router: Router
  ) {
    this.accountForm = this.fb.group({
      accountHolderName: ['', [Validators.required, Validators.minLength(3)]],
      initialBalance: [0, [Validators.required, Validators.min(0)]]
    });
  }

  get f() {
    return this.accountForm.controls;
  }

  onSubmit(): void {
    this.errorMessage = '';

    if (this.accountForm.invalid) {
      this.accountForm.markAllAsTouched();
      return;
    }

    this.submitting = true;
    this.accountService.createAccount(this.accountForm.value).subscribe({
      next: () => {
        this.submitting = false;
        this.router.navigate(['/accounts']);
      },
      error: (err: HttpErrorResponse) => {
        this.submitting = false;
        this.errorMessage = extractErrorMessage(err);
      }
    });
  }
}