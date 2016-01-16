#sh redeploy.sh
native2ascii washing-scheduler-web/src/main/resources/header.properties washing-scheduler-web/src/main/resources/header.properties 
native2ascii washing-scheduler-web/src/main/resources/menu.properties washing-scheduler-web/src/main/resources/menu.properties 
native2ascii washing-scheduler-web/src/main/resources/aside.properties washing-scheduler-web/src/main/resources/aside.properties 
native2ascii washing-scheduler-web/src/main/resources/footer.properties washing-scheduler-web/src/main/resources/footer.properties 
native2ascii washing-scheduler-web/src/main/resources/index.properties washing-scheduler-web/src/main/resources/index.properties 
native2ascii washing-scheduler-web/src/main/resources/create.properties washing-scheduler-web/src/main/resources/create.properties 
native2ascii washing-scheduler-web/src/main/resources/queue.properties washing-scheduler-web/src/main/resources/queue.properties 
mvn clean compile package
cd washing-scheduler-web
mvn jetty:run

