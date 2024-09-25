package com.example.bonkgarage

import com.google.firebase.firestore.FirebaseFirestore

class CarDataManager(private val db: FirebaseFirestore) {

    fun addCarDataToFirestore(carDataList: List<HashMap<String, Any>>) {
        val db = FirebaseFirestore.getInstance()
        val colRef = db.collection("car_models") 

        colRef.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result
                    if (documents.isEmpty) {  // Collection doesn't exist, add data
                        for (carData in carDataList) {
                            colRef.add(carData)
                        }
                    } else {
                        print("Car Data Already Exists")
                    }
                }
            }
    }
}
