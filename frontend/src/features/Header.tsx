import { useContext } from "react";
import "../styles/Header.css";
import { AuthContext } from "../context/AuthContext";
import { Link } from "react-router-dom";

function Header() {
  const auth = useContext(AuthContext);

  const handleLogout = () => {
    console.log("Logging out...");
    auth?.setToken(null);
  };

  return (
    <header className="header">
      <Link className="link" to="/">
        <h1>AlgoNet</h1>
      </Link>
      <div className="loggedIn">
        {auth?.token && (
          <>
            <p>
              Hello, <br /> <b>{auth?.user?.username}</b>
            </p>
            <button onClick={handleLogout}>Logout</button>
          </>
        )}
      </div>
    </header>
  );
}

export default Header;
