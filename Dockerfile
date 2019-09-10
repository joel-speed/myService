FROM docker-registry.speedledger.net/shared/java8-sbt:master

ENTRYPOINT ["bin/myservice"]

CMD -Dconfig.resource=application.conf

WORKDIR /app

COPY . /app
