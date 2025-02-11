import { useContext } from 'react';
import './styles/App.css';
import Header from './features/Header';
import Login from './pages/Login';
import Signup from './pages/Signup';
import { AuthContext } from './context/AuthContext';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Dashboard from './pages/Dashboard';
import TagPage from './pages/TagPage';

function App() {
  const auth = useContext(AuthContext);

  return (
    <>
      <Router>
        <Header />
        <Routes>
          <Route path="/" element={auth?.token ? <Dashboard /> : <Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/tag/:tagName" element={<TagPage />} />
        </Routes>
      </Router>
    </>
  );
}

export default App;
