import { Dna } from "lucide-react";

// Configuration for the different suitability states
const suitabilityConfig = {
  GOOD: { color: "text-green-400" },
  MODERATE: { color: "text-yellow-400" },
  BAD: { color: "text-red-400" },
};

// Renders a 1-5 rating as filled and empty circles
function RatingDots({ value }) {
  if (value == null) return null;
  return (
    <div className="flex gap-1.5">
      {[1, 2, 3, 4, 5].map((i) => (
        <span
          key={i}
          className={`w-1.5 h-1.5 rounded-full ${
            i <= value ? "bg-white" : "bg-white/20"
          }`}
        />
      ))}
    </div>
  );
}

// All traits with their display labels
const traits = [
  { key: "goodWithChildren", label: "With Children" },
  { key: "goodWithOtherDogs", label: "With Dogs" },
  { key: "goodWithStrangers", label: "With Strangers" },
  { key: "playfulness", label: "Playful" },
  { key: "energy", label: "Energy" },
  { key: "trainability", label: "Trainable" },
  { key: "protectiveness", label: "Protective" },
  { key: "barking", label: "Barking" },
  { key: "shedding", label: "Shedding" },
  { key: "grooming", label: "Grooming" },
  { key: "drooling", label: "Drooling" },
  { key: "coatLength", label: "Coat Length" },
];

function AnalysisResult({ result }) {
  const { color } =
    suitabilityConfig[result.suitability] || suitabilityConfig.BAD;

  const breedInfo = result.breedInfo;

  return (
    <div className="text-white w-full h-full flex flex-col">
      {/* Header section with Verdict Summary */}
      <div className="mb-8">
        <h2 className="text-2xl md:text-3xl font-bold mb-1.5 tracking-tight text-white/90">
          Suitability Analysis
        </h2>
        {(result.matchedBreed || result.matchedCity) && (
          <p className="text-gray-400 text-sm md:text-base font-medium">
            <span className="text-white">{result.matchedBreed || result.breed}</span> in <span className="text-white">{result.matchedCity || result.city}</span>
          </p>
        )}
      </div>

      <div className="grow flex flex-col gap-6 overflow-y-auto pr-2 custom-scrollbar pb-4">
        {/* Main Verdict block */}
        <div className="bg-white/5 border border-white/10 rounded-3xl p-6 backdrop-blur-md">
           <div className="flex flex-wrap items-center justify-between gap-4 pb-4">
             <span className={`text-xl font-extrabold uppercase tracking-widest ${color}`}>
               {result.suitability}
             </span>
             {result.temperature != null && (
               <span className="text-gray-400 text-xs font-mono bg-white/10 px-3 py-1.5 rounded-lg border border-white/5">
                 Current: {result.temperature}&deg;C &bull; {result.humidity}% humidity
               </span>
             )}
           </div>
           
           <p className="text-gray-300 text-sm md:text-base leading-relaxed">
             {result.reason}
           </p>
        </div>

        {/* Breed details section */}
        {breedInfo && (
          <div>
            {/* Optional Breed Image */}
            {breedInfo.imageLink && (
              <div className="mb-6 w-full h-40 md:h-48 rounded-3xl overflow-hidden border border-white/10 relative shadow-xl">
                <img 
                  src={breedInfo.imageLink} 
                  alt={result.matchedBreed || result.breed}
                  className="w-full h-full object-cover object-[center_20%]"
                />
                <div className="absolute inset-x-0 bottom-0 h-1/2 bg-linear-to-t from-[#161616] to-transparent"></div>
              </div>
            )}

            {/* Traits grid */}
            <div className="bg-white/5 border border-white/10 rounded-3xl p-6 backdrop-blur-md">
              <div className="pb-6">
                <div className="flex items-center gap-3 mb-2">
                  <Dna className="w-5 h-5 text-white/70" />
                  <h4 className="text-xs font-bold text-gray-400 uppercase tracking-widest">
                    Breed Characteristics
                  </h4>
                </div>
                <p className="text-sm text-gray-400 leading-relaxed md:pr-4">
                  Beyond climate, everyday compatibility matters. Review these typical traits to see if this breed aligns with your lifestyle, household, and experience level.
                </p>
              </div>
              
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-x-8 gap-y-4">
                {traits.map(({ key, label }) => (
                  breedInfo[key] != null && (
                    <div key={key} className="flex items-center justify-between group">
                      <span className="text-xs text-gray-400 group-hover:text-gray-200 transition-colors font-medium">{label}</span>
                      <RatingDots value={breedInfo[key]} />
                    </div>
                  )
                ))}
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default AnalysisResult;
