//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        int card_number;
        int pin_code;
        BankType bank_type;

        System.out.println("Добрый день!");
        System.out.println("Введите номер карты");
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) {
            System.out.println("Ошибка: номер карты должен быть числом.");
            return;
        }
        else {
            card_number = scanner.nextInt();
            if (card_number < 10000 || card_number > 99999) {
                System.out.println("Ошибка: номер карты должен состоять из 5 цифр.");
                return;
            }

        }
        System.out.println("Введите пин-код");
        if (!scanner.hasNextInt()) {
            System.out.println("Ошибка: пин-код должен быть числом.");
            return;
        }
        else {
            pin_code = scanner.nextInt();
            if (pin_code < 100 || pin_code > 999) {
                System.out.println("Ошибка: PIN-код должен состоять из 3 цифр.");
                return;
            }

        }


        Account referenceAccount = new Account(12345, 999, 10000.00, BankType.AUM);
        if (card_number != referenceAccount.getCardNumber() || pin_code != referenceAccount.getPinCode()) {
            System.out.println("Ошибка доступа: неверный номер карты или пин-код.");
            return;
        } else {
            System.out.println("Успешный вход.");
        }
        double balance = referenceAccount.getBalance();
        CashMachine cashMachine = new CashMachine();
        System.out.println("Тест 1. Введите сумму для внесения: ");
        double summa_deposit;
        if (!scanner.hasNextDouble()) {
            System.out.println("Ошибка: сумма должна быть числом.");
            return;
        }
        else {
            summa_deposit = scanner.nextDouble();
            if (summa_deposit < 0) {
                System.out.println("Ошибка: сумма не может быть отрицательной.");
                return;
            }
            System.out.println("Обновленный баланс");
            balance = cashMachine.depositMoney(balance, summa_deposit);
            System.out.println(balance);
        }




        System.out.println("Тест 2. Введите сумму для снятия: ");
        double summa_withdraw;
        if (!scanner.hasNextDouble()) {
            System.out.println("Ошибка: сумма должна быть числом.");
            return;
        }
        else {
            summa_withdraw = scanner.nextDouble();
            if (summa_withdraw < 0) {
                System.out.println("Ошибка: сумма не может быть отрицательной.");
                return;
            }
            System.out.println("Обновленный баланс");
            balance = cashMachine.withdraw(balance, summa_withdraw, referenceAccount.getBankType());
            System.out.println(balance);
        }
    }
}

