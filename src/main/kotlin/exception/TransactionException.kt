package exception

class TransactionException(message: String, cause: Throwable? = null) : Exception(message, cause) {

    companion object {
        val INSUFFICIENT_CHANGE_PRESENT = TransactionException("Amount due is greater than change present or cannot be provided with change present")
        val AMOUNT_GIVEN_LESS_THAN_PRICE = TransactionException("Amount paid is less than the price")
    }
}
