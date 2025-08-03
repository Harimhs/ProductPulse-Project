import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function Login() {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [triggerSubmit, setTriggerSubmit] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Username:', username);
    console.log('Password:', password);
    setTriggerSubmit(true);
  };

  useEffect(() => {
    if (!triggerSubmit) return;

    const userDetails = { username, password };

    fetch('http://localhost:8080/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username, password }),
        credentials: 'include' // Optional: only if needed for cookies/sessions
        })

      .then((response) => {
        if (!response.ok){
             throw new Error('Login failed');
        }else{
            navigate('/home');
        }
        return response.json();
      }
    )
      .then((data) => {
        console.log('Login success:', data);
        // Optional: handle token, redirection, etc.
      })
      .catch((error) => {
        console.error('Error:', error.message);
      })
      .finally(() => setTriggerSubmit(false));
  }, [triggerSubmit]);

  return (
    <div style={{ maxWidth: '300px', margin: '100px auto' }}>
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: '10px' }}>
          <label>Username:</label><br />
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div style={{ marginBottom: '10px' }}>
          <label>Password:</label><br />
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit">Login</button>
      </form>
    </div>
  );
}

export default Login;