public class Account {
    public int card_number;
    public int pin_code;
    public double balance;
    public BankType bank_type;

    public Account(int card_number, int pin_code, double balance, BankType bank_type){
        if(card_number < 10000 || card_number > 99999){
            this.card_number = 0;
        }
        else{
            this.card_number = card_number;
        }
        if(pin_code < 100 || pin_code > 999){
            this.pin_code = 0;
        }
        else{
            this.pin_code = pin_code;
        }
        if(balance < 0){
            this.balance = 0;
        }
        else {
            this.balance = balance;
        }
        if(bank_type == null || Double.isNaN(balance)){
            this.bank_type = BankType.NEO;
        }
        else {
            this.bank_type = bank_type;
        }
    }

    public int getCardNumber(){
        return card_number;
    }

    public int getPinCode(){
        return pin_code;
    }

    public double getBalance(){
        return balance;
    }

    public BankType getBankType(){
        return bank_type;
    }

    public String toString(){
        String formattedBalance = String.format("%.2f", balance);
        return bank_type.getBankName() + " Карта: " + card_number + " Баланс: " + formattedBalance + " руб.";
    }
}

