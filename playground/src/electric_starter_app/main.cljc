(ns electric-starter-app.main
  (:require [hyperfiddle.electric3 :as e]
            [hyperfiddle.electric-dom3 :as dom]
            [hyperfiddle.electric-forms5 :refer [Input]]
            [com.itonomi.kindly-electric :as kindly]
            [clojure.pprint :refer [pprint]]
            #?(:clj [tablecloth.api :as tc])))

(defn pprint-str [x]
  (with-out-str (pprint x)))

(defmacro example [form]
  `(do 
       (dom/pre (dom/text (pprint-str '~form)))
       ~form
       (dom/hr)))

(e/defn Main [ring-request]
  (e/client
   (binding [dom/node js/document.body
             e/http-request (e/server ring-request)]
     ;; mandatory wrapper div https://github.com/hyperfiddle/electric/issues/74
     (dom/div (dom/props {:style {:display "contents"}})
              (let [x (parse-long (Input 1 :type :range
                                           :max 50
                                           :style {:position "fixed"
   :top "20px"          ;; You can adjust these values
   :right "20px"        ;; to position the element
   :width "300px"       ;; where you want it
   :height "200px"
   :z-index "1000"
   :pointer-events "auto"}))]
                (example (kindly/Render ["# Kindly Electric

The slider here corresponds to x and is which is used in a bunch of different visualizations, just to show off that reactivity is working :^)"]
                                        {:kind/md true}))
                (example (kindly/Render {:animation false
                                         :title {:text "Echarts Example"}
                                         :tooltip {}
                                         :legend {:data ["sales"]}
                                         :xAxis {:data ["Shirts", "Cardigans", "Chiffons",
                                                        "Pants", "Heels", "Socks"]}
                                         :yAxis {}
                                         :series [{:name "sales"
                                                   :type "bar"
                                                   :data [x 20 36
                                                          10 10 20]}]}
                                        {:kind/echarts true}))

                (example (kindly/Render
                          (e/server (->  "https://upload.wikimedia.org/wikipedia/commons/e/eb/Ash_Tree_-_geograph.org.uk_-_590710.jpg"
                                         (java.net.URL.)
                                         (javax.imageio.ImageIO/read)))))
                (example (kindly/Render 
                          ["hello *hello* **hello**"]
                          {:kind/md true}))

                (example (kindly/Render (e/server (tc/dataset {:x (range x)
                                                               :y (repeatedly 3 rand)}))))

                (example (kindly/Render (e/server (tc/dataset [["classic_bike" x -87.626217 41.92393131136619 -87.63582453131676]
                                                               ["electric_bike" 41.869312286 -87.673897266 41.8895 -87.688257]
                                                               ["classic_bike" 41.95600355078549 -87.68016144633293 41.886875 -87.62603]]
                                                              {:column-names [:rideable-type
                                                                              :start-lat :start-lng
                                                                              :end-lat :end-lng]})))))))))
