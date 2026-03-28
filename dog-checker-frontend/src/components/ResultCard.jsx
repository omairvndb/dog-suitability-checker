function ResultCard({ result }) {
  const config = {
    GOOD: { icon: "\u2705", className: "result-good" },
    MODERATE: { icon: "\u26A0\uFE0F", className: "result-moderate" },
    BAD: { icon: "\u274C", className: "result-bad" },
  };

  const { icon, className } = config[result.suitability] || config.BAD;

  return (
    <div className={`result-card ${className}`}>
      <div className="result-icon">{icon}</div>
      <div className="result-suitability">{result.suitability}</div>
      <div className="result-details">
        <span>
          <strong>{result.matchedBreed || result.breed}</strong> in{" "}
          <strong>{result.matchedCity || result.city}</strong>
        </span>
      </div>
      {result.temperature != null && (
        <div className="result-weather">
          {result.temperature}°C · {result.humidity}% humidity
        </div>
      )}
      <div className="result-reason">{result.reason}</div>
    </div>
  );
}

export default ResultCard;
