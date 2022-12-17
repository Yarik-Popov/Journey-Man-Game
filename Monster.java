/**
 * April 26, 2021
 * This is the Monster class, it handles the values related to a single
 * monster. It deals and takes damage. Can't steal resources
 * */
 
public class Monster extends Npc
{
    //Class constants
    public static final int ONLY_SWORD = 0;
    public static final int BOTH_WEAPONS = 1;
    public static final int HIGHER_CHANCE_TO_USE_BOW = 2;
    public static final int CHANCE_TO_USE_BOW_WHEN_BOTH = 3;
    public static final int CHANCE_TO_USE_SWORD_WHEN_HIGHER_CHANCE_OF_BOW = 3;
    // 1/CHANCE_TO_USE_BOW_WHEN_BOTH to use a bow
    
    //Constants related to creating random health
    //The health needs to be multiplied by 5
    public static final int MIN_HEALTH5 = 32; //Min health = MIN_HEALTH5*5;
    public static final int MAX_HEALTH5 = 39; //Max health = MAX_HEALTH5*5;
    
    //Variables related to a Monster
    private int health;
    private boolean rangeShouldBeDecreased;
    private final Weapon sword;
    private final Bow bow;
    
    /**
     * Constructor where sword and bow objects need to be created before the
     * Monster
     * */
    public Monster(String name, int health, Weapon sword, Bow bow)
    {
        super(name);
        this.health = health;
        this.sword = sword;
        this.bow = bow;
        rangeShouldBeDecreased = false;
        setTypeOfNpc(Npc.NO_INTERACTION_WITH_RESOURCES);
    }
    
    //Creates a random monster 
    public Monster()
    {
        super(Npc.NO_INTERACTION_WITH_RESOURCES);
        health = Randomizer.nextInt(MIN_HEALTH5, MAX_HEALTH5)*5;
        sword = new Weapon(Weapon.RANDOM_WEAPON);
        bow = new Bow(Weapon.RANDOM_WEAPON);
        rangeShouldBeDecreased = false;
    }
    
    //Returns if the Monster can use a bow or not
    //This method will be used in the combat to make sure the player is
    //within the range of the Monster
    public boolean canUseBow()
    {
        return bow.hasArrows();
    }
    
    //Returns the health of the monster
    public int getHealth()
    {
        return health;
    }
    
    //Gets the damage depending on the range
    public int getDamage(int num)
    {
        if(isAlive())
        {
            rangeShouldBeDecreased = false;
            if(num == ONLY_SWORD)
            {
                return sword.getDamage();
            }
            else if(num == HIGHER_CHANCE_TO_USE_BOW)
            {
                int useOfWeapon = Randomizer.nextInt(CHANCE_TO_USE_SWORD_WHEN_HIGHER_CHANCE_OF_BOW);
                if(useOfWeapon == 0)
                {
                    rangeShouldBeDecreased = true;
                    return sword.getDamage();
                }
                else
                {
                    return bow.shoot();
                }
            }
            else
            {
                int useOfWeapon = Randomizer.nextInt(CHANCE_TO_USE_BOW_WHEN_BOTH);
                if(useOfWeapon == 0 && bow.hasArrows())
                {
                    return bow.shoot();
                }
                else
                {
                    rangeShouldBeDecreased = true;
                    return sword.getDamage();
                }
            } 
        }
        else
        {
            return 0;
        }
    }
    
    /**
     *  This method returns a boolean of whether or not the range should be
     * decreased in the player class. This method returns true if the monster
     * used the sword last and the range is not ONLY_SWORD;
     * */
    public boolean getRangeShouldBeDecreased()
    {
        return rangeShouldBeDecreased;
    }
    
    //Sets the rangeShouldBeDecreased boolean
    public void setRangeShouldBeDecreased(boolean value)
    {
        rangeShouldBeDecreased = value;
    }
    
    //Makes the Monster take damage
    public void takeDamage(int damage)
    {
        if(damage >0 && damage<health)
        {
            health -=damage;
        }
        else if(damage >= health)
        {
            health = 0;
        }
    }
    
    //Returns if the health is greater than 0
    public boolean isAlive()
    {
        return health>0;
    }
    
    //String representation of a single Monster object
    public String toString()
    {
        return getName()+" has "+health+" health points with a "+ sword+" and a "+bow;
    }
}