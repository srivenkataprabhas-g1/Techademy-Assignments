import React, { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import "./App.css";

function RegistrationForm() {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors, isValid },
  } = useForm({ mode: "onChange" });

  const [passwordStrength, setPasswordStrength] = useState({
    score: 0,
    label: "",
    color: "",
  });

  const password = watch("password", "");

  // Evaluate password strength
  useEffect(() => {
    const evaluatePasswordStrength = (password) => {
      let score = 0;

      if (password.length >= 6) score++;
      if (/[A-Z]/.test(password)) score++;
      if (/[a-z]/.test(password)) score++;
      if (/[0-9]/.test(password)) score++;
      if (/[^A-Za-z0-9]/.test(password)) score++;

      // Determine label and color based on score
      if (score <= 2) {
        return { score, label: "Weak", color: "red" };
      } else if (score === 3 || score === 4) {
        return { score, label: "Medium", color: "orange" };
      } else if (score === 5) {
        return { score, label: "Strong", color: "green" };
      }
      return { score, label: "", color: "" };
    };

    setPasswordStrength(evaluatePasswordStrength(password));
  }, [password]);

  const onSubmit = (data) => {
    console.log("Registration Form Submitted:", data);
  };

  return (
    <div className="form-container">
      <h2 className="form-title">Registration Form</h2>
      <form onSubmit={handleSubmit(onSubmit)}>
        {/* Full Name */}
        <div className="form-group">
          <label className="form-label">Full Name:</label>
          <input
            className="form-input"
            type="text"
            {...register("fullName", {
              required: "Full Name is required",
            })}
          />
          {errors.fullName && (
            <span className="error-message">{errors.fullName.message}</span>
          )}
        </div>

        {/* Email */}
        <div className="form-group">
          <label className="form-label">Email:</label>
          <input
            className="form-input"
            type="email"
            {...register("email", {
              required: "Email is required",
              pattern: {
                value: /\S+@\S+\.\S+/,
                message: "Email format is invalid",
              },
            })}
          />
          {errors.email && (
            <span className="error-message">{errors.email.message}</span>
          )}
        </div>

        {/* Password */}
        <div className="form-group">
          <label className="form-label">Password:</label>
          <input
            className="form-input"
            type="password"
            {...register("password", {
              required: "Password is required",
              minLength: {
                value: 6,
                message: "Password must be at least 6 characters",
              },
            })}
          />
          {errors.password && (
            <span className="error-message">{errors.password.message}</span>
          )}
        </div>

        {/* Confirm Password */}
        <div className="form-group">
          <label className="form-label">Confirm Password:</label>
          <input
            className="form-input"
            type="password"
            {...register("confirmPassword", {
              required: "Confirm Password is required",
              validate: (value) =>
                value === password || "Passwords do not match",
            })}
          />
          {errors.confirmPassword && (
            <span className="error-message">
              {errors.confirmPassword.message}
            </span>
          )}
        </div>

        {/* Password Strength Bar */}
        {password && (
          <div className="password-strength-container">
            <div
              className="password-strength-bar"
              style={{
                width: `${(passwordStrength.score / 5) * 100}%`,
                backgroundColor: passwordStrength.color,
                height: "8px",
                borderRadius: "4px",
                transition: "width 0.3s ease-in-out",
                marginBottom: "6px",
              }}
            />
            <span
              style={{
                color: passwordStrength.color,
                fontWeight: "600",
              }}
            >
              {passwordStrength.label}
            </span>
          </div>
        )}

        {/* Submit Button */}
        <button className="submit-button" type="submit" disabled={!isValid}>
          Register
        </button>
      </form>
    </div>
  );
}

export default RegistrationForm;