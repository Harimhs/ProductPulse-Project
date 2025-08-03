import React, { useState, useEffect } from 'react';

function Register() {
  const [userDetails, setUserDetails] = useState({
    username: '',
    password: '',
    company_name: ''
  });

  const [submitted, setSubmitted] = useState(false);

  const handleChange = (e) => {
    setUserDetails({
      ...userDetails,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setSubmitted(true); // Trigger useEffect
  };

  useEffect(() => {
    if (submitted) {
      const sendData = async () => {
        try {
          const response = await fetch('http://localhost:8080/register', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(userDetails)
          });

          const data = await response.json();
          console.log('Registration successful:', data);
        } catch (error) {
          console.error('Error during registration:', error);
        }
      };

      sendData();
      setSubmitted(false); // Reset trigger
    }
  }, [submitted, userDetails]);

  return (
    <div style={{ maxWidth: '300px', margin: '100px auto' }}>
      <h2>Register</h2>
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: '10px' }}>
          <label>Username:</label><br />
          <input
            type="text"
            name="username"
            value={userDetails.username}
            onChange={handleChange}
            required
          />
        </div>
        <div style={{ marginBottom: '10px' }}>
          <label>Password:</label><br />
          <input
            type="password"
            name="password"
            value={userDetails.password}
            onChange={handleChange}
            required
          />
        </div>
        <div style={{ marginBottom: '10px' }}>
          <label>Company Name:</label><br />
          <input
            type="text"
            name="company_name"
            value={userDetails.company_name}
            onChange={handleChange}
            required
          />
        </div>
        <button type="submit">Register</button>
      </form>
    </div>
  );
}

export default Register;