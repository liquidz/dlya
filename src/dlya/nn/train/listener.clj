(ns dlya.nn.train.listener
  (:require
    [clojure.spec :as s])
  (:import
    [org.deeplearning4j.optimize.listeners    ScoreIterationListener]
    [org.deeplearning4j.ui.weights            HistogramIterationListener]
    [org.deeplearning4j.ui.flow               FlowIterationListener]))

(s/def :listener/score-iteration int?)
(s/def :listener/histgram-iteration int?)
(s/def :listener/flow-iteration int?)
(s/def ::listener (s/keys :req-un [(or :listener/score-iteration
                                       :listener/histgram-iteration
                                       :listener/flow-iteration)]))

(defn convert
  [opt]
  (remove
    nil?
    [(some-> opt :score-iteration    (ScoreIterationListener.))
     (some-> opt :histgram-iteration (HistogramIterationListener.))
     (some-> opt :flow-iteration     (FlowIterationListener.))]))
