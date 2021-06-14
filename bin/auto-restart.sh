#!/bin/bash

while true; do ./gradlew clean run; test $? -gt 128 && break; done
