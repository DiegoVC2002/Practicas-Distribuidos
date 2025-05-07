#Materia: Sistemas Distribuidos
#Autor: Diego Valdes Castillo
#Fecha de  creacion: 05/04/2025
#Version: 1.0
#Practica 7 - Microservicios
# state_service.py -Flask
from flask import Flask, request, jsonify

app = Flask(__name__)

race_status = {
    "cars": {},
    "max_distance": 100,
    "podium": []
}

@app.route('/data', methods=['POST'])
def update_race_data():
    data = request.get_json()
    action = data.get('action')
    car_id = data.get('car_id')

    if action == "register":
        if car_id in race_status["cars"]:
            return jsonify({"error": "El auto ya está registrado"}), 400
        race_status["cars"][car_id] = {"position": 0}
        return jsonify({"message": f"Auto {car_id} registrado exitosamente"})

    elif action == "move":
        if car_id not in race_status["cars"]:
            return jsonify({"error": "El auto no está registrado"}), 400
        if car_id in race_status["podium"]:
            return jsonify({"error": "Este auto ya terminó la carrera"}), 400
        if len(race_status["cars"]) < 4:
            return jsonify({"error": "La carrera inicia cuando haya al menos 4 autos"}), 400

        distance = data.get("distance", 5)
        race_status["cars"][car_id]["position"] += distance

        if race_status["cars"][car_id]["position"] >= race_status["max_distance"]:
            race_status["cars"][car_id]["position"] = race_status["max_distance"]
            if car_id not in race_status["podium"]:
                race_status["podium"].append(car_id)

        return jsonify({"message": f"{car_id} avanzó {distance} metros", "position": race_status["cars"][car_id]["position"]})

    return jsonify({"error": "Acción inválida"}), 400

@app.route('/race_status', methods=['GET'])
def get_race_status():
    return jsonify(race_status)

if __name__ == '__main__':
    app.run(port=5003, debug=True)
