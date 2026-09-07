import { useState } from 'react';
import type { Status, Ticket } from '../types/ticket';
import { STATUS_TRANSITIONS } from '../types/ticket';
import { transitionStatus } from '../services/ticketService';
import { getErrorMessage } from '../utils/api';

interface StatusActionsProps {
  ticket: Ticket;
  onUpdated: (ticket: Ticket) => void;
}

export function StatusActions({ ticket, onUpdated }: StatusActionsProps) {
  const [error, setError] = useState('');
  const [loadingStatus, setLoadingStatus] = useState<Status | null>(null);

  const availableTransitions = STATUS_TRANSITIONS[ticket.status];

  async function handleTransition(targetStatus: Status) {
    setError('');
    setLoadingStatus(targetStatus);

    try {
      const updated = await transitionStatus(ticket.id, targetStatus);
      onUpdated(updated);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoadingStatus(null);
    }
  }

  if (availableTransitions.length === 0) {
    return null;
  }

  return (
    <div className="card">
      <h3>Change Status</h3>
      {error && <div className="error-banner" role="alert">{error}</div>}
      <div className="status-actions">
        {availableTransitions.map((status) => (
          <button
            key={status}
            className="btn btn-secondary"
            type="button"
            disabled={loadingStatus !== null}
            onClick={() => handleTransition(status)}
          >
            {loadingStatus === status ? 'Updating...' : `Move to ${status}`}
          </button>
        ))}
      </div>
    </div>
  );
}
