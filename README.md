## Description
**SpringBoot & MongoDB REST/ JSON API**

Basic example to create an REST/ JSON API with Spring and MongoDb.  


## Install/Start Application

1) You need MongoDb installed in your machine. (You can find the installer in https://www.mongodb.com/download-center#community. The Community Version is free. Download and install the correct version for you OS).

2) Open a CLI and execute the mongod service. (Example in Windows: C:\Program Files\MongoDB\Server\3.6\bin\mongod.exe or add the path C:\Program Files\MongoDB\Server\3.6\bin to the environment variables).

3) Open a CLI and go to the root directory of the project (where the pom.xml exists).

4) Execute command: mvnw package && java -jar target/mycharacters-api-server-0.1.0.jar. (This download all dependencies with maven, execute the test and launch the application in an embedded HTTP server).

5) Open a browser and go to localhost:8080/. If everything was ok, you can see a list of functions of the API Rest.

6) You can use curl via CLI to communicate with the application (GET, POST, PUT, DELETE).

- Possible connection issues: Verify in application.properties file the ports of mongodb and the embedded server are the same that you are using. By default they are mongodb=27017, server=8080.

