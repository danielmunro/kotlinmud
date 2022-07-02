FROM gradle
WORKDIR /mud
COPY --chown=gradle:gradle . .
RUN gradle clean build -x test --no-daemon
EXPOSE 9999
ENTRYPOINT ["gradle", "run"]