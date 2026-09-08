export function isBlank(value: string): boolean {
  return value.trim().length === 0;
}

export type FieldErrors<T extends string> = Partial<Record<T, string>>;

export interface TicketFieldErrors {
  title?: string;
  description?: string;
  assignee?: string;
}

export function validateTicketFields(values: {
  title: string;
  description: string;
  assignee: string;
}): TicketFieldErrors {
  const errors: TicketFieldErrors = {};

  if (isBlank(values.title)) {
    errors.title = 'Title is required.';
  }
  if (isBlank(values.description)) {
    errors.description = 'Description is required.';
  }
  if (isBlank(values.assignee)) {
    errors.assignee = 'Assignee is required.';
  }

  return errors;
}

export function validateCommentText(text: string): string | undefined {
  if (isBlank(text)) {
    return 'Comment is required.';
  }
  return undefined;
}

export function hasFieldErrors<T extends string>(errors: Partial<Record<T, string>>): boolean {
  return Object.keys(errors).length > 0;
}
