import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Transaction, DepositWithdrawRequest, TransferRequest } from '../models/transaction.model';
import { Page } from '../models/page.model';

@Injectable({ providedIn: 'root' })
export class TransactionService {
  private baseUrl = `${environment.apiBaseUrl}/transactions`;

  constructor(private http: HttpClient) {}

  deposit(request: DepositWithdrawRequest): Observable<Transaction> {
    return this.http.post<Transaction>(`${this.baseUrl}/deposit`, request);
  }

  withdraw(request: DepositWithdrawRequest): Observable<Transaction> {
    return this.http.post<Transaction>(`${this.baseUrl}/withdraw`, request);
  }

  transfer(request: TransferRequest): Observable<Transaction> {
    return this.http.post<Transaction>(`${this.baseUrl}/transfer`, request);
  }

  getAllTransactions(
    page: number = 0,
    size: number = 20,
    sortBy: string = 'createdAt',
    direction: string = 'DESC'
  ): Observable<Page<Transaction>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sortBy', sortBy)
      .set('direction', direction);
    return this.http.get<Page<Transaction>>(this.baseUrl, { params });
  }

  getTransactionById(transactionId: string): Observable<Transaction> {
    return this.http.get<Transaction>(`${this.baseUrl}/${transactionId}`);
  }
}