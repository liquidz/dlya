(ns dlya.nn.spec
  (:require
    [clojure.spec :as s]))

(s/def ::import string?)
(s/def ::seed int?)
(s/def ::iterations int?)
(s/def ::learning-rate double?)
(s/def ::regularization boolean?)
(s/def ::momentum double?)
(s/def ::l2 double?)
(s/def ::layer (s/coll-of :dlya.nn.params.layer/layer))
(s/def ::backprop boolean?)
(s/def ::pretrain boolean?)

(s/def ::model
  (s/keys :req-un [(or ::layer ::import)]
          :opt-un [::seed
                   ::iterations
                   ::learning-rate
                   ::regularization
                   ::momentum
                   ::l2
                   :dlya.nn.params.optimization-algorith/optimization-algo
                   :dlya.nn.params.weight-init/weight-init
                   :dlya.nn.params.updater/updater
                   ::backprop
                   ::pretrain]))


