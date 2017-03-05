(ns chapter-1.visualisations
  (:require   [incanter.core :as i]
              [incanter.charts :as c]
              [incanter.distributions :as d]
              [chapter-1.data :refer :all]
              [chapter-1.descriptivestatistics :as ds]))

(defn visualise-histograph []
  (-> (i/$ "Electorate" (load-data :uk-scrubbed))
      (c/histogram)
      (i/view)))

(defn visualise-with-too-many-bins []
  (-> (i/$ "Electorate" (load-data :uk-scrubbed))
      (c/histogram :nbins 200)
      (i/view)))

(defn visualise-with-best-20-bins []
  (-> (i/$ "Electorate" (load-data :uk-scrubbed))
      (c/histogram :x-label "UK electorate"
                   :nbins 20)
      (i/view)))

(defn visualise-uniform-distribution []
  (let [xs (->> (repeatedly rand)
                (take 10000))]
    (-> (c/histogram xs
                     :x-label "Uniform distribution"
                     :nbins 20)
        (i/view))))

(defn visualise-means-of-random-sequences []
  (let [xs (->> (repeatedly rand)
                (partition 10)
                (map )
                (take 10000))]
    (-> (c/histogram xs
                     :x-label "Distribution of means"
                     :nbins 20)
        (i/view))))

(defn visualise-incanters-normal-distributions []
  (let [distribution (d/normal-distribution)
        xs (->> (repeatedly #(d/draw distribution))
                (take 10000))]
    (-> (c/histogram xs
                     :x-label "Normal distribution"
                     :nbins 20)
        (i/view))))
