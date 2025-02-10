import { useContext, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../styles/Signup.css";
import { AuthContext } from "../context/AuthContext";

function Signup() {
  const [email, setEmail] = useState<string>("");
  const [username, setUsername] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(false);

  const navigate = useNavigate();
  const auth = useContext(AuthContext);

  const handleLogin = async () => {
    try {
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
      setError(err as string);
    }
  };

  const handleSignup = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    if (!email || !username || !password) {
      setError("All fields are required");
      setLoading(false);
      return;
    }

    try {
      const response = await fetch(
        "http://localhost:8080/api/v1/auth/register",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ email, username, password }),
        }
      );

      if (!response.ok) {
        throw new Error("Failed to sign up");
      }

      handleLogin();
      setEmail("");
      setUsername("");
      setPassword("");
    } catch (err) {
      setError(err as string);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <form onSubmit={handleSignup} className="login-form">
        <h2>Sign Up</h2>
        {error && <p className="error-text">{error}</p>}

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

        <button type="submit" disabled={loading}>
          {loading ? "Signing up..." : "Sign Up"}
        </button>

        <div className="signUp">
          <p>Already have an account?</p>
          <Link className="link" to="/login">
            Login
          </Link>
        </div>
      </form>
    </div>
  );
}

export default Signup;
