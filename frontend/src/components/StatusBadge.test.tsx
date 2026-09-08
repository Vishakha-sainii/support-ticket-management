import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import { StatusBadge } from './StatusBadge';
import { STATUSES } from '../types/ticket';

describe('StatusBadge', () => {
  it.each(STATUSES)('renders %s with distinct status styling', (status) => {
    render(<StatusBadge status={status} />);

    const badge = screen.getByLabelText(`Status: ${status}`);
    expect(badge).toHaveTextContent(status);
    expect(badge).toHaveClass(
      'status-badge',
      `status-${status.toLowerCase().replace(/_/g, '-')}`,
    );
  });
});
