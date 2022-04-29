#!/usr/bin/env bash

set -e

module_name="public-sentiment-model-server"

function show_usage() {
  echo "usage: $0 [-p <profile>]"
}

function is_profile_valid() {
  [[ $1 == "develop" ]] || [[ $1 == "testing" ]] || [[ $1 == "product" ]]
}

while getopts "p:" arg; do
  case "$arg" in
  p)
    p="$OPTARG"

    if ! is_profile_valid "$p"; then
      echo "invalid profile: '$p'. profile must be one of 'develop', 'testing', 'product'"
      exit 1
    fi

    profile="$p"
    ;;
  *)
    show_usage
    exit 1
    ;;
  esac
done

if [[ -d "./output" ]]; then
  cd ./output
fi

if [ -n "$profile" ]; then
  echo "run in $profile profile"
else
  echo "profile is unset. run in product profile"
  profile="product"
fi

run_command="nohup java -jar $module_name.jar --spring.profiles.active=$profile >> $module_name.log 2>&1 &"
echo "eval \"$run_command\""
eval "$run_command"
