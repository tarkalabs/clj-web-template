(ns clj-web-template.core
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [noir.util.middleware :refer [app-handler]]
            [clj-web-template.layout :as layout]))

(defroutes base-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defroutes app-routes
  (GET "/" [] (layout/render "index.html" {:name "world"})))

(def handler (app-handler [app-routes base-routes]))
