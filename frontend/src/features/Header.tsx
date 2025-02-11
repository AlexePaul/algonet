import { useContext } from 'react';
import styles from './Header.module.css';
import { AuthContext } from '../context/AuthContext';
import { Link } from 'react-router-dom';

function Header() {
  const auth = useContext(AuthContext);

  const handleLogout = () => {
    console.log('Logging out...');
    auth?.setToken(null);
  };

  return (
    <header className={styles.header}>
      <Link className={styles.link} to="/">
        <h1>AlgoNet</h1>
      </Link>
      <div className={styles.container}>
        {auth?.token && (
          <>
            <p>
              Hello, <b>{auth?.user?.username}</b>
            </p>
            <button onClick={handleLogout}>Logout</button>
          </>
        )}
        {!auth?.token && (
          <>
            <Link className={styles.link} to="/login">
              <button>Log in</button>
            </Link>
            <Link className={styles.link} to="/signup">
              <button>Sign up</button>
            </Link>
          </>
        )}
      </div>
    </header>
  );
}

export default Header;
