import { useState } from 'react';
import type { Priority } from '../types/ticket';
import { PRIORITIES } from '../types/ticket';
import { getErrorMessage } from '../utils/api';

export interface TicketFormValues {
  title: string;
  description: string;
  priority: Priority;
  assignee: string;
}

interface TicketFormProps {
  initialValues: TicketFormValues;
  submitLabel: string;
  onSubmit: (values: TicketFormValues) => Promise<void>;
  onCancel?: () => void;
}

export function TicketForm({
  initialValues,
  submitLabel,
  onSubmit,
  onCancel,
}: TicketFormProps) {
  const [values, setValues] = useState<TicketFormValues>(initialValues);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  async function handleSubmit(event: React.FormEvent) {
    event.preventDefault();
    setError('');
    setSubmitting(true);

    try {
      await onSubmit(values);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <form className="card" onSubmit={handleSubmit}>
      {error && <div className="error-banner" role="alert">{error}</div>}

      <div className="form-group">
        <label htmlFor="title">Title</label>
        <input
          id="title"
          value={values.title}
          maxLength={200}
          required
          onChange={(e) => setValues({ ...values, title: e.target.value })}
        />
      </div>

      <div className="form-group">
        <label htmlFor="description">Description</label>
        <textarea
          id="description"
          value={values.description}
          maxLength={5000}
          required
          onChange={(e) => setValues({ ...values, description: e.target.value })}
        />
      </div>

      <div className="form-group">
        <label htmlFor="priority">Priority</label>
        <select
          id="priority"
          value={values.priority}
          required
          onChange={(e) => setValues({ ...values, priority: e.target.value as Priority })}
        >
          {PRIORITIES.map((priority) => (
            <option key={priority} value={priority}>{priority}</option>
          ))}
        </select>
      </div>

      <div className="form-group">
        <label htmlFor="assignee">Assignee</label>
        <input
          id="assignee"
          value={values.assignee}
          maxLength={100}
          required
          onChange={(e) => setValues({ ...values, assignee: e.target.value })}
        />
      </div>

      <div className="actions-row">
        <button className="btn btn-primary" type="submit" disabled={submitting}>
          {submitting ? 'Saving...' : submitLabel}
        </button>
        {onCancel && (
          <button className="btn btn-secondary" type="button" onClick={onCancel}>
            Cancel
          </button>
        )}
      </div>
    </form>
  );
}
