FROM tomcat:8.5-jdk11-slim

RUN rm -rf $CATALINA_HOME/webapps/*
ADD ./target/*.war $CATALINA_HOME/webapps/ROOT.war
ADD tomcat-users.xml $CATALINA_HOME/conf/tomcat-users.xml