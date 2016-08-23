(ns dlya.nn.params.updater
  (:require
    [clojure.spec :as s])
  (:import
    [org.deeplearning4j.nn.conf Updater]))

(s/def ::updater
  #{"adadelta" "adagrad" "adam" "custom"
    "nesterovs" "none" "rmsprop" "sgd"})

(s/fdef convert
  :args (s/cat :k ::updater)
  :ret  (partial instance? Updater))

(defn convert
  [k]
  (case k
    "adadelta"  Updater/ADADELTA
    "adagrad"   Updater/ADAGRAD
    "adam"      Updater/ADAM
    "custom"    Updater/CUSTOM
    "nesterovs" Updater/NESTEROVS
    "none"      Updater/NONE
    "rmsprop"   Updater/RMSPROP
    "sgd"       Updater/SGD))

