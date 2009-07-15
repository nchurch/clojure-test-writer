(use 'clojure.contrib.duck-streams)
(use 'clojure.contrib.test-is)
(def *testdir* "~/.test-writer")
(def *testfile* "~/.test-writer/test")
(def *default* "~/.test-writer/test")
(def *last-expr* nil)
(def *last-val* nil)
(defn mwrite ([file obj]
               (do (make-parents (file-str file))
                 (spit (apply file-str file) (binding [*print-dup* true *print-meta* true] (prn-str obj)))))
  ([obj] (mwrite *default* obj)))
(defn mappend ([file obj] (binding [*append-to-writer* true] (mwrite file obj)))
  ([obj] (mappend *default* obj)))
(defmacro write-test []
  `(is (= ~*last-expr* ~*last-val*)))
(defmacro t [exp]
  `(do (def *last-expr* '~exp)
     (def *last-val* ~(eval exp))
     ~(eval exp)))
(defn r []
  (with-out-str (print "  ") (print *last-expr*) (print " => ") (print *last-val*) (print "  ")))
(defmacro s ([] `(mappend ~*testfile* (macroexpand-1 '(write-test))))
  ([override] `(binding [*last-val* ~override] (s))))
