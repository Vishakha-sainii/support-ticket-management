import { Link, Navigate, Outlet, useNavigate } from 'react-router-dom';
import { LoadingState } from './LoadingState';
import { useAuth } from '../context/AuthContext';

export function ProtectedLayout() {
  const { user, loading, logout } = useAuth();
  const navigate = useNavigate();

  if (loading) {
    return <LoadingState label="Loading..." />;
  }

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  async function handleLogout() {
    await logout();
    navigate('/login');
  }

  return (
    <div className="app">
      <header className="app-header">
        <h1>
          <Link to="/" className="app-title">Ticket Management System</Link>
        </h1>
        <div className="header-auth">
          <span className="meta">
            {user.username} ({user.role})
          </span>
          <button className="btn btn-secondary" type="button" onClick={() => void handleLogout()}>
            Logout
          </button>
        </div>
      </header>

      <Outlet />
    </div>
  );
}
