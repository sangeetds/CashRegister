import money.Change
import exception.TransactionException
import exception.TransactionException.Companion.AMOUNT_GIVEN_LESS_THAN_PRICE
import exception.TransactionException.Companion.INSUFFICIENT_CHANGE_PRESENT
import money.Coin

/**
 * The CashRegister class holds the logic for performing transactions.
 *
 * @param change The change that the CashRegister is holding.
 */
class CashRegister(private var change: Change,
                    private val changeStrategy: ChangeStrategy) {

    /**
     * Performs a transaction for a product/products with a certain price and a given amount.
     *
     * @param price The price of the product(s).
     * @param amountPaid The amount paid by the shopper.
     *
     * @return The change for the transaction.
     *
     * @throws TransactionException If the transaction cannot be performed.
     */
    fun performTransaction(price: Long, amountPaid: Change): Change {
        val amountDue = amountPaid.total - (price * 100)
        if (amountDue > 0 && change.total >= amountDue) {
            val changeToReturn = changeStrategy.calculateChange(amountDue, change)
            change -= changeToReturn
            return changeToReturn
        } else if (amountDue == 0L) {
            return Change.none()
        } else {
            throw
                if (amountDue < 0) AMOUNT_GIVEN_LESS_THAN_PRICE
                else INSUFFICIENT_CHANGE_PRESENT
        }
    }
}
