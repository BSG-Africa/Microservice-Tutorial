# Setup your API Gateway
###Run 'jhipster' command in terminal from this directory and follow these steps:

1. Which *type* of application would you like to create? - **Select 'Microservice gateway' from options**
2. What is the base name of your application? - **Give it a name of 'ApiGateway'**
3. As you are running in a microservice architecture, on which port would like your server to run? It should be unique to avoid port conflicts. -**Use port 8081**
4. What is your default Java package name? - **Use package name 'za.co.bsg'**
5. Which service discovery server do you want to use? - **Select 'JHipster Registry (uses Eureka, provides Spring Cloud Config support and monitoring dashboards)' from options**
6. Which *type* of authentication would you like to use? - **Select 'Authentication with JHipster UAA server (the server must be generated separately)' from options**
7. What is the folder path of your UAA application? - **Default Option should show ../uaa - If so press Enter (if not type in ../uaa and press Enter)**
8. Which *type* of database would you like to use? - **Select 'SQL (H2, MySQL, MariaDB, PostgreSQL, Oracle, MSSQL)'  from options**
9. Which *production* database would you like to use? - **Select 'PostgreSQL' from options**
10. Which *development* database would you like to use? - **Select 'PostgreSQL' from options**
11. Do you want to use Hibernate 2nd level cache? - **Type 'n' and press Enter**
12. Would you like to use Maven or Gradle for building the backend? - **Select 'Maven' from options**
13. Which other technologies would you like to use? - **Press Enter**
14. Which *Framework* would you like to use for the client? - **Select 'Angular 5' from options**
15. Would you like to enable *SASS* support using the LibSass stylesheet preprocessor? - **Type 'n' and press Enter**
16. Would you like to enable internationalization support? - **Type 'n' and press Enter**
17. Besides JUnit and Karma, which testing frameworks would you like to use? - **Press Enter**
18. Would you like to install other generators from the JHipster Marketplace? - **Type 'n' and press Enter**

#Update Postgres config
1. Locate api-gateway/src/main/docker/postgresql.yml
2. Update the image value to: postgres:10.1-alpine
3. Update ports value to: 5431:5432

#Run Postgres Container
1. In terminal set api-gateway/src/main/docker to working directory.
2. Run 'docker-compose -f postgresql.yml up -d'

###Check for image
1. In terminal type 'docker images'
You should have have postgres in your REPOSITORY column and 10.1-alpine in your TAG column
2. Note that you will still only have a single entry for postgres because the image was already added to your docker registry
when you created your UAA Postgres instance.

###Check for container
1. In terminal type 'docker ps -a'
You should have have postgres container running a postgres:10.1-alpine IMAGE
2. You should also notice that in PORTS, your postgres container is forwarding for 5431 -> 5432. This means that
externally we are exposing port 5431 for connections and internally we are preserving the postgres default port which is 5432.
This becomes necessary when running multiple postgres servers as more than one server cannot occupy port 5432.
3. You should notice that in NAMES, there will be some alias which includes api-gateway

#Run JHipster Registry Container
1. In terminal set uaa/src/main/docker to working directory.
2. Run 'docker-compose -f jhipster-registry up -d'

###Check for image
1. In terminal type 'docker images'
You should have have jhipster/jhipster-registry in your REPOSITORY column

