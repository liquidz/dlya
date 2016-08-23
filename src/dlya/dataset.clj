(ns dlya.dataset
  (:require
    [dlya.dataset.csv   :as ddc]
    [dlya.dataset.iris  :as ddi]
    [dlya.dataset.mnist :as ddm]
    [clojure.java.io    :as io]
    [clojure.spec       :as s])
  (:import
    [org.deeplearning4j.datasets.datavec RecordReaderDataSetIterator]
    [org.deeplearning4j.datasets.iterator BaseDatasetIterator]))

(s/def ::batch-size int?)
(s/def ::examples   int?)
(s/def ::train      boolean?)
(s/def ::seed       int?)

(s/def :csv/type #{"csv"})
(s/def :csv/path string?)
(s/def :csv/skip int?)
(s/def :csv/delimiter string?)
(s/def :csv/no-label boolean?)
(s/def :csv/label-index int?)
(s/def :csv/num-classes int?)

(s/def :csv/dataset
       (s/keys :req-un [:csv/type ::path]
               :opt-un [::batch-size :csv/skip :csv/delimiter
                        :csv/no-label :csv/label-index
                        :csv/num-classes]))

(s/def :iris/type #{"iris"})
(s/def :iris/dataset
  (s/keys :req-un [:iris/type]
          :opt-un [::batch-size ::examples]))

(s/def :mnist/type #{"mnist"})
(s/def :mnist/dataset
  (s/keys :req-un [:mnist/type]
          :opt-un [::batch-size ::train ::seed]))

(s/def ::dataset (s/or :csv   :csv/dataset
                       :iris  :iris/dataset
                       :mnist :mnist/dataset))

(s/fdef convert
  :args (s/cat :opt ::dataset)
  :ret #(or (instance? RecordReaderDataSetIterator %)
            (instance? BaseDatasetIterator %)))

(defn convert
  [opt]
  (case (:type opt)
    "csv"   (ddc/load-csv opt)
    "iris"  (ddi/iris opt)
    "mnist" (ddm/mnist opt)))
