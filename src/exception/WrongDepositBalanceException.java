package exception;

public class WrongDepositBalanceException extends  Exception {
    public WrongDepositBalanceException(String message) {
        super(message);
    }
}
