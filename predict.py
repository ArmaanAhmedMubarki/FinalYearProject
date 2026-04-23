import joblib

# Load saved model
model = joblib.load("performance_model.pkl")

# Example new player
new_player = [[20, 12, 6, 78, 7, 7, 8, 7]]

prediction = model.predict(new_player)

print("Predicted Performance:", prediction[0])