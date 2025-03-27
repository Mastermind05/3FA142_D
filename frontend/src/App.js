// App.js

import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import Login from './pages/login';

console.log('App component rendering...');

// const Login = () => {
//   return Login;
// }

const Home = () => {
  console.log('Home component rendering');
  return <h2>Home Page</h2>;
};

const About = () => {
  console.log('About component rendering');
  return <h2>About Page</h2>;
};

const Contact = () => {
  console.log('Contact component rendering');
  return <h2>Contact Page</h2>;
};

const App = () => {
  console.log('App component mounted');
  
  return (
    <Router>
      <div>
        <nav>
          <ul>
            <li><Link to="/">Home</Link></li>
            <li><Link to="/about">About</Link></li>
            <li><Link to="/contact">Contact</Link></li>
            <li><Link to="/login">Login</Link></li>
          </ul>
        </nav>

        {/* Define Routes */}
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/about" element={<About />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/login" element={<Login />} />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
