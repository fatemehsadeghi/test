package depositType;

public class ShortTermDeposit implements DepositType {

    @Override
    public int getInterestRate() {
        return 10;
    }
}

