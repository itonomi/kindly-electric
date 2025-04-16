(ns electric-starter-app.main
  (:require [hyperfiddle.electric3 :as e]
            [hyperfiddle.electric-dom3 :as dom]
            [hyperfiddle.electric-forms5 :refer [Input]]
            [com.itonomi.kindly-electric :as kindly]))

(e/defn Main [ring-request]
  (e/client
   (binding [dom/node js/document.body
             e/http-request (e/server ring-request)]
     ;; mandatory wrapper div https://github.com/hyperfiddle/electric/issues/74
     (dom/div (dom/props {:style {:display "contents"}})
              (kindly/Render {:animation false
                              :title {:text "Echarts Example"}
                              :tooltip {}
                              :legend {:data ["sales"]}
                              :xAxis {:data ["Shirts", "Cardigans", "Chiffons",
                                             "Pants", "Heels", "Socks"]}
                              :yAxis {}
                              :series [{:name "sales"
                                        :type "bar"
                                        :data [(Input 1 :type :range :max 50) 20 36
                                               10 10 20]}]}
                             {:kind/echarts true})))))
