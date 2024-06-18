FROM maven:3.9.6-eclipse-temurin-17 as build
COPY pom.xml .
COPY src/ src/
RUN mvn -f pom.xml -Pprod clean package

FROM eclipse-temurin:17-jre as run
RUN useradd ayman
USER ayman
COPY --from=build /target/app-tennis.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]