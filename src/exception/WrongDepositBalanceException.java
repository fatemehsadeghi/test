package exception;

/**
 * Created by dotinschool1 on 4/16/2017.
 */
public class WrongDepositBalanceException extends  Exception {
    public WrongDepositBalanceException(String message) {
        super(message);
    }
}
