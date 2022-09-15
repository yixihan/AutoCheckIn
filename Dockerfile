FROM java:8
EXPOSE 9999

VOLUME /tmp
ADD *.jar  /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
