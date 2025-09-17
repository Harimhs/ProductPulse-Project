"use client";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";

export default function CreateCompanyForm() {
  const router = useRouter();
  const [industries, setIndustries] = useState([]);
  const [companyDetails, setCompanyDetails] = useState({
    name: "",
    industry: "",
    description: "",
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
    fetch("/api/enum-industries")
      .then((res) => res.json())
      .then(setIndustries)
      .catch((err) =>
        setErrors("Failed to load industries: " + err.message)
      );
  }, []);

  useEffect(() => {
    if (!submitted) return;

    const sendData = async () => {
      try {
        const res = await fetch("/api/company", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(companyDetails),
          credentials: "include",
        });

        if (!res.ok) {
          const errorText = await res.text();
          throw new Error(errorText || "Company registration failed");
        }

        const data = await res.json();
        console.log("Company registration successful:", data);
        router.push("/home");
      } catch (err) {
        setErrors(err.message);
      } finally {
        setSubmitted(false);
      }
    };

    sendData();
  }, [submitted, router]);

  return (
    <div style={{ maxWidth: "400px", margin: "100px auto" }}>
      <h2>Company Register</h2>
      {errors && <p style={{ color: "red" }}>{errors}</p>}
      <form onSubmit={handleSubmit}>
        <label>Company Name:</label>
        <br />
        <input
          type="text"
          name="name"
          value={companyDetails.name}
          onChange={handleChange}
          required
        />
        <br />
        <br />

        <label>Description:</label>
        <br />
        <textarea
          name="description"
          value={companyDetails.description}
          onChange={handleChange}
        />
        <br />

        <label>Industry:</label>
        <br />
        <select
          name="industry"
          value={companyDetails.industry}
          onChange={handleChange}
          required
        >
          <option value="">-- Select Industry --</option>
          {industries.map((ind) => (
            <option key={ind.value} value={ind.value}>
              {ind.label}
            </option>
          ))}
        </select>
        <br />
        <br />

        <button type="submit">Register Company</button>
      </form>
    </div>
  );
}
