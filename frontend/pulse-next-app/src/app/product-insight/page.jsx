"use client"
import React, { useState } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export default      function ProductInsightPage() {
  const [formData, setFormData] = useState({
    productName: "",
    productDescription: "",
    productLaunchDate: "",
    regionCountry:" "
  });

  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value
    }));
  };

  const validateForm = () => {
    if (!formData.productName.trim()) {
      toast.error("Product Name is required");
      return false;
    }
    if (!formData.productDescription.trim()) {
      toast.error("Product Description is required");
      return false;
    }
    if (!formData.productLaunchDate) {
      toast.error("Product Launch Date is required");
      return false;
    }
    if (!formData.regionCountry) {
      toast.error("regionCountry is required");
      return false;
    }
    return true;
  };


  const handleSubmit = async (e) => {
    console.log(formData);
    e.preventDefault();

    if (!validateForm()) return;

    setLoading(true);
    try {
      const response = await fetch(
        `http://localhost:8080/api/product`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify(formData)
        }
      );

      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }

      const result = await response.json();
      console.log("Product saved:", result);
      toast.success("Product data submitted successfully!");

      setFormData({
        productName: "",
        productDescription: "",
        productLaunchDate: "",
        regionCountry: ""
      });
    } catch (error) {
      console.error("Error:", error);
      toast.error("Failed to submit product data");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <label>Product Name</label>
        <input
          type="text"
          name="productName"
          placeholder="Enter your product name"
          value={formData.productName}
          onChange={handleChange}
        />

        <label>Product Description</label>
        <textarea
          name="productDescription"
          placeholder="Enter product description"
          value={formData.productDescription}
          onChange={handleChange}
        />

        <label>Product Launch Date</label>
        <input
          type="date"
          name="productLaunchDate"
          value={formData.productLaunchDate}
          onChange={handleChange}
        />
        <label>Region/Country</label>
        <input
          name="regionCountry"
          type="text"
          onChange={handleChange}
        />

        <button type="submit" disabled={loading}>
          {loading ? "Submitting..." : "Search the product insights"}
        </button>
      </form>

      <ToastContainer position="top-right" autoClose={3000} />
    </div>
  );
}
