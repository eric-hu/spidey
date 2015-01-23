(ns spidey.core
  (:gen-class)
  (:require [net.cgrand.enlive-html :as html])
  (:require clojure.pprint))

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

(defn -main "I don't do a whole lot." [& args]
  (let [first-url "http://killsixbilliondemons.com/?comic=kill-six-billion-demons-chapter-1" ]
    (find-all-urls first-url)))
