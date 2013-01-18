(ns pallet.crate.git
  "Crate to install git."
  (:use
   [pallet.actions :only [package packages package-manager 
                          exec-script exec-checked-script]]
   [pallet.crate.package.epel :only [add-epel]]
   [pallet.crate :only [defplan]]
   [pallet.thread-expr :only [when->]]
   [pallet.core.session :only [session os-family admin-user]]))

(defplan install-git
  "Install git"
  []
  (when->
   (#{:amzn-linux :centos} (os-family (session)))
   (add-epel :version "5-4"))
  (package-manager :update)
  (packages
   :yum ["git" "git-email"]
   :aptitude ["git-core" "git-email"]
   :pacman ["git"]))

(defplan branch?
  "Which branch is checkout-dir currently using?"
  [checkout-dir & {:keys [user] :as options}]
  (let [as-user (if (nil? user) (-> (session) admin-user :username) user)]
    (exec-script 
     (do (cd ~checkout-dir)
         (sudo "-u" ~as-user
               (pipe (git branch)
                     (head "-1")
                     (awk "'{print $2}'")))))))

(defplan clone-or-pull
  "Checkout or update a repository including switching branches."
  [uri checkout-dir & {:keys [branch remote-branch user] :as options}]
  (let [as-user (if (nil? user) (-> (session) admin-user :username) user)]
    (do (exec-script
         (do (if (directory? ~checkout-dir)
               (do (cd ~checkout-dir)
                   (sudo -u ~as-user git pull))
               (sudo -u ~as-user git clone ~uri ~checkout-dir))))
        (if-not (nil? branch)
          (exec-script
           (do (cd ~checkout-dir)
               (sudo -u ~as-user git "show-ref" "--verify" "--quiet" (str "refs/heads/" ~branch))
               (if-not (= 0 @?) ;; equivalent to if [ $? -ne 0 ]
                 (sudo -u ~as-user git checkout "-b" ~branch ~remote-branch)
                 (sudo -u ~as-user git checkout ~branch))))))))