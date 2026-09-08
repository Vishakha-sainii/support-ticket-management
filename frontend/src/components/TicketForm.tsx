import { useState } from 'react';
import type { Priority } from '../types/ticket';
import { PRIORITIES } from '../types/ticket';
import { getErrorMessage } from '../utils/api';
import {
  hasFieldErrors,
  type TicketFieldErrors,
  validateTicketFields,
} from '../utils/formValidation';

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

function RequiredLabel({ htmlFor, children }: { htmlFor: string; children: string }) {
  return (
    <label htmlFor={htmlFor}>
      {children}
      <span className="required-mark" aria-hidden="true"> *</span>
    </label>
  );
}

export function TicketForm({
  initialValues,
  submitLabel,
  onSubmit,
  onCancel,
}: TicketFormProps) {
  const [values, setValues] = useState<TicketFormValues>(initialValues);
  const [fieldErrors, setFieldErrors] = useState<TicketFieldErrors>({});
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  function clearFieldError(field: keyof TicketFieldErrors) {
    setFieldErrors((current) => {
      if (!current[field]) {
        return current;
      }
      const next = { ...current };
      delete next[field];
      return next;
    });
  }

  async function handleSubmit(event: React.FormEvent) {
    event.preventDefault();
    setError('');

    const validationErrors = validateTicketFields(values);
    setFieldErrors(validationErrors);
    if (hasFieldErrors(validationErrors)) {
      return;
    }

    setSubmitting(true);

    try {
      await onSubmit({
        ...values,
        title: values.title.trim(),
        description: values.description.trim(),
        assignee: values.assignee.trim(),
      });
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <form className="card" onSubmit={handleSubmit} noValidate>
      {error && <div className="error-banner" role="alert">{error}</div>}

      <div className="form-group">
        <RequiredLabel htmlFor="title">Title</RequiredLabel>
        <input
          id="title"
          value={values.title}
          maxLength={200}
          aria-invalid={fieldErrors.title ? 'true' : undefined}
          aria-describedby={fieldErrors.title ? 'title-error' : undefined}
          className={fieldErrors.title ? 'field-invalid' : undefined}
          onChange={(e) => {
            setValues({ ...values, title: e.target.value });
            clearFieldError('title');
          }}
        />
        {fieldErrors.title && (
          <p id="title-error" className="field-error" role="alert">{fieldErrors.title}</p>
        )}
      </div>

      <div className="form-group">
        <RequiredLabel htmlFor="description">Description</RequiredLabel>
        <textarea
          id="description"
          value={values.description}
          maxLength={5000}
          aria-invalid={fieldErrors.description ? 'true' : undefined}
          aria-describedby={fieldErrors.description ? 'description-error' : undefined}
          className={fieldErrors.description ? 'field-invalid' : undefined}
          onChange={(e) => {
            setValues({ ...values, description: e.target.value });
            clearFieldError('description');
          }}
        />
        {fieldErrors.description && (
          <p id="description-error" className="field-error" role="alert">{fieldErrors.description}</p>
        )}
      </div>

      <div className="form-group">
        <RequiredLabel htmlFor="priority">Priority</RequiredLabel>
        <select
          id="priority"
          value={values.priority}
          onChange={(e) => setValues({ ...values, priority: e.target.value as Priority })}
        >
          {PRIORITIES.map((priority) => (
            <option key={priority} value={priority}>{priority}</option>
          ))}
        </select>
      </div>

      <div className="form-group">
        <RequiredLabel htmlFor="assignee">Assignee</RequiredLabel>
        <input
          id="assignee"
          value={values.assignee}
          maxLength={100}
          aria-invalid={fieldErrors.assignee ? 'true' : undefined}
          aria-describedby={fieldErrors.assignee ? 'assignee-error' : undefined}
          className={fieldErrors.assignee ? 'field-invalid' : undefined}
          onChange={(e) => {
            setValues({ ...values, assignee: e.target.value });
            clearFieldError('assignee');
          }}
        />
        {fieldErrors.assignee && (
          <p id="assignee-error" className="field-error" role="alert">{fieldErrors.assignee}</p>
        )}
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
