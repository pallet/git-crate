# Pallet crate for git

This a crate to install and run git via [Pallet](http://pallet.github.com/pallet).

[Release Notes](ReleaseNotes.md) &#xb7; 
[API docs](http://palletops.com/java-crate/api/0.8/) &#xb7;
[Annotated code](http://palletops.com/java-crate/annotated/0.8/uberdoc.html).

```
[com.palletops/git-crate "0.8.0-alpha.1"]
```

## Usage

To add git to a `server-spec` or a `group-spec`:

```clj
(require '[pallet.crate.git :as git])
(server-spec :extends (git/git {}))
```

Provides `clone`, `pull` and `checkout` functions, and a `branch?` function.

## Support

[On the group](http://groups.google.com/group/pallet-clj), or #pallet on freenode irc.

## License

Licensed under [EPL](http://www.eclipse.org/legal/epl-v10.html)

Copyright 2010, 2011, 2012, 2013 Hugo Duncan.
