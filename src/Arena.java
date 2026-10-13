public class Arena {
    private final Hero fighter1;
    private final Hero fighter2;
    private int roundCounter;

    public Arena(Hero fighter1, Hero fighter2) {
        if (fighter1 == null || fighter2 == null) {
            throw new IllegalArgumentException("Участники не могут быть null");
        }

        if (fighter1 == fighter2) {
            throw new IllegalArgumentException("В бою должно быть два разных героя");
        }

        this.fighter1 = fighter1;
        this.fighter2 = fighter2;
        this.roundCounter = 0;
    }

    public void startTournament() {
        System.out.println("Начало боя");
        System.out.println(fighter1.getName() + " против " + fighter2.getName());

        while (fighter1.isAlive() && fighter2.isAlive()) {
            roundCounter++;

            System.out.println("Раунд " + roundCounter);

            Hero attacker;
            Hero defender;

            if (Math.random() < 0.5) {
                attacker = fighter1;
                defender = fighter2;
            } else {
                attacker = fighter2;
                defender = fighter1;
            }

            performTurn(attacker, defender);

            if (!defender.isAlive()) {
                break;
            }

            performTurn(defender, attacker);

            printStatus();
        }

        System.out.println("Бой окончен");

        if (fighter1.isAlive()) {
            System.out.println("Победитель: " + fighter1.getName());
        } else if (fighter2.isAlive()) {
            System.out.println("Победитель: " + fighter2.getName());
        } else {
            System.out.println("Оба героя пали в бою!");
        }
    }

    private void performTurn(Hero attacker, Hero defender) {
        if (!attacker.isAlive() || !defender.isAlive()) {
            return;
        }

        ActionType action = attacker.makeTurn(defender);

        System.out.println("Игрок " + attacker.getName() + " совершил действие: " + action.getDescription());
    }

    private void printStatus() {
        System.out.println("Состояние бойцов");
        printHeroStatus(fighter1);
        printHeroStatus(fighter2);
    }

    private void printHeroStatus(Hero hero) {
        System.out.println(hero.getName() + ": здоровье " + hero.getHealth() + "/" + hero.getMaxHealth() + getResourceStatus(hero));
    }

    private String getResourceStatus(Hero hero) {
        if (hero instanceof Mage mage) {
            return ", мана " + mage.getMana() + "/" + mage.getMaxMana();
        }

        if (hero instanceof Warrior) {
            return ", воин";
        }

        return "";
    }

}
