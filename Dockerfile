FROM gradle:6.9.2-jdk11-alpine
COPY --chown=gradle:gradle . /mud
WORKDIR /mud
RUN gradle build --no-daemon
EXPOSE 9999
ENTRYPOINT ["gradle", "run"]