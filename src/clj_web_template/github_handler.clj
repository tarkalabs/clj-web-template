(ns clj-web-template.github-handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [cemerick.friend :as friend]
            [clojure.pprint :refer [pprint]]
            [cheshire.core :as j]
            [clj-http.client :as client]
            [friend-oauth2.workflow :as oauth2]
            [friend-oauth2.util :refer [format-config-uri get-access-token-from-params]]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])))


(def config-auth {:roles #{::user}})

(def client-config
  {:client-id "8663349aea320df6735f"
   :client-secret "96a80c4fe2e71492976a75c6a9e7968300f574b3"
   :callback {:domain "http://localhost:3000" :path "/oauth2callback"}})

(def uri-config
  {:authentication-uri {:url "https://github.com/login/oauth/authorize"
                        :query {:client_id (:client-id client-config)
                                :response_type "code"
                                :redirect_uri (format-config-uri client-config)
                                :scope "user"}}

   :access-token-uri {:url "https://github.com/login/oauth/access_token"
                      :query {:client_id (:client-id client-config)
                              :client_secret (:client-secret client-config)
                              :grant_type "authorization_code"
                              :redirect_uri (format-config-uri client-config)}}})


(defn credential-fn
  [token]
  ;;lookup token in DB or whatever to fetch appropriate :roles
  {:identity token :roles #{::user}})

(defn wrap-authentication [ring-app]
  (friend/authenticate
    ring-app
    {:allow-anon? true
     :workflows [(oauth2/workflow
                   {:client-config client-config
                    :uri-config uri-config
                    :access-token-parsefn get-access-token-from-params
                    :credential-fn credential-fn
                    :config-auth config-auth})]}))

(defn get-github-repos 
  "Github API call for the current authenticated users repository list."
  [access-token]
  (let [url (str "https://api.github.com/user/repos?access_token=" access-token)
        response (client/get url {:accept :json})
        repos (j/parse-string (:body response) true)]
    repos))

(defn show-repos
  [request]
  (let [authentications (get-in request [:session :cemerick.friend/identity :authentications])
        access-token (:access-token (first (first authentications)))
        repos-response (get-github-repos access-token)]
    (str (vec (map :name repos-response)))))

(defroutes app-routes
  (GET "/github" request (friend/authorize #{::user} (show-repos request))))

(def app (wrap-authentication app-routes))
