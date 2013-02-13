(ns pallet.crate.git-test
  (:use pallet.crate.git
        [pallet.api :only [lift plan-fn]]
        [pallet.actions :only [package packages package-manager exec-script exec-checked-script]]
        [pallet.crate.automated-admin-user :only [automated-admin-user]]
        [pallet.crate.package.epel :only [add-epel]]
        [pallet.core.session :only [session os-family]]
        [pallet.thread-expr :only [when->]]
        clojure.test
        clojure.pprint
        pallet.test-utils)
  (:require
   [pallet.context :as context]
   [pallet.build-actions :as build-actions]
   [pallet.live-test :as live-test]))

(deftest git-test
  []
  (let [apt-server {:server {:image {} :packager :aptitude}}]
    (is (= (first (build-actions/build-actions
                   (conj {:phase-context "install-git"} apt-server)
                   (when->
                    (#{:amzn-linux :centos} 
                     (os-family (session)))
                    (add-epel :version "5-4"))
                   (package-manager :update)
                   (context/with-phase-context
                     {:msg "packages"}
                     (package "git-core")
                     (package "git-email"))))
           (first  (build-actions/build-actions
                    apt-server
                    (install-git))))))
  (let [yum-server {:server {:image {} :packager :yum}} ]
    (do (pprint (first (build-actions/build-actions
                 (conj {:phase-context "install-git"} yum-server)
                 (when->
                  (#{:amzn-linux :centos} 
                   (os-family (session)))
                  (add-epel :version "5-4"))
                 (package-manager :update)
                 (context/with-phase-context
                   {:msg "packages"}
                   (package "git")
                   (package "git-email")))))
        (pprint (first (build-actions/build-actions
                 yum-server
                 (install-git)))))))

(comment
  ;; TODO: fix this test
  (deftest git-clone-test
   []
   (let [apt-server {:server {:image {} :packager :aptitude}}]
     (is (= (first (build-actions/build-actions
                    (conj {:phase-context "clone-or-pull"} apt-server)
                    (when->
                     (#{:amzn-linux :centos}
                      (os-family (session)))
                     (add-epel :version "5-4"))))
            (first (build-actions/build-actions
                    apt-server
                    (clone-or-pull "git://github.com/zolrath/wemux.git" 
                                   "/usr/local/share/wemux"))))))))

(deftest live-test
  (doseq [image live-test/*images*]
    (live-test/test-nodes
     [compute node-map node-types]
     {:git
      {:image image
       :count 1
       :phases {:bootstrap (plan-fn
                            (package-manager :update)
                            (package "coreutils") ;; for debian
                            (automated-admin-user))
                :configure #'install-git
                :verify (plan-fn
                         (exec-checked-script
                          "check git command found"
                          (git "--version")))}}}
     (lift (:git node-types) :phase :verify :compute compute))))
