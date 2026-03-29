import { Check, AlertTriangle, X, Dna } from "lucide-react";

// Configuration for the different suitability states
const suitabilityConfig = {
  GOOD: { icon: <Check className="w-6 h-6" />, color: "text-green-400", bgIcon: "bg-green-500/20 text-green-400 border-green-500/30" },
  MODERATE: { icon: <AlertTriangle className="w-6 h-6" />, color: "text-yellow-400", bgIcon: "bg-yellow-500/20 text-yellow-400 border-yellow-500/30" },
  BAD: { icon: <X className="w-6 h-6" />, color: "text-red-400", bgIcon: "bg-red-500/20 text-red-400 border-red-500/30" },
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
  const { icon, color, bgIcon } =
    suitabilityConfig[result.suitability] || suitabilityConfig.BAD;

  const breedInfo = result.breedInfo;

  return (
    <div className="text-white w-full h-full flex flex-col pt-2 md:pt-4">
      
      {/* Header section with Verdict Summary */}
      <div className="flex items-start justify-between mb-8">
        <div>
          <h2 className="text-2xl md:text-3xl font-bold mb-1.5 tracking-tight text-white/90">
            Suitability Analysis
          </h2>
          <p className="text-gray-400 text-sm md:text-base font-medium">
            in <span className="text-white">{result.matchedCity || result.city}</span>
          </p>
        </div>
        <div className={`w-12 h-12 rounded-full flex items-center justify-center border-2 text-xl font-extrabold ${bgIcon} shadow-lg shrink-0 ml-4`}>
          {icon}
        </div>
      </div>

      <div className="grow flex flex-col gap-6 overflow-y-auto pr-2 custom-scrollbar pb-4">
        
        {/* Main Verdict block */}
        <div className="bg-white/5 border border-white/10 rounded-3xl p-6 backdrop-blur-md relative overflow-hidden group">
           <div className="absolute top-0 left-0 w-2 h-full bg-current opacity-30 group-hover:opacity-100 transition-opacity" style={{ color: color.replace('text-', '') }} />
           
           <div className="flex flex-wrap items-center gap-3 mb-3 pl-2">
             <span className={`text-xl font-extrabold uppercase tracking-widest ${color}`}>
               {result.suitability}
             </span>
             {result.temperature != null && (
               <span className="text-gray-400 text-xs font-mono bg-white/10 px-2.5 py-1.5 rounded-lg border border-white/5">
                 {result.temperature}&deg;C &bull; {result.humidity}% humidity
               </span>
             )}
           </div>
           
           <div className="pl-2">
             <h3 className="text-xl font-semibold text-white mb-2">
               {result.matchedBreed || result.breed}
             </h3>
             <p className="text-gray-300 text-sm leading-relaxed">
               {result.reason}
             </p>
           </div>
        </div>

        {/* Breed details section */}
        {breedInfo && (
          <div className="mt-2 text-white px-1">
            
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
              <div className="flex items-center gap-3 mb-5 border-b border-white/10 pb-3">
                <Dna className="w-5 h-5 text-white/70" />
                <h4 className="text-xs font-bold text-gray-400 uppercase tracking-widest">
                  Breed Characteristics
                </h4>
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
