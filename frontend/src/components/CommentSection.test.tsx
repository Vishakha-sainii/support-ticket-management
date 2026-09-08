import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import { CommentSection } from './CommentSection';
import type { Ticket } from '../types/ticket';

vi.mock('../services/ticketService', () => ({
  addComment: vi.fn(),
  fetchTicket: vi.fn(),
}));

import * as ticketService from '../services/ticketService';

const ticket: Ticket = {
  id: 1,
  title: 'Title',
  description: 'Description',
  priority: 'HIGH',
  status: 'OPEN',
  assignee: 'support-user',
  createdAt: '2026-01-01T10:00:00Z',
  updatedAt: '2026-01-01T10:00:00Z',
  comments: [],
};

describe('CommentSection', () => {
  it('shows required indicator on comment label', () => {
    render(<CommentSection ticket={ticket} onUpdated={vi.fn()} />);

    expect(screen.getByText('Comment')).toBeInTheDocument();
    expect(screen.getByText('*')).toBeInTheDocument();
  });

  it('prevents submit when comment text is blank', async () => {
    render(<CommentSection ticket={ticket} onUpdated={vi.fn()} />);

    fireEvent.click(screen.getByRole('button', { name: 'Add Comment' }));

    await waitFor(() => {
      expect(screen.getByText('Comment is required.')).toBeInTheDocument();
    });
    expect(ticketService.addComment).not.toHaveBeenCalled();
  });

  it('rejects whitespace-only comment text', async () => {
    render(<CommentSection ticket={ticket} onUpdated={vi.fn()} />);

    fireEvent.change(screen.getByLabelText(/comment/i), { target: { value: '   ' } });
    fireEvent.click(screen.getByRole('button', { name: 'Add Comment' }));

    await waitFor(() => {
      expect(screen.getByText('Comment is required.')).toBeInTheDocument();
    });
    expect(screen.getByLabelText(/comment/i)).toHaveClass('field-invalid');
    expect(ticketService.addComment).not.toHaveBeenCalled();
  });

  it('submits trimmed comment text', async () => {
    vi.mocked(ticketService.addComment).mockResolvedValue({
      id: 1,
      text: 'Valid comment',
      createdAt: '2026-01-01T10:00:00Z',
    });
    vi.mocked(ticketService.fetchTicket).mockResolvedValue(ticket);
    const onUpdated = vi.fn();

    render(<CommentSection ticket={ticket} onUpdated={onUpdated} />);

    fireEvent.change(screen.getByLabelText(/comment/i), { target: { value: ' Valid comment ' } });
    fireEvent.click(screen.getByRole('button', { name: 'Add Comment' }));

    await waitFor(() => {
      expect(ticketService.addComment).toHaveBeenCalledWith(1, 'Valid comment');
    });
  });
});
