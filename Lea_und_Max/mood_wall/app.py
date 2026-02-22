import os
import json
import pika
import threading
import time
from flask import Flask, render_template, jsonify

app = Flask(__name__)

RABBITMQ_HOST = os.environ.get('RABBITMQ_HOST', 'localhost')
RABBITMQ_PORT = int(os.environ.get('RABBITMQ_PORT', 5672))
RABBITMQ_USER = os.environ.get('RABBITMQ_USER', 'guest')
RABBITMQ_PASS = os.environ.get('RABBITMQ_PASS', 'guest')
QUEUE_NAME = 'mood_events'

mood_stats = {
    'good': 0,
    'bad': 0,
    'score': 0
}

def get_rabbitmq_connection():
    credentials = pika.PlainCredentials(RABBITMQ_USER, RABBITMQ_PASS)
    parameters = pika.ConnectionParameters(
        host=RABBITMQ_HOST,
        port=RABBITMQ_PORT,
        credentials=credentials,
        heartbeat=600,
        blocked_connection_timeout=300
    )
    
    max_retries = 10
    for attempt in range(max_retries):
        try:
            connection = pika.BlockingConnection(parameters)
            channel = connection.channel()
            channel.queue_declare(queue=QUEUE_NAME, durable=True)
            return connection, channel
        except Exception as e:
            print(f"Connection attempt {attempt + 1} failed: {e}")
            if attempt < max_retries - 1:
                time.sleep(3)
            else:
                raise e

def callback(ch, method, properties, body):
    try:
        message = json.loads(body)
        mood_type = message.get('type')
        timestamp = message.get('timestamp')
        
        print(f"Received mood: {mood_type} at {timestamp}")
        
        if mood_type == 'good':
            mood_stats['good'] += 1
        elif mood_type == 'bad':
            mood_stats['bad'] += 1
        
        mood_stats['score'] = mood_stats['good'] - mood_stats['bad']
        
        print(f"Current stats: {mood_stats}")
        
        ch.basic_ack(delivery_tag=method.delivery_tag)
        
    except Exception as e:
        print(f"Error processing message: {e}")
        ch.basic_ack(delivery_tag=method.delivery_tag)

def start_consumer():
    print("Starting RabbitMQ consumer...")
    time.sleep(5)
    
    try:
        connection, channel = get_rabbitmq_connection()
        channel.basic_qos(prefetch_count=1)
        channel.basic_consume(queue=QUEUE_NAME, on_message_callback=callback)
        
        print("Consumer started. Waiting for messages...")
        channel.start_consuming()
        
    except Exception as e:
        print(f"Consumer error: {e}")
        time.sleep(5)
        start_consumer()

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/stats')
def get_stats():
    return jsonify(mood_stats)

if __name__ == '__main__':
    print("Starting Mood Wall service...")
    print(f"RabbitMQ Host: {RABBITMQ_HOST}")
    
    consumer_thread = threading.Thread(target=start_consumer, daemon=True)
    consumer_thread.start()
    
    app.run(host='0.0.0.0', port=5000, debug=False)
