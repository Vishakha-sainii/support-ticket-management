import { useCallback, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { ErrorMessage } from '../components/ErrorMessage';
import { LoadingState } from '../components/LoadingState';
import { TicketTable } from '../components/TicketTable';
import { fetchTickets } from '../services/ticketService';
import type { Status, TicketSummary } from '../types/ticket';
import { STATUSES } from '../types/ticket';
import { getErrorMessage } from '../utils/api';

export function TicketListPage() {
  const [tickets, setTickets] = useState<TicketSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState<Status | ''>('');

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
        <Link className="btn btn-primary" to="/tickets/new">Create Ticket</Link>
      </div>

      <ErrorMessage message={error} />

      {loading ? <LoadingState label="Loading tickets..." /> : <TicketTable tickets={tickets} />}
    </div>
  );
}
