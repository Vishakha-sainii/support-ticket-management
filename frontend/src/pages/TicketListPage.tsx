import { useCallback, useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { ErrorMessage } from '../components/ErrorMessage';
import { LoadingState } from '../components/LoadingState';
import { TicketTable } from '../components/TicketTable';
import { useAuth } from '../context/AuthContext';
import { fetchTickets } from '../services/ticketService';
import type { Status, TicketSummary } from '../types/ticket';
import { STATUSES } from '../types/ticket';
import { getErrorMessage } from '../utils/api';

export function TicketListPage() {
  const { isAdmin } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();
  const [tickets, setTickets] = useState<TicketSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [accessDeniedMessage, setAccessDeniedMessage] = useState('');
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState<Status | ''>('');

  useEffect(() => {
    const state = location.state as { accessDenied?: boolean } | null;
    if (state?.accessDenied) {
      setAccessDeniedMessage('You do not have permission to create tickets.');
      navigate(location.pathname, { replace: true, state: null });
    }
  }, [location.pathname, location.state, navigate]);

  const loadTickets = useCallback(async () => {
    setLoading(true);
    setError('');

    try {
      const data = await fetchTickets(statusFilter || null, search);
      setTickets(data);
    } catch (err) {
      setError(getErrorMessage(err));
      setTickets([]);
    } finally {
      setLoading(false);
    }
  }, [search, statusFilter]);

  useEffect(() => {
    void loadTickets();
  }, [loadTickets]);

  return (
    <div>
      <div className="toolbar">
        <input
          type="search"
          placeholder="Search tickets..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          aria-label="Search tickets"
        />
        <select
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value as Status | '')}
          aria-label="Filter by status"
        >
          <option value="">All</option>
          {STATUSES.map((status) => (
            <option key={status} value={status}>{status}</option>
          ))}
        </select>
        {isAdmin && (
          <Link className="btn btn-primary" to="/tickets/new">Create Ticket</Link>
        )}
      </div>

      {accessDeniedMessage && (
        <div className="error-banner" role="alert">{accessDeniedMessage}</div>
      )}

      <ErrorMessage message={error} />

      {loading ? <LoadingState label="Loading tickets..." /> : <TicketTable tickets={tickets} />}
    </div>
  );
}
