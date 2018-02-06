# TODO : Service section; `kubectl expose`; `minikube service list`

#Run Postgres Container
1. In terminal set uaa/src/main/docker to working directory.
2. Run 'docker-compose -f postgresql.yml up -d'

###Check for image
1. In terminal type 'docker images'
You should have have postgres in your REPOSITORY column and 10.1-alpine in your TAG column

###Check for container
1. In terminal type 'docker ps -a'
You should have have postgres container running a postgres:10.1-alpine IMAGE
2. You should also notice that in PORTS, your postgres container is forwarding for 5430 -> 5432. This means that
externally we are exposing port 5430 for connections and internally we are preserving the postgres default port which is 5432.
This becomes necessary when running multiple postgres servers as more than one server cannot occupy port 5432.

#Run JHipster Registry Container
1. In terminal set uaa/src/main/docker to working directory.
2. Run 'docker-compose -f jhipster-registry up -d'

###Check for image
1. In terminal type 'docker images'
You should have have jhipster/jhipster-registry in your REPOSITORY column

###Check for container
1. In terminal type 'docker ps -a'
You should have have postgres container running a jhipster/jhipster-registry:v3.2.4 IMAGE

###Login to Frontend
1. Navigate to http://localhost:8761/#/ 
2. Login Details: 
- User: admin  
- Password: admin
