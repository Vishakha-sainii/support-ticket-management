import { describe, expect, it } from 'vitest';
import { STATUS_TRANSITIONS } from './ticket';

describe('STATUS_TRANSITIONS', () => {
  it('allows only valid transitions from OPEN', () => {
    expect(STATUS_TRANSITIONS.OPEN).toEqual(['IN_PROGRESS', 'CANCELLED']);
  });

  it('allows only valid transitions from IN_PROGRESS', () => {
    expect(STATUS_TRANSITIONS.IN_PROGRESS).toEqual(['RESOLVED', 'CANCELLED']);
  });

  it('allows only valid transitions from RESOLVED', () => {
    expect(STATUS_TRANSITIONS.RESOLVED).toEqual(['CLOSED']);
  });

  it('has no transitions from terminal states', () => {
    expect(STATUS_TRANSITIONS.CLOSED).toEqual([]);
    expect(STATUS_TRANSITIONS.CANCELLED).toEqual([]);
  });
});
