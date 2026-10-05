FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /src
# El plugin de toolchains busca el JDK 21 en toolchains.xml; en la imagen es el JAVA_HOME.
RUN printf '<toolchains><toolchain><type>jdk</type><provides><version>21</version></provides><configuration><jdkHome>%s</jdkHome></configuration></toolchain></toolchains>' "$JAVA_HOME" > /toolchains.xml
ENV MAVEN_ARGS="-t /toolchains.xml"
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src src
RUN mvn -q package -DskipTests

FROM eclipse-temurin:21-jre
COPY --from=build /src/target/app.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
