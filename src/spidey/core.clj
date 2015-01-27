(ns spidey.core
  (:gen-class)
  (:require [net.cgrand.enlive-html :as html])
  (:require clojure.pprint)
  (:use [vision core] :reload-all))

(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn find-next-url [url]
  (let [page (fetch-url url)
        anchor-tags (html/select page [:a])
        relevant-anchors (html/select anchor-tags [:.navi-next])
        image-url (:src (:attrs (first (html/select page [:a :img]))))
        next-page-url (:href (:attrs (first relevant-anchors)))]
    (println image-url)
    next-page-url))

(defn find-all-urls [url]
  (let [next-url (find-next-url url)]
    (println next-url)
    (recur next-url)))

;(defn -main "I don't do a whole lot." [& args]
  ;(let [first-url "http://killsixbilliondemons.com/?comic=kill-six-billion-demons-chapter-1" ]
    ;(find-all-urls first-url)))

(defn -main "read an image and OCR it" [& args]
  (let [image (load-image "./soccerfield.jpeg" :color)
        processed (--> (convert-color image :bgr-hsv)
                       (in-range-s [40 25 155 0] [50 70 255 0])
                       (smooth :gaussian 3 3 0 0))
        [[x y width height]] (with-contours [c [processed :external :chain-approx-none [0 0]]]
                               (bounding-rects c))
        ]
    (rectangle image [x y] [(+ width x) (+ height y)] java.awt.Color/red 4)
    (view :result image)
    (release [processed image])))
