import { useState } from "react";
import SuitabilityForm from "./components/SuitabilityForm";
import AnalysisResult from "./components/AnalysisResult";
import { PawPrint, Loader2 } from "lucide-react";

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

  // Determine accent color for the right pane if we have a result
  let accentColor = "bg-transparent";
  if (result) {
    if (result.suitability === "GOOD") accentColor = "bg-green-500 shadow-[0_0_80px_rgba(34,197,94,0.3)]";
    else if (result.suitability === "MODERATE") accentColor = "bg-yellow-400 shadow-[0_0_80px_rgba(250,204,21,0.3)]";
    else if (result.suitability === "BAD") accentColor = "bg-red-500 shadow-[0_0_80px_rgba(239,68,68,0.3)]";
  }

  return (
    <div className="min-h-screen bg-[#EAE8E3] flex items-center justify-center p-4 sm:p-8 font-sans">
      <div className="w-full max-w-[1100px] h-auto min-h-[700px] bg-[#F5F4F0] rounded-[2.5rem] shadow-2xl overflow-hidden flex flex-col md:flex-row border-4 border-white/60">
        
        {/* Left Pane (Context & Form) */}
        <div className="w-full md:w-1/2 p-10 lg:p-14 flex flex-col justify-center relative z-10">
          <div className="mb-10 text-left">
            <h1 className="text-4xl lg:text-5xl font-extrabold text-[#2A2A2A] leading-tight tracking-tight mb-4">
              Pawsitive Match
            </h1>
            <p className="text-lg text-gray-500 leading-relaxed font-medium">
              We check if your dream dog breed thrives in your local climate. Give us the details, we'll give you the verdict.
            </p>
          </div>

          <SuitabilityForm onSubmit={handleSubmit} loading={loading} />

          {loading && (
            <div className="mt-8 flex items-center gap-3 text-[#2A2A2A] font-semibold animate-pulse">
              <Loader2 className="w-5 h-5 animate-spin" />
              Analyzing climate data...
            </div>
          )}

          {error && (
            <div className="mt-8 bg-red-50 border-l-4 border-red-500 text-red-700 rounded-lg rounded-l-none px-5 py-4 text-sm font-medium shadow-sm">
              {error}
            </div>
          )}
        </div>

        {/* Right Pane (Dynamic Result) */}
        <div className="w-full md:w-1/2 p-4 md:p-6 lg:p-8 flex items-center justify-center relative">
          <div className="absolute inset-0 bg-[#161616] rounded-4xl m-2 shadow-inner overflow-hidden">
            {/* Soft background glow based on result */}
            {result && (
              <div 
                className={`absolute top-0 right-0 w-96 h-96 rounded-full blur-[120px] opacity-40 transition-all duration-1000 -translate-y-1/2 translate-x-1/3 ${
                  result.suitability === "GOOD" ? "bg-green-500" :
                  result.suitability === "MODERATE" ? "bg-amber-500" : "bg-red-500"
                }`}
              ></div>
            )}
            
            <div className="relative w-full h-full p-8 lg:p-10 flex flex-col justify-center z-10">
              {result ? (
                <AnalysisResult result={result} />
              ) : (
                <div className="flex flex-col items-center justify-center text-center h-full text-gray-400">
                  <div className="w-20 h-20 mb-6 rounded-full bg-white/5 flex items-center justify-center border border-white/10 shadow-lg">
                    <PawPrint className="w-8 h-8 text-white/80" />
                  </div>
                  <h2 className="text-2xl font-bold text-white mb-2">Awaiting Context</h2>
                  <p className="text-gray-500 text-sm max-w-[250px] leading-relaxed">
                    Submit the form to generate a localized suitability profile.
                  </p>
                </div>
              )}
            </div>
          </div>
        </div>

      </div>
    </div>
  );
}

export default App;
