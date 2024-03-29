(defproject d3-reagent-sandbox "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring-server "0.5.0"]
                 [reagent "0.8.1" :exclusions [cljsjs/create-react-class
                                               cljsjs/react-dom-server
                                               cljsjs/react-dom
                                               cljsjs/react
                                               org.clojure/clojurescript]]
                 [reagent-utils "0.3.3"]
                 [ring "1.7.1"]
                 [ring/ring-defaults "0.3.2"]
                 [hiccup "1.0.5"]
                 [yogthos/config "1.1.3"]
                 [org.clojure/clojurescript "1.10.520" :scope "provided"]
                 [com.google.javascript/closure-compiler-unshaded "v20190325"]
                 [org.clojure/google-closure-library "0.0-20190213-2033d5d9"]
                 [metosin/reitit "0.3.7"]
                 [pez/clerk "1.0.0"]
                 [venantius/accountant "0.2.4"
                  :exclusions [org.clojure/tools.reader]]]

  :plugins [[lein-environ "1.1.0"]
            [lein-cljsbuild "1.1.7"]
            [lein-asset-minifier "0.4.6"
             :exclusions [org.clojure/clojure]]]

  :ring {:handler      d3-reagent-sandbox.handler/app
         :uberwar-name "d3-reagent-sandbox.war"}

  :min-lein-version "2.5.0"
  :uberjar-name "d3-reagent-sandbox.jar"
  :main d3-reagent-sandbox.server
  :clean-targets ^{:protect false}
  [:target-path
   [:cljsbuild :builds :app :compiler :output-dir]
   [:cljsbuild :builds :app :compiler :output-to]]

  :source-paths ["src/clj" "src/cljc" "src/cljs"]
  :resource-paths ["resources" "target/cljsbuild"]

  :minify-assets
  [[:css {:source "resources/public/css/site.css"
          :target "resources/public/css/site.min.css"}]]

  :cljsbuild
  {:builds {:min
            {:source-paths ["src/cljs" "src/cljc" "env/prod/cljs"]
             :compiler
             {:output-to     "target/cljsbuild/public/js/app.js"
              :output-dir    "target/cljsbuild/public/js"
              :source-map    "target/cljsbuild/public/js/app.js.map"
              :optimizations :advanced
              :infer-externs true
              :pretty-print  false}}
            :app
            {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
             :figwheel     {:on-jsload "d3-reagent-sandbox.core/mount-root"}
             :compiler
             {:main          "d3-reagent-sandbox.dev"
              :asset-path    "/js/out"
              :output-to     "target/cljsbuild/public/js/app.js"
              :output-dir    "target/cljsbuild/public/js/out"
              :source-map    true
              :optimizations :none
              :pretty-print  true}}
            :cards
            {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
             :figwheel     {:devcards true}
             :compiler     {:main                 "d3-reagent-sandbox.cards"
                            :asset-path           "js/devcards_out"
                            :output-to            "target/cljsbuild/public/js/app_devcards.js"
                            :output-dir           "target/cljsbuild/public/js/devcards_out"
                            :source-map-timestamp true
                            :optimizations        :none
                            :pretty-print         true}}}}


  :figwheel
  {:http-server-root "public"
   :server-port      3449
   :nrepl-port       7002
   :nrepl-middleware [cider.piggieback/wrap-cljs-repl]
   :css-dirs         ["resources/public/css"]
   :ring-handler     d3-reagent-sandbox.handler/app}

  :profiles {:dev {:repl-options {:init-ns d3-reagent-sandbox.repl}
                   :dependencies [[cider/piggieback "0.4.1"]
                                  [binaryage/devtools "0.9.10"]
                                  [ring/ring-mock "0.4.0"]
                                  [ring/ring-devel "1.7.1"]
                                  [prone "1.6.3"]
                                  [figwheel-sidecar "0.5.18"]
                                  [nrepl "0.6.0"]
                                  [thheller/shadow-cljs "2.8.37"]
                                  [devcards "0.2.6" :exclusions [cljsjs/create-react-class
                                                                 cljsjs/react-dom-server
                                                                 cljsjs/react-dom
                                                                 cljsjs/react
                                                                 org.clojure/clojurescript]]
                                  [pjstadig/humane-test-output "0.9.0"]]

                   :source-paths ["env/dev/clj" "env/dev/cljs"]
                   :plugins      [[lein-figwheel "0.5.19"]
                                  [cider/cider-nrepl "0.21.1"]
                                  [refactor-nrepl "2.4.0"
                                   :exclusions [org.clojure/clojure]]
                                  [org.clojure/tools.namespace "0.3.0-alpha4"
                                   :exclusions [org.clojure/tools.reader]]]

                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]

                   :env {:dev true}}

             :uberjar {:hooks        [minify-assets.plugin/hooks]
                       :source-paths ["env/prod/clj"]
                       :prep-tasks   ["compile" ["cljsbuild" "once" "min"]]
                       :env          {:production true}
                       :aot          :all
                       :omit-source  true}})
