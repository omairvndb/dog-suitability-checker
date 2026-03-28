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
    <form onSubmit={handleSubmit} className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
      <div className="mb-4">
        <label htmlFor="breed" className="block text-sm font-semibold text-gray-700 mb-1.5">
          Dog Breed
        </label>
        <input
          id="breed"
          type="text"
          placeholder="e.g. Labrador"
          value={breed}
          onChange={(e) => setBreed(e.target.value)}
          disabled={loading}
          className="w-full px-3 py-2.5 border border-gray-300 rounded-lg text-base focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent disabled:opacity-50"
        />
      </div>
      <div className="mb-5">
        <label htmlFor="city" className="block text-sm font-semibold text-gray-700 mb-1.5">
          City
        </label>
        <input
          id="city"
          type="text"
          placeholder="e.g. Brussels"
          value={city}
          onChange={(e) => setCity(e.target.value)}
          disabled={loading}
          className="w-full px-3 py-2.5 border border-gray-300 rounded-lg text-base focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent disabled:opacity-50"
        />
      </div>
      <button
        type="submit"
        disabled={loading || !breed.trim() || !city.trim()}
        className="w-full py-2.5 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
      >
        {loading ? "Checking..." : "Check Suitability"}
      </button>
    </form>
  );
}

export default CheckerForm;
