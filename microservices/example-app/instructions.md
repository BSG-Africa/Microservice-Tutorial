# Setup your Example Microservice
###Run 'jhipster' command in terminal from this directory and follow these steps:

1. Which *type* of application would you like to create? - **Select 'Microservice application' from options**
2. What is the base name of your application? - **Give it a name of 'Example'**
3. As you are running in a microservice architecture, on which port would like your server to run? It should be unique to avoid port conflicts. -**Use port 8082**
4. What is your default Java package name? - **Use package name 'za.co.bsg'**
5. Which service discovery server do you want to use? - **Select 'JHipster Registry (uses Eureka, provides Spring Cloud Config support and monitoring dashboards)' from options**
6. Which *type* of authentication would you like to use? - **Select 'Authentication with JHipster UAA server (the server must be generated separately)' from options**
7. What is the folder path of your UAA application? - **Default Option should show ../uaa - If so press Enter (if not type in ../uaa and press Enter)**
8. Which *type* of database would you like to use? - **Select 'SQL (H2, MySQL, MariaDB, PostgreSQL, Oracle, MSSQL)'  from options**
9. Which *production* database would you like to use? - **Select 'PostgreSQL' from options**
10. Which *development* database would you like to use? - **Select 'PostgreSQL' from options**
11. Do you want to use the Spring cache abstraction? - **Select No (when using an SQL database, this will also disable the Hibernate L2 cache) from options**
12. Would you like to use Maven or Gradle for building the backend? - **Select 'Maven' from options**
13. Which other technologies would you like to use? - **Press Enter**
14. Would you like to enable internationalization support? - **Type 'n' and press Enter**
15. Besides JUnit and Karma, which testing frameworks would you like to use? - **Select Cucumber from options using spacebar and Press Enter**
16. Would you like to install other generators from the JHipster Marketplace? - **Type 'n' and press Enter**


#Update Postgres config
1. Locate example-app/src/main/docker/postgresql.yml
2. Update the image value to: postgres:10.1-alpine

#Run Postgres Container
1. In terminal set example-app/src/main/docker to working directory.
2. Run 'docker-compose -f postgresql.yml up -d'

###Check for image
1. In terminal type 'docker images'
You should have have postgres in your REPOSITORY column and 10.1-alpine in your TAG column
2. Note that you will still only have a single entry for postgres because the image was already added to your docker registry
when you created your UAA Postgres instance.

###Check for container
1. In terminal type 'docker ps -a'
You should have have postgres container running a postgres:10.1-alpine IMAGE
2. You should also notice that in PORTS, your postgres container is forwarding for 5432 -> 5432. This means that
externally we are exposing port 5432 for connections and internally we are preserving the postgres default port which is 5432.
This becomes necessary when running multiple postgres servers as more than one server cannot occupy port 5432.
3. You should notice that in NAMES, there will be some alias which includes example

#Create a Movie Ticket API
1. In terminal, navigate to example-app/
2. Type `jhipster entity MovieTicket` and Press Enter
3. Do you want to add a field to your entity? - **Type `Y` and press Enter **
4. What is the name of your field - **Type `movieName` and press Enter **
5. What is the type of your field? - **Select `String` and press Enter **
6. Do you want to add validation rules to your field? - **Type `Y` and press Enter **
7. Which validation rules do you want to add? - **Select `Maximum length` using Space bar and press Enter **
8. What is the maximum length of your field? - **Type `40` and press Enter **
9. Repeat process to add a field called `userLogin` and then Type `N` to exit adding fields
10. Do you want to add a relationship to another entity? - **Type `N` and press Enter **
11. Do you want to use separate service class for your business logic? - **Select `No, the REST controller should use the repository directly` and press Enter **
12. Do you want pagination on your entity? - **Select `No, the REST controller should use the repository directly` and press Enter **
13. Overwrite src/main/resources/config/liquibase/master.xml? - **Type `y` and press Enter **


#Create Feign Client with Test Stub
1. Create a new package in za.co.bsg called `feign`.
2. Refactor location of UserDTO from `help-files` into feign package.
3. Refactor location of UserServiceClient from `help-files` into feign package.
4. Create an interface called UserService with a method UserDTO getLoggedInUserByUsername(String username).
5. Implement UserService in two classes (Both should be decorated with the Spring annotation `Service`)
    1. UserServiceImpl - add a Spring profile with name `dev`
    2. UserServiceStub - add a Spring profile with name `default` (This should return a UserDTO which has the login manually set to `stubbed-login`)