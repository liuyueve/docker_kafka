#!/bin/sh
if [ "$(id -u)" = '0' ]; then
	find . \! -user kafka -exec chown kafka '{}' +
	exec gosu kafka "$0" "$@"
fi

exec kafka-server-start.sh "$@"