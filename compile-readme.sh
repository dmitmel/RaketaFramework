#!/usr/bin/env bash

common-mark-gen --code-style file:///Users/dmitmel/node_modules/highlight.js/styles/github.css README.ru.md; open README.ru.html
common-mark-gen --code-style file:///Users/dmitmel/node_modules/highlight.js/styles/github.css README.md; open README.html
