(ns pallet.crate.git
  "Crate to install and use git."
  (:require
   [clojure.string :as string]
   [pallet.actions :refer [packages exec-script exec-checked-script]]
   [pallet.api :refer [server-spec plan-fn]]
   [pallet.crate :refer [defplan admin-user os-family]]
   [pallet.crate.package.epel :refer [add-epel]]))

(defplan install
  "Install git"
  []
  (when (#{:amzn-linux :centos} (os-family))
   (add-epel :version "5-4"))
  (packages
   :yum ["git" "git-email"]
   :aptitude ["git-core" "git-email"]
   :pacman ["git"]))

(defn repo-name [repo-uri]
  "Find a repository name from a repo uri string"
  (-> (string/split repo-uri #"/") last (string/replace #"\..*$" "")))

(defplan branch?
  "Which branch is checkout-dir currently using?"
  [checkout-dir & {:keys [user] :as options}]
  (exec-script
   (pipe ("git" branch)
         ("head" "-1")
         ("xawk" "'{print $2}'"))))

(defplan clone
  "Clone a repository from `repo-uri`, a uri string.
By default the `:checkout-dir` option is found from the `repo-uri`.
`:args` can be used to pass a sequence of arbitrary arguments to the
clone command."
  [repo-uri & {:keys [checkout-dir args]
               :or {checkout-dir (repo-name repo-uri)}
               :as options}]
  (exec-checked-script
   (str "Clone " repo-uri " into " checkout-dir)
   (if (not (file-exists? ~(str checkout-dir "/.git/config")))
     ("git" clone ~@(or (seq args) [""]) ~repo-uri ~checkout-dir))))


(defplan pull
  "Pull `:branch` from the specified `:remote` name.  The branch and remote
are optional"
  [& {:keys [branch remote args] :as options}]
  (exec-checked-script
   (str "Pull"
        (if branch (str " " branch) "")
        (if remote (str " from " remote)))
   ("git" pull ~@(or (seq (concat args (remove nil? [remote branch]))) [""]))))

(defplan checkout
  "Checkout a branch.
An optional `:remote-branch` may be specified, which is used to create the
branch if it doesn't already exist."
  [branch & {:keys [remote-branch]}]
  (exec-checked-script
   (str "Checkout " branch (if remote-branch (str " from "  remote-branch) ""))
   (if ("git" "show-ref" "--verify" "--quiet" (str "refs/heads/" ~branch))
     ("git" checkout ~branch)
     ("git" checkout "-b" ~branch ~(or remote-branch branch)))))

(defn git
  [_]
  (server-spec
   :phases {:install (plan-fn (install))}))
