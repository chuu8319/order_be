FROM openjdk:22
VOLUME /tmp
COPY target/order-0.0.1-SNAPSHOT.jar order-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/order-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]