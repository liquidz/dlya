(ns dlya.yaml
  (:require
    [clj-yaml.core   :as yaml]
    [clojure.java.io :as io]
    [clojure.string  :as str]
    [clojure.spec    :as s]))

(s/def ::value (s/or :s string? :n number? :b boolean?))
(s/def ::vars (s/map-of keyword? ::value))

(defn- var-name
  [k]
  (str "$" (name k)))

(defn- apply-vars
  [body vars]
  {:pre [(s/valid? ::vars vars)]}
  (reduce
    (fn [res [k v]]
      (str/replace res (var-name k) (str v)))
    body
    vars))

(defn load-yaml
  [file]
  (let [body (slurp file)
        vars (-> body yaml/parse-string :vars)
        body (if (nil? vars) body (apply-vars body vars))]
    (yaml/parse-string body)))
