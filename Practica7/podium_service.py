#Materia: Sistemas Distribuidos
#Autor: Diego Valdes Castillo
#Fecha de  creacion: 05/04/2025
#Version: 1.0
#Practica 7 - Microservicios
# podium_service.py - Flask
from flask import Flask, jsonify
import requests

app = Flask(__name__)
STATE_SERVICE_URL = "http://localhost:5003/race_status"

@app.route('/podium', methods=['GET'])
def get_podium():
    try:
        r = requests.get(STATE_SERVICE_URL)
        data = r.json()
        podium = data.get("podium", [])
        if len(podium) >= 4:
            return jsonify({"podium": podium})
        return jsonify({"message": "La carrera aún no termina"})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(port=5004, debug=True)
