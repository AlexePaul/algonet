import { API_BASE_URL } from '../constants';
import { tag } from '../types';

export const fetchTags = async (token: String): Promise<tag[]> => {
  try {
    const response = await fetch(`${API_BASE_URL}/tag`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      credentials: 'include'
    });
    if (!response.ok) throw new Error('Failed to load tags');

    return response.json();
  } catch (error) {
    console.error('Error fetching tags:', error);
    throw error;
  }
};
