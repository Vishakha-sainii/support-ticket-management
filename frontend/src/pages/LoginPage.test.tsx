import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { describe, expect, it, vi, beforeEach } from 'vitest';
import { AuthProvider } from '../context/AuthContext';
import { LoginPage } from './LoginPage';
import { ApiClientError } from '../utils/api';

vi.mock('../services/authService', () => ({
  login: vi.fn(),
  getCurrentUser: vi.fn(),
  logout: vi.fn(),
}));

import * as authService from '../services/authService';

describe('LoginPage', () => {
  beforeEach(() => {
    vi.mocked(authService.getCurrentUser).mockRejectedValue(new Error('Unauthenticated'));
    vi.mocked(authService.login).mockReset();
  });

  it('renders username and password fields', async () => {
    render(
      <MemoryRouter>
        <AuthProvider>
          <LoginPage />
        </AuthProvider>
      </MemoryRouter>,
    );

    await waitFor(() => {
      expect(screen.getByLabelText('Username')).toBeInTheDocument();
    });
    expect(screen.getByLabelText('Password')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Sign In' })).toBeInTheDocument();
  });

  it('shows meaningful error for invalid credentials', async () => {
    vi.mocked(authService.login).mockRejectedValue(
      new ApiClientError({
        timestamp: '2026-01-01T10:00:00Z',
        status: 401,
        error: 'AUTHENTICATION_ERROR',
        message: 'Invalid username or password',
        path: '/api/auth/login',
      }),
    );

    render(
      <MemoryRouter>
        <AuthProvider>
          <LoginPage />
        </AuthProvider>
      </MemoryRouter>,
    );

    await waitFor(() => {
      expect(screen.getByLabelText('Username')).toBeInTheDocument();
    });

    fireEvent.change(screen.getByLabelText('Username'), { target: { value: 'admin' } });
    fireEvent.change(screen.getByLabelText('Password'), { target: { value: 'wrong' } });
    fireEvent.click(screen.getByRole('button', { name: 'Sign In' }));

    await waitFor(() => {
      expect(screen.getByRole('alert')).toHaveTextContent('Invalid username or password');
    });
  });

  it('submits credentials on successful login', async () => {
    vi.mocked(authService.login).mockResolvedValue({ username: 'admin', role: 'ADMIN' });

    render(
      <MemoryRouter>
        <AuthProvider>
          <LoginPage />
        </AuthProvider>
      </MemoryRouter>,
    );

    await waitFor(() => {
      expect(screen.getByLabelText('Username')).toBeInTheDocument();
    });

    fireEvent.change(screen.getByLabelText('Username'), { target: { value: 'admin' } });
    fireEvent.change(screen.getByLabelText('Password'), { target: { value: 'admin123' } });
    fireEvent.click(screen.getByRole('button', { name: 'Sign In' }));

    await waitFor(() => {
      expect(authService.login).toHaveBeenCalledWith({
        username: 'admin',
        password: 'admin123',
      });
    });
  });
});
