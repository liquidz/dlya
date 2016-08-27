(ns dlya.dataset.csv
  (:require
    [dlya.dataset.list :as ddl]
    [clojure.string    :as str]
    [clojure.java.io   :as io]))

(defn- parse
  [path delimiter]
  (let [del (re-pattern delimiter)]
    (->> path io/file slurp str/split-lines
         (map #(str/split % del)))))

(defn load-csv
  "
  return
    //org.nd4j.linalg.dataset.DataSet
    org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator
  "
  [{:keys [path skip delimiter] :or {skip 0, delimiter ","} :as opt}]
  (let [csv (drop skip (parse path delimiter))]
    (ddl/convert (merge opt {:contents csv}))))
