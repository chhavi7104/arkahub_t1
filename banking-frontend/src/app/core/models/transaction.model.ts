export interface Transaction {
  id: number;
  transactionId: string;
  fromAccount: string | null;
  toAccount: string | null;
  amount: number;
  transactionType: 'DEPOSIT' | 'WITHDRAW' | 'TRANSFER';
  status: 'SUCCESS' | 'FAILED';
  createdAt: string;
}

export interface DepositWithdrawRequest {
  accountNumber: string;
  amount: number;
}

export interface TransferRequest {
  senderAccountNumber: string;
  receiverAccountNumber: string;
  amount: number;
}