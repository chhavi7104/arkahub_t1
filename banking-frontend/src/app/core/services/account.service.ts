import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Account, CreateAccountRequest, UpdateAccountRequest } from '../models/account.model';

@Injectable({ providedIn: 'root' })
export class AccountService {
  private baseUrl = `${environment.apiBaseUrl}/accounts`;

  constructor(private http: HttpClient) {}

  getAllAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(this.baseUrl);
  }

  getAccountByNumber(accountNumber: string): Observable<Account> {
    return this.http.get<Account>(`${this.baseUrl}/${accountNumber}`);
  }

  createAccount(request: CreateAccountRequest): Observable<Account> {
    return this.http.post<Account>(this.baseUrl, request);
  }

  updateAccount(accountNumber: string, request: UpdateAccountRequest): Observable<Account> {
    return this.http.put<Account>(`${this.baseUrl}/${accountNumber}`, request);
  }

  deleteAccount(accountNumber: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${accountNumber}`);
  }
}