import java.math.BigDecimal;
import java.math.RoundingMode;

public interface WithdrawalOperations {

    default double applyCommission(double summa, BankType bank_type){
        if(Double.isNaN(summa) || bank_type == null){
            return 0;
        }
        double commission = summa * bank_type.getCommission();
        BigDecimal bd = new BigDecimal(String.valueOf(commission)).setScale(2, RoundingMode.HALF_UP);
        double rounded_commision = bd.doubleValue();
        return rounded_commision;
    }




    double withdraw(double balance, double withdraw_amount, BankType bank_type);
}

