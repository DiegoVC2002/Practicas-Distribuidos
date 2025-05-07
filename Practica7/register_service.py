#Materia: Sistemas Distribuidos
#Autor: Diego Valdes Castillo
#Fecha de  creacion: 05/04/2025
#Version: 1.0
#Practica 7 - Microservicios
# register_service.py - Flask
from flask import Flask, request, jsonify
import requests

app = Flask(__name__)
STATE_SERVICE_URL = "http://localhost:5003/data"

@app.route('/register', methods=['POST'])
def register_car():
    data = request.get_json()
    car_id = data.get('car_id')
    if not car_id:
        return jsonify({"error": "Debes proporcionar un ID de auto"}), 400

    payload = {
        "action": "register",
        "car_id": car_id
    }

    try:
        r = requests.post(STATE_SERVICE_URL, json=payload)
        return r.json(), r.status_code
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(port=5001, debug=True)
