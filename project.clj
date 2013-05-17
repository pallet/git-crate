(defproject com.palletops/git-crate "0.8.0-SNAPSHOT"
  :description "Pallet crate to install, configure and use git"
  :url "http://palletops.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :scm {:url "git@github.com:pallet/git-crate.git"}

  :dependencies [[org.clojure/clojure "1.4.0"]
                 [com.palletops/pallet "0.8.0-beta.1"]]
  :repositories {"sonatype"
                 {:url "https://oss.sonatype.org/content/repositories/releases/"
                  :snapshots false}}
  :resource {:resource-paths ["doc-src"]
             :target-path "target/classes/pallet_crate/git_crate/"
             :includes [#"doc-src/USAGE.*"]}
  :prep-tasks ["resource" "crate-doc"])
