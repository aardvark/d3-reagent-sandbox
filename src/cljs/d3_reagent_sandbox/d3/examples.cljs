(ns d3-reagent-sandbox.d3.examples
  (:require
   [d3 :as d3]
   [d3-reagent-sandbox.d3.utils :as d3.utils]))


(defn example-page [title d3fn]
  (fn []
    [:span.main
     [:h1 title]
     (d3.utils/d3el d3fn)]))

(defn- circles
  [chart-div]
  (let [height    50
        width   540
        data    [5 10 15 20 25]
        data    (into-array data)
        cx      (fn [_ i] (+ 25 (* i 50)))
        cy      25
        r       (fn [x] x) 
        svg     (-> d3
                    (.select chart-div)
                    (.append "svg")
                    (.attr "height" height)
                    (.attr "width" width))
        circles (-> svg
                    (.selectAll "circle")
                    (.data data)
                    (.enter)
                    (.append "circle"))
        circles (-> circles
                    (.attr "cx" cx)
                    (.attr "cy" cy)
                    (.attr "r" r))]
    circles))


(defn svg-circles-6-11
  "Example from \"Interractive Data Visualization for the Web\"
   Figure 6-11. Row of data circles"
  []
  (fn [chart-div]
    (circles chart-div)
    nil))

(defn svg-circles-6-12
  "Example from \"Interractive Data Visualization for the Web\"
   Figure 6-11. Row of data circles"
  []
  (fn [chart-div]
    (-> (circles chart-div)
        (.attr "fill" "yellow")
        (.attr "stroke" "orange")
        (.attr "stroke-width" (fn [x] (/ x 2))))
    nil))


