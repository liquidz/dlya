(ns dlya.dataset.list
  (:import
    [org.datavec.api.records.reader.impl.collection ListStringRecordReader]
    [org.datavec.api.split ListStringSplit ]
    [org.deeplearning4j.datasets.datavec RecordReaderDataSetIterator]))

(defn to-string-list
  [contents]
  (map #(map str %) contents))

(defn- last-index
  [contents]
  (-> contents first count dec))

(defn- labels
  [contents label-index]
  (->> contents
       (map #(nth % label-index))
       distinct))

(defn convert
  [{:keys [contents batch-size no-label label-index num-labels]
           :or {no-label false}}]
  (let [contents (to-string-list contents)
        splt (ListStringSplit. contents)
        rr   (doto (ListStringRecordReader.) (.initialize splt))]
    (if no-label
      (RecordReaderDataSetIterator. rr batch-size)
      (let [label-index (or label-index (last-index contents))
            num-labels  (or num-labels  (count (labels contents label-index)))]
        (.reset rr)
        (RecordReaderDataSetIterator. rr batch-size label-index num-labels)))))
