lint:
	ktlint -F "src/**/*.kt"

run:
	./gradlew clean run

run-auto-restart:
	while true; do ./gradlew clean run; test $? -gt 128 && break; done

test:
	./gradlew clean test