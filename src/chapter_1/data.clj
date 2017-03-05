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

;; (i/$ "Election Year" (load-data :uk))

;;( ->> (load-data :uk) (i/$ "Election Year") (distinct))

;; (->> (load-data :uk) (i/$ "Election Year") (frequencies))

;; (-> (load-data :uk) (i/query-dataset {"Election Year" {:$eq nil}}))

;; (->> (load-data :uk) (i/$where {"Election Year" {:$eq nil}}) (i/to-map))

;; (->> (load-data :uk) (i/$where {"Election Year" {:$eq nil}}))

(defmethod load-data :uk-scrubbed [_]
  (->> (load-data :uk)
       (i/$where {"Election Year" {:$ne nil}})))

;; (->> (load-data :uk-scrubbed) (i/$ "Electorate") (count))

(defmethod load-data :ru [_]
  (i/conj-rows (-> (io/resource "Russia2011_1of2.xls")
                   (str)
                   (xls/read-xls))
               (-> (io/resource "Russia2011_2of2.xls")
                   (str)
                   (xls/read-xls))))

;; (-> (load-data :ru) (i/col-names))

(defmethod load-data :ru-victors [_]
  (->> (load-data :ru)
       (i/rename-cols
        {"Number of voters included in voters list" :electorate
         "Number of valid ballots" :valid-ballots
         "United Russia" :victors})
       (i/add-derived-column :victors-share
                             [:victors :valid-ballots] i/safe-div)
       (i/add-derived-column :turnout
                             [:valid-ballots :electorate] /)))
