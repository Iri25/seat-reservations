Java client-server application structured on 6 modules with layered architecture. The model module comprises the data access layer with data validation ([domain package](https://github.com/Iri25/sse-orm-project-Iri25/tree/main/TheaterCompany/model/src/main/java/domain)). The entity data has been mapped to the Hibernate ORM (Object Relational Mapping). The persistence module comprises the persistence layer cu Java Database Connectivity (JDBC) for accessing the relational databases from Java program ([repository package](https://github.com/Iri25/sse-orm-project-Iri25/tree/main/TheaterCompany/persistence/src/main/java/repository)) and the services module contains the business layer which is an interface of all services ([service package](https://github.com/Iri25/sse-orm-project-Iri25/tree/main/TheaterCompany/services/src/main/java/service)). The server module implements all the functionalities required for the client module that comprises the presentation layer ([controller package](https://github.com/Iri25/sse-orm-project-Iri25/tree/main/TheaterCompany/client/src/main/java/client/controller)). 

The data is saved in the MySQL database using the connection configurations from the app.config file (client and server). The interaction with the user is done through a graphical interface (GUI), developed in JavaFX (fxml files are found in [views package](https://github.com/Iri25/sse-orm-project-Iri25/tree/main/TheaterCompany/client/src/main/resources/views)).

Key concepts are abstraction, encapsulation, inheritance, polymorphism, validations, exceptions, reading from files and storing in files, reading from a database and storing from a database.

Application for a social network with a graphical interface which supports operations such as:

SEAT RESERVATIONS

A theatrical institution provides the spectators with a system for reserving seats for performances.
Every day, the institution has only one performance, for which spectators can reserve seats starting from morning. For a place in the hall, the following information is stored: position (row x, lodge y, etc.), number and price. The terminals made available to spectators display the entire configuration of the hall, specifying for each place the position, number, price and condition (free or reserved).
Using such of the terminal, the spectator can enter his personal data, can select one or more places and can trigger a button to reserve them. After each booking, all terminals will display updated situation regarding the occupancy of the room.

The solution is based on the concepts of software systems engineering.
1. The first step is the functional model (UML diagram of use cases with textual / tabular description of use cases, with normal scenario and possible alternative / exceptional scenarios), use case planning on 3 iterations.
2. The second step is the conceptual model (represented using a UML class diagram) and the prototype of the graphical user interface.
3. Then, the representation of the scenarios related to the use cases from iteration 1, through interaction diagrams (sequence diagrams and communication / collaboration diagrams)
Followed by the refinement of the initial class diagram, including the entities in the field of the solution related to iteration 1. Implementation iteration 1: the first functional version of the application.
4. Representation of use case scenarios in iteration 2, through interaction diagrams (sequence diagrams and communication / collaboration). Refine the initial class diagram, including the entities in the iteration 2 solution field Implementation of iteration 2: the second functional version of the application.
5. Representation of the scenarios related to the use cases from iteration 3, through interaction diagrams (sequence and communication / collaboration diagrams). Refining the initial class diagram, including the entities in the field of the solution related to iteration 3. Iteration 3: the final version of the application.
