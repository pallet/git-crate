## Usage

To add git to a `server-spec` or a `group-spec`:

```clj
(require '[pallet.crate.git :as git])
(server-spec :extends (git/git {}))
```

Git is installed during the `:install` phase, so make sure to include
that phase during converge.

Provides `clone`, `pull` and `checkout` functions, and a `branch?` function.
