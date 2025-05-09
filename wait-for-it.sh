#!/bin/sh
# shellcheck disable=SC1128


# host와 port 분리
HOST=$(echo "$1" | cut -d: -f1)
PORT=$(echo "$1" | cut -d: -f2)
shift
cmd="$@"

until nc -z $host; do
  >&2 echo "$host 사용할 수 있기를 기다리고 있습니다 .."
  sleep 1
done

>&2 echo "$host 가 UP- 실행 명령입니다"
exec $cmd