import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import { TicketForm, type TicketFormValues } from './TicketForm';
import { ApiClientError } from '../utils/api';

const initialValues: TicketFormValues = {
  title: 'Title',
  description: 'Description',
  priority: 'HIGH',
  assignee: 'support-user',
};

describe('TicketForm', () => {
  it('displays backend validation error on failed submit', async () => {
    const onSubmit = vi.fn().mockRejectedValue(
      new ApiClientError({
        timestamp: '2026-01-01T10:00:00Z',
        status: 400,
        error: 'VALIDATION_ERROR',
        message: 'Title must not be blank',
        path: '/api/tickets',
      }),
    );

    render(
      <TicketForm
        initialValues={initialValues}
        submitLabel="Create Ticket"
        onSubmit={onSubmit}
      />,
    );

    fireEvent.click(screen.getByRole('button', { name: 'Create Ticket' }));

    await waitFor(() => {
      expect(screen.getByRole('alert')).toHaveTextContent('Title must not be blank');
    });
  });

  it('calls onSubmit with current form values', async () => {
    const onSubmit = vi.fn().mockResolvedValue(undefined);

    render(
      <TicketForm
        initialValues={initialValues}
        submitLabel="Save"
        onSubmit={onSubmit}
      />,
    );

    fireEvent.change(screen.getByLabelText('Title'), { target: { value: 'Updated title' } });
    fireEvent.click(screen.getByRole('button', { name: 'Save' }));

    await waitFor(() => {
      expect(onSubmit).toHaveBeenCalledWith({
        ...initialValues,
        title: 'Updated title',
      });
    });
  });
});
