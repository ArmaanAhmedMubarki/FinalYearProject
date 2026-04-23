from flask import Flask, request, jsonify
import joblib

app = Flask(__name__)

model = joblib.load("performance_model.pkl")

@app.route("/predict", methods=["POST"])
def predict():
    data = request.json["features"]
    prediction = model.predict([data])
    return jsonify({"performance": prediction[0]})

app.run(port=5000)