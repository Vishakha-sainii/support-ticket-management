import { describe, expect, it } from 'vitest';
import { ApiClientError, getErrorMessage } from '../utils/api';
import type { ApiError } from '../types/ticket';

describe('getErrorMessage', () => {
  it('returns api error message for ApiClientError', () => {
    const apiError: ApiError = {
      timestamp: '2026-01-01T10:00:00Z',
      status: 400,
      error: 'VALIDATION_ERROR',
      message: 'Title must not be blank',
      path: '/api/tickets',
    };

    const error = new ApiClientError(apiError);
    expect(getErrorMessage(error)).toBe('Title must not be blank');
  });

  it('returns generic message for unknown errors', () => {
    expect(getErrorMessage('oops')).toBe('An unexpected error occurred. Please try again.');
  });

  it('returns Error message for standard errors', () => {
    expect(getErrorMessage(new Error('Network failed'))).toBe('Network failed');
  });
});
