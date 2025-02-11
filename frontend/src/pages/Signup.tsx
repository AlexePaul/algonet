import { useContext, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styles from './Signup.module.css'; // ✅ Use CSS Module
import { AuthContext } from '../context/AuthContext';
import { login, signup } from '../services/authService';

function Signup() {
  const [email, setEmail] = useState<string>('');
  const [username, setUsername] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(false);

  const navigate = useNavigate();
  const auth = useContext(AuthContext);

  const handleLogin = async () => {
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

  const handleSignup = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    if (!email || !username || !password) {
      setError('All fields are required');
      setLoading(false);
      return;
    }

    try {
      setUsername(username.trim());
      setPassword(password.trim());
      setEmail(email.trim());
      const response = await signup(email, username, password);

      if (!response.ok) {
        throw new Error('Failed to sign up');
      }

      handleLogin();
      setEmail('');
      setUsername('');
      setPassword('');
    } catch (err) {
      setError(err as string);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.signupContainer}>
      <form onSubmit={handleSignup} className={styles.signupForm}>
        <h2>Sign Up</h2>
        {error && <p className={styles.errorText}>{error}</p>}
        <label>
          Email:
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </label>
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
        <button
          type="submit"
          disabled={loading}
          className={styles.signupButton}
        >
          {loading ? 'Signing up...' : 'Sign Up'}
        </button>
        <div className={styles.signUp}>
          <p>Already have an account?</p>
          <Link className={styles.link} to="/login">
            Login
          </Link>
        </div>
      </form>
    </div>
  );
}

export default Signup;
