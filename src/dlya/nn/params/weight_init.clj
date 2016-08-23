(ns dlya.nn.params.weight-init
  (:require
    [clojure.spec :as s])
  (:import
    [org.deeplearning4j.nn.weights WeightInit]))

(s/def ::weight-init
  #{"distribution" "normalized" "relu"
    "size" "uniform" "vi" "xavier" "zero"})

(s/fdef convert
  :args (s/cat :k ::weight-init)
  :ret  (partial instance? WeightInit))

(defn convert
  [k]
  (case k
    "distribution" WeightInit/DISTRIBUTION
    "normalized"   WeightInit/NORMALIZED
    "relu"         WeightInit/RELU
    "size"         WeightInit/SIZE
    "uniform"      WeightInit/UNIFORM
    "vi"           WeightInit/VI
    "xavier"       WeightInit/XAVIER
    "zero"         WeightInit/ZERO))
