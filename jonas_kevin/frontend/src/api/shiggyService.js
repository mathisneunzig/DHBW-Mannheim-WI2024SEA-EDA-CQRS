import axios from 'axios';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const API_URL = 'http://localhost:8080';
const WS_URL = 'http://localhost:8080/ws';

class ShiggyService {
  constructor() {
    this.stompClient = null;
    this.connected = false;
  }

  connect(onStatusUpdate) {
    return new Promise((resolve, reject) => {
      const socket = new SockJS(WS_URL);
      
      this.stompClient = new Client({
        webSocketFactory: () => socket,
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        
        onConnect: () => {
          console.log('WebSocket connected');
          this.connected = true;
          
          this.stompClient.subscribe('/topic/status', (message) => {
            const status = JSON.parse(message.body);
            console.log('Received status update:', status);
            onStatusUpdate(status);
          });
          
          this.stompClient.publish({
            destination: '/app/requestStatus'
          });
          
          resolve();
        },
        
        onStompError: (frame) => {
          console.error('STOMP error:', frame);
          this.connected = false;
          reject(frame);
        },
        
        onWebSocketClose: () => {
          console.log('WebSocket connection closed');
          this.connected = false;
        }
      });
      
      this.stompClient.activate();
    });
  }

  disconnect() {
    if (this.stompClient) {
      this.stompClient.deactivate();
      this.connected = false;
    }
  }

  async feedShiggy() {
    try {
      await axios.post(`${API_URL}/commands/feed`);
      console.log('Feed command sent');
    } catch (error) {
      console.error('Error sending feed command:', error);
      throw error;
    }
  }

  async sleepShiggy() {
    try {
      await axios.post(`${API_URL}/commands/sleep`);
      console.log('Sleep command sent');
    } catch (error) {
      console.error('Error sending sleep command:', error);
      throw error;
    }
  }

  async playShiggy() {
    try {
      await axios.post(`${API_URL}/commands/play`);
      console.log('Play command sent');
    } catch (error) {
      console.error('Error sending play command:', error);
      throw error;
    }
  }

  async getStatus() {
    try {
      const response = await axios.get(`${API_URL}/query/status`);
      return response.data;
    } catch (error) {
      console.error('Error fetching status:', error);
      throw error;
    }
  }
}

export default new ShiggyService();
