;; migrations/20140924230437620-create-users.clj

(defn up []
  ["create table users(
    id serial primary key,
    email varchar,
    password varchar,
    created_at timestamp,
    updated_at timestamp
   )"
   "create table identities(
    id serial primary key,
    user_id integer not null,
    provider varchar,
    access_token varchar,
    refresh_token varchar,
    provider_user_id varchar,
    provider_profile json,
    created_at timestamp,
    updated_at timestamp
   )"])

(defn down []
  ["drop table identities" "drop table users"])
