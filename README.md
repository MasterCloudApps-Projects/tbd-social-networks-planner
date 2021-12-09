# TBD-SOCIAL-NETWORKS-PLANNER

![diagrama-tbd-social-network](https://user-images.githubusercontent.com/42999826/144487980-87618975-8039-41d6-8221-06df54fc18f9.png)


This project consists in an application developed with the TBD technique, the application allows the user to interact with Twitter and Instagram by on demand or scheduled posting.

It is a monolith deployed in heroku with a PostgreSQL database, we use that database to manage the posts images with the AWS S3 PAAS.

We have two pipelines that runs:
- `main.yml`: On pushing to main
- `main-pr.yml`: On a pull request over main

The first runs the tests, in case of ok, it builds the project, generates an image and publishes it into Heroku Registry and DockerHub, finally deploys the application in Heroku.
The second one just runs the tests, in case of ok, it builds the project.

Finally, when the action deploys the application we can see all the possible calls to the api in the following URL: [Swagger TBD-SOCIAL-NETWORK-PLANNER](https://ais-tbd-social-networks.herokuapp.com/swagger-ui.html)

We have several profiles for local deploy and testing:
- test: For the test execution, with an H2 database. HTTPS is disabled
- dev: For the local testing, with an H2 database. HTTPS is enabled.
- flyway: For testing the FlyWay scripts behaviour. We may run the following command to start a PostgreSQL database Docker container.
  ```
  docker run -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres
  ```

The project configuration allows us to work with HTTPS, that is why we should install the certificates located at root project: `keystore.p12` y `mycertificate.cer`.

Using S3, implies that we should also configure the credentials file, if it is Linux or Mac, we will create it in `~/.aws/credentials`, if it is Windows, we will create it in `C:\Users\{tu_usuario}\.aws\credentials`. We have to add the following variables, `aws_access_key_id` and `aws_secret_access_key`, theese variables are provided by AWS, when you create an IAM user with programmaticaly acces and the AmazonS3FullAcces permission.

Finally, we should set some variables to be able to run the project:

- CONSUMER_KEY: Twitter API Consumer Key
- CONSUMER_SECRET: Twitter API Secret
- INSTAGRAM_ACCESS_TOKEN: Instagram/Facebook API token
- AWS_S3_BUCKET_NAME: S3 bucket name
- AWS_S3_REGION: S3 bucket region
