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
   [pallet.context :as context ]
   [pallet.build-actions :as build-actions]
   [pallet.live-test :as live-test]
   [pallet.test-utils]))

(deftest git-test
  (let [apt-server {:server {:image {} :packager :aptitude}}]
    (is (script-no-comment=
         (first (build-actions/build-actions
                    (conj {:phase-context "install"} apt-server)
                  (context/with-phase-context
                    {:msg "packages"}
                    (package "git-core")
                    (package "git-email"))))
         (first  (build-actions/build-actions
                     apt-server
                   (install))))))
  (let [yum-server {:server {:image {} :packager :yum}} ]
    (is (script-no-comment=
         (first (build-actions/build-actions
                    (conj {:phase-context "install"} yum-server)
                  (context/with-phase-context
                    {:msg "packages"}
                    (package "git")
                    (package "git-email"))))
         (first (build-actions/build-actions
                    yum-server
                  (install)))))))

(deftest git-clone-test
  (let [apt-server {:server {:image {} :packager :aptitude}}]
    (is (script-no-comment=
         (first (build-actions/build-actions
                    (conj {:phase-context "clone"} apt-server)
                  (exec-checked-script
                   "Clone git://github.com/zolrath/wemux.git into wemux"
                   (if (not (file-exists? "wemux/.git/config"))
                     ("git" clone
                      "git://github.com/zolrath/wemux.git" "wemux")))))
         (first (build-actions/build-actions apt-server
                  (clone "git://github.com/zolrath/wemux.git")))))))

(deftest git-pull-test
  (let [apt-server {:server {:image {} :packager :aptitude}}]
    (testing "defaults"
      (is (script-no-comment=
           (first (build-actions/build-actions
                      (conj {:phase-context "pull"} apt-server)
                    (exec-checked-script "Pull" ("git" pull))))
           (first (build-actions/build-actions apt-server
                    (pull))))))
    (testing "remote"
      (is (script-no-comment=
           (first (build-actions/build-actions
                      (conj {:phase-context "pull"} apt-server)
                    (exec-checked-script
                     "Pull from origin"
                     ("git" pull origin))))
           (first (build-actions/build-actions apt-server
                    (pull :remote "origin"))))))
    (testing "remote and branch"
      (is (script-no-comment=
           (first (build-actions/build-actions
                      (conj {:phase-context "pull"} apt-server)
                    (exec-checked-script
                     "Pull master from origin"
                     ("git" pull origin master))))
           (first (build-actions/build-actions apt-server
                    (pull :remote "origin" :branch "master"))))))))

(deftest git-checkout-test
  (let [apt-server {:server {:image {} :packager :aptitude}}]
    (is (script-no-comment=
         (first
          (build-actions/build-actions
              (conj {:phase-context "checkout"} apt-server)
            (exec-checked-script
             "Checkout master"
             (if ("git" "show-ref" "--verify" "--quiet" "refs/heads/master")
               ("git" checkout master)
               ("git" checkout "-b" master master)))))
         (first (build-actions/build-actions apt-server
                  (checkout "master")))))))

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
                :configure #'install
                :verify (plan-fn
                         (exec-checked-script
                          "check git command found"
                          ("git" "--version")))}}}
     (lift (:git node-types) :phase :verify :compute compute))))
