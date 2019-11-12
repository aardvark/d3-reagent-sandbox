(ns d3-reagent-sandbox.prod
  (:require [d3-reagent-sandbox.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
