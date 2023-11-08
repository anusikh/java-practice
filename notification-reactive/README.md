### Reactive Programming

- uses Spring WebFlux, Reactive RabbitMQ and Reactive Redis
- it's a basic notification service
- `curl http://locahost:8080/stream` returns sse event
- `curl --location 'http://localhost:8080/put' \
  --header 'Content-Type: application/json' \
  --data '{
  "id": "456",
  "message": "anusikh panda"
  }'` for saving the event in redis and sending it to mq
- in RabbitMQ, a producer never sends a message directly to a queue. Instead, it uses an exchange as a routing mediator.