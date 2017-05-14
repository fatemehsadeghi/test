package depositType;

public class LongTermDeposit implements DepositType {
    @Override
    public int getInterestRate() {
        return 20;
    }
}
