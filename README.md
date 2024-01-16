## PD Library Catalog

Catalog is one of the services that makes PD Library.

It keeps track of book titles, authors, descriptions, reviews, ratings, QR codes, and genres. 
Basically of everything that can be read on the physical book itself, plus reviews and ratings left by readres, that also help to explain the book.   

It does not know who rented the book or if there is an available copy of the book to rent.

It can be considered as one of those static paper catalogs from Lidl or Ikea, whose purpose is only to
inform consumers about its offer.

### Delivery mechanism

* REST API served over [Gateway](https://github.com/ProductDock/rbc-library-gateway)

### Security

* Protected by the User Profile JWT token issued by the [PD Library User Profile](https://github.com/ProductDock/rbc-library-user-profiles) service

### Dependencies

1. MySQL database
2. Kafka

Dependencies can be run from [Infrastructure](https://github.com/ProductDock/rbc-library-infrastructure) project,
by [docker compose](https://docs.docker.com/compose/) scripts.

### Build and run locally

Locally, application can be started in two ways.

* From source code
* From latest published Docker image
 
#### From source code (Run from IntelliJ)

_Suitable when developing features in this project._ 

1. Set [Spring Profile](https://docs.spring.io/spring-boot/docs/1.1.4.RELEASE/reference/html/boot-features-profiles.html) to local by following these steps:
- Open "Edit Configurations"
- in the section "Library Application" find the field "Program arguments";
- enter the following command: --spring.profiles.active=local
2. Press Run or Debug Library Application

#### From latest published Docker image

_Suitable when developing features in other projects, and you need this one as a dependency._

For this purpose see [Infrastructure](https://github.com/ProductDock/rbc-library-infrastructure) project.  