package com.example.bonkgarage.Model

data class Transaction(
    var carImg : Int,
    var transactionQty : Int,
    var transactionPrice: Double,
    var carName : String,
    var uid: String,
    var email: String,
    var documentId: String
)

object Transactions {
    var listTransactions : ArrayList<Transaction> = ArrayList()

    fun addTransactions(carImg: Int, transactionQty: Int, transactionPrice: Double, carName: String, uid: String, email: String, documentId: String)
    {
        val transactionId = listTransactions.size + 1
        listTransactions.add(Transaction(carImg, transactionQty,transactionPrice,carName, uid, email, documentId))
    }
}
