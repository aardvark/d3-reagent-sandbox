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
   Figure 6-12"
  []
  (fn [chart-div]
    (-> (circles chart-div)
        (.attr "fill" "yellow")
        (.attr "stroke" "orange")
        (.attr "stroke-width" (fn [x] (/ x 2))))
    nil))

(defn bars
  [chart-div data x bar-width]
  (let [height  100
        width   500
        dataset data
        dataset (into-array dataset)
        x       x
        svg     (-> d3
                    (.select chart-div)
                    (.append "svg")
                    (.attr "height" height)
                    (.attr "width" width))
        rect    (-> svg
                    (.selectAll "rect")
                    (.data dataset)
                    (.enter)
                    (.append "rect")
                    (.attr "x" x)
                    (.attr "y" 0)
                    (.attr "width" (bar-width width data))
                    (.attr "height" 100))]))
                

(defn svg-bars-6-14
  []
  (fn [chart-div]
    (bars chart-div
          [5 10 13 19 21 25 22 18 15 13 11 12 15 20 18 17 16 18 23 25]
          0
          (fn [_ _] 20))
          
    nil))

(defn svg-bars-6-15
  []
  (fn [chart-div]
    (bars chart-div
          [5 10 13 19 21 25 22 18 15 13 11 12 15 20 18 17 16 18 23 25]
          (fn [_ i] (* i 21))
          (fn [_ _] 20))        
    nil))

(defn spaced-bars
  [chart-div data bar-padding]
  (let [height  100
        width   500
        dataset data
        dataset (into-array dataset)
        rect-width (/ width (count data))
        svg     (-> d3
                    (.select chart-div)
                    (.append "svg")
                    (.attr "height" height)
                    (.attr "width" width))
        rect    (-> svg
                    (.selectAll "rect")
                    (.data dataset)
                    (.enter)
                    (.append "rect")
                    (.attr "x" (fn [_ i] (* i rect-width)))
                    (.attr "y" 0)
                    (.attr "width" (- rect-width bar-padding))
                    (.attr "height" 100))]))

(defn svg-bars-6-19
  []
  (fn [chart-div]
    (spaced-bars chart-div
          [5 10 13 19 21 25 22 18 15 13 11 12 15 20 18 17 16 18 23 25]
          5)
    nil))
  

  
