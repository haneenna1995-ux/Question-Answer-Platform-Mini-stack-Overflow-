FROM gradle:9-jdk17

WORKDIR /opt/app

COPY ./build/libs/Question-Answer-platform-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar Question-Answer-platform-0.0.1-SNAPSHOT.jar"]