import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, RegisterRequest } from '../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = `${environment.apiBaseUrl}/auth`;
  private readonly TOKEN_KEY = 'auth_token';
  private readonly USERNAME_KEY = 'auth_username';
  private isBrowser: boolean;

  constructor(private http: HttpClient, @Inject(PLATFORM_ID) platformId: Object) {
    // Server (SSR) environment has no localStorage — every read/write below
    // is guarded so the app never crashes during a server-rendered pass.
    this.isBrowser = isPlatformBrowser(platformId);
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/register`, request).pipe(
      tap((res) => this.storeSession(res))
    );
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, request).pipe(
      tap((res) => this.storeSession(res))
    );
  }

  logout(): void {
    if (!this.isBrowser) return;
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USERNAME_KEY);
  }

  getToken(): string | null {
    if (!this.isBrowser) return null;
    return localStorage.getItem(this.TOKEN_KEY);
  }

  getUsername(): string | null {
    if (!this.isBrowser) return null;
    return localStorage.getItem(this.USERNAME_KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  private storeSession(res: AuthResponse): void {
    if (!this.isBrowser) return;
    localStorage.setItem(this.TOKEN_KEY, res.token);
    localStorage.setItem(this.USERNAME_KEY, res.username);
  }
}