import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import { PriorityBadge } from './PriorityBadge';
import { PRIORITIES } from '../types/ticket';

describe('PriorityBadge', () => {
  it.each(PRIORITIES)('renders %s with distinct priority styling', (priority) => {
    render(<PriorityBadge priority={priority} />);

    const badge = screen.getByLabelText(`Priority: ${priority}`);
    expect(badge).toHaveTextContent(priority);
    expect(badge).toHaveClass('priority-badge', `priority-${priority.toLowerCase()}`);
  });
});
