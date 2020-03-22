#!/bin/bash

set -e

DOCKER="$(command -v docker)"

$DOCKER system prune -af
$DOCKER run -p 9999:9999 quay.io/dmunro/kotlinmud