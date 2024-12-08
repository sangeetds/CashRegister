import TestUtility.Companion.change
import exception.TransactionException
import exception.TransactionException.Companion.AMOUNT_GIVEN_LESS_THAN_PRICE
import exception.TransactionException.Companion.INSUFFICIENT_CHANGE_PRESENT
import money.Bill
import money.Change
import money.Coin
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import strategy.ChangeStrategy
import kotlin.test.assertEquals

class CashRegisterTest {

    private lateinit var cashRegister: CashRegister

    // Simple mock class
    private val changeStrategy = object : ChangeStrategy {
        override fun calculateChange(amountDue: Long, remainingChange: Change): Change {
            return Change()
                .add(Bill.TEN_EURO, 1)
                .add(Coin.ONE_EURO, 2)
                .add(Bill.FIVE_EURO, 2)
        }
    }

    @BeforeEach
    fun setup() {
        cashRegister = CashRegister(change, changeStrategy)
    }

    @Test
    fun `test minimal change`() {
        val changeAmount = 22L
        val paidAmount = Change()
            .add(Bill.ONE_HUNDRED_EURO, 1)
        val result = cashRegister.performTransaction(78L, amountPaid = paidAmount)

        assertEquals(changeAmount * 100, result.total)
    }

    @Test
    fun `test no possible change when amount paid is less`() {
        val exception =
            assertThrows<TransactionException> { cashRegister.performTransaction(100L, amountPaid = Change.none()) }
        assertEquals(AMOUNT_GIVEN_LESS_THAN_PRICE.message, exception.message)
    }

    @Test
    fun `test no possible change when performing an initial transaction`() {
        cashRegister.performTransaction(28L, amountPaid = Change().add(Bill.ONE_HUNDRED_EURO, 1))

        val exception =
            assertThrows<TransactionException> { cashRegister.performTransaction(8L , amountPaid = Change().add(Bill.ONE_HUNDRED_EURO, 1)) }
        assertEquals(INSUFFICIENT_CHANGE_PRESENT.message, exception.message)
    }


    @Test
    fun `test minimal change when zero amount`() {
        val amountPaid = Change().add(Bill.ONE_HUNDRED_EURO, 1)
        val changeProvided = cashRegister.performTransaction(100L, amountPaid = amountPaid)

        assertEquals(Change.none(), changeProvided)
    }
}
