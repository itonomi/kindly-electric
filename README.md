# Kindly Electric
This is a workspace to develop a good integration of Electric Clojure and Kindly.

This is WIP and we're figuring out all the things as we speak. Ping me
(Olav) on the Clojurians Slack if you're interested or have feeback :^).

Try it out in the playground directory.
```
$ cd playground && clojure -A:dev -X dev/-main
```
Built on https://github.com/scicloj/kindly-render


```
com.itonomi/kindly-electric {:git/url "https://github.com/olavfosse-itonomi/kindly-electric"
                             :git/sha "1a51985e70411c28589439583bfc29d79d01d06d"}
```

Made by Daniel Slutsky and Olav Fosse

## Todo
- [x] Package as a library
- [X] Change the API to take values with metadata
  (kindly/Render ^:kind/md ["# Check out this markdown header!"])
- [ ] Add shorthands -- or maybe not
- [ ] Make tables look like in the Noj/Kindly documentation
  - Missing CSS?
- [ ] playground: input field to test rendering expression interactively
  - [ ] fail gracefully
