public enum BankType {
    NEO("НеоКредит Банк", 0.01), AUM("Арум Финтех", 0.02), VTA("Вектор Альянс Банк", 0.00);
    private String name;
    private double commission;

    BankType(String name, double commission){
        this.name = name;
        this.commission = commission;
    }

    public String getBankName(){
        return name;
    }

    public double getCommission(){
        return commission;
    }
}

