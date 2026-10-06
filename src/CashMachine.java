import java.math.BigDecimal;
import java.math.RoundingMode;

public class CashMachine implements WithdrawalOperations, DepositOperations {

    @Override
    public double depositMoney(double balance, double summa) {
        if(Double.isNaN(summa) || summa <= 0){
            return balance;
        }
        BigDecimal bd = new BigDecimal(String.valueOf(balance + summa))
                .setScale(2, RoundingMode.HALF_UP);
        double rounded_balance = bd.doubleValue();
        return rounded_balance;
    }

    @Override
    public double withdraw(double balance, double summa, BankType bank_type){
        double commission = applyCommission(summa, bank_type);
        double total = commission + summa;
        if(total > balance){
            System.out.println("Недостаточно средств");
            return balance;
        }
        if(Double.isNaN(summa) || summa <= 0){
            return balance;
        }
        BigDecimal bd = new BigDecimal(String.valueOf(balance - total));
        BigDecimal rounded = bd.setScale(2, RoundingMode.HALF_UP);
        double result = rounded.doubleValue();
        return result;
    }
}
