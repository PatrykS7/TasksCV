FROM openjdk:19-alpine

ADD target/Tasks-1.0.0.jar Tasks-1.0.0.jar

EXPOSE 8090

ENTRYPOINT [ "java", "-jar", "Tasks-1.0.0.jar" ]