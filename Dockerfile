FROM eclipse-temurin:17-jre

COPY ./target/sem-1.0-SNAPSHOT-jar-with-dependencies.jar /tmp/app.jar

WORKDIR /tmp

ENTRYPOINT ["java", "-jar", "app.jar"]
