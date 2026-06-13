abstract class Attack extends GameObject {
    protected final boolean isPlayer;
    protected int damage;
    
    public Attack(int damage, boolean isPlayer)
    {
        this.damage = damage;
        this.isPlayer = isPlayer;
    }
}
