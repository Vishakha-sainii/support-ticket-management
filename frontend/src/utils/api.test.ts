import { describe, expect, it, vi } from 'vitest';
import { ApiClientError, apiRequest, getErrorMessage } from './api';
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

describe('apiRequest', () => {
  it('sends credentials with requests', async () => {
    const fetchMock = vi.fn().mockResolvedValue({
      ok: true,
      status: 200,
      json: async () => ([]),
    });
    vi.stubGlobal('fetch', fetchMock);

    await apiRequest('/api/tickets');

    expect(fetchMock).toHaveBeenCalledWith(
      '/api/tickets',
      expect.objectContaining({ credentials: 'include' }),
    );

    vi.unstubAllGlobals();
  });
});
