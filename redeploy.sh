#sh redeploy.sh
native2ascii washing-scheduler-web/src/main/resources/header.properties washing-scheduler-web/src/main/resources/header.properties 
native2ascii washing-scheduler-web/src/main/resources/menu.properties washing-scheduler-web/src/main/resources/menu.properties 
native2ascii washing-scheduler-web/src/main/resources/aside.properties washing-scheduler-web/src/main/resources/aside.properties 
native2ascii washing-scheduler-web/src/main/resources/footer.properties washing-scheduler-web/src/main/resources/footer.properties 
native2ascii washing-scheduler-web/src/main/resources/index.properties washing-scheduler-web/src/main/resources/index.properties 
native2ascii washing-scheduler-web/src/main/resources/create.properties washing-scheduler-web/src/main/resources/create.properties 
native2ascii washing-scheduler-web/src/main/resources/queue.properties washing-scheduler-web/src/main/resources/queue.properties 

native2ascii washing-scheduler-web/src/main/resources/header_pl.properties washing-scheduler-web/src/main/resources/header_pl.properties 
native2ascii washing-scheduler-web/src/main/resources/menu_pl.properties washing-scheduler-web/src/main/resources/menu_pl.properties 
native2ascii washing-scheduler-web/src/main/resources/aside_pl.properties washing-scheduler-web/src/main/resources/aside_pl.properties 
native2ascii washing-scheduler-web/src/main/resources/footer_pl.properties washing-scheduler-web/src/main/resources/footer_pl.properties 
native2ascii washing-scheduler-web/src/main/resources/index_pl.properties washing-scheduler-web/src/main/resources/index_pl.properties 
native2ascii washing-scheduler-web/src/main/resources/create_pl.properties washing-scheduler-web/src/main/resources/create_pl.properties 
native2ascii washing-scheduler-web/src/main/resources/queue_pl.properties washing-scheduler-web/src/main/resources/queue_pl.properties 
native2ascii washing-scheduler-web/src/main/resources/sessions_create_pl.properties washing-scheduler-web/src/main/resources/sessions_create_pl.properties 

native2ascii washing-scheduler-web/src/main/resources/header_ru.properties washing-scheduler-web/src/main/resources/header_ru.properties 
native2ascii washing-scheduler-web/src/main/resources/menu_ru.properties washing-scheduler-web/src/main/resources/menu_ru.properties 
native2ascii washing-scheduler-web/src/main/resources/aside_ru.properties washing-scheduler-web/src/main/resources/aside_ru.properties 
native2ascii washing-scheduler-web/src/main/resources/footer_ru.properties washing-scheduler-web/src/main/resources/footer_ru.properties 
native2ascii washing-scheduler-web/src/main/resources/index_ru.properties washing-scheduler-web/src/main/resources/index_ru.properties 
native2ascii washing-scheduler-web/src/main/resources/create_ru.properties washing-scheduler-web/src/main/resources/create_ru.properties 
native2ascii washing-scheduler-web/src/main/resources/queue_ru.properties washing-scheduler-web/src/main/resources/queue_ru.properties 
native2ascii washing-scheduler-web/src/main/resources/sessions_create_ru.properties washing-scheduler-web/src/main/resources/sessions_create_ru.properties 

mvn clean compile package
cd washing-scheduler-web
mvn jetty:run

