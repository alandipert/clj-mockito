(ns org.dipert.mockito
  (:import org.mockito.Mockito))

(defn mock
  "Mocks a class"
  [klass]
  (Mockito/mock klass))

(defmacro stub-method
  [mocked method arglist action result]
  `(-> ~mocked
     (. ~method ~@arglist)
     Mockito/when
     ~(condp = action
	 :throwing `(.thenThrow ~result)
	 :returning `(.thenReturn ~result))))


(defmacro verify
  "Verifies that call(s) were made to mocked"
  ([mocked call]
     (let [[method & args] call]
       (list* method `(Mockito/verify ~mocked) args)))
  ([mocked call & more]
     (let [calls (list* call more)]
       `(do ~@(map #(list `verify `~mocked %) calls)))))


; Allows (verify-2 klass add "one")  =>  (.add (org.mockito.Mockito/verify klass) "one")
;        (verify-2 klass clear)      =>  (.clear (org.mockito.Mockito/verify klass))

(defmacro verify-2
  "Verifies that call(s) were made to mocked. Arugments to the verified
  method are optional.

  usage:
    (verify-2 klass method arg1 arg2 arg3 ...)"
  [mocked call & args]
  (if (nil? args)
    (list* (symbol (str \. call)) `((Mockito/verify ~mocked)))
    (list* (symbol (str \. call)) `(Mockito/verify ~mocked) args)))


(defmacro verify-all
  "Verifies that all call(s) were made to mocked

  usage:
    (verify-all klass [method1 arg1 arg2] [method2 arg1] [method3])"
  [klass & methodlists]
  `(do ~@(map #(list* `(verify-2 ~klass ~(first %) ~@(rest %))) methodlists)))
