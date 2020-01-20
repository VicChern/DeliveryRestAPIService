# Kinda Express King
##### Project of the SoftServe IT Academy Java group KV-061

##### Concept: 
BaaS for delivery services like Glovo, Uber or even for regular mail. Also, a simple web front-end should be provided as an example of a typical consumer's app.

##### Key features:
- Unique public TrackingID for every item
- A full audit of every item movement and state changes
- Realtime tracking (via Open Street Maps)
- Different roles: sender, receiver, deliveryman, tenant admin, global admin
- Multiple tenants (organizations)
- On-Premise (with local DB) and Cloud deployment options
- Public RESTful API
- OAuth powered by Auth0
- Global admin page (for debug and demos)

##### Example app features:
- A usage example for every API
- MVP Web UI (bootstrap or even something simpler)
- Demo mode (with procedural scenarios)

##### Technical requirements:
- Fully covered with unit tests
- Platform independent (IaaS, Docker)
- Uses only "always free" integration options
- Should be able to handle 10-100k items per minute

##### Stack: 
- Java 11, Spring (Core, Data, Security), Hibernate, PostgreSQL, GCP/AWS, REST API, GitLab