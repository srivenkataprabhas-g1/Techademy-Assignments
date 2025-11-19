import React, { useState, useEffect } from "react";
import "./App.css";

function App() {
  const [formData, setFormData] = useState({
    name: "",
    category: "",
    gender: "",
    hobbies: [],
    date: ""
  });

  const [errors, setErrors] = useState({});
  const [isFormValid, setIsFormValid] = useState(false);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    if (type === "checkbox") {
      let updatedHobbies = [...formData.hobbies];

      if (checked) {
        updatedHobbies.push(value);
      } else {
        updatedHobbies = updatedHobbies.filter((h) => h !== value);
      }

      setFormData({ ...formData, hobbies: updatedHobbies });
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };

  useEffect(() => {
    const newErrors = {};

    if (!formData.name) newErrors.name = "Name is required";
    if (!formData.category) newErrors.category = "Please select a category";
    if (!formData.gender) newErrors.gender = "Please select gender";
    if (formData.hobbies.length === 0)
      newErrors.hobbies = "Select at least one hobby";
    if (!formData.date) newErrors.date = "Date is required";

    setErrors(newErrors);

    // If no errors → form valid
    setIsFormValid(Object.keys(newErrors).length === 0);
  }, [formData]);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (isFormValid) {
      alert("Form submitted successfully!");
      console.log("Form Data:", formData);
    }
  };

  return (
    <div className="container">
      <h2>Survey Form for Human Behaviour</h2>

      <form onSubmit={handleSubmit}>
        
        <label>
          Name:
          <input type="text" name="name" value={formData.name} onChange={handleChange} />
          {errors.name && <p className="error">{errors.name}</p>}
        </label>

        <label>
          Category:
          <select name="category" value={formData.category} onChange={handleChange}>
            <option value="">-- Select Category --</option>
            <option value="Student">Student</option>
            <option value="Employee">Employee</option>
            <option value="Other">Other</option>
          </select>
          {errors.category && <p className="error">{errors.category}</p>}
        </label>

        <label>Gender:</label>
        <label>
          <input
            type="radio"
            name="gender"
            value="Male"
            checked={formData.gender === "Male"}
            onChange={handleChange}
          />
          Male
        </label>
        <label>
          <input
            type="radio"
            name="gender"
            value="Female"
            checked={formData.gender === "Female"}
            onChange={handleChange}
          />
          Female
        </label>
        {errors.gender && <p className="error">{errors.gender}</p>}

        <label>Hobbies:</label>
        <label>
          <input type="checkbox" value="Reading" onChange={handleChange} /> Reading
        </label>
        <label>
          <input type="checkbox" value="Traveling" onChange={handleChange} /> Traveling
        </label>
        <label>
          <input type="checkbox" value="Sports" onChange={handleChange} /> Sports
        </label>
        {errors.hobbies && <p className="error">{errors.hobbies}</p>}

        <label>
          Select Date of Birth:
          <input type="date" name="date" value={formData.date} onChange={handleChange} />
          {errors.date && <p className="error">{errors.date}</p>}
        </label>

        {}
        <button
          type="submit"
          disabled={!isFormValid}
          className={isFormValid ? "btn-green" : "btn-red"}
        >
          Submit
        </button>

      </form>
    </div>
  );
}

export default App;