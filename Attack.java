abstract class Attack extends GameObject {
    protected final boolean isPlayer;
    protected int damage;
    protected static int cooldown;
    
    public Attack(int damage, boolean isPlayer)
    {
        this.damage = damage;
        this.isPlayer = isPlayer;
    }
}