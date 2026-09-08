import type { Priority } from '../types/ticket';

interface PriorityBadgeProps {
  priority: Priority;
}

export function PriorityBadge({ priority }: PriorityBadgeProps) {
  const className = `badge priority-badge priority-${priority.toLowerCase()}`;

  return (
    <span className={className} aria-label={`Priority: ${priority}`}>
      {priority}
    </span>
  );
}
