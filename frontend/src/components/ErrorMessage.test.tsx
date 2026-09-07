import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import { ErrorMessage } from '../components/ErrorMessage';

describe('ErrorMessage', () => {
  it('displays human-readable error text', () => {
    render(<ErrorMessage message="Ticket not found" />);
    expect(screen.getByRole('alert')).toHaveTextContent('Ticket not found');
  });

  it('renders nothing when message is empty', () => {
    const { container } = render(<ErrorMessage message="" />);
    expect(container).toBeEmptyDOMElement();
  });
});
