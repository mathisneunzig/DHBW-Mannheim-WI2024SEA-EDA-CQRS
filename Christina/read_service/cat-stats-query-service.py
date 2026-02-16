from flask import Flask, jsonify
import threading, pika, json

app = Flask(__name__)

# Unser Readmodel (In-Memory für die Aufgabe) [cite: 79]
stats = {"feed_count": 0, "last_fed": None}

def consume_events():
    connection = pika.BlockingConnection(pika.ConnectionParameters(host='rabbitmq'))
    channel = connection.channel()
    channel.exchange_declare(exchange='cat_events', exchange_type='fanout')
    
    # Temporäre Queue für diesen Service
    result = channel.queue_declare(queue='', exclusive=True)
    channel.queue_bind(exchange='cat_events', queue=result.method.queue)

    def callback(ch, method, properties, body):
        data = json.loads(body)
        if data['event'] == 'cat.fed':
            stats["feed_count"] += 1
            stats["last_fed"] = data['data']['name']
            print(f"Readmodel aktualisiert: {stats}")

    channel.basic_consume(queue=result.method.queue, on_message_callback=callback, auto_ack=True)
    channel.start_consuming()

# Starte den Event-Consumer in einem eigenen Thread
threading.Thread(target=consume_events, daemon=True).start()

@app.route('/stats', methods=['GET'])
def get_stats():
    # Schnelle Abfrage des optimierten Readmodels [cite: 57, 79]
    return jsonify(stats)