#!/usr/bin/env bash
sed --in-place=.bak 's/1/whi/g; s/2/red/g; s/3/yel/g; s/4/gre/g; s/5/blu/g; s/6/ora/g' $*