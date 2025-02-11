import { useContext, useState } from 'react';
import styles from './Login.module.css';
import { AuthContext } from '../context/AuthContext';
import { Link, useNavigate } from 'react-router-dom';
import { login } from '../services/authService';

function Login() {
  const [username, setUsername] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const auth = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    if (!username || !password) {
      setError('Both fields are required');
      setLoading(false);
      return;
    }

    setUsername(username.trim());
    setPassword(password.trim());
    try {
      var token = await login(username, password);
      setUsername('');
      setPassword('');
      auth?.setToken(token);
      navigate('/');
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.loginContainer}>
      <form onSubmit={handleLogin} className={styles.loginForm}>
        <h2>Login</h2>
        {error && <p className={styles.errorText}>{error}</p>}
        <label>
          Username:
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </label>
        <label>
          Password:
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </label>
        <button type="submit" disabled={loading} className={styles.loginButton}>
          {loading ? 'Logging in...' : 'Login'}
        </button>
        <div className={styles.signUp}>
          <p>Don't have an account?</p>
          <Link className={styles.link} to="/signup">
            Sign up
          </Link>
        </div>
      </form>
    </div>
  );
}

export default Login;
