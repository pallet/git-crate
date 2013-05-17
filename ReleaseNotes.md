# Release notes

## git-0.8.0-alpha.2

- Only strip ".git" from repo-name
  Before this fix, checking out:
     git://github.com/gittip/www.gittip.com.git repo-name would return
  "www". With this fix, repo-name returns "www.gittip.com"

- Don't add-epel on Amazon Linux (fails; also already there)

- Pull in pallet-lein in the project profile
  Without this fix in place (and no mention of pallet-lein in the user
  profile), one gets this error
     $ lein live-test
     'pallet' is not a task. See 'lein help'.

  With this change, lein-pedantic gives a warning. I didn't figure out how
  to get lein-pedantic to be happy.

- Avoid pulling in older version of stencil
  This commit avoids a lein-pedantic complaint:

  Failing dependency resolution because:

  [lein-resource "0.3.2"] -> [stencil "0.3.1"]
   is overrulling
  [com.palletops/lein-pallet-crate "0.1.0"] -> [stencil "0.3.2"]

  Please use
  [lein-resource "0.3.2" :exclusions [stencil]] to get [stencil "0.3.2"] or
  use
  [com.palletops/lein-pallet-crate "0.1.0" :exclusions [stencil]] to get
  [stencil "0.3.1"].

- Fix mistaken "java" with "git"

- Lowercase header in metadata

- Add a git plan for calling arbitrary git tasks

- Rename git server-spec to server-spec

- Rename install-git plan-fn to install

- Add documentation for :install phase

- correct url for generated docs to point at git-crate

- add plugin for generating docs with copy-doc

- move installing git to install phase

- Update to support crate metadata
  Adds in crate metadata and README.md generation.

## git-0.8.0-alpha.1

- Use leiningen rather than maven

- Split out clone, pull and checkout
  Also adds a server-spec.

- first try at 0.8 port

## git-0.7.0-beta.1

- Update for pallet 0.7

## pallet-crates-0.5.0


## pallet-crates-0.4.4

- Update for repository management in separate namespaces


## pallet-crates-0.4.3

- Update for 0.5.0-SNAPSHOT
  Change pallet.resource.* to pallet.action.*. Change stevedore calls to
  script functions to use unquote and the pallet.script.lib namespace.
  Change request to session.  Change build-resources to build-actions.


## pallet-crates-0.4.2


## pallet-crates-0.4.1


## pallet-crates-0.4.0


## pallet-crates-0.4.0-beta-1

- Extend git crate support to centos and arch

- Add live test to git crate
