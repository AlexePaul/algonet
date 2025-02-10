import { useNavigate } from "react-router-dom";

function home() {
  const navigate = useNavigate();

  function handleClick() {
    navigate("/login");
  }

  function handleClickRegister() {
    navigate("/signup");
  }
  return (
    <>
      <button onClick={handleClick}>Login</button>
      <button onClick={handleClickRegister}>Signup</button>
    </>
  );
}

export default home;
