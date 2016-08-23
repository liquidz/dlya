(ns dlya.spec
  (:require
    [clojure.spec :as s]))

(s/def ::config
  (s/keys :req-un [:dlya.nn.spec/model]
          :opt-un [:dlya.yaml/vars
                   :dlya.nn.train/train
                   :dlya.nn.evaluate/evaluate
                   :dlya.nn.predict/predict]))
