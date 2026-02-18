import pika

connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()
channel.queue_declare(queue='events')

def callback(ch, method, properties, body):
    print(f"Received {body}")

channel.basic_consume(queue='events',
                    auto_ack=True,
                    on_message_callback=callback)

if __name__ == "__main__":
    connection.close()