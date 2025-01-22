# seat-reservations
Java Spring Boot client-server applicationfor seat reservations from a theater company with a graphical interface. 

Desktop application structured on 6 modules with layered architecture. 
- The model module comprises the data access layer with its validation ([domain package](https://github.com/Iri25/sse-orm-project-Iri25/tree/main/TheaterCompany/model/src/main/java/domain)).
- The entity data has been mapped with Hibernate ORM (Object Relational Mapping).
- The persistence module comprises the persistence layer with Java Database Connectivity (JDBC) for accessing the relational databases from Java program ([repository package](https://github.com/Iri25/sse-orm-project-Iri25/tree/main/TheaterCompany/persistence/src/main/java/repository)) and the services module contains the business layer which is an interface of all services ([service package](https://github.com/Iri25/sse-orm-project-Iri25/tree/main/TheaterCompany/services/src/main/java/service)).
- The server module implements all the functionalities required for the client module that comprises the presentation layer ([controller package](https://github.com/Iri25/sse-orm-project-Iri25/tree/main/TheaterCompany/client/src/main/java/client/controller)).
- The data is saved in the SQLite database using the connection configurations from the app.config file (client and server package).
- The Java library, Apache Log4j (client and server package) was used for messages history.
- The interaction with the user is done through a graphical interface (GUI), developed in JavaFX (fxml files are found in [views package](https://github.com/Iri25/sse-orm-project-Iri25/tree/main/TheaterCompany/client/src/main/resources/views)).

## Key concepts:
-- abstraction, encapsulation, inheritance, polymorphism, validations, exceptions, alerts, mapping, reading from a database and storing from a database.

## Requirements

A theatrical institution provides the spectators with a system for reserving seats for performances. 
Every day, the institution has only one performance, for which spectators can reserve seats starting from morning. 
For a place in the hall, the following information is stored: position (row x, lodge y, etc.), number and price. 
The terminals made available to spectators display the entire configuration of the hall, specifying for each place the position, number, price and condition (free or reserved). 
Using such of the terminal, the spectator can enter his personal data, can select one or more places and can trigger a button to reserve them. After each booking, all terminals will display updated situation regarding the occupancy of the room.

The application supports the following operations:
1. Registration
2. Login
3. Logout
4. Add show (admin)
5. Update show (admin)
6. Delete show (admin)
7. View shows (admin, spectator)
9. View seats (spectator)
10. Seat reservation (spectator)
11. Buy ticket in cash or by card (spectator)

The use cases can and application interaction planning can be found in the [Use cases. Iterations planning](https://github.com/Iri25/seat-reservations/blob/main/Use%20cases.%20Iterations%20planning.pdf) file.
