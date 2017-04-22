package deposits;

import depositType.DepositType;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import  parser.*;

/**
 * Created by dotinschool1 on 4/10/2017.
 */
public class Deposit {

    BigDecimal payedInterest = new BigDecimal(1);
    private String customerNumber;
    DepositType depositType = new DepositType();
    private BigDecimal depositBalance;
    private int durationInDays;
    private int tempInterestRate = depositType.getInterestRate();

    public int getTempInterestRate() {
        return tempInterestRate;
    }

    public void setTempInterestRate(int tempInterestRate) {
        this.tempInterestRate = tempInterestRate;
    }

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

    public Deposit() {
    }

    public Deposit(String customerNumber, int tempInterestRate, BigDecimal depositBalance, int durationInDays) {
        this.tempInterestRate = tempInterestRate;
        this.customerNumber = customerNumber;
        this.depositBalance = depositBalance;
        this.durationInDays = durationInDays;
    }
    public BigDecimal calculatePayedInterest(Deposit deposit) {
        ParsingXml p = new ParsingXml();
        final int days = 6500;
        BigDecimal payedInterest = new BigDecimal(1);
        payedInterest = payedInterest.multiply(new BigDecimal(deposit.getDurationInDays())).multiply(deposit.getDepositBalance()).multiply(new BigDecimal(deposit.getTempInterestRate()));

        payedInterest = payedInterest.divide(new BigDecimal(days), MathContext.DECIMAL64);

        return payedInterest;

    }


}
