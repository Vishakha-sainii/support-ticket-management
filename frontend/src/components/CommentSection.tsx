import { useState } from 'react';
import type { Ticket } from '../types/ticket';
import { addComment, fetchTicket } from '../services/ticketService';
import { getErrorMessage } from '../utils/api';

interface CommentSectionProps {
  ticket: Ticket;
  onUpdated: (ticket: Ticket) => void;
}

function formatDate(value: string): string {
  return new Date(value).toLocaleString();
}

export function CommentSection({ ticket, onUpdated }: CommentSectionProps) {
  const [text, setText] = useState('');
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(event: React.FormEvent) {
    event.preventDefault();
    setError('');
    setSubmitting(true);

    try {
      await addComment(ticket.id, text);
      const latest = await fetchTicket(ticket.id);
      onUpdated(latest);
      setText('');
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="card">
      <h3>Comments</h3>

      {ticket.comments.length === 0 ? (
        <p className="meta">No comments yet.</p>
      ) : (
        <ul className="comment-list">
          {ticket.comments.map((comment) => (
            <li key={comment.id} className="comment-item">
              <p>{comment.text}</p>
              <p className="meta">{formatDate(comment.createdAt)}</p>
            </li>
          ))}
        </ul>
      )}

      <form onSubmit={handleSubmit} style={{ marginTop: '1rem' }}>
        {error && <div className="error-banner" role="alert">{error}</div>}
        <div className="form-group">
          <label htmlFor="comment-text">Add comment</label>
          <textarea
            id="comment-text"
            value={text}
            maxLength={2000}
            required
            onChange={(e) => setText(e.target.value)}
          />
        </div>
        <button className="btn btn-primary" type="submit" disabled={submitting}>
          {submitting ? 'Submitting...' : 'Add Comment'}
        </button>
      </form>
    </div>
  );
}
