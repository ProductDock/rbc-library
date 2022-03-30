## RBC Library

##Pre requirements

In order to start the application locally
environment variable SPRING_DATASOURCE_PASSWORD should be set.


Put this in your .bash_profile

export SPRING_DATASOURCE_PASSWORD=YOUR_DB_PASSWORD

###Setup Spring profiles

Before starting our application, we need to set up an active spring profile.

Follow these steps:
- Open "Edit Configurations";
- in the section "Library Application" find the field "Program arguments";
- enter the following command: --spring.profiles.active=local

If you need "Prod development", command is:--spring.profiles.active=prod
