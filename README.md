slice
=====

0. Run:
  - ant run: starts server and 3 client instances
  - login using user1 (Buyer), user2 (Manufacturer) and user3 (Manufacturer);
    password is not important, but the username must be one of those 3 (they correspond to config files user1.cfg, user2.cfg, user3.cfg)

1. Structure:
  - constants: contains global constants
  - gui: contains interfaces/classes related to GUI
  - images: contains images for GUI
  - mediator: contains interfaces/calsses related to Mediator
  - model folder: contains interfaces / classes shared between all entities
  (for example: when a transfer occurs, Service should be sent from GUI
  to Mediator and then to Network); all classes that are here don't
  contain logic, but only information
  - model.service: Service interfaces / classes
  - model.service.info: Service info, like users with this service,
    service / offer / transfer states etc
  - model.state: Service / Offer / Transfer states
  - model.user: User interfaces / classes
  - network: contains interfaces/calsses related to Network
  - network.common: contains common code for Server / Client
  - network.model: contains classes shared between Server / Client
  - network.model.command: contains classes that respect Command Pattern
  - network.server: contains server classes
  - webserviceclient: contains interfaces/calsses related to Web
    Service Client

2. Observations:
  - User / Service objects are equal if they have same username / name
  - log files are in "log/" folder
  - transfer files are in "services/" and they are named like this:
    receiverUsername_serviceName_senderUsername

3. Testing:
  - different events (like making an offer, the user that makes the
    offer, etc) are generated randomly
  - initial users are generated random too so we could have more case
    scenario
  - there are services with no correspondent users too sometime and
    sometime they have a lot of users (depending on random generator)

4. Source Code Management:
  - git
  - https://github.com/cmihail/slice

5. GUI
  - Has a Login frame
  - Uses two tables, one for services, one for Buyers/ Manufacturers
    When a service is selected, the Buyers/Manufacturers belonging to that service.
  - Most of the funtionalities in MainFrame.java, extended by two classes for Buyer
    and Manufacturer, implementing the parts that are different

