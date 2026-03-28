const suitabilityConfig = {
  GOOD: { icon: "\u2705", border: "border-green-500", bg: "bg-green-50" },
  MODERATE: { icon: "\u26A0\uFE0F", border: "border-amber-500", bg: "bg-amber-50" },
  BAD: { icon: "\u274C", border: "border-red-500", bg: "bg-red-50" },
};

// Renders a 1-5 rating as filled and empty circles
function RatingDots({ value }) {
  return (
    <div className="flex gap-1">
      {[1, 2, 3, 4, 5].map((i) => (
        <span
          key={i}
          className={`w-3 h-3 rounded-full ${
            i <= value ? "bg-indigo-500" : "bg-gray-200"
          }`}
        />
      ))}
    </div>
  );
}

// All traits with their display labels
const traits = [
  { key: "goodWithChildren", label: "Good with Children" },
  { key: "goodWithOtherDogs", label: "Good with Other Dogs" },
  { key: "goodWithStrangers", label: "Good with Strangers" },
  { key: "playfulness", label: "Playfulness" },
  { key: "energy", label: "Energy" },
  { key: "trainability", label: "Trainability" },
  { key: "protectiveness", label: "Protectiveness" },
  { key: "barking", label: "Barking" },
  { key: "shedding", label: "Shedding" },
  { key: "grooming", label: "Grooming" },
  { key: "drooling", label: "Drooling" },
  { key: "coatLength", label: "Coat Length" },
];

function ResultCard({ result }) {
  const { icon, border, bg } =
    suitabilityConfig[result.suitability] || suitabilityConfig.BAD;

  const breedInfo = result.breedInfo;

  return (
    <div className={`mt-6 rounded-xl border-2 ${border} ${bg} p-6`}>
      {/* Breed image */}
      {breedInfo?.imageLink && (
        <div className="flex justify-center mb-4">
          <img
            src={breedInfo.imageLink}
            alt={result.matchedBreed || result.breed}
            className="w-48 h-48 object-cover rounded-xl shadow-md"
          />
        </div>
      )}

      {/* Suitability verdict */}
      <div className="text-center">
        <div className="text-4xl mb-2">{icon}</div>
        <div className="text-2xl font-bold mb-2">{result.suitability}</div>
        <p className="text-gray-600">
          <strong>{result.matchedBreed || result.breed}</strong> in{" "}
          <strong>{result.matchedCity || result.city}</strong>
        </p>
        {result.temperature != null && (
          <p className="text-sm text-gray-400 mt-1">
            {result.temperature}&deg;C &middot; {result.humidity}% humidity
          </p>
        )}
        <p className="mt-3 text-gray-700 text-sm leading-relaxed">
          {result.reason}
        </p>
      </div>

      {/* Dog breed details (only shown when data is available) */}
      {breedInfo && (
        <div className="mt-6 border-t border-gray-200 pt-4">
          <h3 className="text-sm font-semibold text-gray-500 uppercase mb-3">
            Breed Traits
          </h3>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            {traits.map(({ key, label }) => (
              <div key={key} className="flex items-center justify-between">
                <span className="text-sm text-gray-600">{label}</span>
                <RatingDots value={breedInfo[key]} />
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

export default ResultCard;
