# Run and interact with your first microservice - _User Authorization and Authentication (UAA)_

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

### Deploy PostgreSQL to Minikube

Minikube is a single-node Kubernetes cluster running in a virtual machine on your host. It requires a VM driver, such as
VirtualBox.

Create a deployment:

    kubectl run uaa-db --image postgres:10.1-alpine --env "POSTGRES_USER=uaa" --env "POSTGRES_PASSWORD=" --port 5432

Watch the container come up:

    kubectl get pods -w

Port-forward the port on the pod to the host and connect using the `psql` client:

    kubectl port-forward <uaa-db pod name> 5432:5432
    # In a second terminal:
    psql -h localhost -p 5432 -w -U uaa uaa 
    \dt

Expose a service:

    kubectl expose deployment uaa-db --port 5432 --target-port 5432 --type NodePort --name uaa-db

Confirm that the service was created:

    kubectl get service

Because we used `--type NodePort` each service is assigned a random port on the VM. Inspect using:

    minikube service list 

Then, using that information, connect to the DB:

    # Replace the host and port with the appropriate values
    psql -h 192.168.99.100 -p 32116 -w -U uaa uaa

### Deploy the UAA application to Minikube

First, deploy the service registry:

    kubectl run jhipster-registry --image "jhipster/jhipster-registry:v3.2.4" --env "SPRING_PROFILES_ACTIVE=dev,native,swagger" --env "SECURITY_USER_PASSWORD=admin" --env "JHIPSTER_REGISTRY_PASSWORD=admin" --port 8761 --expose
    
Deploy the UAA application:

    kubectl run uaa --image=uaa --image-pull-policy=IfNotPresent --env "SPRING_PROFILES_ACTIVE=dev,swagger" --env "EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://admin:admin@jhipster-registry:8761/eureka" --env "SPRING_CLOUD_CONFIG_URI=http://admin:admin@jhipster-registry:8761/config" --env "SPRING_DATASOURCE_URL=jdbc:postgresql://uaa-db:5432/uaa" --env "JHIPSTER_SLEEP=30" --port 8080 --expose

The deployment fails because the docker daemon in Minikube can't find the UAA image locally or in docker hub.

### Point the docker client to Minikube's docker daemon

Because we want to run the application using Kubernetes running in Minikube we need to make the image 
available to Minikube. To do this point docker client to Minikube's docker daemon. Currently your docker client is 
pointing to your host's daemon.

Start minikube:

    minikube start
    
Change environment variables to point the docker client to Minikube's docker daemon:

First, inspect the output of:

    minikube docker-env

Follow that instruction, and run: 

    eval $(minikube docker-env)
    
Now, run `docker images` and note that it is different, because it is a different docker daemon. Also, note that the 
uaa image is not present.

Next, build the image again using maven, as before.

The image should now be available in Minikube's docker daemon, and the application should start.
