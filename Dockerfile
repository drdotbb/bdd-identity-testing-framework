FROM maven:3.8.7-eclipse-temurin-17

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src/ /app/src/

CMD ["mvn", "clean", "test"]