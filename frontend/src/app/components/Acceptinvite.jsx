"use client";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";

export default function AcceptInviteForm({ partialUser, inviteToken }) {
  const router = useRouter();

  const [userDetails, setUserDetails] = useState({
    username: "",
    password: "",
    confirmPassword: ""
  });
  const [errors, setErrors] = useState({});

  const validate = () => {
    const newErrors = {};
    if (userDetails.username.length < 5 || userDetails.username.length > 50) {
      newErrors.username = "Username must be 5 to 50 characters long";
    }
    const strongRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{6,}$/;
    if (!strongRegex.test(userDetails.password)) {
      newErrors.password = "Password must be 6+ chars, 1 uppercase, 1 lowercase, 1 number, 1 special char";
    }
    if (userDetails.password !== userDetails.confirmPassword) {
      newErrors.confirmPassword = "Passwords do not match";
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {
    setUserDetails({ ...userDetails, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    try {
      const response = await fetch("/api/register/inviteuser", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          ...userDetails,
          email: partialUser.email,
          inviteToken,
        }),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText);
      }

      const data = await response.json();
      try{
      if (data?.data) {
        router.push("/home");
      }
       else {
        alert("Registration completed. Please login.");
      }}catch(err){
        setErrors({ api: err.message });
      }
      
    } catch (err) {
      console.log("Problem is here!")
      setErrors({ api: err.message });
    }
  };

  return (
    <div style={{ maxWidth: "400px", margin: "50px auto" }}>
      <h2>Welcome! Complete your registration</h2>
      <p>Email (from invite): <b>{partialUser.email}</b></p>
      <p>Role: <b>{partialUser.role}</b></p>

      <form onSubmit={handleSubmit}>
        <label>Email:</label>
        <input type="email" value={partialUser.email} readOnly /><br />

        <label>Username:</label>
        <input type="text" name="username" value={userDetails.username} onChange={handleChange} required />
        {errors.username && <div style={{ color: "red" }}>{errors.username}</div>}<br />

        <label>Password:</label>
        <input type="password" name="password" value={userDetails.password} onChange={handleChange} required />
        {errors.password && <div style={{ color: "red" }}>{errors.password}</div>}<br />

        <label>Confirm password:</label>
        <input type="password" name="confirmPassword" value={userDetails.confirmPassword} onChange={handleChange} required />
        {errors.confirmPassword && <div style={{ color: "red" }}>{errors.confirmPassword}</div>}<br />

        <button type="submit">Register</button>
      </form>
    </div>
  );
}
