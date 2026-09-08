import { render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { describe, expect, it, vi, beforeEach } from 'vitest';
import { TicketListPage } from './TicketListPage';

vi.mock('../services/ticketService', () => ({
  fetchTickets: vi.fn().mockResolvedValue([]),
}));

vi.mock('../context/AuthContext', () => ({
  useAuth: vi.fn(),
}));

import { useAuth } from '../context/AuthContext';

describe('TicketListPage', () => {
  beforeEach(() => {
    vi.mocked(useAuth).mockReturnValue({
      user: { username: 'admin', role: 'ADMIN' },
      loading: false,
      isAdmin: true,
      login: vi.fn(),
      logout: vi.fn(),
    });
  });

  it('shows Create Ticket action for ADMIN', async () => {
    render(
      <MemoryRouter>
        <TicketListPage />
      </MemoryRouter>,
    );

    await waitFor(() => {
      expect(screen.getByRole('link', { name: 'Create Ticket' })).toBeInTheDocument();
    });
  });

  it('hides Create Ticket action for USER', async () => {
    vi.mocked(useAuth).mockReturnValue({
      user: { username: 'user', role: 'USER' },
      loading: false,
      isAdmin: false,
      login: vi.fn(),
      logout: vi.fn(),
    });

    render(
      <MemoryRouter>
        <TicketListPage />
      </MemoryRouter>,
    );

    await waitFor(() => {
      expect(screen.getByLabelText('Search tickets')).toBeInTheDocument();
    });

    expect(screen.queryByRole('link', { name: 'Create Ticket' })).not.toBeInTheDocument();
  });

  it('shows access denied message when redirected from create route', async () => {
    render(
      <MemoryRouter initialEntries={[{ pathname: '/', state: { accessDenied: true } }]}>
        <TicketListPage />
      </MemoryRouter>,
    );

    await waitFor(() => {
      expect(screen.getByRole('alert')).toHaveTextContent(
        'You do not have permission to create tickets.',
      );
    });
  });
});
