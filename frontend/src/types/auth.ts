export type Role = 'ADMIN' | 'USER';

export interface AuthUser {
  username: string;
  role: Role;
}

export interface LoginRequest {
  username: string;
  password: string;
}
