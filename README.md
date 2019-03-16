# contact-services
Basic CRUD API's for contacts developed with Spring Boot

    1. Services is developed as a fat jar. once started will use tomcat embedded and runs at  **http://localhost:8080**
    2. Database used is H2. In memory database. H2 console can be seen at **http://localhost:8080/h2console**
    3. DB details: 
            jdbc url: jdbc:h2:mem:contactdb
            username: sa
            password: {blank}
    4. Authentication is enable for all api's. username=sarath, password=password
         Authorization header is needed
         Authorization:Basic c2FyYXRoOnBhc3N3b3Jk
    5. Pagination is enabled with query parameters **offset** and **limit** 
    6. Swagger UI is used and url can be seen at **http://localhost:8080/swagger-ui.html#/Contacts**
    7. Using Try it out. Api's can be directly tested from the swagger. Authentication is removed for h2 console & swagger ui paths
