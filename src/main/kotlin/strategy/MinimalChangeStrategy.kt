package strategy

import MonetaryElement
import exception.TransactionException.Companion.INSUFFICIENT_CHANGE_PRESENT
import money.Change

/**
 * This class implements the ChangeCalculationStrategy interface using a minimal
 * approach to calculate the optimal change.
 */
class MinimalChangeStrategy : ChangeStrategy {

    /**
     * Calculates the optimal change for the given amount using recursive minimal coin calculation.
     *
     * @param amountDue The amount of change to be returned.
     * @param remainingChange The available change in the register.
     * @return The optimal Change object, or throws TransactionException if the change cannot be provided.
     */
    override fun calculateChange(amountDue: Long, remainingChange: Change): Change {
        val currentChange = mutableListOf<MonetaryElement>()
        val allChanges = mutableListOf<MonetaryElement>()
        val elements = remainingChange.getElements()

        elements.forEach { element ->
            repeat(remainingChange.getCount(element)) {
                allChanges.add(element)
            }
        }

        val (totalElements, monetaryElements) = recursiveMinimumChange(amountDue, allChanges, currentChange)
        if (totalElements == Int.MAX_VALUE) {
            throw INSUFFICIENT_CHANGE_PRESENT
        }

        val changeToReturn = Change()
        changeToReturn.addAll(monetaryElements)

        return changeToReturn
    }

    private fun recursiveMinimumChange(
        remainingAmount: Long,
        allChanges: List<MonetaryElement>,
        currentChange: List<MonetaryElement>,
        index: Int = 0
    ): Pair<Int, List<MonetaryElement>> {
        when {
            remainingAmount == 0L                                 -> return Pair(0, currentChange)
            remainingAmount  < 0L || index > allChanges.lastIndex -> return Pair(Int.MAX_VALUE - 1, currentChange)
        }

        val takeMoneyForChange = recursiveMinimumChange(
            remainingAmount - allChanges[index].minorValue,
            allChanges,
            currentChange + allChanges[index],
            index + 1,
        )

        val doNotTakeMoneyForChange = recursiveMinimumChange(
            remainingAmount,
            allChanges,
            currentChange,
            index + 1,
        )

        return if (takeMoneyForChange.first + 1 < doNotTakeMoneyForChange.first) {
            Pair(takeMoneyForChange.first + 1, takeMoneyForChange.second)
        } else {
            doNotTakeMoneyForChange
        }
    }
}