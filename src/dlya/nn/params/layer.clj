(ns dlya.nn.params.layer
  (:require
    [clojure.spec :as s]
    [dlya.nn.params.weight-init :as pwi]
    [dlya.nn.params.loss-function :as plf])
  (:import
    [org.deeplearning4j.nn.conf.layers OutputLayer$Builder DenseLayer$Builder]))

(def ^:private INT_MAX 2147483647)

(defn- dense-layer
  [{:keys [in out activation weight-init drop-out name]
      :or {activation  "relu"
           weight-init "relu"
           drop-out    0.0
           name (str (gensym "dense"))}}]
  (-> (doto (DenseLayer$Builder.)
        (.nIn in)
        (.nOut out)
        (.name name)
        (.activation activation)
        (.weightInit (pwi/convert weight-init))
        (.dropOut drop-out))
      (.build)))

(defn- output-layer
  [{:keys [in out activation weight-init drop-out loss-fn name]
      :or {activation  "softmax"
           weight-init "xavier"
           drop-out    0.0
           loss-fn     "mcxent"
           name (str (gensym "output"))}}]
  (-> (doto (OutputLayer$Builder.)
        (.nIn in)
        (.nOut out)
        (.name name)
        (.activation activation)
        (.weightInit (pwi/convert weight-init))
        (.dropOut drop-out)
        (.lossFunction (plf/convert loss-fn)))
      (.build)))

(s/def ::type #{"dense" "output"})
(s/def ::in (s/and int? pos?  #(< % INT_MAX)))
(s/def ::out (s/and int? pos?  #(< % INT_MAX)))
(s/def ::activation
  #{"relu" "tanh" "sigmoid" "softmax" "hardtanh"
    "leakyrelu" "maxout" "softsign" "softplus"})

(s/def ::drop-out double?)
(s/def ::name string?)

(s/def ::layer
  (s/keys :req-un [::type ::in ::out]
          :opt-un [::name ::activation ::drop-out
                   :dlya.nn.params.weight-init/weight-init
                   :dlya.nn.params.loss-function/loss-function]))

(s/fdef convert
  :args (s/cat :opt ::layer)
  :ret  (partial instance? org.deeplearning4j.nn.conf.layers.Layer))

(defn convert
  [opt]
  (case (:type opt)
    "dense"  (dense-layer opt)
    "output" (output-layer opt)))
