import { useState } from "react";
import { Sparkles, Loader2 } from "lucide-react";

function SuitabilityForm({ onSubmit, loading }) {
  const [breed, setBreed] = useState("");
  const [city, setCity] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    if (breed.trim() && city.trim()) {
      onSubmit(breed.trim(), city.trim());
    }
  };

  return (
    <form onSubmit={handleSubmit} className="w-full max-w-sm">
      <div className="mb-5">
        <label htmlFor="breed" className="block text-xs font-bold text-gray-500 mb-2 pl-1 uppercase tracking-wider">
          Dog Breed
        </label>
        <input
          id="breed"
          type="text"
          placeholder="e.g. Labrador Retriever"
          value={breed}
          onChange={(e) => setBreed(e.target.value)}
          disabled={loading}
          className="w-full px-5 py-3.5 bg-white/80 border-2 border-white/50 focus:border-gray-200 focus:bg-white rounded-2xl text-lg text-[#2A2A2A] font-medium placeholder-gray-400 focus:outline-none focus:ring-4 focus:ring-black/5 disabled:opacity-50 transition-all shadow-sm backdrop-blur-sm"
        />
      </div>
      <div className="mb-8">
        <label htmlFor="city" className="block text-xs font-bold text-gray-500 mb-2 pl-1 uppercase tracking-wider">
          City
        </label>
        <input
          id="city"
          type="text"
          placeholder="e.g. Brussels"
          value={city}
          onChange={(e) => setCity(e.target.value)}
          disabled={loading}
          className="w-full px-5 py-3.5 bg-white/80 border-2 border-white/50 focus:border-gray-200 focus:bg-white rounded-2xl text-lg text-[#2A2A2A] font-medium placeholder-gray-400 focus:outline-none focus:ring-4 focus:ring-black/5 disabled:opacity-50 transition-all shadow-sm backdrop-blur-sm"
        />
      </div>
      <button
        type="submit"
        disabled={loading || !breed.trim() || !city.trim()}
        className="group inline-flex items-center justify-center gap-2 px-8 py-3.5 bg-[#1A1A1A] text-white font-medium rounded-full hover:bg-black hover:shadow-lg disabled:opacity-50 disabled:hover:translate-y-0 disabled:cursor-not-allowed transition-all active:scale-95"
      >
        {loading ? (
          <Loader2 className="w-4 h-4 text-[#a1a1aa] animate-spin" />
        ) : (
          <Sparkles className="w-4 h-4 text-[#a1a1aa] group-hover:text-white transition-colors" />
        )}
        {loading ? "Analyzing..." : "Check Suitability"}
      </button>
    </form>
  );
}

export default SuitabilityForm;
