import { useState } from "react";

function CheckerForm({ onSubmit, loading }) {
  const [breed, setBreed] = useState("");
  const [city, setCity] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    if (breed.trim() && city.trim()) {
      onSubmit(breed.trim(), city.trim());
    }
  };

  return (
    <form className="checker-form" onSubmit={handleSubmit}>
      <div className="form-group">
        <label htmlFor="breed">Dog Breed</label>
        <input
          id="breed"
          type="text"
          placeholder="e.g. Labrador"
          value={breed}
          onChange={(e) => setBreed(e.target.value)}
          disabled={loading}
        />
      </div>
      <div className="form-group">
        <label htmlFor="city">City</label>
        <input
          id="city"
          type="text"
          placeholder="e.g. Brussels"
          value={city}
          onChange={(e) => setCity(e.target.value)}
          disabled={loading}
        />
      </div>
      <button type="submit" disabled={loading || !breed.trim() || !city.trim()}>
        {loading ? "Checking..." : "Check Suitability"}
      </button>
    </form>
  );
}

export default CheckerForm;
