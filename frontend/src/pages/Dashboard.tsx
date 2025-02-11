import { useContext, useEffect, useState } from 'react';
import styles from './Dashboard.module.css';
import { fetchTags, tag } from '../services/tagService';
import { AuthContext } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

function Dashboard() {
  const [tags, setTags] = useState<tag[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const auth = useContext(AuthContext);
  const navigate = useNavigate();

  useEffect(() => {
    const loadTags = async () => {
      try {
        console.log('loading tags...');
        const data = await fetchTags(auth?.token as string);
        console.log('tags loaded:', data);
        setTags(data);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'An error occurred');
      } finally {
        setLoading(false);
      }
    };

    loadTags();
  }, []);

  const handleClick = (event: any) => {
    navigate(`/tag/${event.target.innerText}`);
  };

  if (loading) return <p>Loading...</p>;
  if (error) return <p className={styles.error}>{error}</p>;

  return (
    <>
      <h1 className={styles.title}>Dashboard</h1>
      <div className={styles.itemList}>
        {tags.map((item) => (
          <div key={item.id} className={styles.itemCard} onClick={handleClick}>
            <h3>{item.name}</h3>
          </div>
        ))}
      </div>
    </>
  );
}

export default Dashboard;
