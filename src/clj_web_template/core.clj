(ns clj-web-template.core
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [cemerick.friend :as friend]
            [friend-oauth2.workflow :as oauth2]
            [noir.util.middleware :refer [app-handler]]
            [clj-web-template.github-handler :as github]
            [clj-web-template.layout :as layout]))

(defroutes base-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn credential-fn
  [token]
  (println token)
  ;;lookup token in DB or whatever to fetch appropriate :roles
  {:identity token :roles #{::user}})


(defroutes app-routes
  (GET "/" [] (layout/render "index.html" {:name "world"})))

(def handler (app-handler [github/app  app-routes base-routes]))
