import { useState } from "react";
import CheckerForm from "./components/CheckerForm";
import ResultCard from "./components/ResultCard";
import "./App.css";

const API_BASE = "http://localhost:8080";

function App() {
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);

  const handleSubmit = async (breed, city) => {
    setLoading(true);
    setResult(null);
    setError(null);

    try {
      // Submit the check request
      const checkRes = await fetch(`${API_BASE}/check`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ breed, city }),
      });

      if (!checkRes.ok) {
        const text = await checkRes.text();
        throw new Error(text || "Failed to submit check request");
      }

      const { id } = await checkRes.json();

      // Poll for status every 2 seconds
      const pollStatus = () =>
        new Promise((resolve, reject) => {
          const interval = setInterval(async () => {
            try {
              const statusRes = await fetch(`${API_BASE}/status/${id}`);
              const statusData = await statusRes.json();

              if (statusData.status === "DONE" || statusData.status === "FAILED") {
                clearInterval(interval);
                resolve();
              }
            } catch (err) {
              clearInterval(interval);
              reject(err);
            }
          }, 2000);
        });

      await pollStatus();

      // Fetch the result
      const resultRes = await fetch(`${API_BASE}/result/${id}`);
      if (!resultRes.ok) {
        throw new Error("Failed to fetch result");
      }
      const resultData = await resultRes.json();
      setResult(resultData);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="app">
      <h1>Dog Suitability Checker</h1>
      <p className="subtitle">
        Check if a dog breed is suitable for the current weather in your city
      </p>
      <CheckerForm onSubmit={handleSubmit} loading={loading} />
      {loading && <div className="loading">Checking...</div>}
      {error && <div className="error">{error}</div>}
      {result && <ResultCard result={result} />}
    </div>
  );
}

export default App;
