(ns electric-starter-app.main
  (:require [hyperfiddle.electric3 :as e]
            [hyperfiddle.electric-dom3 :as dom]
            [hyperfiddle.electric-forms5 :refer [Input]]
            [electric-tutorial.chat-monitor :refer [ChatMonitor]]
            #?(:clj [scicloj.kindly-render.note.to-hiccup-inline-js :as to-hiccup-inline-js])
            #?(:clj [hiccup.core :as hiccup])))

#?(:clj
   (def example
     (with-meta
       {:title {:text "Echarts Example"}
        :tooltip {}
        :legend {:data ["sales"]}
        :xAxis {:data ["Shirts", "Cardigans", "Chiffons",
                       "Pants", "Heels", "Socks"]}
        :yAxis {}
        :series [{:name "sales"
                  :type "bar"
                  :data [5 20 36
                         10 10 20]}]}
       {:kind/echarts true})))

#?(:clj
   (def hiccup-result
     (:hiccup
      (to-hiccup-inline-js/render
       {:value example}))))

#?(:clj
   (def html-result
     (hiccup/html hiccup-result)))


#?(:cljs (defn set-inner-html-and-evaluate-scripts [elm html]
           (set! (.-innerHTML elm) html)
           
           (doseq [old-script-el (array-seq (.querySelectorAll elm "script"))]
             (let [new-script-el (.createElement js/document "script")
                   script-text (.createTextNode js/document (.-innerHTML old-script-el))]
               
               (doseq [attr (array-seq (.-attributes old-script-el))]
                 (.setAttribute new-script-el (.-name attr) (.-value attr)))
               
               (.appendChild new-script-el script-text)
               (.replaceChild (.-parentNode old-script-el) new-script-el old-script-el)))))

#?(:clj
   (defn kindly-html [value meta-data]
     (hiccup/html
      (:hiccup
       (to-hiccup-inline-js/render
        {:value (with-meta value meta-data)})))))

(e/defn Kindly [value meta-data]
  (dom/div 
   (set-inner-html-and-evaluate-scripts
    dom/node
    (e/server
     (kindly-html value meta-data)))))

(e/defn Main [ring-request]
  (e/client
   (binding [dom/node js/document.body
             e/http-request (e/server ring-request)]
     ;; mandatory wrapper div https://github.com/hyperfiddle/electric/issues/74
     (dom/div (dom/props {:style {:display "contents"}})
              (Kindly {:animation false
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
