## PD Library REST API


## Build and run by executing following steps manually

### Production

Production is running in GCP.

1. #### Follow [usual steps](https://cloud.google.com/compute/docs/instances/connecting-to-instance) for connecting to a VM running in GCP 
   Connect via SSH to a **rbc-library-vm**.
2. #### Go to a folder where backend is:
    ``` 
    cd /home/pd-library/rbc-library
    ``` 
3. #### This is a Git repository. Pull most recent version from main:
    ```  
   sudo git checkout main
   sudo git pull
    ``` 
4. #### Maven is already installed, so you can build new jar with maven:

   **!!! Important: You need to switch to a root user !!!**

   _Why? Otherwise it will download entire .m2 repository to a home folder of a user who made SSH connection._
    ```  
    sudo su
   ../apache-maven-3.8.5/bin/mvn package
    ``` 
5. #### Run a jar from maven target folder:

   Actually, **rbc-library-vm** is configured with startup-script. Startup script already contains instructions for running the jar:

   **Startup-script content:**
    
    ```
    #! /bin/bash
    SPRING_DATASOURCE_PASSWORD=<REAL_PASS_HERE> SPRING_PROFILES_ACTIVE=prod java -jar /home/pd-library/rbc-library/target/rbc-library-0.0.1-SNAPSHOT.jar
    ``` 
6. Restart **rbc-library-vm** for new changes to take effect.




### Local

Locally, application is started and built by IDE (IntelliJ).

IntelliJ needs to be instructed to run app with following parameters:
- SPRING_DATASOURCE_PASSWORD
- SPRING_PROFILES_ACTIVE

It's either by putting them in **.bash_profile** or configuring them in IntelliJ.

When running from IntelliJ IDEA, follow these steps:
- Open "Edit Configurations";
- in the section "Library Application" find the field "Program arguments";
- enter the following command: --spring.profiles.active=local --spring.datasource.password=
