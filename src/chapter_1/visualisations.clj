(ns chapter-1.visualisations
  (:require   [incanter.core :as i]
              [incanter.charts :as c]
              [chapter-1.data :as d])OB)

(-> (i/$ "Electorate" (d/load-data :uk-scrubbed))
    (c/histogram)
    (i/view))

(-> (i/$ "Electorate" (d/load-data :uk-scrubbed))
    (c/histogram :nbins 200)
    (i/view))

(-> (i/$ "Electorate" (d/load-data :uk-scrubbed))
    (c/histogram :x-label "UK electorate"
                 :nbins 20)
    (i/view))
