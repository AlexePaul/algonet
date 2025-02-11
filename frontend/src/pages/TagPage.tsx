import { useParams } from 'react-router-dom';
import styles from './TagPage.module.css';

function TagPage() {
  const { tagName } = useParams(); // Get the tagName from the URL

  return (
    <div>
      <h1 className={styles.title}> {tagName}</h1>
    </div>
  );
}

export default TagPage;
