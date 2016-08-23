(ns dlya.nn.params.optimization-algo
  (:require
    [clojure.spec :as s])
  (:import
    [org.deeplearning4j.nn.api OptimizationAlgorithm]))

(s/def ::optimization-algo
  #{"conjugate-gradient" "hessian-free" "lbfgs"
    "line-gradient-descent" "stochastic-gradient-descent"})

(s/fdef convert
  :args (s/cat :k ::optimization-algo)
  :ret  (partial instance? OptimizationAlgorithm))

(defn convert
  [k]
  (case k
    "conjugate-gradient"          OptimizationAlgorithm/CONJUGATE_GRADIENT
    "hessian-free"                OptimizationAlgorithm/HESSIAN_FREE
    "lbfgs"                       OptimizationAlgorithm/LBFGS
    "line-gradient-descent"       OptimizationAlgorithm/LINE_GRADIENT_DESCENT
    "stochastic-gradient-descent" OptimizationAlgorithm/STOCHASTIC_GRADIENT_DESCENT))
