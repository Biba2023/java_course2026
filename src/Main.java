import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Создание первого героя");
        Hero fighter1 = createHero(scanner);
        System.out.println("Создание второго героя");
        Hero fighter2 = createHero(scanner);
        Arena arena = new Arena(fighter1, fighter2);
        arena.startTournament();


}

private static Hero createHero(Scanner scanner) {
    System.out.println("Выберите тип (1 — Воин, 2 — Маг): ");
    int type = readInt(scanner);
    while (type != 1 && type != 2) {
        System.out.println("Ошибка: выберите тип (1 — Воин, 2 — Маг): ");
        type = scanner.nextInt();
        scanner.nextLine();
    }

    System.out.println("Введите имя героя: ");
    String name = scanner.nextLine();

    while (name.equals("")) {
        System.out.println("Имя не может быть пустым.");
        System.out.print("Введите имя героя: ");
        name = scanner.nextLine().trim();
    }

    System.out.println("Максимальное здоровье: ");
    int maxHealth, baseAttack;
    maxHealth = readInt(scanner);

    while (maxHealth < Hero.MIN_STAT_VALUE) {
        System.out.println("Здоровье должно быть не меньше 1");
        System.out.println("Максимальное здоровье: ");
        maxHealth = readInt(scanner);
    }

    System.out.println("Базовая атака: ");
    baseAttack = readInt(scanner);
    while (baseAttack < Hero.MIN_STAT_VALUE) {
        System.out.println("Атака должна быть не меньше 1");
        System.out.println("Базовая атака: ");
        baseAttack = readInt(scanner);
    }

    if (type == 1) {
        System.out.println("Броня: ");
        int armor = readInt(scanner);

        while (armor < Hero.MIN_STAT_VALUE) {
            System.out.println("Броня должна не меньше 1");
            System.out.println("Броня: ");
            armor = readInt(scanner);

        }

        return new Warrior(name, maxHealth, baseAttack, armor);
    }

    System.out.println("Максимальная мана: ");
    int maxMana= readInt(scanner);

    while (maxMana < Hero.MIN_STAT_VALUE) {
        System.out.println("Максимальная мана должна быть не меньше 1");
        System.out.println("Максимальная мана: ");
        maxMana= readInt(scanner);
    }

    return new Mage(name, maxHealth, baseAttack, maxMana);
}
    private static int readInt(Scanner scanner) {
        int number;
        while (true) {
            if (scanner.hasNextInt()) {
                number = scanner.nextInt();
                scanner.nextLine();
                break;
            } else {
                System.out.println("Значение должно быть целым числом");
                scanner.nextLine();
            }
        }
        return  number;
    }



}
