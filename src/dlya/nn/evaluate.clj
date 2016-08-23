(ns dlya.nn.evaluate
  (:require
    [dlya.dataset :as dd]
    [clojure.spec :as s]
    )
  (:import
    [org.deeplearning4j.eval               Evaluation]
    [org.nd4j.linalg.dataset.api.preprocessor NormalizerMinMaxScaler]
    )
  )

(s/def ::out-num int?)
(s/def ::show-accuracy boolean?)
(s/def ::show-stats boolean?)
(s/def ::normalize boolean?)

(s/def :evaluate/body (s/keys :req-un [:dlya.dataset.spec/dataset ::out-num]
                              :opt-un [::show-accuracy ::show-stats ::normalize]))
(s/def ::evaluate (s/or :single :evaluate/body
                        :multi  (s/coll-of :evaluate/body)))

(defn get-output
  [model dataset]
  (.output model (.getFeatureMatrix dataset)))

(defn evaluate
  [model {:keys [dataset show-accuracy show-stats out-num normalize]
                 :or {show-accuracy false, show-stats false, normalize false} :as opt}]
  {:pre [(s/valid? ::evaluate opt)]}
  (let [iter      (dd/convert dataset)
        ev        (Evaluation. out-num)
        processor (NormalizerMinMaxScaler.)]
    (.reset iter)
    (when normalize
      (.fit processor iter)
      (.setPreProcessor iter processor))

    (loop [_ true]
      (when (.hasNext iter)
        (let [dataset (.next iter)
              output  (get-output model dataset)]
          (.eval ev (.getLabels dataset) output)
          (recur true))))
    (when show-accuracy
      (println "Accuracy:" (.accuracy ev)))
    (when show-stats
      (println (.stats ev)))
    ev))
