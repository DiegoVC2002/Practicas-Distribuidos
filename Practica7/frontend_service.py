#Materia: Sistemas Distribuidos
#Autor: Diego Valdes Castillo
#Fecha de  creacion: 05/04/2025
#Version: 1.0
#Practica 7 - Microservicios
# frontend_service.py - Flask
from flask import Flask, render_template, jsonify
import requests

app = Flask(__name__)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/race_status')
def race_status():
    r = requests.get("http://localhost:5003/race_status")
    return jsonify(r.json())

if __name__ == '__main__':
    app.run(port=5000, debug=True)
