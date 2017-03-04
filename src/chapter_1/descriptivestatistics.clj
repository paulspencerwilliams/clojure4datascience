(ns chapter-1.descriptivestatistics
  (:require   [incanter.core :as i]
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
