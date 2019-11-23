(ns d3-reagent-sandbox.cards
  (:require
   [cljsjs.react]
   [cljsjs.react.dom]
   [reagent.core :as reagent :refer [atom]]
   [d3-reagent-sandbox.core :as core]
   [devcards.core :as dc])
  (:require-macros
   [devcards.core
    :as dc
    :refer [defcard defcard-doc defcard-rg deftest]]))

(defcard-rg first-card
  [:div>h1 "This is your first devcard!"])

(defcard-rg d3-card
  [core/d3-page])

(defcard-rg d3-2
  [core/d3-page2])

;(defcard-rg home-page-card
;  [core/home-page])

(reagent/render [:div] (.getElementById js/document "app"))

(devcards.core/start-devcard-ui!)
;; remember to run 'lein figwheel devcards' and then browse to
;; http://localhost:3449/cards
