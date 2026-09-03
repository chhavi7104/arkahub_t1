import { HttpErrorResponse } from '@angular/common/http';

// The backend's GlobalExceptionHandler always returns { message, status, error, path, timestamp }.
// This pulls out `message` when present, and falls back to something sensible
// for network failures (status 0) or anything unexpected.
export function extractErrorMessage(err: HttpErrorResponse): string {
  if (err.error && typeof err.error === 'object' && 'message' in err.error) {
    return (err.error as { message: string }).message;
  }
  if (err.status === 0) {
    return 'Cannot reach the server. Please check that the backend is running.';
  }
  return `Something went wrong (HTTP ${err.status}). Please try again.`;
}