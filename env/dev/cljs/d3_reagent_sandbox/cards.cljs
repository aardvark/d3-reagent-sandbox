(ns d3-reagent-sandbox.cards
  (:require
   [cljsjs.react]
   [cljsjs.react.dom]
   [reagent.core :as reagent :refer [atom]]
   [d3-reagent-sandbox.core :as core]
   [d3-reagent-sandbox.d3.examples :as d3.examples]
   [devcards.core :as dc])
  (:require-macros
   [devcards.core
    :as dc
    :refer [defcard defcard-doc defcard-rg deftest]]))

(defcard-rg first-card
  [:div>h1 "This is your first devcard!"])

(defcard-rg d3-card
  [core/d3-page])

(defcard-rg example-page-card
  [d3.examples/example-page "Figure 6-11. Row of data circles"
                            d3.examples/svg-circles-6-11])

(defcard-rg circles-compose
  [d3.examples/example-page "Figure 6-12. Colorfull data circles"
   d3.examples/svg-circles-6-12])

(defcard-rg simple-bars
  [d3.examples/example-page  "Figure 6-14. One lonely bar"
   d3.examples/svg-bars-6-14])

(defcard-rg by-function-x-bars
  [d3.examples/example-page  "Figure 6-15. Twenty bars"
   d3.examples/svg-bars-6-15])

(defcard-rg by-function-x-bars
  [d3.examples/example-page  "Figure 6-19. Twenty evenly spaced bars with dynamic widths"
   d3.examples/svg-bars-6-19])

(defcard-rg dynamic-bars
  [d3.examples/example-page  "Figure 6-25. Growing bigger from above"
   d3.examples/svg-bars-6-25])

;(defcard-rg home-page-card
;  [core/home-page])

(reagent/render [:div] (.getElementById js/document "app"))

(devcards.core/start-devcard-ui!)
;; remember to run 'lein figwheel devcards' and then browse to
;; http://localhost:3449/cards
