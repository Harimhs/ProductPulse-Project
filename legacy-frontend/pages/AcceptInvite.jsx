import { useEffect, useState } from "react";
import { useParams, useLocation, useNavigate } from "react-router-dom";

function AcceptInvite() {
  const { companyId } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  const query = new URLSearchParams(location.search);
  const token = query.get("token");

  const [userDetails, setUserDetails]= useState({
    username: "",
    password: "",
    confirmPassword: ""
  });

  const[errors, setErrors] = useState({});
  const[submitted, setSubmitted]= useState(false);
   
  const [partialUser, setPartialUser] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  const validate = () => {
    const newErrors = {};

    if (userDetails.username.length < 5 || userDetails.username.length > 50) {
      newErrors.username = 'Username must be 5 to 50 characters long';
    }

    const strongRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{6,}$/;
    if (!strongRegex.test(userDetails.password)) {
      newErrors.password = 'Password must be 6+ chars, 1 uppercase, 1 lowercase, 1 number, 1 special char';
    }

    if (userDetails.password !== userDetails.confirmPassword) {
      newErrors.confirmPassword = 'Passwords do not match';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {
    setUserDetails({
      ...userDetails,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validate()) {
      setSubmitted(true);
    }
  };

    useEffect(() => {
    if (!token) {
      setError("Invalid invite link.");
      setLoading(false);
      return;
    }acceptInvite();
  }, [companyId, token]);


    const acceptInvite = async () => {
      try {
        const res = await fetch(
          `http://localhost:8080/api/company/${companyId}/invites/accept?token=${token}`,
          { method: "POST" }
        );

        if (!res.ok) throw new Error("Invite expired or invalid");
        const data = await res.json();
        setPartialUser(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

   useEffect(() => {
      if (submitted) {
        const sendData = async () => {
          try {
            const response = await fetch('http://localhost:8080/register/invite', {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json'
              },
              body: JSON.stringify({
                ...userDetails,
                email: partialUser.email,   
                inviteToken: token,      
              })
              
            });
            if(response.ok){
              const data = await response.json();
              console.log("Token to save:", data.data);
              console.log('Registration successful:', data);
              if (data?.data) {
                localStorage.setItem("authToken", data.data);
                console.log("Saved OTP token:", data.data);
                navigate("/home");
              }
            else{
              alert("User Registration completed! Please try logging in.");
            }

            console.log("Saved token:", localStorage.getItem("authToken"));
  
          } else {
            const errorText = await response.text();
            alert(errorText);
          }

          } catch (error) {
            setErrors({api:'Error during registration: '+ error.message});
          }
        };
  
        sendData();
        setSubmitted(false);
      }
    }, [submitted]);

  if (loading) return <p>Loading invite...</p>;
  if (error) return <p style={{ color: "red" }}>{error}</p>;
  if (!partialUser) return null;

  return (
    <div style={{ maxWidth: "400px", margin: "50px auto" }}>
      <h2>Welcome! Complete your registration</h2>
      <p>
        Email (from invite): <b>{partialUser.email}</b>
      </p>
      <p>
        Role: <b>{partialUser.role}</b>
      </p>
      <form onSubmit={handleSubmit}>
      
        <label>Email:</label><br />
        <input type="email" name="email" value={partialUser.email} readOnly />
        <br />
       
        <label>Username:</label><br />
        <input type="text" name="username" value={userDetails.username} onChange={handleChange} required />
        {errors.username && <div style={{ color: 'red' }}>{errors.username}</div>}<br />

        <label>Password:</label><br />
        <input type="password" name="password" value={userDetails.password} onChange={handleChange} required />
        {errors.password && <div style={{ color: 'red' }}>{errors.password}</div>}<br />

        <label>Confirm Password:</label><br />
        <input type="password" name="confirmPassword" value={userDetails.confirmPassword} onChange={handleChange} required />
        {errors.confirmPassword && <div style={{ color: 'red' }}>{errors.confirmPassword}</div>}<br />

        <button type="submit">Register</button>
      </form>
    </div>
  );
}

export default AcceptInvite;
