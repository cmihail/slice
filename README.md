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
