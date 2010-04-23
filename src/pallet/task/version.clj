(ns pallet.task.version
  "Print Pallet's version to standard out.")

(defn version []
  (println "Pallet" (System/getProperty "pallet.version")
           "on Java" (System/getProperty "java.version")
           (System/getProperty "java.vm.name")))
