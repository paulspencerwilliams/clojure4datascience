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

(defn visualise-xyplots-of-cdfs-to-compare-bakers []
  (let [sample-honest (->>  (honest-baker 1000 30) 
                            (take 1000))
        sample-dishonest (->>  (dishonest-baker 950 30) 
                               (take 1000))
        ecdf-honest (s/cdf-empirical sample-honest)
        ecdf-dishonest (s/cdf-empirical sample-dishonest)]
    (-> (c/xy-plot sample-honest
                     (map ecdf-honest sample-honest)
                     :x-label "Loaf weight (g)"
                     :y-label "Probablity"
                     :legend true
                     :series-label "Honest baker")
         (c/add-lines sample-dishonest
                      (map ecdf-dishonest sample-dishonest)
                      :series-label "Dishonest baker")
         (i/view))))
(defn visualise-russian-electorial-data-on-histogram []
  (-> (i/$ :turnout  (load-data :ru-victors))
       (c/histogram :x-label "Russia turnout"
                    :nbins 20)
       (i/view)))

(defn visualise-russian-electorial-data-on-qq []
  (->> (load-data :ru-victors)
       (i/$ :turnout)
       (c/qq-plot)
       (i/view)))

(defn as-pmf [bins]
  (let [histogram (frequencies bins)
        total (reduce + (vals histogram))]
    (->> histogram
         (map (fn [[k v]]
                [k (/ v total)]))
         (into {}))))

(defn visualise-uk-victors []
  (->> (load-data :uk-victors)
       (i/$ :victors-share)
       (c/qq-plot)
       (i/view)))

(defn visualise-pmf-of-uk-russian-elections []
  (let [n-bins 40
        uk (->> (load-data :uk-victors)
                (i/$ :turnout)
                (bin n-bins)
                (as-pmf))
        ru (->> (load-data :ru-victors)
                (i/$ :turnout)
                (bin n-bins)
                (as-pmf))]
    (-> (c/xy-plot (keys uk) (vals uk)
                   :series-label "UK"
                   :legend true
                   :x-label "Turnout Bins"
                   :y-label "Probability")
        (c/add-lines (keys ru) (vals ru)
                     :series-label "Russia")
        (i/view))))

(defn visualising-uk-turnout-via-scatter-plot []
  (let [data (load-data :uk-victors)]
    (-> (c/scatter-plot (i/$ :turnout data)
                        (i/$ :victors-share data)
                        :x-label "Turnout"
                        :y-label "Victor's share")
        (i/view))))

(defn visualising-russia-turnout-via-scatter-plot []
  (let [data (load-data :ru-victors)]
    (-> (c/scatter-plot (i/$ :turnout data)
                        (i/$ :victors-share data)
                        :x-label "Turnout"
                        :y-label "Victor's share")
        (i/view))))

(defn visualising-russian-plot-with-opacity-due-to-noise []
  (let [data (-> (load-data :ru-victors)
                 (s/sample :size 10000))]
    (-> (c/scatter-plot (i/$ :turnout data)
                        (i/$ :victors-share data)
                        :x-label "Turnout"
                        :y-label "Victor share")
        (c/set-alpha 0.05)
        (i/view))))
