import { describe, expect, it } from 'vitest';
import {
  hasFieldErrors,
  isBlank,
  validateCommentText,
  validateTicketFields,
} from './formValidation';

describe('formValidation', () => {
  it('treats empty and whitespace-only values as blank', () => {
    expect(isBlank('')).toBe(true);
    expect(isBlank('   ')).toBe(true);
    expect(isBlank('valid')).toBe(false);
    expect(isBlank(' valid ')).toBe(false);
  });

  it('returns errors for blank ticket fields', () => {
    const errors = validateTicketFields({
      title: '',
      description: '   ',
      assignee: '',
    });

    expect(errors.title).toBe('Title is required.');
    expect(errors.description).toBe('Description is required.');
    expect(errors.assignee).toBe('Assignee is required.');
    expect(hasFieldErrors(errors)).toBe(true);
  });

  it('returns no errors for valid ticket fields', () => {
    const errors = validateTicketFields({
      title: 'Title',
      description: 'Description',
      assignee: 'Assignee',
    });

    expect(hasFieldErrors(errors)).toBe(false);
  });

  it('returns error for blank comment text', () => {
    expect(validateCommentText('')).toBe('Comment is required.');
    expect(validateCommentText('   ')).toBe('Comment is required.');
    expect(validateCommentText('Valid comment')).toBeUndefined();
  });
});
