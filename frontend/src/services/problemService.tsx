import { API_BASE_URL } from '../constants';
import { problem } from '../types';

export const fetchProblems = async (
  token: string,
  tag: string
): Promise<problem[]> => {
  var data = await fetch(`${API_BASE_URL}/problem/tag/${tag}`, {
    method: 'GET',
    headers: {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    credentials: 'include'
  });
  return data.json();
};
