import { useState } from "react";
import CheckerForm from "./components/CheckerForm";
import ResultCard from "./components/ResultCard";

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
      await new Promise((resolve, reject) => {
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

      const resultRes = await fetch(`${API_BASE}/result/${id}`);
      if (!resultRes.ok) throw new Error("Failed to fetch result");
      setResult(await resultRes.json());
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex items-start justify-center px-4 py-12">
      <div className="w-full max-w-md">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Dog Suitability Checker</h1>
          <p className="mt-2 text-gray-500">
            Check if a dog breed is suitable for the current weather in your city
          </p>
        </div>

        <CheckerForm onSubmit={handleSubmit} loading={loading} />

        {loading && (
          <p className="mt-6 text-center text-blue-600 font-semibold animate-pulse">
            Checking...
          </p>
        )}

        {error && (
          <div className="mt-6 bg-red-50 border border-red-200 text-red-700 rounded-lg px-4 py-3 text-sm">
            {error}
          </div>
        )}

        {result && <ResultCard result={result} />}
      </div>
    </div>
  );
}

export default App;
