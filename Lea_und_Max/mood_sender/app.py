import os
import json
import pika
from datetime import datetime
from flask import Flask, render_template, jsonify
import time

app = Flask(__name__)

RABBITMQ_HOST = os.environ.get('RABBITMQ_HOST', 'localhost')
RABBITMQ_PORT = int(os.environ.get('RABBITMQ_PORT', 5672))
RABBITMQ_USER = os.environ.get('RABBITMQ_USER', 'guest')
RABBITMQ_PASS = os.environ.get('RABBITMQ_PASS', 'guest')
QUEUE_NAME = 'mood_events'

def get_rabbitmq_connection():
    credentials = pika.PlainCredentials(RABBITMQ_USER, RABBITMQ_PASS)
    parameters = pika.ConnectionParameters(
        host=RABBITMQ_HOST,
        port=RABBITMQ_PORT,
        credentials=credentials,
        heartbeat=600,
        blocked_connection_timeout=300
    )
    
    max_retries = 5
    for attempt in range(max_retries):
        try:
            connection = pika.BlockingConnection(parameters)
            channel = connection.channel()
            channel.queue_declare(queue=QUEUE_NAME, durable=True)
            return connection, channel
        except Exception as e:
            if attempt < max_retries - 1:
                time.sleep(2)
            else:
                raise e

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/send/<mood_type>')
def send_mood(mood_type):
    if mood_type not in ['good', 'bad']:
        return jsonify({'error': 'Invalid mood type'}), 400
    
    try:
        connection, channel = get_rabbitmq_connection()
        
        message = {
            'type': mood_type,
            'timestamp': datetime.utcnow().isoformat()
        }
        
        channel.basic_publish(
            exchange='',
            routing_key=QUEUE_NAME,
            body=json.dumps(message),
            properties=pika.BasicProperties(
                delivery_mode=2,
            )
        )
        
        connection.close()
        
        return jsonify({
            'success': True,
            'message': f'{mood_type.capitalize()} mood sent!',
            'data': message
        })
    
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    print("Starting Mood Sender service...")
    print(f"RabbitMQ Host: {RABBITMQ_HOST}")
    app.run(host='0.0.0.0', port=5000, debug=True)
