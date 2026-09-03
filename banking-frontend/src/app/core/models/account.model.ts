export interface Account {
  id?: number;
  accountNumber: string;
  accountHolderName: string;
  balance: number;
  status: 'ACTIVE' | 'INACTIVE' | 'BLOCKED';
  createdAt?: string;
}

export interface CreateAccountRequest {
  accountHolderName: string;
  initialBalance: number;
}

export interface UpdateAccountRequest {
  accountHolderName: string;
  status: 'ACTIVE' | 'INACTIVE' | 'BLOCKED';
}