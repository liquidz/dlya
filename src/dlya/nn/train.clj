(ns dlya.nn.train
  (:require
    [dlya.dataset :as dd]
    [dlya.nn.serializer :as dns]
    [dlya.nn.train.listener :as dntl]
    [clojure.spec :as s])
  (:import
    [org.nd4j.linalg.dataset.api.preprocessor NormalizerMinMaxScaler]))

(s/def ::epoch int?)
(s/def ::export string?)
(s/def ::normalize boolean?)

(s/def ::train
  (s/keys :req-un [:dlya.dataset/dataset]
          :opt-un [::epoch :dlya.nn.train.listener/listener
                   ::normalize ::export]))

(defn train
  [model {:keys [epoch listener dataset normalize export] :or {epoch 1, normalize false} :as opt}]
  {:pre [(s/valid? ::train opt)]}
  (doto model
    (.init)
    (.setListeners (dntl/convert listener)))
  (let [iter (dd/convert dataset)
        processor (NormalizerMinMaxScaler.)]
    (when normalize
      (.fit processor iter)
      (.setPreProcessor iter processor))
    (dotimes [n epoch]
      (.reset iter)
      (.fit model iter)))
  (when export
    (dns/write-model export model))
  model)
