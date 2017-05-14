package deposits;

import depositType.DepositType;

import java.math.BigDecimal;
import java.math.MathContext;

public class Deposit {

    private String customerNumber;
    private DepositType depositType;
    private BigDecimal depositBalance;
    private int durationInDays;
    private BigDecimal payedInterest;

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public void setDepositType(DepositType depositType) {
        this.depositType = depositType;
    }

    public void setDepositBalance(BigDecimal depositBalance) {
        this.depositBalance = depositBalance;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }

    public BigDecimal getPayedInterest() {
        return payedInterest;
    }

    public void calculatePayedInterest() {
        final int DAYS = 36500;
        payedInterest = depositBalance.multiply(new BigDecimal(durationInDays))
                .multiply(new BigDecimal(depositType.getInterestRate()));
        payedInterest = payedInterest.divide(new BigDecimal(DAYS), MathContext.DECIMAL64);
    }

}
