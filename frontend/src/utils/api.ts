import type { ApiError } from '../types/ticket';

export class ApiClientError extends Error {
  readonly apiError: ApiError;

  constructor(apiError: ApiError) {
    super(apiError.message);
    this.name = 'ApiClientError';
    this.apiError = apiError;
  }
}

export function getErrorMessage(error: unknown): string {
  if (error instanceof ApiClientError) {
    return error.message;
  }
  if (error instanceof Error) {
    return error.message;
  }
  return 'An unexpected error occurred. Please try again.';
}

async function parseResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const body = await response.json().catch(() => null);
    if (body && typeof body.message === 'string') {
      throw new ApiClientError(body as ApiError);
    }
    if (response.status === 404) {
      throw new ApiClientError({
        timestamp: new Date().toISOString(),
        status: response.status,
        error: 'NOT_FOUND',
        message: 'The requested resource was not found.',
        path: '',
      });
    }
    throw new Error('Unable to reach the server. Please try again later.');
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return response.json() as Promise<T>;
}

export async function apiRequest<T>(
  path: string,
  options: RequestInit = {},
): Promise<T> {
  const headers: HeadersInit = {
    Accept: 'application/json',
    ...(options.body ? { 'Content-Type': 'application/json' } : {}),
    ...options.headers,
  };

  const response = await fetch(path, { ...options, headers });
  return parseResponse<T>(response);
}
