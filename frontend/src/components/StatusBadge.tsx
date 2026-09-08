import type { Status } from '../types/ticket';

interface StatusBadgeProps {
  status: Status;
}

function toStatusClass(status: Status): string {
  return status.toLowerCase().replace(/_/g, '-');
}

export function StatusBadge({ status }: StatusBadgeProps) {
  const className = `badge status-badge status-${toStatusClass(status)}`;

  return (
    <span className={className} aria-label={`Status: ${status}`}>
      {status}
    </span>
  );
}
