# Run and interact with your first microservice - _User Authorization and Authentication (UAA)_

# TODO : Service section; `kubectl expose`; `minikube service list`

## Run Postgres Container
1. In terminal set uaa/src/main/docker to working directory.
2. Run `docker-compose -f postgresql.yml up -d`

### Check for image
1. In terminal type `docker images`
You should have have postgres in your REPOSITORY column and 10.1-alpine in your TAG column

### Check for container
1. In terminal type `docker ps`
You should have have postgres container running a postgres:10.1-alpine IMAGE
2. You should also notice that in PORTS, your postgres container is forwarding for 5430 -> 5432. This means that
externally we are exposing port 5430 for connections and internally we are preserving the postgres default port which is 5432.
This becomes necessary when running multiple postgres servers as more than one server cannot occupy port 5432.
3. You should notice that in NAMES, there will be some alias which includes uaa

## Run JHipster Registry Container
1. In terminal set uaa/src/main/docker to working directory.
2. Run `docker-compose -f jhipster-registry.yml up -d`

### Check for image
1. In terminal type `docker images`
You should have have jhipster/jhipster-registry in your REPOSITORY column

### Check for container
1. In terminal type `docker ps`
You should have have postgres container running a jhipster/jhipster-registry:v3.2.4 IMAGE

### Login to Frontend
1. Navigate to http://localhost:8761/#/ 
2. Login Details: 
- User: admin  
- Password: admin

## Run the UAA app using IntelliJ

1. Open this project with IntelliJ.
1. Right-click on the file `microservices/uaa/pom.xml` and click "Add as Maven Project"
1. Once IntelliJ has loaded the project, open _(Ctrl-n on most systems)_ the class `UaaApp`
1. Configure a SDK 
1. Run the application

### Interact with the UAA server

Check that the server is healthy using cURL:

    curl http://localhost:8080/management/health
    
Stop the application in IntelliJ.

## Build and run the UAA app using its build artifact

Change your working directory to `microservices/uaa/`. Build the application using maven:

    ./mvnw -Pdev clean package

Run the application using the `java -jar` command:

    java -jar target/*.war

Use cURL to check that everything worked. Stop the application when you're done.

## Package the UAA app in a Docker image

### Build the image using maven

First, inspect the Dockerfile, `microservices/uaa/src/main/docker/Dockerfile`.

Next, build the image:

    ./mvnw verify -Pprod dockerfile:build
    
Check that the image was added to docker:

    docker images

### Run the docker container

First, inspect the compose file, `microservices/uaa/src/main/docker/app.yml`.

docker-compose -f src/main/docker/app.yml up -d

## Deploy the UAA app in Minikube

### Point the docker client to Minikube's docker daemon
Because we want to eventually run the application using Kubernetes running in Minikube we need to make the image 
available to Minikube. To do this point docker client to Minikube's docker daemon. Currently your docker client is 
pointing to your host's daemon.

Start minikube:

    minikube start
    
Change environment variables to point the docker client to Minikube's docker daemon:

First, inspect the output of:

    minikube docker-env

Follow that instruction, and run: 

    eval $(minikube docker-env)
    
Now, run `docker images` and note that it is different, because it is a different docker daemon.
