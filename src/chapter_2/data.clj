(ns chapter-2.data
  (:require  [clojure.java.io :as io]
             [incanter.core :as i]
             [incanter.io :as iio]))
(defn load-data [file]
  (-> (io/resource file)
      (iio/read-dataset :header true :delim \tab)))

(defn show-data []
  (-> (load-data "ch2/dwell-times.tsv")
                       (i/view)))
