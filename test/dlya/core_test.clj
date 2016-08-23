(ns dlya.core-test
  (:require
    [clojure.test :refer :all]
    [clojure.string :as str]
    [clojure.spec.test :as stest]
    ;[dlya.core :refer :all]
    ;dlya.nn.params.weight-init
    ))

;(defn my-fixture
;  [f]
;  (binding [*target-ns* (-> *ns* ns-name str (str/replace #"-test$" "") symbol)]
;    (f)
;    )
;  )

;(use-fixtures :once my-fixture)

(deftest specs-test
  (doseq [n '(
              dlya.nn.params.weight-init
              ;dlya.nn.params.updater
              ;dlya.nn.params.optimization-algo
              ;dlya.nn.params.loss-function
              ;dlya.nn.params.layer
              ;dlya.dataset
              )]
    (println "n=" n)
    (let [ret (-> n stest/enumerate-namespace 
                  stest/check
                  )]
      (println ret)
      #_(doseq [x ret]
        (let [y (-> x stest/abbrev-result)]
          (is (not (contains? y :failure)) (:failure y))))
      ))
  (is false)
  )
