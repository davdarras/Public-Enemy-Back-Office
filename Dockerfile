FROM tomcat:8.5-jdk11-slim

RUN rm -rf $CATALINA_HOME/webapps/*
COPY publicenemybo.properties $CATALINA_HOME/webapps/
ADD ./target/*.war $CATALINA_HOME/webapps/ROOT.war
