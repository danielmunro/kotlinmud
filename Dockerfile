FROM gradle
WORKDIR /mud
COPY --chown=gradle:gradle . .
RUN gradle build --no-daemon
EXPOSE 9999
ENTRYPOINT ["gradle", "run"]