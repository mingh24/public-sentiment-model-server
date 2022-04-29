#!/usr/bin/env bash

set -e

module_name="public-sentiment-model-server"

mvn clean package

if [[ ! -d "./output" ]]; then
  mkdir ./output
fi

cp ./target/"$module_name".jar ./output
