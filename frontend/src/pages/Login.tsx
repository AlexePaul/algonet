import { useContext, useState } from "react";
import styles from "./Login.module.css";
import { AuthContext } from "../context/AuthContext";
import { Link, useNavigate } from "react-router-dom";

function Login() {
  const [username, setUsername] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const auth = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    if (!username || !password) {
      setError("Both fields are required");
      setLoading(false);
      return;
    }

    try {
      setUsername(username.trim());
      setPassword(password.trim());
      const response = await fetch("http://localhost:8080/api/v1/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({ username, password }),
      });

      if (!response.ok) {
        throw new Error("Invalid credentials");
      }

      const data = await response.text();
      console.log(data);
      auth?.setToken(data);
      setUsername("");
      setPassword("");
      navigate("/");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Login failed");
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
          {loading ? "Logging in..." : "Login"}
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
