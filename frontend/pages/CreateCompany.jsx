import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getToken, logout } from "../src/authUtils";

function CreateCompany() {
  const navigate = useNavigate();
  const [industries, setIndustries] = useState([]);
  const [companyDetails, setCompanyDetails] = useState({
    name: '',
    industry: '',
    description: '',
  });
  const [errors, setErrors] = useState(null);
  const [submitted, setSubmitted] = useState(false);

  const handleChange = (e) => {
    setCompanyDetails({
      ...companyDetails,
      [e.target.name]: e.target.value,   
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setSubmitted(true);
  };

 useEffect(() => {
  fetch("http://localhost:8080/api/enums/industries")
  .then(async res => {
    if (!res.ok) {
      throw new Error(`HTTP ${res.status}`);
    }
    return res.json();
  })
  .then(data => setIndustries(data))
  .catch(err => setErrors("Failed to load industries: " + err.message));
}, []);


  useEffect(() => {
  if (!submitted) return;

  const sendData = async () => {
    try {
      const token = getToken();
      if (!token) {
        logout();
        return;
      }

      const response = await fetch('http://localhost:8080/register/company', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(companyDetails),
      });

      if (response.status === 401 || response.status === 403) {
        logout();
        return;
      }

      let data = null;
      if (response.headers.get("content-type")?.includes("application/json")) {
        data = await response.json();
      }
            
     if (data?.token) {
  localStorage.setItem("authToken", data.token);
  console.log("New ADMIN token saved:", data.token);
}

      console.log('Company Registration successful:', data || 'No JSON returned');
      navigate("/home");

      } catch (error) {
        setErrors(error.message);
      } finally {
        setSubmitted(false);
      }
    };

    sendData();
  }, [submitted, navigate]);


  return (
    <div style={{ maxWidth: '400px', margin: '100px auto' }}>
      <h2>Company Register</h2>
      {errors && <p style={{ color: 'red' }}>{errors}</p>}
      <form onSubmit={handleSubmit}>
        <label>Company Name:</label><br />
        <input
          type="text"
          name="name"
          value={companyDetails.name}
          onChange={handleChange}
          required
        /><br /><br />

        <label>Description:</label><br />
        <textarea
          name="description"
          value={companyDetails.description}
          onChange={handleChange}
        /><br />

        <label>Industry:</label><br />
        <select
          name="industry"
          value={companyDetails.industry}
          onChange={handleChange}
          required
        >
          <option value="">-- Select Industry --</option>
          {industries.map(ind => (
            <option key={ind.value} value={ind.value}>{ind.label}</option>
          ))}
        </select>
        <br /><br />

        <button type="submit">Register Company</button>
      </form>
    </div>
  );
}

export default CreateCompany;
