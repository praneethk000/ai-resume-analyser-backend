#FROM ubuntu:latest
#LABEL authors="savit"
#
#ENTRYPOINT ["top", "-b"]

FROM amazoncorretto:17

WORKDIR /app

COPY ./build/libs/ai-resume-analyser-backend-0.0.1-SNAPSHOT.jar /app/ai-resume-analyser-backend.jar

EXPOSE 8080

CMD ["java", "-jar", "ai-resume-analyser-backend.jar"]