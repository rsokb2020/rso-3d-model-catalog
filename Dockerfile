FROM adoptopenjdk:14-jre-hotspot
# openjdk:14.0.2 <-- večja verzija
# adoptopenjdk:14-jre-hotspot <-- prilagojena manjša verzija

RUN mkdir /app

WORKDIR /app

ADD ./api/target/model-3d-catalog-api-1.0.0-SNAPSHOT.jar /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "image-catalog-api-1.0.0-SNAPSHOT.jar"]
#CMD java -jar image-catalog-api-1.0.0-SNAPSHOT.jar