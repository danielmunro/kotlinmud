FROM gradle:jdk11-alpine
COPY --chown=gradle:gradle . /mud
WORKDIR /mud
RUN gradle build -x test --no-daemon
EXPOSE 9999
ENTRYPOINT ["gradle", "run"]