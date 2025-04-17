(ns electric-starter-app.main
  (:require [hyperfiddle.electric3 :as e]
            [hyperfiddle.electric-dom3 :as dom]
            [hyperfiddle.electric-forms5 :refer [Input]]
            ;; We can't use the alias `kindly` because there's
            ;; already [scicloj.kindly.v4.api :as kindly]. Maybe
            ;; encouraging [com.itonomi.kindly-electric :refer [Kindly-render]]
            ;; or something like that is better. I would like it to be
            ;; concise. Maybe just `Kindly`. I kinda like that.
            ;;
            ;; + Concise
            ;; + Clear what it does (if you know what kindly is)
            ;; + Won't conflict because of the capitalization
            [com.itonomi.kindly-electric :as ek :refer [Kindly2]]
            [clojure.pprint :refer [pprint]]
            #?(:clj [tablecloth.api :as tc])
            #?(:clj [scicloj.kindly.v4.api :as kindly])

            ;; I'm able to require kind in cljs but not to use it 🤔
            ;; (dom/text (e/client (kind/md "test"))) ;; fails
            [scicloj.kindly.v4.kind :as kind]
            #?(:clj [tablecloth.api :as tc])
            #?(:clj [clojure.math :as math])
            #?(:clj [tablecloth.api :as tc])
            #?(:clj [tablecloth.column.api :as tcc])
            #?(:clj [scicloj.kindly.v4.kind :as kind])
            #?(:clj [emmy.env :as e.e :refer :all #_[D square cube tanh cos sin up down]])
            #?(:clj [emmy.viewer :as ev])
            #?(:clj [emmy.mafs :as mafs])
            #?(:clj [emmy.mathbox.plot :as plot])
            #?(:clj [emmy.leva :as leva])))

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

                 #_(example (Kindly2 (e/server (mafs/of-x e.e/sin {:color :blue}))))

                 (example (Kindly2 (e/server (kind/plotly (let [n 20
                                                                walk (fn [bias]
                                                                       (->> (repeatedly n #(-> (rand)
                                                                                               (- 0.5)
                                                                                               (+ bias)))
                                                                            (reductions +)))]
                                                            {:data [{:x (walk (/ x 50))
                                                                     :y (walk -1)
                                                                     :z (map #(* % %)
                                                                             (walk 2))
                                                                     :type :scatter3d
                                                                     :mode :lines+markers
                                                                     :opacity 0.2
                                                                     :line {:width 10}
                                                                     :marker {:size 20
                                                                              :colorscale :Viridis}}]})))))

                 (example (Kindly2 (e/server (kind/reagent
                                              ['(fn [{:keys [initial-value
                                                             background-color]}]
                                                  (let [*click-count (reagent.core/atom initial-value)]
                                                    (fn []
                                                      [:div {:style {:background-color background-color}}
                                                       "The atom " [:code "*click-count"] " has value: "
                                                       @*click-count ". "
                                                       [:input {:type "button" :value "Click me!"
                                                                :on-click #(swap! *click-count inc)}]])))
                                               {:initial-value 9
                                                :background-color "#d4ebe9"}]))))

                 #_(example (Kindly2 (e/server (kind/reagent
                                              ['(fn []
                                                  [:div {:style {:height "200px"}
                                                         :ref (fn [el]
                                                                (let [m (-> js/L
                                                                            (.map el)
                                                                            (.setView (clj->js [51.505 -0.09])
                                                                                      13))]
                                                                  (-> js/L
                                                                      .-tileLayer
                                                                      (.provider "OpenStreetMap.Mapnik")
                                                                      (.addTo m))
                                                                  (-> js/L
                                                                      (.marker (clj->js [51.5 -0.09]))
                                                                      (.addTo m)
                                                                      (.bindPopup "A pretty CSS popup.<br> Easily customizable.")
                                                                      (.openPopup))))}])]
                                              ;; Note we need to mention the dependency:
                                              {:html/deps [:leaflet]}))))


                 (example (Kindly2 (e/server [(kind/md "Portal works but not as expected, seems to show the metadata?")
                                              (kind/portal {:x (range 3)})
                                              (kind/md "I have a feeling this problem will go away if i do things more properly")])))

                 (example (Kindly2 (e/server (kind/pprint {:foo :buzz
                                                           :fuzz {:this [:is "super useful!"]}
                                                           :if "only it worked rn..."}))))
                 (example (Kindly2 (e/server (kind/edn {:foo :buzz
                                                        :fuzz {:this [:is "super useful!"]}
                                                        :if "only it worked rn..."}))))

                 (example (Kindly2 (e/server (kind/portal
                                              [(kind/hiccup [:img {:height 50 :width 50
                                                                   :src "https://clojure.org/images/clojure-logo-120b.png"}])
                                               (kind/hiccup [:img {:height 50 :width 50
                                                                   :src "https://raw.githubusercontent.com/djblue/portal/fbc54632adc06c6e94a3d059c858419f0063d1cf/resources/splash.svg"}])]))))


                 (example (Kindly2 (e/server (kind/video {:src "https://www.sample-videos.com/video321/mp4/240/big_buck_bunny_240p_30mb.mp4"}))))
                 (Kindly2 (e/server (kind/md "## How come kind/md does not work on Cljs?")))
                 (example (Kindly2 (e/server (kind/md "In Electric, some of the metadata seems to disappear during compile time?

This form here for example:


``
(example (meta ^:foo {}))
```

Does NOT render as

{:foo true}

The metadata is gone!?

That's unusual but I think it's fine for now, as we can use
the [scicloj.kindly.v4.kind :as kind] namespace with great success.

Having to do (Kindly2 (kind/<kind> content)) is kinda wordy but it's
not actually /that/ bad. We could maybe re-export and do something
like (ek/<Kind> content)

`(ek/Md \"# hey ho\")` is not bad at all I think.

Or perhaps consumer can refer and have something as neat as

(Md \"# hey ho\")

That's kinda obscuring the use of kindly though.

In any case it's not that bad at all!

Oh well i forgot that we also have 
")))) 
                 
                 (example (Kindly2 (e/server (kind/md  ["# *TODO:* we're missing something for rendering Latex"]))))
                 
                 (example (Kindly2 ^:kind/md ["# *TODO:* we're missing something for rendering Latex"]))
                 
                 (example (ek/Render  ["# *TODO:* we're missing something for rendering Latex"]
                                      {:kind/md true}))
                 
                 (example (Kindly2 (e/server (kind/tex "x^2=\\alpha"))))
                 (example (ek/Render (e/server (kind/tex "x^2=\\alpha"))
                                     (e/server (meta (kind/tex "x^2=\\alpha")))))
                 (example (ek/Render ["# Kindly Electric

The slider here corresponds to x and is which is used in a bunch of different visualizations, just to show off that reactivity is working :^)"]
                                     {:kind/md true}))
                 (example (ek/Render {:animation false
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

                 (example (ek/Render
                           (e/server (->  "https://upload.wikimedia.org/wikipedia/commons/e/eb/Ash_Tree_-_geograph.org.uk_-_590710.jpg"
                                          (java.net.URL.)
                                          (javax.imageio.ImageIO/read)))))
                 (example (ek/Render 
                           ["hello *hello* **hello**"]
                           {:kind/md true}))

                 (example (ek/Render (e/server (tc/dataset {:x (range x)
                                                            :y (repeatedly 3 rand)}))))

                 (example (ek/Render (e/server (tc/dataset [["classic_bike" x -87.626217 41.92393131136619 -87.63582453131676]
                                                            ["electric_bike" 41.869312286 -87.673897266 41.8895 -87.688257]
                                                            ["classic_bike" 41.95600355078549 -87.68016144633293 41.886875 -87.62603]]
                                                           {:column-names [:rideable-type
                                                                           :start-lat :start-lng
                                                                           :end-lat :end-lng]}))))

                 )))))
