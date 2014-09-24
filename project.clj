(defproject clj-web-template "0.1.0-SNAPSHOT"
  :description "A sample web template project"
  :url "http://github.com/tarkalabs/clj-web-template"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[clj-sql-up "0.3.3"]]
  :clj-sql-up {:database "jdbc:postgresql://postgres@127.0.0.1:5432/cljwebtemplate"
               :database-test "jdbc:postgresql://postgres@127.0.0.1:5432/cljwebtemplate_test"
               :deps [[org.postgresql/postgresql "9.3-1100-jdbc4"]]}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.postgresql/postgresql "9.3-1100-jdbc4"]])
