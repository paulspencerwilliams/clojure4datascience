(ns chapter-1.descriptivestatistics
  (:require   [incanter.core :as i]
              [incanter.stats :as s]
              [chapter-1.data :as d]))

(defn mean [xs]
  (/ (reduce + xs)
     (count xs)))

(->> (d/load-data :uk-scrubbed)
     (i/$ "Electorate")
     (mean))

(defn median [xs]
  (let [n (count xs)
        mid (int (/ n 2))]
    (if (odd? n)
      (nth (sort xs) mid)
      (->> (sort xs)
           (drop (dec mid))
           (take 2)
           (mean)))))

(->> (d/load-data :uk-scrubbed)
     (i/$ "Electorate")
     median)

(defn variance [xs]
  (let [x-bar (mean xs)
        n (count xs)
        square-deviation (fn [x] (i/sq (- x x-bar)))]
    (mean (map square-deviation xs))))

(defn quartile [q xs]
  (let [n (dec (count xs))
        i (-> (* n q)
              (+ 1/2)
              (int))]
    (nth (sort xs) i)))

(defn my-quartile []
  (let [xs (->> (d/load-data :uk-scrubbed)
                (i/$ "Electorate"))
        f (fn [q] (quartile q xs))]
    (map f [0 1/4 1/2 3/4 1])))

(defn incanter-quartile []
  (let [xs (->> (d/load-data :uk-scrubbed)
               (i/$ "Electorate"))] 
   (s/quantile xs :probs [0 1/4 1/2 3/4 1])))
