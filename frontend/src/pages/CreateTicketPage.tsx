import { useNavigate } from 'react-router-dom';
import { TicketForm, type TicketFormValues } from '../components/TicketForm';
import { createTicket } from '../services/ticketService';

const initialValues: TicketFormValues = {
  title: '',
  description: '',
  priority: 'MEDIUM',
  assignee: '',
};

export function CreateTicketPage() {
  const navigate = useNavigate();

  async function handleSubmit(values: TicketFormValues) {
    const ticket = await createTicket(values);
    navigate(`/tickets/${ticket.id}`);
  }

  return (
    <div>
      <h2>Create Ticket</h2>
      <TicketForm
        initialValues={initialValues}
        submitLabel="Create Ticket"
        onSubmit={handleSubmit}
        onCancel={() => navigate('/')}
      />
    </div>
  );
}
