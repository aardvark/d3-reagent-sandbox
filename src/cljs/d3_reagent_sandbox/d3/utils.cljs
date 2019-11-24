(ns d3-reagent-sandbox.d3.utils
  (:require
   ["d3" :as d3]
   ["react-faux-dom" :as faux-dom]
   ["react" :as react]
   ["create-react-class" :as create-class])) 

;; d3 faux dom utils

(defn- duration [^js props] (.-animateDuration props))
(defn- mutations [^js props] (.-d3fn props))
(defn- containerCallback [^js props] (.-containerCallback props))
(defn- chart [^js props] (.-chart props))

(def Container
  (create-class
   #js {:componentDidMount (fn []
                             (this-as this
                               (let [connect            (.-connectFauxDOM (.-props this))
                                     animate            (.-animateFauxDOM (.-props this))
                                     animation-duration (duration (.-props this))
                                     callback           (containerCallback (.-props this))
                                     d3-mutations       (mutations (.-props this))]
                                        ; callback to parent component to provide access for updates
                                 (when callback (callback this))
                                        ; create the faux div and store in the "chart" prop
                                 (let [faux (connect "div" "chart")]
                                        ; mutations might be delayed so being defensive
                                   (when d3-mutations (d3-mutations faux)))
                                        ; initial animate if required
                                 (when animation-duration (animate animation-duration))

                                 nil)))
        :render (fn []
                  (this-as this
                    (if-let [c (chart (.-props this))]
                      c                 ; return the faux node when present
                                        ; otherwise create a div so that something is always returned by render
                      (.createElement react "div" #js {:className "placeholder"}))))}))


(defn container
  "create a plain component using a faux dom and a fn to mutate that dom.
   the props must contain a :d3fn arity-1 fn that performs the d3 mutation"
  [props]
  (.createElement react (faux-dom/withFauxDOM Container) (clj->js props)))

;; d3 faux dom utils end


;; d3 support functions
(defn d3el
  "Wrapper around div faux container.
   Accepts d3-function for DOM mutation"
  [d3f]
  [:div {} (container {:d3fn (d3f)})])

(defn svg-div
  [div h w]
  (-> d3 
      (.select div)
      (.append "svg")
      (.attr "height" h)
      (.attr "width" w)))

