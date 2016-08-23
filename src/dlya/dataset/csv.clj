(ns dlya.dataset.csv
  (:require
    [clojure.java.io :as io])
  (:import
    [org.datavec.api.records.reader.impl.csv CSVRecordReader]
    [org.datavec.api.split FileSplit]
    [org.datavec.api.transform.schema Schema]
    [org.deeplearning4j.datasets.datavec RecordReaderDataSetIterator]))

(defn- record-reader->list
  "RecordReader をリストに変換する"
  [rr]
  (.reset rr)
  (->> (repeatedly #(if (.hasNext rr) (.next rr)))
       (take-while (comp not nil?))
       (map #(map str %))))

(defn- record-reader->labels
  "RecordReader とラベルのインデックスからユニークなラベルのリストを返す"
  [rr label-index]
  (.reset rr)
  (->> rr
       record-reader->list
       (map #(nth % label-index))
       distinct))

(defn load-csv
  "
  return
    //org.nd4j.linalg.dataset.DataSet
    org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator
  "
  [{:keys [path skip delimiter batch-size no-label label-index num-classes]
           :or {skip 0, delimiter ",", no-label false}}]
  (let [file        (io/file path)
        splt        (FileSplit. file)
        rr          (doto (CSVRecordReader. skip delimiter) (.initialize splt))
        ]
    (if no-label
      (RecordReaderDataSetIterator. rr batch-size)
      (let [label-index (or label-index (-> (.next rr) count dec))
            num-classes (or num-classes (count (record-reader->labels rr label-index)))]
        (.reset rr)
        (RecordReaderDataSetIterator. rr batch-size label-index num-classes)))))

