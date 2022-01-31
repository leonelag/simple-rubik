#!/usr/bin/env bash
sed --in-place=.bak 's/whi/1/g; s/red/2/g; s/yel/3/g; s/gre/4/g; s/blu/5/g; s/ora/6/g' $1