FROM openjdk:12-alpine

WORKDIR /app

#ENV DB_USERNAME=leagueadmin
#ENV DB_PASSWORD=password
#ENV DB_URL=
#ENV PROFILE=production
#ENV CURRENCY_API_URL=https://restcountries.eu/rest/v2/all

COPY target/stuffexchange-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9000

ENTRYPOINT [ "java" , "-jar" , "app.jar" ]