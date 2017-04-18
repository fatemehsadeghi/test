package interest;

import depositType.DepositType;
import deposits.Deposit;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeMap;

/**
 * Created by dotinschool1 on 4/16/2017.
 */
public class DepositInterest {


    Deposit deposit = new Deposit();

    public TreeMap calculatePayedInterest(Deposit deposit){
        TreeMap<BigDecimal , Deposit> depositMap = new TreeMap();
        //Collections.reverseOrder()
        System.out.println("----------"+deposit.getTempInterestRate());
        System.out.println("---------"+deposit.getCustomerNumber());
        System.out.println("----------"+deposit.getDepositBalance());
        System.out.println("---------"+deposit.getDurationInDays());
        final int days = 6500;
        BigDecimal payedInterest=new BigDecimal(1);
        payedInterest =payedInterest.multiply(new BigDecimal(deposit.getDurationInDays())).multiply(deposit.getDepositBalance()).multiply(new BigDecimal(deposit.getTempInterestRate()));
        System.out.println(payedInterest);
        payedInterest=payedInterest.divide(new BigDecimal(days) , MathContext.DECIMAL64);
        System.out.println(" PAYE IS " + payedInterest);
        depositMap.put(payedInterest ,deposit);
        TreeMap<BigDecimal, Deposit> sorted = new TreeMap<BigDecimal, Deposit>(depositMap);
        System.out.println("Sorted Map: " + Arrays.toString(sorted.entrySet().toArray()));
        System.out.println("my key" + "/t "+depositMap.ceilingEntry(payedInterest));
        //System.out.println("Sorted" + "/t "+sorted.ceilingEntry(payedInterest));
        return depositMap ;
    }



}
