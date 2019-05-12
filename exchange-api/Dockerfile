FROM openjdk:11.0.2-jdk-slim-stretch


RUN apt-get update
RUN apt-get install apt-utils zip unzip wget -y

WORKDIR /opt/gradle
RUN wget https://services.gradle.org/distributions/gradle-4.10.2-bin.zip
RUN unzip -d /opt/gradle gradle-4.10.2-bin.zip

ADD src /code/src
ADD build.gradle /code
ADD gradle.properties /code

WORKDIR /code
RUN /opt/gradle/gradle-4.10.2/bin/gradle build --stacktrace -x test

WORKDIR /code/build/libs
RUN ls -la
CMD ["java", "-jar", "code-0.0.1.jar"]
