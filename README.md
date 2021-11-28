# TBD-SOCIAL-NETWORKS-PLANNER

Este proyecto consiste en una aplicación desarrollada por el método de TBD que permite la interacción con Twitter e Instagram ya sea posteando al momento o programando dicho post a modo de scheduler.

Es un monolito desplegado sobre heroku integrado con una base de datos PostgreSQL la cual utilizamos para la gestión de los posts utilizando también un PAAS, en este caso AWS S3, para el almacenamiento de las imágenes.

Disponemos de dos pipelines que se ejecutan:
- `main.yml`: Al hacer un push sobre main
- `main-pr.yml`: Al hacer un merge request sobre main

El primero ejecuta los tests, en caso de que vayan OK, hace el build del proyecto, publica la imagen en Heroku Registry y en DockerHub y despliega la aplicación en Heroku.
El segundo simplemente ejecuta los tests y en caso de que vayan OK hace el build del proyecto.

Para desplegarlo y testearlo en local disponemos de varios perfiles:
- test: Para la ejecución de los tests, con BBDD H2 y no tiene habilitado el HTTPS
- dev: Para las pruebas en local, con BBDD H2 y con HTTPS
- flyway: Para probar el correcto funcionamiento de los scripts de flyway. Habría que ejecutar el siguiente comando para levantar un Docker con una BBDD PostgreSQL
  ```
  docker run -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres
  ```

El proyecto está configurado para trabajar con HTTPS por lo tanto habría que instalar los certificados que encontramos en la raíz del proyecto: `keystore.p12` y `mycertificate.cer`.

Al utilizar S3, deberemos configurar también en nuestro equipo el archivo credentials, si es MAC o Linux en la ruta `~/.aws/credentials`, si es Windows en `C:\Users\{tu_usuario}\.aws\credentials` con las variables `aws_access_key_id` y `aws_secret_access_key` que te proporciona AWS al crear un usuario IAM con tipo de acceso mediante programación y con el permiso AmazonS3FullAcces.  

Por último, para poder ejecutar el proyecto deberemos informar una serie de variables para la configuración inicial:

- CONSUMER_KEY: Consumer Key del API de Twitter
- CONSUMER_SECRET: Secret de la Key del API de Twitter
- INSTAGRAM_ACCESS_TOKEN: Token de la API de Instagram/Facebook
- AWS_S3_BUCKET_NAME: Nombre del bucket de S3
- AWS_S3_REGION: Región del bucket de S3