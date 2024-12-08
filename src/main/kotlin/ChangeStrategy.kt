import money.Change

interface ChangeStrategy {
    fun calculateChange(amountDue: Long, remainingChange: Change): Change
}
