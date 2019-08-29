1) установить pom otus-java-hw в локальный репозиторий: 
    clean install
2) в "%CATALINA_HOME%"\conf\tomcat-users.xml добавить пользователя для деплоя war файлов, например:
    <role rolename="manager-gui"/>
    <role rolename="manager-script"/>
    <user username="admin" password="admin" roles="manager-gui, manager-script"/>
3) запустить локальный tomcat:
    "%CATALINA_HOME%"\bin\startup.bat
4) для модуля otus-java-hw/hw15-messageSystem выполнить:
    clean install -pl hw15-3-fe clean package tomcat7:redeploy -pl hw15-0-ms clean package tomcat7:redeploy