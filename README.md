slice
=====

1. Structure:
  - gui folder: contains interfaces/classes related to GUI
  - mediator folder: contains interfaces/calsses related to Mediator
  - model: contains interfaces / classes shared between all entities
  (for example: when a transfer occurs, Service should be sent from GUI
  to Mediator and then to Network); all classes that are here don't
  contain logic, but only information
  - model.service: Service interfaces / classes
  - model.service.info: Service info, like users with this service,
    service / offer / transfer states etc
  - model.state: Service / Offer / Transfer states
  - model.user: User interfaces / classes
  - network folder: contains interfaces/calsses related to Network
  - webserviceclient folder: contains interfaces/calsses related to Web
    Service Client

2. Observations:
  - User / Service objects are equal if they have same username / name

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

  
 Radu-started working on the gui
