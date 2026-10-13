public class Warrior extends Hero implements Castable, Restable{
    private final int armor;
    public Warrior(String name, int maxHealth, int baseAttack, int armor) {
        super(name, maxHealth, baseAttack);
        if(armor < 0){
            this.armor = MIN_STAT_VALUE;
        }
        else this.armor = armor;
    }

    @Override
    public void takeDamage(int damage){
        int finalDamage = damage - armor;
        if(finalDamage < 1){
            finalDamage = 1;
        }
        super.takeDamage(finalDamage);
    }



    @Override
    public void castSpecialSkill(Hero target) {
        if (target == null || !target.isAlive() || !isAlive()) {
            return;
        }

        if (!canCast()) {
            System.out.println(getName().toString() + " попытался ударить щитом, но потерял равновесие!");
            return;
        }

        int damage = getBaseAttack() + armor * 2;

        target.takeDamage(damage);
        System.out.println(getName() + " наносит удар щитом по " + target.getName() + " и наносит " + damage + " урона");


    }

    @Override
    public boolean canCast() {
        if(getMaxHealth() * 0.5 > getHealth()){
            return true;
        }
        return false;
    }

    @Override
    void attack(Hero target) {
        if (target == null || !target.isAlive() || !isAlive()) {
            return;
        }
        target.takeDamage(getBaseAttack());
        System.out.println(getName() + " аттакует" + target.getName() + " и наносит " + getBaseAttack() + " урона.");
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
        heal(armor * 2);
        System.out.println(getName() + "восстановливает здоровье");
    }

    @Override
    public boolean needsRest() {
        if(getMaxHealth() * 0.15 > getHealth()){
            return true;
        }
        return false;
    }


}
