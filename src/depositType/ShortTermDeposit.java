package depositType;

/**
 * Created by dotinschool1 on 4/10/2017.
 */
public class ShortTermDeposit extends DepositType {
    int interestRate=10;

    public void setInterestRate(int interestRate) {
        this.interestRate = 10;
    }

    @Override
    public int getInterestRate() {
        return 10;
    }
}

