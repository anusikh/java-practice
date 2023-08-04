for zipkin server: docker run -d -p 9411:9411 openzipkin/zipkin

possible approaches for rabbitmq:
- u can create a new queue for every user login and send messages to only that queue
```java
    @Autowired
    private AmqpAdmin amqpAdmin;
    amqpAdmin.declareQueue(new Queue(authRequest.getUsername()));
```

```java
    @Autowired 
    private RabbitTemplate rabbitTemplate;
    rabbitTemplate.convertAndSend("anusikh123", "hello");
```

- u can send messages to a single queue and segregate based on routing_key which will be the "key.<username>" (current-impl)

- to consume the notifications in js
```js
const amqplib = require("amqplib/callback_api");

amqplib.connect('amqp://anusikh2001:Malaya143!@127.0.0.1', (err, conn) => {
  if (err) throw err;

  // Listener
  conn.createChannel((err, ch2) => {
    if (err) throw err;

    ch2.assertQueue("sample_queue");

    ch2.consume("sample_queue", (msg) => {
      if (msg !== null) {
        console.log(msg.fields.routingKey);
        console.log(msg.properties);
        console.log(msg.content.toString());
        ch2.ack(msg);
      } else {
        console.log('Consumer cancelled by server');
      }
    });
  });
});
```