export type Priority = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';

export type Status = 'OPEN' | 'IN_PROGRESS' | 'RESOLVED' | 'CLOSED' | 'CANCELLED';

export const PRIORITIES: Priority[] = ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'];

export const STATUSES: Status[] = [
  'OPEN',
  'IN_PROGRESS',
  'RESOLVED',
  'CLOSED',
  'CANCELLED',
];

export interface TicketSummary {
  id: number;
  title: string;
  priority: Priority;
  status: Status;
  assignee: string;
  createdAt: string;
}

export interface Comment {
  id: number;
  text: string;
  createdAt: string;
}

export interface Ticket {
  id: number;
  title: string;
  description: string;
  priority: Priority;
  status: Status;
  assignee: string;
  createdAt: string;
  updatedAt: string;
  comments: Comment[];
}

export interface CreateTicketPayload {
  title: string;
  description: string;
  priority: Priority;
  assignee: string;
}

export interface UpdateTicketPayload {
  title: string;
  description: string;
  priority: Priority;
  assignee: string;
}

export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
}

export const STATUS_TRANSITIONS: Record<Status, Status[]> = {
  OPEN: ['IN_PROGRESS', 'CANCELLED'],
  IN_PROGRESS: ['RESOLVED', 'CANCELLED'],
  RESOLVED: ['CLOSED'],
  CLOSED: [],
  CANCELLED: [],
};
