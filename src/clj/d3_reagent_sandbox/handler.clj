(ns d3-reagent-sandbox.handler
  (:require
   [reitit.ring :as reitit-ring]
   [d3-reagent-sandbox.middleware :refer [middleware]]
   [hiccup.page :refer [include-js include-css html5]]
   [config.core :refer [env]]))

(def mount-target
  [:div#app
   [:h2 "Welcome to d3-reagent-sandbox"]
   [:p "please wait while Figwheel/shadow-cljs is waking up ..."]
   [:p "(Check the js console for hints if nothing exciting happens.)"]])


(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))])


(defn loading-page []
  (html5
   (head)
   [:body {:class "body-container"}
    mount-target
    (include-js "/js/app.js")
    [:script "d3_reagent_sandbox.core.init_BANG_()"]]))


(defn cards-page []
  (html5
    (head)
    [:body
     mount-target
     (include-js "/js/cards.js")]))


(defn cards-handler
  [_]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (cards-page)})


(defn index-handler
  [_request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (loading-page)})


(defn d3-handler
  [_request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (loading-page)})


(def app
  (reitit-ring/ring-handler
   (reitit-ring/router
    [["/"      {:get {:handler index-handler}}]
     ["/about" {:get {:handler index-handler}}]
     ["/cards" {:get {:handler cards-handler}}]
     ["/d3"    {:get {:handler d3-handler}}]
     ["/items"
      ["" {:get {:handler index-handler}}]
      ["/:item-id" {:get {:handler    index-handler
                          :parameters {:path {:item-id int?}}}}]]])
   (reitit-ring/routes
    (reitit-ring/create-resource-handler {:path "/" :root "/public"})
    (reitit-ring/create-default-handler))
   {:middleware middleware}))
