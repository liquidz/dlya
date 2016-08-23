(ns dlya.nn.serializer
  (:require
    [clojure.java.io :as io])
  (:import
    [org.deeplearning4j.util ModelSerializer]))

(defn write-model
  [filename model]
  (ModelSerializer/writeModel model (io/file filename) true))

(defn restore-model
  [filename]
  (ModelSerializer/restoreMultiLayerNetwork (io/file filename)))
