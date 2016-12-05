# ticket-service
example ticket booking service
* Description:
  * It’s a basic implementation of TicketSevice based on  in-memory storage. But it does have hooks for extending it to DB/or any other storage mechanism by implementing DAO for corresponding backend.
    * It finds best seats based on price, but more complex implementation based on seat rank can be plugged in to it by providing the implementation for BestSeatStrategy
    * Code has been organized in package: com.example.ticket.api.* and com.example.ticket.impl.*
      * com.example.ticket.api.* - this contains the generic classes or interfaces
      * com.example.ticket.impl.* - this contains the specific implementation classes for classes/interfaces contained in com.example.ticket.api.* package
    * Design thoughts
      * Loose coupling
      * Easy to extend
      * Easy to test
    * Design patterns applied:
      * DAO
      * Factory
      * Abstract Factory
      * Dependency Injection
    
* Limitations with current implementation:
 * It’s not thread safe
 * It doesn’t support transaction
 * Some of the implementation classes will give guaranteed results in single instance of application
   For example, SimpleSeatHoldIdGenerator is such a class that will guarantee uniqueness in single instance of application.
   For multi instance/horizontal scalining there is a need to implement more sophisticated version of SeatHoldIdGenerator.
 * (Due to lack of time) Only one class has been juint tested, that is TicketServiceImplTest  but this should give the idea of what kind of thought process has been given while writing juints.
 * It doesn't expose any server port where client can connect remotely lik

* TODO:
  Need to add more junits

* Softwares Required:
  * Java 8
  * maven 3.3.9

* Build steps:
 * 1)create a new directry and cd to it.
 * 2)git clone https://github.com/esskeje/ticket-service.git
 * 3)cd ticket-service/
 * 4) mvn clean install ---this will build the code and run juints

* Run a simple java main program to see the E2E service in action:
  * java -jar target/ticket-service-0.0.1-SNAPSHOT-fat.jar 

* Output:
  ```
  Ticket Service! START
  Initial numSeatsAvailable: 90
  SeatHold=1, abc1@gmail.com
  After 2 seats held, numSeatsAvailable: 88
  After above 2 seats reserved, numSeatsAvailable: 88
  SeatHold=2, abc2@gmail.com
  After another 2 seats held, numSeatsAvailable: 86
  After above 2 held seats reserved, numSeatsAvailable: 86
  Ticket Service! END.
  ```
