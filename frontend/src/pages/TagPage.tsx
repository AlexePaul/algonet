import { useParams, useLocation, useNavigate } from 'react-router-dom';
import styles from './TagPage.module.css';
import { useContext, useEffect, useState } from 'react';
import { AuthContext } from '../context/AuthContext';
import { fetchProblems } from '../services/problemService';
import { problemPreview } from '../types';

function TagPage() {
  const { tagName } = useParams(); // Get the tagName from the URL
  const auth = useContext(AuthContext);
  const [problems, setProblems] = useState<problemPreview[]>([]);
  const location = useLocation();
  const tagId = location.state?.tagId;
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  function handleClick(
    event: React.MouseEvent<HTMLDivElement, MouseEvent>
  ): void {
    const input = event.currentTarget.querySelector('h3')?.innerText;
    const match = input?.match(/@(\d+)$/);
    const id = match ? parseInt(match[1], 10) : null;
    console.log(id);
    navigate(`/problem/${id}`);
  }

  function truncateText(text: string, maxLength: number): string {
    if (text.length <= maxLength) return text; // No need to truncate

    let truncated = text.slice(0, maxLength + 1); // Include an extra char for checking
    let lastSpace = truncated.lastIndexOf(' '); // Find the last space

    if (lastSpace > 0) {
      truncated = truncated.slice(0, lastSpace); // Cut at last space
    } else {
      truncated = truncated.slice(0, maxLength); // If no space, hard cut
    }

    return truncated + '...'; // Add ellipsis
  }

  useEffect(() => {
    const loadTags = async () => {
      setLoading(true);
      setError(null);
      try {
        const data = await fetchProblems(auth?.token as string, tagId);
        setProblems(data);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'An error occurred');
      } finally {
        setLoading(false);
      }
    };

    loadTags();
  }, []);

  if (loading) return <p>Loading...</p>;
  if (error) return <p className={styles.error}>{error}</p>;

  return (
    <div>
      <h1 className={styles.title}>{tagName}</h1>
      <div className={styles.itemList}>
        {problems.map((item) => (
          <div key={item.id} className={styles.itemCard} onClick={handleClick}>
            <h3 className={styles.title}>
              {item.title} <b>@{item.id}</b>
            </h3>
            <p className={styles.body}>{truncateText(item.body, 200)}</p>
          </div>
        ))}
      </div>
    </div>
  );
}

export default TagPage;
