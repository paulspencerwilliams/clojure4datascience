(ns chapter-1.data
  (:require  [clojure.java.io :as io]
             [incanter.core :as i]
             [incanter.excel :as xls]))

(defmulti load-data identity)

(defmethod load-data :uk [_]
  (-> (io/resource "UK2010.xls")
      (str)
      (xls/read-xls)))

(defn uk-column-names []
  (i/col-names (load-data :uk)))

(i/$ "Election Year" (load-data :uk))


(->> (load-data :uk)
     (i/$ "Election Year")
     (distinct))

(->> (load-data :uk)
     (i/$ "Election Year")
     (frequencies))

(-> (load-data :uk)
    (i/query-dataset {"Election Year" {:$eq nil}}))

(->> (load-data :uk)
     (i/$where {"Election Year" {:$eq nil}})
     (i/to-map))

(->> (load-data :uk)
     (i/$where {"Election Year" {:$eq nil}}))

(defmethod load-data :uk-scrubbed [_]
  (->> (load-data :uk)
       (i/$where {"Election Year" {:$ne nil}})))

(->> (load-data :uk-scrubbed)
     (i/$ "Electorate")
     (count))
