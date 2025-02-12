import { API_BASE_URL } from '../constants';
import { problem, problemPreview } from '../types';

export const fetchProblems = async (
  token: string,
  tag: string
): Promise<problemPreview[]> => {
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

export const fetchProblemsById = async (
  token: string,
  id: string
): Promise<problem> => {
  var data = await fetch(`${API_BASE_URL}/problem/${id}`, {
    method: 'GET',
    headers: {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    credentials: 'include'
  });
  return data.json();
};
