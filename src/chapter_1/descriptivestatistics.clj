(ns chapter-1.descriptivestatistics
  (:require   [incanter.core :as i]
              [chapter-1.data :as d]))

(defn mean [xs]
  (/ (reduce + xs)
     (count xs)))

(->> (d/load-data :uk-scrubbed)
     (i/$ "Electorate")
     (mean))
