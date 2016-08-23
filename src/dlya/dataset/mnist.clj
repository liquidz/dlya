(ns dlya.dataset.mnist
  (:import
    [org.deeplearning4j.datasets.iterator.impl MnistDataSetIterator]))

(defn mnist
  [{:keys [batch-size train seed] :or {batch-size 10, train true, seed 123}}]
  (MnistDataSetIterator. batch-size train seed))
