import { Link } from 'react-router-dom';
import type { TicketSummary } from '../types/ticket';

interface TicketTableProps {
  tickets: TicketSummary[];
}

function formatDate(value: string): string {
  return new Date(value).toLocaleString();
}

export function TicketTable({ tickets }: TicketTableProps) {
  if (tickets.length === 0) {
    return (
      <div className="empty-state">
        No tickets found. Try adjusting your search or filter, or create a new ticket.
      </div>
    );
  }

  return (
    <table className="ticket-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Title</th>
          <th>Priority</th>
          <th>Status</th>
          <th>Assignee</th>
          <th>Created</th>
        </tr>
      </thead>
      <tbody>
        {tickets.map((ticket) => (
          <tr key={ticket.id}>
            <td>{ticket.id}</td>
            <td>
              <Link to={`/tickets/${ticket.id}`}>{ticket.title}</Link>
            </td>
            <td>{ticket.priority}</td>
            <td><span className="status-badge">{ticket.status}</span></td>
            <td>{ticket.assignee}</td>
            <td>{formatDate(ticket.createdAt)}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
