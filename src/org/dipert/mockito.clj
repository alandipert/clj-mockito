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