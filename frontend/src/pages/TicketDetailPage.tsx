import { useCallback, useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { CommentSection } from '../components/CommentSection';
import { ErrorMessage } from '../components/ErrorMessage';
import { LoadingState } from '../components/LoadingState';
import { StatusActions } from '../components/StatusActions';
import { TicketForm, type TicketFormValues } from '../components/TicketForm';
import { fetchTicket, updateTicket } from '../services/ticketService';
import type { Ticket } from '../types/ticket';
import { getErrorMessage } from '../utils/api';

function formatDate(value: string): string {
  return new Date(value).toLocaleString();
}

export function TicketDetailPage() {
  const { id } = useParams();
  const ticketId = Number(id);

  const [ticket, setTicket] = useState<Ticket | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [editing, setEditing] = useState(false);

  const loadTicket = useCallback(async () => {
    setLoading(true);
    setError('');

    try {
      const data = await fetchTicket(ticketId);
      setTicket(data);
    } catch (err) {
      setError(getErrorMessage(err));
      setTicket(null);
    } finally {
      setLoading(false);
    }
  }, [ticketId]);

  useEffect(() => {
    if (!Number.isFinite(ticketId)) {
      setError('Invalid ticket ID.');
      setLoading(false);
      return;
    }
    void loadTicket();
  }, [loadTicket, ticketId]);

  async function handleUpdate(values: TicketFormValues) {
    const updated = await updateTicket(ticketId, values);
    setTicket(updated);
    setEditing(false);
  }

  if (loading) {
    return <LoadingState label="Loading ticket details..." />;
  }

  if (!ticket) {
    return (
      <div>
        <ErrorMessage message={error || 'Ticket not found.'} />
        <Link to="/">Back to tickets</Link>
      </div>
    );
  }

  const formValues: TicketFormValues = {
    title: ticket.title,
    description: ticket.description,
    priority: ticket.priority,
    assignee: ticket.assignee,
  };

  return (
    <div>
      <p><Link to="/">← Back to tickets</Link></p>
      <ErrorMessage message={error} />

      {editing ? (
        <div>
          <h2>Edit Ticket #{ticket.id}</h2>
          <TicketForm
            initialValues={formValues}
            submitLabel="Save Changes"
            onSubmit={handleUpdate}
            onCancel={() => setEditing(false)}
          />
        </div>
      ) : (
        <div className="card">
          <h2>{ticket.title}</h2>
          <p className="meta">
            #{ticket.id} · {ticket.priority} · <span className="status-badge">{ticket.status}</span>
          </p>
          <p>{ticket.description}</p>
          <p className="meta">Assignee: {ticket.assignee}</p>
          <p className="meta">Created: {formatDate(ticket.createdAt)}</p>
          <p className="meta">Updated: {formatDate(ticket.updatedAt)}</p>
          <button className="btn btn-secondary" type="button" onClick={() => setEditing(true)}>
            Edit Ticket
          </button>
        </div>
      )}

      <StatusActions ticket={ticket} onUpdated={setTicket} />
      <CommentSection ticket={ticket} onUpdated={setTicket} />
    </div>
  );
}
