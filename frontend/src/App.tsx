import { Route, Routes } from 'react-router-dom';
import { AdminRoute } from './components/AdminRoute';
import { ProtectedLayout } from './components/ProtectedLayout';
import { CreateTicketPage } from './pages/CreateTicketPage';
import { LoginPage } from './pages/LoginPage';
import { TicketDetailPage } from './pages/TicketDetailPage';
import { TicketListPage } from './pages/TicketListPage';

export function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route element={<ProtectedLayout />}>
        <Route path="/" element={<TicketListPage />} />
        <Route path="/tickets/:id" element={<TicketDetailPage />} />
        <Route element={<AdminRoute />}>
          <Route path="/tickets/new" element={<CreateTicketPage />} />
        </Route>
      </Route>
    </Routes>
  );
}
