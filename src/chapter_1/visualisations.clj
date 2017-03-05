(ns chapter-1.visualisations
  (:require   [incanter.core :as i]
              [incanter.charts :as c]
              [incanter.distributions :as d]
              [incanter.stats :as s]
              [chapter-1.data :refer :all]
              [chapter-1.descriptivestatistics :refer :all]))

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

(defn honest-baker [mean sd]
  (let [distribution (d/normal-distribution mean sd)]
    (repeatedly #(d/draw distribution))))

(defn visualise-honest-baker []
  (-> (take 10000 (honest-baker 1000 30))
      (c/histogram :x-label "Honest baker"
                   :nbins 25)
      (i/view)))

(defn dishonest-baker [mean sd]
  (let [distribution (d/normal-distribution mean sd)]
    (->> (repeatedly #(d/draw distribution))
         (partition 13)
         (map (partial apply max)))))

(defn visualise-dishonest-baker []
  (-> (take 10000 (dishonest-baker 950 30))
      (c/histogram :x-label "Dishonest baker"
                   :nbins 25)
      (i/view)))

(defn skew-of-loaves [loaves]
  (let [weights (take 10000 loaves)]
    {:mean (mean weights)
     :median (median weights)
     :skewness (s/skewness weights)}))

(defn skew-of-dishonest-baker []
  (skew-of-loaves (dishonest-baker 950 30)))

(defn skew-of-honest-baker []
  (skew-of-loaves (honest-baker 1000 30)))

(defn visualise-with-qq-plots-to-compare-bakers []
  (->> (honest-baker 1000 30)
       (take 10000)
       (c/qq-plot)
       (i/view))
  (->> (dishonest-baker 950 30)
       (take 10000)
       (c/qq-plot)
       (i/view)))

(defn visualise-with-box-plots-to-compare-bakers []
  (-> (c/box-plot (->> (honest-baker 1000 30)
                       (take 10000))
                  :legend true
                  :y-label "Loaf weight (g)"
                  :series-label "Honest baker")
      (c/add-box-plot (->> (dishonest-baker 950 30)
                           (take 10000))
                      :series-label "Dishonest baker")
      (i/view)))
