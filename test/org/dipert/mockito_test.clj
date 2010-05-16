(ns org.dipert.mockito-test
  (:use [clojure.test])
  (:require [org.dipert.mockito :as mockito])
  (:import [java.util ArrayList]))


(deftest test-stub-method
         (let [mocklist (mockito/mock java.util.ArrayList)]
           (mockito/stub-method mocklist get [0] :returning "first")
           (is (= (.get mocklist 0)
                  "first"))))


(deftest test-verify
         (let [mocklist (mockito/mock java.util.ArrayList)]
           (.add mocklist "one")
           (.clear mocklist)
           (mockito/verify-2 mocklist add "one")
           (is (thrown? org.mockito.exceptions.verification.WantedButNotInvoked
                        (mockito/verify-2 mocklist add "two")))
           (mockito/verify-2 mocklist clear)))


(deftest test-verify-all
         (let [mocklist (mockito/mock java.util.ArrayList)]
           (.add mocklist "one")
           (.add mocklist "two")
           (.clear mocklist)
           (mockito/verify-all mocklist
                               [add "one"]
                               [add "two"]
                               [clear])
           (is (thrown? org.mockito.exceptions.verification.WantedButNotInvoked
                        (mockito/verify-all mocklist
                                            [add "three"]
                                            [clear])))))
