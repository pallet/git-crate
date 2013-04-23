[Repository](https://github.com/pallet/git-crate) &#xb7; 
[Issues](https://github.com/pallet/git-crate/issues) &#xb7; 
[API docs](http://palletops.com/git-crate/0.8/api) &#xb7; 
[Annotated source](http://palletops.com/git-crate/0.8/annotated/uberdoc.html) &#xb7; 
[Release Notes](https://github.com/pallet/git-crate/blob/develop/ReleaseNotes.md)

Install and configure git.

### Dependency Information

```clj
:dependencies [[com.palletops/git-crate "0.8.0-alpha.1"]]
```

### Releases

<table>
<thead>
  <tr><th>Pallet</th><th>Crate Version</th><th>Repo</th><th>GroupId</th></tr>
</thead>
<tbody>
  <tr>
    <th>0.8.0-beta.1</th>
    <td>0.8.0-alpha.1</td>
    <td>clojars</td>
    <td>com.palletops</td>
    <td><a href='https://github.com/pallet/git-crate/blob/git-0.8.0-alpha.1/ReleaseNotes.md'>Release Notes</a></td>
    <td><a href='https://github.com/pallet/git-crate/blob/git-0.8.0-alpha.1/'>Source</a></td>
  </tr>
</tbody>
</table>

## Usage

To add git to a `server-spec` or a `group-spec`:

```clj
(require '[pallet.crate.git :as git])
(server-spec :extends (git/git {}))
```

Git is installed during the `:install` phase, so make sure to include
that phase during converge.

Provides `clone`, `pull` and `checkout` functions, and a `branch?` function.

## Support

[On the group](http://groups.google.com/group/pallet-clj), or `#pallet` on
freenode irc.

## License

Licensed under [EPL](http://www.eclipse.org/legal/epl-v10.html)

Copyright 2010, 2011, 2012, 2013 Hugo Duncan.
