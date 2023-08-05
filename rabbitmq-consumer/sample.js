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