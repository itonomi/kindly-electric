(ns com.itonomi.kindly-electric
  (:require [hyperfiddle.electric3 :as e]
            [hyperfiddle.electric-dom3 :as dom]
            #?(:clj [scicloj.kindly-render.note.to-hiccup-inline-js :as to-hiccup-inline-js])
            #?(:clj [hiccup.core :as hiccup])))

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
   (defn kindly-html [value meta]
     (hiccup/html
      (:hiccup
       (to-hiccup-inline-js/render
        {:value
         ;; We MUST handle the case of no metadata, so that we can
         ;; render things that cant have metadat, such as
         ;; java.awt.image.BufferedImage
         (if meta
           (with-meta value meta)
           value)})))))

(e/defn Render
  ;; Even though Electric does not send metadata over the wire, there
  ;; is nothing stopping us from doing that at the Render boundary.
  ;;
  ;; If we encode/decode metadata to a data form as it enters and
  ;; leaves Render we should be good I think. Then kindly/Render usage
  ;; will be more similar to regular Kindly usage.
  ([value] (Render value nil))
  ([value meta]
   (dom/div 
     (set-inner-html-and-evaluate-scripts
      dom/node
      (e/server
        (kindly-html value meta))))))
