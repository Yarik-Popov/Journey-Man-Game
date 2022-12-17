/**
 * April 24, 2021
 * This class handles the logic and values behind use of the bow in the game
 * */
public class Bow extends Weapon
{
    //Constants related to creating a random bow object
    public static final String DEFAULT_NAME = "Bow";
    public static final int MIN_ARROWS = 6;
    public static final int MAX_ARROWS = 12;
    //The chance of getting a miss is 1/missChance
    public static final int MIN_MISS_CHANCE = 3;
    public static final int MAX_MISS_CHANCE = 6;
    
    //Instance variables specific related to bows and not general weapon
    private int arrows;
    private final int missChance;
    
    //Constructor where all values are inputted
    public Bow(String name, int minDamage, int maxDamage, double increaseInDamage, int chanceOfMaxDamage, int missChance, int arrows)
    {
        super(name, minDamage, maxDamage, increaseInDamage, chanceOfMaxDamage);
        this.arrows = arrows;
        this.missChance = missChance;
        createValue();
    }
    
    //Constructor to create a random or worst bow object
    public Bow(int num)
    {
        super(num);
        setName(DEFAULT_NAME);
        //Creates the worst possible bow
        if(num == WORST_WEAPON)
        {
            arrows = MIN_ARROWS;
            missChance = MIN_MISS_CHANCE;
        }
        //Creates a random bow
        else
        {
            arrows = Randomizer.nextInt(MIN_ARROWS, MAX_ARROWS);
            missChance = Randomizer.nextInt(MIN_MISS_CHANCE, MAX_MISS_CHANCE);
        }
        createValue();
    }
    
    //Constructor to create a blank bow, used when initializing player
    public Bow()
    {
        super();
        setName(DEFAULT_NAME);
        arrows = UNSET;
        missChance = UNSET;
    }

    /**
     * This is a helper method to set the value and price. The value is determined
     * by taking the average of the possible values for to initialize bow and the
     * best possible bow. The initialized bow average damage times number of arrows times missChance
     * is divided by the best possible bow average damage times best number of arrows times best missChance
     * If the bow is created in the 1 param, int constructor the value will always be between 0 and 1. 
     * */   
    private void createValue()
    {
        setValue(getValue()*(arrows*missChance/(double)(MAX_ARROWS*MAX_MISS_CHANCE)));
    }
    
    /**
     * This method shoots an arrow or gets the damage of the bow. The bow only
     * gets a damage if the bow has arrows and the arrow didn't miss. An arrow
     * is still used even if no damage is returned. 
     * @return damage. The damage of an arrow or 0 if it fails any of the above
     * mentionned checks
     * */
    public int shoot()
    {
        if(hasArrows() && missChance != UNSET)
        {
            int hit = Randomizer.nextInt(missChance);
            arrows--;

            if(hit+1 != missChance)
                return getDamage();
            return 0;
        }
        return 0;
    }
    
    //Returns if the bow has arrows
    public boolean hasArrows()
    {
        return arrows>0;
    }
    
    //Adds num to the number of arrows. Makes sure that arrows is positive number
    public void addArrows(int num)
    {
        if(num>0)
            arrows+=num;
    }
    
    //Returns the number of arrows
    public int getArrows()
    {
        return arrows;
    }
    
    //Returns the chance of getting a miss 
    public int getMissChance()
    {
        return missChance;
    }
    
    //Returns a string representation of the object
    public String toString()
    {
        return stringRepresentation()+" and a 1/"+missChance+" of missing with "+arrows+" arrows";
    }
}