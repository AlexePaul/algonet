import { useParams } from 'react-router-dom';
import styles from './ProblemPage.module.css';
import { useContext, useEffect, useState } from 'react';
import { problem } from '../types';
import { fetchProblemsById } from '../services/problemService';
import { AuthContext } from '../context/AuthContext';

function ProblemPage() {
  const { problemId } = useParams();
  const [problem, setProblem] = useState<problem | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const auth = useContext(AuthContext);

  useEffect(() => {
    const loadTags = async () => {
      setLoading(true);
      setError(null);
      try {
        const data = await fetchProblemsById(
          auth?.token as string,
          problemId as string
        );
        setProblem(data);
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
    <div className={styles.problemContainer}>
      <h1 className={styles.title}>
        {problem?.title} <b>@{problem?.id}</b>
      </h1>
      <h3 className={styles.subtitle}>Cerinta:</h3>
      <p className={styles.body}>{problem?.body}</p>
      <h3 className={styles.subtitle}>Restrictii:</h3>
      <p className={styles.body}>{problem?.restrictions}</p>
      <h3 className={styles.subtitle}>Limite:</h3>
      <div className="limit">
        <h4 className={styles.sstitle}>Timp:</h4>
        <span className={styles.body}> {problem?.timeLimit} secunde/test</span>
      </div>
      <div className="limit">
        <h4 className={styles.sstitle}>Memorie:</h4>
        <span className={styles.body}> {problem?.memoryLimit} Kb/test</span>
      </div>
    </div>
  );
}
export default ProblemPage;
