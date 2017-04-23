package deposits;

import depositType.DepositType;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by dotinschool1 on 4/10/2017.
 */
public class Deposit {

    private String customerNumber;
    DepositType depositType ;
    private BigDecimal depositBalance;
    private int durationInDays;

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public BigDecimal getDepositBalance() {
        return depositBalance;
    }

    public void setDepositBalance(BigDecimal depositBalance) {
        this.depositBalance = depositBalance;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }

    public DepositType getDepositType() {
        return depositType;
    }

    public void setDepositType(DepositType depositType) {
        this.depositType = depositType;
    }

    public Deposit() {
    }

    public Deposit(String customerNumber, BigDecimal depositBalance, int durationInDays, DepositType depositType) {
        this.customerNumber = customerNumber;
        this.depositBalance = depositBalance;
        this.durationInDays = durationInDays;
        this.depositType = depositType;
    }
    public BigDecimal calculatePayedInterest(Deposit deposit) {
        final int days = 6500;
        BigDecimal payedInterest = new BigDecimal(1);
        payedInterest = payedInterest.multiply(new BigDecimal(deposit.getDurationInDays()))
                .multiply(deposit.getDepositBalance()).multiply(new BigDecimal(deposit.depositType.getInterestRate()));
        payedInterest = payedInterest.divide(new BigDecimal(days), MathContext.DECIMAL64);
        return payedInterest;
    }
}
