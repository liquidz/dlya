(ns dlya.nn.predict
  (:require
    [dlya.dataset :as dd]
    [clojure.spec :as s]))

(s/def ::predict
  (s/keys :req-un [:dlya.dataset/dataset]))

(defn- get-output
  [model dataset]
  (.output model (.getFeatureMatrix dataset)))

(defn- iterator->datasets
  [iter]
  (.reset iter)
  (loop [res []]
    (if (.hasNext iter)
      (recur (conj res (.next iter)))
      res)))

(defn predict
  [model {:keys [dataset]}]
  (let [iter (dd/convert dataset)]
    (doseq [output (map #(get-output model %)
                        (iterator->datasets iter))]
      (dotimes [i (.rows output)]
        (println (.. output  (getRow i) toString trim))))))
