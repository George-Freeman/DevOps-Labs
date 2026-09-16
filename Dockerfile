FROM eclipse-temurin:17-jre

COPY ./target/classes/com /tmp/com

WORKDIR /tmp

ENTRYPOINT ["java", "com.napier.sem.App"]