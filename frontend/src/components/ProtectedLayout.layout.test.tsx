import { render } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { describe, expect, it, vi } from 'vitest';
import { ProtectedLayout } from './ProtectedLayout';

vi.mock('../context/AuthContext', () => ({
  useAuth: vi.fn(),
}));

import { useAuth } from '../context/AuthContext';

describe('ProtectedLayout layout', () => {
  it('uses full-width app container without narrow max-width constraint', () => {
    vi.mocked(useAuth).mockReturnValue({
      user: { username: 'admin', role: 'ADMIN' },
      loading: false,
      isAdmin: true,
      login: vi.fn(),
      logout: vi.fn(),
    });

    const { container } = render(
      <MemoryRouter initialEntries={['/']}>
        <Routes>
          <Route element={<ProtectedLayout />}>
            <Route path="/" element={<div>Ticket List</div>} />
          </Route>
        </Routes>
      </MemoryRouter>,
    );

    const app = container.querySelector('.app');
    expect(app).toBeInTheDocument();
    expect(app).not.toHaveStyle({ maxWidth: '960px' });
  });
});
