const PROJECTION_SERVICE_URL = 'http://localhost:8083/queries/orders';
const ORDER_SERVICE_URL = 'http://localhost:8080/commands/orders';

const orderForm = document.getElementById('orderForm');
const ordersList = document.getElementById('ordersList');
const refreshBtn = document.getElementById('refreshBtn');
const formMessage = document.getElementById('formMessage');

async function fetchOrders() {
  ordersList.innerHTML = '<div class="loading">Bestellungen werden geladen...</div>';

  try {
    const response = await fetch(PROJECTION_SERVICE_URL);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const orders = await response.json();
    displayOrders(orders);
  } catch (error) {
    console.error('Fehler beim Laden der Bestellungen:', error);
    ordersList.innerHTML =
      '<div class="error">Bestellungen konnten nicht geladen werden. Prüfe bitte, ob der Projection Service (Port 8083) läuft.</div>';
  }
}

function displayOrders(orders) {
  if (!orders || orders.length === 0) {
    ordersList.innerHTML = '<div class="empty-state">Es sind noch keine Bestellungen vorhanden.</div>';
    return;
  }

  const sortedOrders = [...orders].sort((a, b) => {
    return new Date(b.createdAt) - new Date(a.createdAt);
  });

  ordersList.innerHTML = sortedOrders
    .map(
      (order) => `
        <div class="order-card" data-id="${order.id}">
          <div class="order-header">
            <span class="order-id">${order.id}</span>
          </div>

          <div class="order-product">${escapeHtml(order.product)}</div>

          <div class="order-details">
            <div class="order-detail">
              <span class="order-detail-label">Kunde</span>
              <span class="order-detail-value">${escapeHtml(order.customerName)}</span>
            </div>

            <div class="order-detail">
              <span class="order-detail-label">Menge</span>
              <span class="order-detail-value">${order.quantity}</span>
            </div>

            <div class="order-detail">
              <span class="order-detail-label">Preis</span>
              <span class="order-detail-value">€${parseFloat(order.price).toFixed(2)}</span>
            </div>

            <div class="order-detail">
              <span class="order-detail-label">Gesamt</span>
              <span class="order-detail-value">€${(parseFloat(order.quantity) * parseFloat(order.price)).toFixed(2)}</span>
            </div>

            <div class="order-detail">
              <span class="order-detail-label">Erstellt am</span>
              <span class="order-detail-value">${formatDate(order.createdAt)}</span>
            </div>
          </div>
        </div>
      `
    )
    .join('');
}

function escapeHtml(text) {
  const div = document.createElement('div');
  div.textContent = text;
  return div.innerHTML;
}

function formatDate(dateString) {
  const date = new Date(dateString);
  return date.toLocaleString('de-DE', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
}

function showMessage(message, type) {
  formMessage.textContent = message;
  formMessage.className = `message ${type}`;

  if (type === 'success') {
    setTimeout(() => {
      formMessage.className = 'message';
    }, 5000);
  }
}

async function createOrder(event) {
  event.preventDefault();

  const customerName = document.getElementById('customerName').value;
  const product = document.getElementById('product').value;
  const quantity = parseFloat(document.getElementById('quantity').value);
  const price = parseFloat(document.getElementById('price').value);

  const orderData = {
    customerName,
    product,
    quantity,
    price,
  };

  try {
    const response = await fetch(ORDER_SERVICE_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(orderData),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const result = await response.json();

    showMessage(`Bestellung wurde angelegt! ID: ${result.id}`, 'success');

    orderForm.reset();

    setTimeout(() => {
      fetchOrders();

      const newOrderCard = document.querySelector(`[data-id="${result.id}"]`);
      if (newOrderCard) {
        newOrderCard.classList.add('new');
      }
    }, 500);
  } catch (error) {
    console.error('Fehler beim Anlegen der Bestellung:', error);
    showMessage('Bestellung konnte nicht angelegt werden. Prüfe bitte, ob der Order Service (Port 8080) läuft.', 'error');
  }
}

orderForm.addEventListener('submit', createOrder);
refreshBtn.addEventListener('click', fetchOrders);

document.addEventListener('DOMContentLoaded', fetchOrders);