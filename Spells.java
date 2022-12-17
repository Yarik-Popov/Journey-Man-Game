/**
 * Yarik Popov
 * ICS3U1-16, Mr. Matchen
 * April 25, 2021,
 * This class handles the values related to spell in the combat system
 * of the game. The logic behind the spells will be handled by the Player
 * class.
 * */
public class Spells
{
    //Edge cases for spell length and effect value
    public static final double EFFECT_VALUE_NO_AMOUNT = -1;
    public static final int INSTANT_SPELL = 1;
    public static final int INFINITE_SPELL_LENGTH = -1; //Unused value
    public static final int UNSET = 0;
    
    //types of spells
    public static final int STRENGTH = 0;
    public static final int REGENERATION = 1;
    public static final int HEAL = 2;
    public static final int RESISTANCE = 3;
    
    //Constants related to creating a random spell
    public static final int[] TYPES_LIST = {STRENGTH, REGENERATION, HEAL, RESISTANCE};
    
    //Amount constants
    public static final int MIN_AMOUNT = 2;
    public static final int MAX_AMOUNT = 4;
    
    //Time values range 
    public static final int MIN_TIME_NON_INSTANT = 3;
    public static final int MAX_TIME_NON_INSTANT = 5;
    
    //Values related to random strength
    public static final double MIN_STRENGTH = 1.1;
    public static final double MAX_STRENGTH = 1.75;
    
    //Values related to random regeneration
    public static final int MIN_REGENERATION = 5;
    public static final int MAX_REGENERATION = 12;
    
    //Values related to random heal
    public static final int MIN_HEAL = 20;
    public static final int MAX_HEAL = 35;
    
    //Values related to random resistance
    public static final double MIN_RESISTANCE = 0.7;
    public static final double MAX_RESISTANCE = 0.9;
    
    //Instance variables related to a single batch of spell
    private double effectValue;
    private int amount;
    private final int type;
    private int time;
    
    //These values are calculated
    private double value; //Value doesn't change once initialized
    private int price;
    
    //Constructor where all values need to be inputted
    public Spells(int type, double effectValue, int time, int amount)
    {
        this.effectValue = effectValue;
        this.amount = amount;
        this.type = type;
        this.time = time;
        createValue();
    }
    
    //Constructor related to a random spell
    public Spells()
    {
        //Sets the type and amount of spells
        type = Randomizer.nextInt(TYPES_LIST.length);
        amount = Randomizer.nextInt(MIN_AMOUNT, MAX_AMOUNT);
        
        //Creates the effectValue depending on the type
        if(type == STRENGTH)
        {
            effectValue = Randomizer.nextDouble(MIN_STRENGTH, MAX_STRENGTH);
        }
        else if(type == REGENERATION)
        {
            effectValue = (double) Randomizer.nextInt(MIN_REGENERATION, MAX_REGENERATION);
        }
        else if(type == HEAL)
        {
            effectValue = (double) Randomizer.nextInt(MIN_HEAL, MAX_HEAL);
            //Sets the time of the spell to 1
            time = INSTANT_SPELL;
        }
        else if(type == RESISTANCE)
        {
            effectValue = Randomizer.nextDouble(MIN_RESISTANCE, MAX_RESISTANCE);
        }
        
        //Sets the time if the spell isn't a heal spell
        if(type != HEAL)
        {
            time = Randomizer.nextInt(MIN_TIME_NON_INSTANT, MAX_TIME_NON_INSTANT);
        }
        createValue();
    }
    
    //Gets the string of the type of spell based on the input
    public static String typeOfSpellToString(int type)
    {
        return new Spells(type, 0, 0, 0).typeToString();
    }
    
    /**
     * This is a helper method, it creates the value of the spell. The value
     * of the spell is the product of amount, time & length divided by the best
     * possible spell. If the spell is created using the no param constructor, 
     * the value is always less than 1 but greater than 0.
     * */
    private void createValue()
    {
        //Initalizes the values used to calculate value
        double tempValue = effectValue*amount;
        double maxValue = MAX_AMOUNT;
        
        //Multiplies the maxValue amount by the best possible effectValue for each type of spell
        if(type == STRENGTH)
        {
            maxValue*=MAX_STRENGTH;
        }
        else if(type == REGENERATION)
        {
            maxValue *=MAX_REGENERATION;
        }
        else if(type == HEAL)
        {
            maxValue *= MAX_HEAL;
        }
        else if(type == RESISTANCE)
        {
            /*
              The lower the resistance the better, because resistance is an effect which
              affects the player damage. The lower the resistance effect value the less damage
              the opponent does.
              */
            maxValue *= 1/MIN_RESISTANCE;
        }
        
        /*
          This part multiplies the values by the time, if the type isn't heal, because
          a heal spell's time is only 1 move vs the other spells which have a time of greater than 2
          */
        if(type != HEAL)
        {
            tempValue *=time;
            maxValue *=MAX_TIME_NON_INSTANT;
        }
        
        //Calculates the value and price used within the object
        value = tempValue/maxValue;
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
    
    //Change the amount of spells
    public void changeNumberOfSpells(int num)
    {
        amount+=num;
    }
    
    //Returns the value of this spell batch
    public double getValue()
    {
        return value;
    }
    
    //Gets how long the spell last for 
    public int getTime()
    {
        return time;
    }
    
    /**
     * This method uses a spell by reducing the amount by 1 and returns 
     * the effectValue of the spell.
     * @return effectValue or EFFECT_VALUE_NO_AMOUNT, double. 
     * If the amount of spells is greater than 0 it returns the effectValue 
     * If the amount of spells is 0 than it returns -1.
     * */
    public double use()
    {
        if(hasSpells())
        {
            amount--;
            return effectValue;
        }
        return EFFECT_VALUE_NO_AMOUNT;
    }
    
    //Returns if this batch has spells or not
    public boolean hasSpells()
    {
        return amount >0;
    }
    
    //Gets the type of spell
    public int getType()
    {
        return type;
    }
    
    //Get effect value
    public double getEffectValue()
    {
        return effectValue;
    }
    
    //Get amount of spells
    public int getAmount()
    {
        return amount;
    }
    
    //Returns a string representing the type of spell
    public String typeToString()
    {
        if(type == STRENGTH)
        {
            return "Strength";
        }
        else if(type == REGENERATION)
        {
            return "Regeneration";
        }
        else if(type == HEAL)
        {
            return "Heal";
        }
        else if(type == RESISTANCE)
        {
            return "Resistance";
        }

    //This case only occurs when the type is inputted incorrectly in the param constructor
    return "Invalid Spell Type";
    }
    
    //String representation of spell object
    public String toString()
    {
        return amount +" "+typeToString() +" spell with an effect value of "+effectValue+" lasting "+time+" moves";
    }
}