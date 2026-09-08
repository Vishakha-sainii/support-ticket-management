import { render, screen } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { describe, expect, it, vi } from 'vitest';
import { AdminRoute } from './AdminRoute';

vi.mock('../context/AuthContext', () => ({
  useAuth: vi.fn(),
}));

import { useAuth } from '../context/AuthContext';

describe('AdminRoute', () => {
  it('redirects USER away from admin-only route', () => {
    vi.mocked(useAuth).mockReturnValue({
      user: { username: 'user', role: 'USER' },
      loading: false,
      isAdmin: false,
      login: vi.fn(),
      logout: vi.fn(),
    });

    render(
      <MemoryRouter initialEntries={['/tickets/new']}>
        <Routes>
          <Route path="/" element={<div>Ticket List</div>} />
          <Route element={<AdminRoute />}>
            <Route path="/tickets/new" element={<div>Create Ticket</div>} />
          </Route>
        </Routes>
      </MemoryRouter>,
    );

    expect(screen.getByText('Ticket List')).toBeInTheDocument();
    expect(screen.queryByText('Create Ticket')).not.toBeInTheDocument();
  });

  it('allows ADMIN to access admin-only route', () => {
    vi.mocked(useAuth).mockReturnValue({
      user: { username: 'admin', role: 'ADMIN' },
      loading: false,
      isAdmin: true,
      login: vi.fn(),
      logout: vi.fn(),
    });

    render(
      <MemoryRouter initialEntries={['/tickets/new']}>
        <Routes>
          <Route element={<AdminRoute />}>
            <Route path="/tickets/new" element={<div>Create Ticket</div>} />
          </Route>
        </Routes>
      </MemoryRouter>,
    );

    expect(screen.getByText('Create Ticket')).toBeInTheDocument();
  });
});
