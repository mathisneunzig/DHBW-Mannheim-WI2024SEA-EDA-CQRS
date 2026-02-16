from flask import Flask, request, jsonify
import pika, json

app = Flask(__name__)

def send_event(event_type, data):
    # Verbindung zu RabbitMQ aufbauen (Host aus docker-compose)
    connection = pika.BlockingConnection(pika.ConnectionParameters(host='rabbitmq'))
    channel = connection.channel()
    channel.exchange_declare(exchange='cat_events', exchange_type='fanout')
    
    message = json.dumps({'event': event_type, 'data': data})
    channel.basic_publish(exchange='cat_events', routing_key='', body=message)
    connection.close()

@app.route('/feed', methods=['POST'])
def feed_cat():
    # Hier würde normalerweise die Validierung/Logik stehen [cite: 62]
    cat_name = request.json.get('name', 'Unbekannte Katze')
    
    # Event auslösen: "Fütterung durchgeführt" [cite: 70, 83]
    send_event('cat.fed', {'name': cat_name, 'amount': '50g'})
    
    return jsonify({"status": "Command gesendet: Katze wird gefüttert!"}), 202