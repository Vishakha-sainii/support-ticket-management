import type {
  Comment,
  CreateTicketPayload,
  Status,
  Ticket,
  TicketSummary,
  UpdateTicketPayload,
} from '../types/ticket';
import { apiRequest } from '../utils/api';

export async function fetchTickets(
  status?: Status | null,
  search?: string,
): Promise<TicketSummary[]> {
  const params = new URLSearchParams();
  if (status) {
    params.set('status', status);
  }
  if (search?.trim()) {
    params.set('search', search.trim());
  }
  const query = params.toString();
  const path = query ? `/api/tickets?${query}` : '/api/tickets';
  return apiRequest<TicketSummary[]>(path);
}

export async function fetchTicket(id: number): Promise<Ticket> {
  return apiRequest<Ticket>(`/api/tickets/${id}`);
}

export async function createTicket(payload: CreateTicketPayload): Promise<Ticket> {
  return apiRequest<Ticket>('/api/tickets', {
    method: 'POST',
    body: JSON.stringify(payload),
  });
}

export async function updateTicket(
  id: number,
  payload: UpdateTicketPayload,
): Promise<Ticket> {
  return apiRequest<Ticket>(`/api/tickets/${id}`, {
    method: 'PUT',
    body: JSON.stringify(payload),
  });
}

export async function addComment(ticketId: number, text: string): Promise<Comment> {
  return apiRequest<Comment>(`/api/tickets/${ticketId}/comments`, {
    method: 'POST',
    body: JSON.stringify({ text }),
  });
}

export async function transitionStatus(
  ticketId: number,
  status: Status,
): Promise<Ticket> {
  return apiRequest<Ticket>(`/api/tickets/${ticketId}/status`, {
    method: 'PATCH',
    body: JSON.stringify({ status }),
  });
}
