"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";

function Registerpage() {
  const router = useRouter();
  const [userDetails, setUserDetails] = useState({   
    username: "",
    password: "",
    confirmPassword: "",
    email: "",
  });

  const [errors, setErrors] = useState({});
  const [submitted, setSubmitted] = useState(false);
  const [otpSent, setOtpSent] = useState(false);
  const [otp, setOtp] = useState("");
  const [resendCooldown, setResendCooldown] = useState(0);

  const validate = () => {
    const newErrors = {};

    if (userDetails.username.length < 5 || userDetails.username.length > 50) {
      newErrors.username = "Username must be 5 to 50 characters long";
    }

    const strongRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{6,}$/;
    if (!strongRegex.test(userDetails.password)) {
      newErrors.password =
        "Password must be 6+ chars, 1 uppercase, 1 lowercase, 1 number, 1 special char";
    }

    if (userDetails.password !== userDetails.confirmPassword) {
      newErrors.confirmPassword = "Passwords do not match";
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(userDetails.email)) {
      newErrors.email = "Invalid email format";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  useEffect(() => {
    if (resendCooldown > 0) {
      const timer = setTimeout(
        () => setResendCooldown(resendCooldown - 1),
        1000
      );
      return () => clearTimeout(timer);
    }
  }, [resendCooldown]);

  const handleChange = (e) => {
    setUserDetails({
      ...userDetails,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validate()) {
      setSubmitted(true);
    }
  };

  useEffect(() => {
    if (submitted) {
      const sendData = async () => {
        try {
          const response = await fetch("/api/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(userDetails),
          });

          const data = await response.json();
          console.log("Registration successful:", data);

          setOtpSent(true);
        } catch (error) {
          setErrors({ api: "Error during registration: " + error.message });
        }
      };

      sendData();
      setSubmitted(false);

      console.log(userDetails);
    }
  }, [submitted]);

  const handleOtpVerify = async (e) => {
    e.preventDefault();
    e.stopPropagation();
    try {
      const response = await fetch(
        `/api/verifyotp?email=${userDetails.email}&otp=${otp}`,
        {
          method: "POST",
        }
      );

      if (response.ok) {
        const data = await response.json();
        if (data?.data) {
          router.push("/company");
        } else {
          alert("OTP verified but token missing. Please try logging in.");
        }
      } else {
        const errorText = await response.text();
        alert(errorText);
      }
    } catch (error) {
      console.error("OTP verification failed:", error);
      alert("An error occurred during OTP verification.");
    }
  };

  const handleResendOtp = async () => {
    try {
      const response = await fetch(`/api/resendotp?email=${userDetails.email}`, {
      method: 'POST'
    });

      const text = await response.text();
      console.log(text);
      alert(text);
      setResendCooldown(60);
    } catch (error) {
      console.error("Failed to resend OTP:", error);
      alert("Something went wrong. Please try again.");
    }
  };

  return (
    <div style={{ maxWidth: "400px", margin: "100px auto" }}>
      <h2>Register</h2>
      <form onSubmit={handleSubmit}>
        <label>Email:</label>
        <br />
        <input
          type="email"
          name="email"
          value={userDetails.email}
          onChange={handleChange}
          required
        />
        {errors.email && <div style={{ color: "red" }}>{errors.email}</div>}
        <br />

        <label>Username:</label>
        <br />
        <input
          type="text"
          name="username"
          value={userDetails.username}
          onChange={handleChange}
          required
        />
        {errors.username && (
          <div style={{ color: "red" }}>{errors.username}</div>
        )}
        <br />

        <label>Password:</label>
        <br />
        <input
          type="password"
          name="password"
          value={userDetails.password}
          onChange={handleChange}
          required
        />
        {errors.password && (
          <div style={{ color: "red" }}>{errors.password}</div>
        )}
        <br />

        <label>Confirm Password:</label>
        <br />
        <input
          type="password"
          name="confirmPassword"
          value={userDetails.confirmPassword}
          onChange={handleChange}
          required
        />
        {errors.confirmPassword && (
          <div style={{ color: "red" }}>{errors.confirmPassword}</div>
        )}
        <br />

        <button type="submit">Register</button>
      </form>

      {otpSent && (
        <>
          <form onSubmit={handleOtpVerify}>
            <h4>Enter OTP sent to email</h4>
            <input
              type="text"
              value={otp}
              onChange={(e) => setOtp(e.target.value)}
              required
            />
            <button type="submit">Verify OTP</button>
          </form>
          <button
            type="button"
            onClick={handleResendOtp}
            disabled={resendCooldown > 0}
          >
            {resendCooldown > 0 ? `Resend in ${resendCooldown}s` : "Resend OTP"}
          </button>
        </>
      )}
    </div>
  );
}

export default Registerpage;
