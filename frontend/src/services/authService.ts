import type { AuthUser, LoginRequest } from '../types/auth';
import { apiRequest } from '../utils/api';

export function login(request: LoginRequest): Promise<AuthUser> {
  return apiRequest<AuthUser>('/api/auth/login', {
    method: 'POST',
    body: JSON.stringify(request),
  });
}

export function getCurrentUser(): Promise<AuthUser> {
  return apiRequest<AuthUser>('/api/auth/me');
}

export function logout(): Promise<void> {
  return apiRequest<void>('/api/auth/logout', {
    method: 'POST',
  });
}
