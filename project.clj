(defproject dlya "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [;[org.clojure/clojure                    "1.8.0"]
                 [org.clojure/clojure                    "1.9.0-alpha11"]
                 [org.deeplearning4j/deeplearning4j-core "0.5.0"]
                 [org.deeplearning4j/deeplearning4j-ui   "0.5.0"]
                 [org.nd4j/nd4j-native-platform          "0.5.0"]
                 [org.datavec/datavec-api                "0.5.0"]
                 [com.google.guava/guava                 "19.0"]
                 [circleci/clj-yaml                      "0.5.5"]
                 [org.clojure/tools.cli                  "0.3.5"]]


  :uberjar-name "dlya.jar"
  :main dlya.core

  :profiles {:dev {:dependencies [[org.clojure/test.check "0.9.0"]]
                   :source-paths ["dev"]}
             :uberjar {:aot  :all
                       :main dlya.core
                       :dependencies [[org.clojure/tools.cli "0.3.5"]]}}

  :plugins [[autodoc/lein-autodoc "1.1.1"]]
  :autodoc { :name "Bugs", :page-title "Bugs API Documentation"}
  )

