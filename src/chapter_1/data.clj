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
