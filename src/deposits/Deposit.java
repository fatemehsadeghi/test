package deposits;

import depositType.DepositType;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Comparator;

public class Deposit implements Comparator{

    private String customerNumber;
    private DepositType depositType ;
    private BigDecimal depositBalance;
    private int durationInDays;
    private BigDecimal payedInterest ;

    public Deposit() {
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    private BigDecimal getDepositBalance() {
        return depositBalance;
    }

    public void setDepositBalance(BigDecimal depositBalance) {
        this.depositBalance = depositBalance;
    }

    private int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }

    public void setDepositType(DepositType depositType) {
        this.depositType = depositType;
    }

    public BigDecimal getPayedInterest() {
        return payedInterest;
    }

    public void setPayedInterest(BigDecimal payedInterest) {
        this.payedInterest = payedInterest;
    }

    public Deposit(String customerNumber , BigDecimal payedInterest ,int durationInDays ,BigDecimal depositBalance , DepositType depositType) {
        this.customerNumber = customerNumber;
        this.durationInDays = durationInDays;
        this.depositBalance = depositBalance;
        this.depositType = depositType;
        this.payedInterest = payedInterest;
    }

    public Deposit calculatePayedInterest() {
        Deposit deposit = new Deposit();
        final int DAYS = 36500;
            payedInterest = depositBalance.multiply(new BigDecimal(durationInDays))
                    .multiply(new BigDecimal(depositType.getInterestRate()));
            payedInterest = payedInterest.divide(new BigDecimal(DAYS), MathContext.DECIMAL64);
            //deposit.setPayedInterest(payedInterest);
            System.out.println(payedInterest +"ali" + durationInDays + "abbas "+depositBalance );
        return deposit;
    }

    @Override
    public int compare(Deposit deposit1, Deposit deposit2) {
         int result = deposit1.getPayedInterest().compareTo(deposit2.getPayedInterest());
        if(result>0)
        return 0;

    }
}
