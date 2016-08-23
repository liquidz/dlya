(ns dlya.nn
  (:require
    [dlya.nn.params.weight-init       :as dnpwi]
    [dlya.nn.params.optimization-algo :as dnpoa]
    [dlya.nn.params.updater           :as dnpu]
    [dlya.nn.params.layer             :as dnpl]
    [dlya.nn.serializer               :as dns]
    dlya.nn.spec
    [clojure.spec    :as s]
    [clojure.java.io :as io])
  (:import
    [org.deeplearning4j.nn.conf       NeuralNetConfiguration$Builder]
    [org.deeplearning4j.nn.multilayer MultiLayerNetwork]))

(defmacro def-setter-fn
  [key & {:keys [method converter] :or {converter identity}}]
  (let [fn-name     (->> key name (str "set-") symbol)
        method-name (symbol (str "." (name (or method key))))]
    `(defn ~fn-name
       [[builder# {v# ~key :as opt#}]]
       [(if v# (~method-name builder# (~converter v#)) builder#) opt#])))

(def-setter-fn :seed)
(def-setter-fn :optimization-algo :method "optimizationAlgo" :converter dnpoa/convert)
(def-setter-fn :iterations)
(def-setter-fn :weight-init   :method "weightInit" :converter dnpwi/convert)
(def-setter-fn :learning-rate :method "learningRate")
(def-setter-fn :regularization)
(def-setter-fn :l2)
(def-setter-fn :momentum)
(def-setter-fn :updater :converter dnpu/convert)
(def-setter-fn :backprop)
(def-setter-fn :pretrain)

(defn set-layer
  [[builder {v :layer :as opt}]]
  [(if v
     (let [lb (.list builder)]
       (dotimes [n (count v)]
         (.layer lb n (dnpl/convert (nth v n))))
       lb)
     builder)
   opt])

(def neural-net-default-config
  {:seed              123
   :iterations        1
   :optimization-algo "stochastic-gradient-descent"
   :learning-rate     0.001
   :weight-init       "relu"
   :updater           "nesterovs"
   :backprop          true
   :pretrain          false})

(defn neural-net-config
  [opt]
  (-> [(NeuralNetConfiguration$Builder.)
       (merge neural-net-default-config opt)]
      set-seed
      set-optimization-algo
      set-iterations
      set-weight-init
      set-learning-rate
      set-regularization
      set-momentum
      set-l2
      set-updater
      set-layer
      set-backprop
      set-pretrain
      first (.build)))

(defn neural-net
  [opt]
  {:pre [(s/valid? :dlya.nn.spec/model opt)]}
  (if-let [path (:import opt)]
    (dns/restore-model path)
    (MultiLayerNetwork.
      (neural-net-config opt))))



;(defn print-diff
;  [model dataset]
;  (let [output (get-output model dataset)]
;    (dotimes [i (.rows output)]
;      (let [actual   (.. dataset getLabels (getRow i) toString trim)
;            expected (.. output  (getRow i) toString trim)]
;        (println [actual expected])))))

