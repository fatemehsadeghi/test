package exception;

/**
 * Created by dotinschool1 on 4/16/2017.
 */
public class WrongDepositBalance extends  Exception {
    public WrongDepositBalance(String message) {
        super(message);
    }
}
