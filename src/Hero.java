public abstract class Hero {
    public static final int MIN_STAT_VALUE = 1;
    private final String name;
    private final int maxHealth, baseAttack;
    private int health;

    public Hero(String name, int maxHealth, int baseAttack){
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя героя не может быть пустым");
        }
        else this.name = name;
        if(maxHealth >= MIN_STAT_VALUE) {
            this.maxHealth = maxHealth;
        }
        else this.maxHealth = MIN_STAT_VALUE;
        if(baseAttack >= MIN_STAT_VALUE) {
            this.baseAttack = baseAttack;
        }
        else this.baseAttack = MIN_STAT_VALUE;
        this.health = this.maxHealth;
    }

    public String getName(){
        return name;
    }

    public int getMaxHealth(){
        return maxHealth;
    }
    public int getBaseAttack(){
        return baseAttack;
    }

    public int getHealth() {
        return health;
    }

    abstract void attack(Hero target);

    public abstract ActionType makeTurn(Hero target);

    public void takeDamage(int damage){
        if (damage <= 0 || !isAlive()) {
            return;
        }
        health -= damage;
        if(health <= 0){
            health = 0;
            System.out.println(name + " пал в бою!");
        }
    }
    void heal(int amount){
        if (amount <= 0 || !isAlive()) {
            return;
        }
        health += amount;
        if(health > maxHealth){
            health = maxHealth;
        }
    }
    boolean isAlive(){
        if(health > 0){
            return true;
        }
        return false;
    }
}