import { render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { describe, expect, it, vi, beforeEach } from 'vitest';
import { ProtectedLayout } from './ProtectedLayout';

vi.mock('../context/AuthContext', () => ({
  useAuth: vi.fn(),
}));

import { useAuth } from '../context/AuthContext';

describe('ProtectedLayout', () => {
  beforeEach(() => {
    vi.mocked(useAuth).mockReturnValue({
      user: { username: 'admin', role: 'ADMIN' },
      loading: false,
      isAdmin: true,
      login: vi.fn(),
      logout: vi.fn(),
    });
  });

  it('redirects unauthenticated users to login', async () => {
    vi.mocked(useAuth).mockReturnValue({
      user: null,
      loading: false,
      isAdmin: false,
      login: vi.fn(),
      logout: vi.fn(),
    });

    render(
      <MemoryRouter initialEntries={['/']}>
        <Routes>
          <Route path="/login" element={<div>Login Page</div>} />
          <Route element={<ProtectedLayout />}>
            <Route path="/" element={<div>Ticket List</div>} />
          </Route>
        </Routes>
      </MemoryRouter>,
    );

    await waitFor(() => {
      expect(screen.getByText('Login Page')).toBeInTheDocument();
    });
  });

  it('shows logged-in user and logout in header', async () => {
    render(
      <MemoryRouter initialEntries={['/']}>
        <Routes>
          <Route element={<ProtectedLayout />}>
            <Route path="/" element={<div>Ticket List</div>} />
          </Route>
        </Routes>
      </MemoryRouter>,
    );

    await waitFor(() => {
      expect(screen.getByText('admin (ADMIN)')).toBeInTheDocument();
    });
    expect(screen.getByRole('button', { name: 'Logout' })).toBeInTheDocument();
  });
});
