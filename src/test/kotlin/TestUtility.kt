import money.Bill
import money.Change
import money.Coin

class TestUtility {

    companion object {

        val change = Change()
            .add(Bill.TEN_EURO, 3)
            .add(Coin.ONE_EURO, 2)
            .add(Bill.FIVE_EURO, 8)
    }
}