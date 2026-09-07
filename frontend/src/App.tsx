import { Link, Route, Routes } from 'react-router-dom';
import { CreateTicketPage } from './pages/CreateTicketPage';
import { TicketDetailPage } from './pages/TicketDetailPage';
import { TicketListPage } from './pages/TicketListPage';

export function App() {
  return (
    <div className="app">
      <header className="app-header">
        <h1><Link to="/">Support Tickets</Link></h1>
        <Link to="/tickets/new">Create Ticket</Link>
      </header>

      <Routes>
        <Route path="/" element={<TicketListPage />} />
        <Route path="/tickets/new" element={<CreateTicketPage />} />
        <Route path="/tickets/:id" element={<TicketDetailPage />} />
      </Routes>
    </div>
  );
}
