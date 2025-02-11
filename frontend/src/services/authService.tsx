import { API_BASE_URL } from '../constants';
import { user } from '../types';

export const login = async (
  username: string,
  password: string
): Promise<string> => {
  const response = await fetch(`${API_BASE_URL}/auth/login`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    credentials: 'include',
    body: JSON.stringify({ username, password })
  });

  if (!response.ok) {
    throw new Error('Invalid credentials');
  }

  const data = await response.text();
  console.log(data);
  return data;
};

export const signup = async (
  email: string,
  username: string,
  password: string
) => {
  const response = await fetch(`${API_BASE_URL}/auth/register`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ email, username, password })
  });
  return response;
};

export const getUser = async (token: string): Promise<user> => {
  const response = await fetch(`${API_BASE_URL}/auth`, {
    method: 'GET',
    headers: {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    credentials: 'include'
  });
  return response.json();
};
