#!/usr/bin/env bash

output="$(basename $(echo $0) | sed 's#sh#json#g' | sed 's#^#../resources/#g')"

curl --verbose \
    -H 'Authorization: Bearer '$CMA_TOKEN  \
    'https://api.contentful.com/organizations' \
    | tee ${output}
