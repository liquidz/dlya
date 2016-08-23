(ns dlya.core
  (:gen-class)
  (:require
    [dlya.yaml :as yaml]
    [dlya.nn :as dn]
    [dlya.nn.train :as dnt]
    [dlya.nn.evaluate :as dne]
    [dlya.nn.predict :as dnp]
    dlya.spec
    [clojure.java.io :as io]
    [clojure.spec :as s]
    [clojure.tools.cli :refer [parse-opts]]))

(def cli-options
  [["-l" "--lint" "Lint yaml"]
   ["-r" "--repl" "REPL mode"]
   ["-h" "--help"]])

(defn- exit
  ([]  (exit 0))
  ([n] (System/exit n)))

(defn help
  [summary]
  (println "Usage:")
  (println "  java -jar dlya.jar [options] config.yml")
  (println "Options:")
  (println summary))

(defn lint
  [filename]
  (let [config (-> filename io/file yaml/load-yaml)]
    (println (s/explain-str :dlya.spec/config config))
    (s/valid? :dlya.spec/config config)))

(defn process
  [config]
  {:pre [(s/valid? :dlya.spec/config config)]}
  (let [model (-> config :model dn/neural-net)]
    (when-let [t (:train config)]
      (dnt/train model t))

    (when-let [e (:evaluate config)]
      (doseq [x (if (sequential? e) e [e])]
        (dne/evaluate model x)))

    (when-let [p (:predict config)]
      (dnp/predict model p))))

(defn run
  [filename]
  (-> filename io/file yaml/load-yaml process))

(defn -main
  [& args]
  (let [{:keys [options arguments summary errors] :as opt} (parse-opts args cli-options)
        [filename] arguments ]
    (when errors
      (doseq [e errors] (println e))
      (exit 1))

    (when (:help options)
      (help summary)
      (exit))

    (when (:lint options)
      (exit (if (lint filename) 0 1)))

    (when (:repl options)
      (clojure.main/repl :init #(in-ns 'dlya.core))
      (exit))

    (run filename)))

