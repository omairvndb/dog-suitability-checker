const config = {
  GOOD: { icon: "\u2705", border: "border-green-500", bg: "bg-green-50" },
  MODERATE: { icon: "\u26A0\uFE0F", border: "border-amber-500", bg: "bg-amber-50" },
  BAD: { icon: "\u274C", border: "border-red-500", bg: "bg-red-50" },
};

function ResultCard({ result }) {
  const { icon, border, bg } = config[result.suitability] || config.BAD;

  return (
    <div className={`mt-6 rounded-xl border-2 ${border} ${bg} p-6 text-center`}>
      <div className="text-4xl mb-2">{icon}</div>
      <div className="text-2xl font-bold mb-2">{result.suitability}</div>
      <p className="text-gray-600">
        <strong>{result.matchedBreed || result.breed}</strong> in{" "}
        <strong>{result.matchedCity || result.city}</strong>
      </p>
      {result.temperature != null && (
        <p className="text-sm text-gray-400 mt-1">
          {result.temperature}°C · {result.humidity}% humidity
        </p>
      )}
      <p className="mt-3 text-gray-700 text-sm leading-relaxed">{result.reason}</p>
    </div>
  );
}

export default ResultCard;
