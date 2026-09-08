import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { describe, expect, it } from 'vitest';
import { TicketTable } from './TicketTable';
import type { TicketSummary } from '../types/ticket';

const sampleTicket: TicketSummary = {
  id: 1,
  title: 'Login issue',
  priority: 'HIGH',
  status: 'OPEN',
  assignee: 'support-user',
  createdAt: '2026-01-01T10:00:00Z',
};

describe('TicketTable', () => {
  it('shows empty state when no tickets exist', () => {
    render(
      <MemoryRouter>
        <TicketTable tickets={[]} />
      </MemoryRouter>,
    );

    expect(screen.getByText(/No tickets found/i)).toBeInTheDocument();
  });

  it('renders required list fields for tickets', () => {
    render(
      <MemoryRouter>
        <TicketTable tickets={[sampleTicket]} />
      </MemoryRouter>,
    );

    expect(screen.getByText('Login issue')).toBeInTheDocument();
    expect(screen.getByLabelText('Priority: HIGH')).toBeInTheDocument();
    expect(screen.getByLabelText('Status: OPEN')).toBeInTheDocument();
    expect(screen.getByText('support-user')).toBeInTheDocument();
    expect(screen.getByText('1')).toBeInTheDocument();
  });

  it('wraps table in responsive container', () => {
    const { container } = render(
      <MemoryRouter>
        <TicketTable tickets={[sampleTicket]} />
      </MemoryRouter>,
    );

    expect(container.querySelector('.table-container')).toBeInTheDocument();
    expect(container.querySelector('.ticket-table')).toBeInTheDocument();
  });
});
