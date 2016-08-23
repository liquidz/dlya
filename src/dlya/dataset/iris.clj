(ns dlya.dataset.iris
  (:import
    [org.deeplearning4j.datasets.iterator.impl IrisDataSetIterator]))

(defn iris
  [{:keys [batch-size examples] :or {batch-size 10, examples 1000}}]
  (IrisDataSetIterator. batch-size examples))
