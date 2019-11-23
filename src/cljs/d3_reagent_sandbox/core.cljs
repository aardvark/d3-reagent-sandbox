(ns d3-reagent-sandbox.core
  (:require
   [reagent.core :as reagent :refer [atom]]
   [reagent.session :as session]
   [reitit.frontend :as reitit]
   [clerk.core :as clerk]
   [accountant.core :as accountant]
   ["d3" :as d3]
   ["react-faux-dom" :as faux-dom]
   ["react" :as react]
   ["create-react-class" :as create-class])) 

;; -------------------------
;; Routes

(def router
  (reitit/router
   [["/" :index]
    ["/d3" :d3]
    ["/items"
     ["" :items]
     ["/:item-id" :item]]
    ["/about" :about]]))

(defn path-for [route & [params]]
  (if params
    (:path (reitit/match-by-name router route params))
    (:path (reitit/match-by-name router route))))


;; -------------------------
;; Page components

(defn home-page []
  (fn []
    [:span.main
     [:h1 "Welcome to d3-reagent-sandbox"]
     [:ul
      [:li [:a {:href (path-for :d3)} "D3"]]
      [:li [:a {:href (path-for :items)} "Items of d3-reagent-sandbox"]]
      [:li [:a {:href "/broken/link"} "Broken link"]]]]))


(defn items-page []
  (fn []
    [:span.main
     [:h1 "The items of d3-reagent-sandbox"]
     [:ul (map (fn [item-id]
                 [:li {:name (str "item-" item-id) :key (str "item-" item-id)}
                  [:a {:href (path-for :item {:item-id item-id})} "Item: " item-id]])
               (range 1 60))]]))


(defn item-page []
  (fn []
    (let [routing-data (session/get :route)
          item (get-in routing-data [:route-params :item-id])]
      [:span.main
       [:h1 (str "Item " item " of d3-reagent-sandbox")]
       [:p [:a {:href (path-for :items)} "Back to the list of items"]]])))


(defn about-page []
  (fn [] [:span.main
          [:h1 "About d3-reagent-sandbox"]]))

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
                      c                         ; return the faux node when present
                                        ; otherwise create a div so that something is always returned by render
                      (.createElement react "div" #js {:className "placeholder"}))))}))


(defn container
  "create a plain component using a faux dom and a fn to mutate that dom.
   the props must contain a :d3fn arity-1 fn that performs the d3 mutation"
  [props]
  (.createElement react (faux-dom/withFauxDOM Container) (clj->js props)))
;; d3 faux dom utils end


(defn append-svg []
  (fn [chart-div]
    (-> d3
      (.select chart-div)
      (.append "svg")
      (.attr "height" 450)
      (.attr "width" 540))
    nil))

(defn d3-paragraphs []
  (fn [chart-div]
    (-> d3
        (.select chart-div)
        (.selectAll "p")
        (.data (into-array [5 10 15 20 25]))
        (.enter)
        (.append "p")
        (.text (fn [x] x)))
    nil))

(defn d3-page []
  (fn [] [:span.main
          [:h1 "D3"]
          [:div {} (container {:d3fn (d3-paragraphs)})]]))

(defn d3el
  [d3f]
  [:div {} (container {:d3fn (d3f)})])

(defn d3-page2
  []
  (fn []
    [:span.main
     [:h1 "D3-2"]
     (d3el d3-paragraphs)]))  


;; -------------------------
;; Translate routes -> page components

(defn page-for [route]
  (case route
    :index #'home-page
    :about #'about-page
    :d3    #'d3-page
    :items #'items-page
    :item  #'item-page))


;; -------------------------
;; Page mounting component

(defn current-page []
  (fn []
    (let [page (:current-page (session/get :route))]
      [:div
       [:header
        [:p [:a {:href (path-for :index)} "Home"] " | "
         [:a {:href (path-for :about)} "About d3-reagent-sandbox"]]]
       [page]
       [:footer
        [:p "d3-reagent-sandbox was generated by the "
         [:a {:href "https://github.com/reagent-project/reagent-template"} "Reagent Template"] "."]]])))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (clerk/initialize!)
  (accountant/configure-navigation!
   {:nav-handler
    (fn [path]
      (let [match        (reitit/match-by-path router path)
            current-page (:name (:data  match))
            route-params (:path-params match)]
        (reagent/after-render clerk/after-render!)
        (session/put! :route {:current-page (page-for current-page)
                              :route-params route-params})
        (clerk/navigate-page! path)))
    :path-exists?
    (fn [path]
      (boolean (reitit/match-by-path router path)))})
  (accountant/dispatch-current!)
  (mount-root))
