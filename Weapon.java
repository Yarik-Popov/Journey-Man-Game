/**
 * Yarik Popov
 * ICS3U1-16, Mr. Matchen
 * April 23, 2021
 * This is the Weapon class. It handles logic behind the melee weapons
 * a basic weapon type. 
 * */
 
public class Weapon
{
    //Constants related to creating a random sword object
    public static final String DEFAULT_NAME = "Sword";
    public static final int MIN_OF_MIN_DAMAGE = 5;
    public static final int MAX_OF_MIN_DAMAGE = 14;
    
    //Will be divided by 10 to get the increase in damage double
    public static final int MIN_INCREASE_IN_DAMAGE = 7;
    public static final int MAX_INCREASE_IN_DAMAGE = 18;
    
    //The chance to get the max damage is 1/chanceOfMaxDamage
    public static final int MIN_CHANCE_OF_MAX_DAMAGE = 4; //The worst value
    public static final int MAX_CHANCE_OF_MAX_DAMAGE = 10; //The best value
    public static final int RANGE_OF_MAX_DAMAGE = 6;
    public static final int MIN_DIFFERENCE_TO_MAX = 3;
    
    //Values related to creating a blank sword
    public static final int UNSET =0;
    
    //Variables used in the 1 param int constructor to create a random or worst weapon
    public static final int RANDOM_WEAPON = 1;
    public static final int WORST_WEAPON = 0;
    
    //Variables related to the damage of a weapon object
    private int maxDamage;
    private int minDamage;
    private int chanceOfMaxDamage;
    private double increaseInDamage;
    
    //Name of weapon
    private String name;
    //The value of this weapon
    private double value;
    //Price of the item
    private int price;
    
    //Constructor where all values are inputted
    public Weapon(String name, int minDamage, int maxDamage, double increaseInDamage, int chanceOfMaxDamage)
    {
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.name = name;
        this.chanceOfMaxDamage = chanceOfMaxDamage;
        this.increaseInDamage = increaseInDamage;
        createValue();
    }
    
    //Constructor to create a random sword object or worst sword
    public Weapon(int num)
    {
        //Sets the weapon name
        name = DEFAULT_NAME;
        //This value is used later in the calculation
        int range =1;
        
        //Creates the worst weapon using the min values for all instance variables
        if(num == WORST_WEAPON)
        {
            minDamage = MIN_OF_MIN_DAMAGE;
            increaseInDamage = (double)MIN_INCREASE_IN_DAMAGE/10;
            chanceOfMaxDamage = MIN_CHANCE_OF_MAX_DAMAGE;
        }
        //Creates a random weapon
        else
        {
            minDamage = Randomizer.nextInt(MIN_OF_MIN_DAMAGE, MAX_OF_MIN_DAMAGE);
            increaseInDamage = ((double)Randomizer.nextInt(MIN_INCREASE_IN_DAMAGE, MAX_INCREASE_IN_DAMAGE))/10;
            chanceOfMaxDamage = Randomizer.nextInt(MIN_CHANCE_OF_MAX_DAMAGE, MAX_CHANCE_OF_MAX_DAMAGE);
            range = Randomizer.nextInt(RANGE_OF_MAX_DAMAGE);
        }
        maxDamage = minDamage+ (int) (increaseInDamage*chanceOfMaxDamage) +MIN_DIFFERENCE_TO_MAX + range;
        createValue();
    }
    
    //Constructor to create a blank sword. Used when initalizing Player
    public Weapon()
    {
        name = DEFAULT_NAME;
        minDamage = UNSET;
        maxDamage = UNSET;
        increaseInDamage = (double)UNSET;
        chanceOfMaxDamage = UNSET;
        value = UNSET;
        price = UNSET;
    }
    
    /**
     * This is a helper method to set the value and price. The value is determined
     * by taking the average of the possible values for the initialize weapon and the 
     * best possible weapon. The initialized weapon average damage is divided by the 
     * best possible weapon average damage. If the weapon is created in the 1 param, int
     * constructor the value will always be between 0 and 1. 
     * */
    private void createValue()
    {
        double maxValue = ((MAX_OF_MIN_DAMAGE+((double)MAX_INCREASE_IN_DAMAGE/10)*(MAX_CHANCE_OF_MAX_DAMAGE-2))*(MAX_CHANCE_OF_MAX_DAMAGE-1)/2 + MAX_OF_MIN_DAMAGE+MIN_DIFFERENCE_TO_MAX+RANGE_OF_MAX_DAMAGE-1 + ((double)MAX_INCREASE_IN_DAMAGE/10)*MAX_CHANCE_OF_MAX_DAMAGE)/MAX_CHANCE_OF_MAX_DAMAGE;
        value = (((minDamage + increaseInDamage*(chanceOfMaxDamage-2))*(chanceOfMaxDamage-1)/2 + maxDamage)/(double)chanceOfMaxDamage)/maxValue;
        price = UNSET;
    }
    
    //Gets the price
    public int getPrice()
    {
        return price;
    }
    
    //Sets the price
    public void setPrice(int num)
    {
        price = num;
    }
    
    //Returns the value of a single weapon object
    public double getValue()
    {
        return value;
    }
    
    /**
     * Sets the value of this weapon object. Normally the value isn't
     * supposed to change, but this class is extended by the Bow class
     * which needs to change value. So a public method was need to be 
     * created.
     * @param num, double. Sets value to num
    * */
    public void setValue(double num)
    {
        value = num;
    }
    
    //Renames the weapon useful when Trading
    public void setName(String name)
    {
        this.name = name;
    }
    
    //Returns the increase in damage with a higher hit chance
    public double getIncreaseInDamage()
    {
        return increaseInDamage;
    }
    
    //Returns the chance of max damage
    //The chance is 1/chanceOfMaxDamage to get max damage
    public int getChanceOfMaxDamage()
    {
        return chanceOfMaxDamage;
    }
    
    //Get the damage (will be randomized everytime)
    public int getDamage()
    {
        if(chanceOfMaxDamage != UNSET)
        {
            int hit = Randomizer.nextInt(chanceOfMaxDamage);
            if(hit+1 == chanceOfMaxDamage)
            {
                return maxDamage;
            }
            else
            {
                return minDamage+((int)(hit*increaseInDamage));
            }
        }
        else
        {
            return 0;
        }
    }
    
    //Gets the name of weapon
    public String getName()
    {
        return name;
    }
    
    //Gets minimum damage of weapon
    public int getMinDamage()
    {
        return minDamage;
    }
    
    //Gets maximum damage of weapon
    public int getMaxDamage()
    {
        return maxDamage;
    }
    
    //Returns if the weapon is unset or not
    public boolean isUnset()
    {
        return getValue()==UNSET;
    }
    
    /**
     * This is effectively the toString(), but to prevent recursive calls
     * in the bow class. The reason for this is that this method is used
     * in the Bow class in the printing of the object. 
     * */
    public String stringRepresentation()
    {
        return name +" with a minimum damage of "+minDamage+" and the increase in damage between chances is "+increaseInDamage+" and with a 1/"+chanceOfMaxDamage+" to get a maximum damage of "+ maxDamage;
    }
    
    //Gets string representation of weapon
    public String toString()
    {
        return stringRepresentation();
    }
}