import TestUtility.Companion.change
import exception.TransactionException
import exception.TransactionException.Companion.INSUFFICIENT_CHANGE_PRESENT
import money.Bill
import money.Change
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import strategy.MinimalChangeStrategy
import kotlin.test.assertEquals

class MinimalChangeStrategyTest {

    private lateinit var minimalChangeStrategy: MinimalChangeStrategy

    @BeforeEach
    fun setup() {
        minimalChangeStrategy = MinimalChangeStrategy()
    }

    @Test
    fun `test minimal change`() {
        val changeAmount = 22L * 100

        val result = minimalChangeStrategy.calculateChange(changeAmount, change)

        assertEquals(changeAmount, result.total)
    }

    @Test
    fun `test minimal change when exact denomination match`() {
        val denominationsExpected = Change()
            .add(Bill.TEN_EURO, 3)
            .add(Bill.FIVE_EURO, 2)

        val changeAmount = 40L * 100

        val result = minimalChangeStrategy.calculateChange(changeAmount, change)

        assertEquals(changeAmount, result.total)
        assertEquals(denominationsExpected.getCount(Bill.TEN_EURO), result.getCount(Bill.TEN_EURO))
        assertEquals(denominationsExpected.getCount(Bill.FIVE_EURO), result.getCount(Bill.FIVE_EURO))
    }

    @Test
    fun `test no change possible fail test`() {
        val assertThrows = assertThrows<TransactionException> { minimalChangeStrategy.calculateChange(1100, change) }

        assertEquals(assertThrows.message, INSUFFICIENT_CHANGE_PRESENT.message)
    }
}