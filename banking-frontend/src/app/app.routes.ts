import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { Dashboard } from './features/dashboard/dashboard';
import { AccountList } from './features/accounts/account-list/account-list';
import { AccountCreate } from './features/accounts/account-create/account-create';
import { TransactionList } from './features/transactions/transaction-list/transaction-list';
import { Transfer } from './features/transfer/transfer';
import { DepositWithdraw } from './features/deposit-withdraw/deposit-withdraw';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'dashboard', component: Dashboard , canActivate: [authGuard] },
  { path: 'accounts', component: AccountList, canActivate: [authGuard]  },
  { path: 'accounts/create', component: AccountCreate, canActivate: [authGuard]  },
  { path: 'transactions', component: TransactionList, canActivate: [authGuard]  },
  { path: 'transfer', component: Transfer, canActivate: [authGuard]   },
  { path: 'deposit-withdraw', component: DepositWithdraw , canActivate: [authGuard] },
  { path: '**', redirectTo: 'dashboard' }
];