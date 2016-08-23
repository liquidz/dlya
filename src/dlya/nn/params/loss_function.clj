(ns dlya.nn.params.loss-function
  (:require
    [clojure.spec :as s])
  (:import
    [org.nd4j.linalg.lossfunctions LossFunctions$LossFunction]))

(s/def ::loss-function
  #{"custom" "expll" "mcxent" "mse" "negativeloglikelihood"
    "reconstruction-crossentropy" "rmse-xent" "squared-loss" "xent"})

(s/fdef convert
  :args (s/cat :k ::loss-function)
  :ret  (partial instance? LossFunctions$LossFunction))

(defn convert
  [k]
  (case k
    "custom"                      LossFunctions$LossFunction/CUSTOM
    "expll"                       LossFunctions$LossFunction/EXPLL
    "mcxent"                      LossFunctions$LossFunction/MCXENT
    "mse"                         LossFunctions$LossFunction/MSE
    "negativeloglikelihood"       LossFunctions$LossFunction/NEGATIVELOGLIKELIHOOD
    "reconstruction-crossentropy" LossFunctions$LossFunction/RECONSTRUCTION_CROSSENTROPY
    "rmse-xent"                   LossFunctions$LossFunction/RMSE_XENT
    "squared-loss"                LossFunctions$LossFunction/SQUARED_LOSS
    "xent"                        LossFunctions$LossFunction/XENT))
