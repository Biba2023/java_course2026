public class Mage extends Hero implements Castable, Restable{
    private final int maxMana;
    private int mana;
    public Mage(String name, int maxHealth, int baseAttack, int maxMana) {
        super(name, maxHealth, baseAttack);
        if(maxMana < 0){
            this.maxMana = MIN_STAT_VALUE;
        }
        else this.maxMana = maxMana;
        mana = maxMana;
    }

    public int getMana(){
        return mana;
    }
    public int getMaxMana(){
        return maxMana;
    }



    @Override
    public void takeDamage(int damage){
        super.takeDamage(damage);
    }



    @Override
    public void castSpecialSkill(Hero target) {
        if (target == null || !target.isAlive() || !isAlive()) {
            return;
        }

        if (!canCast()) {
            System.out.println(getName() + " пытается создать огненную глыбу, " + "но маны недостаточно!");
            return;
        }

        int damage = getBaseAttack() * 3;
        mana -= 25;
        target.takeDamage(damage);

        System.out.println(
                getName() + " обрушивает огненную глыбу на " + target.getName() + " и наносит " + damage + " урона.");


    }

    @Override
    public boolean canCast() {
        if(mana >= 25){
            return true;
        }
        return false;
    }

    @Override
    void attack(Hero target) {
        if (target == null || !target.isAlive() || !isAlive()) {
            return;
        }

        if (mana >= 10) {
            int damage = getBaseAttack() * 2;
            mana -= 10;
            target.takeDamage(damage);
            System.out.println(getName() + " использует магический выстрел против " + target.getName() + " и наносит " + damage + " урона.");
        } else {
            int damage = getBaseAttack() / 2;

            target.takeDamage(damage);
            mana = (int) Math.min((long) mana + 5, maxMana);

            System.out.println(getName() + " бьёт посохом " + target.getName() + " и наносит " + damage + " урона. Маг восстанавливает 5 маны.");
        }
    }

    @Override
    public ActionType makeTurn(Hero target) {
        if (canCast()) {
            castSpecialSkill(target);
            return ActionType.SPECIAL_SKILL;
        }

        if (needsRest()) {
            rest();
            return ActionType.REST;
        }

        attack(target);
        return ActionType.BASE_ATTACK;
    }

    @Override
    public void rest() {
        if (!isAlive()) {
            return;
        }
        mana = maxMana;
        heal(getBaseAttack());
        System.out.println(getName() + "восстанавливает ману до " + maxMana + " и здоровье на " + getBaseAttack() + ".");
    }

    @Override
    public boolean needsRest() {
        if(mana == 0 || getHealth() < getMaxHealth() * 0.30){
            return true;
        }
        return  false;
    }


}
