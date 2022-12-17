/**
 * Yarik Popov
 * ICS3U1-16, Mr. Matchen
 * April 21, 2021
 * This is the food class what will be used in the game. It handles the 
 * food of the player and the transport (some animal)
 * */

public class Food
{
    /*Class constants related to which type of food this will help later
    on then needing to feed transport or you.
    */
    public static final int HUMAN =0;
    public static final int TRANSPORT =1;
    public static final double SPOILED_MAX_CHANCE = 0.15;
    public static final int SPOIL_CHANCE_IF_FOOD_AMOUNT13 = 13;
    //This amount is to make sure the food can spoil all the way down to 0
    public static final int WILL_NEVER_SPOIL_AMOUNT = 13;
    
    //Constants related to creating a random food object
    public static final int MIN_FOOD_AMOUNT = 300;
    /*The food can exceed the MAX_FOOD_AMOUNT, this constant will be used to
    * calculate how good a food item is when trading
    */
    public static final int MAX_FOOD_AMOUNT = 800;
    public static final int TRANSPORT_MULTIPLIER = 2;
    public static final int[] FOOD_TYPES_LIST = {Food.HUMAN, Food.TRANSPORT};
    public static final int UNSET = 0;
    public static final int NO_FOOD_SPOILED_PERCENTAGE = -1;
    public static final double PERCENTAGE_FOOD_KEPT_AFTER_REMOVING_SPOILED = 0.99;
    
    //Amounts related to food
    private int amount;
    private int spoiledAmount;
    private int maxSpoiledIncrease;
    
    //The value is based on the best food chance
    private double value;
    //The price of the item
    private int price;
    //Is the type of food, For player or Transport
    private int type;
    //The name of the user of the food
    private String name;
    
    //Constructor where type and amount are inputted
    public Food(int amount, int type)
    {
        this.amount = amount;
        this.type = type;
        createVariablesInBothConstructors();
    }
    
    //Creates a food object with a random amount of food
    public Food(int type)
    {
        amount = Randomizer.nextInt(MIN_FOOD_AMOUNT, MAX_FOOD_AMOUNT);
        this.type = type;
        createVariablesInBothConstructors();
    }
    
    /**
     * This method sets or updates values based on the values inputted into
     * the constructors. 
     * */
    private void createVariablesInBothConstructors()
    {
        spoiledAmount =0;
        increaseFoodTransport();
        updateMaxSpoiledIncrease();
        price = UNSET;
        name = convertTypeFoodToString();
    }
    
    /**
     * This method doubles the amount of food if the type is
     * a transport food, because they eat a lot more food than 
     * player
     * */
    private void increaseFoodTransport()
    {
        if(type == TRANSPORT)
        {
            amount *=TRANSPORT_MULTIPLIER;
        }
    }
    
    //This method gets the percentage of spoiled food
    public double getSpoiledPercentage()
    {
        if(amount + spoiledAmount >0)
        {
            return (double) spoiledAmount/( spoiledAmount+ amount);
        }
        else
        {
            return NO_FOOD_SPOILED_PERCENTAGE;
        }
    }
    
    //Sets the name
    public void setName(String name)
    {
        this.name = name;
    }
    
    //Gets the name 
    public String getName()
    {
        return name;
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
    
    /**
     * This method updates the maxSpoiledIncrease and value of this
     * food item. 
     * */
    private void updateMaxSpoiledIncrease()
    {
        maxSpoiledIncrease= (int)(SPOILED_MAX_CHANCE*amount);
        value = (double) amount/MAX_FOOD_AMOUNT;
        if(type == TRANSPORT)
        {
            value /= TRANSPORT_MULTIPLIER;
        }
    }
    
    //Gets the food value
    public double getValue()
    {
        return value;
    }
    
    //Increases the amount of spoiled food
    public void increaseSpoiledFoodAmount(int num)
    {
        spoiledAmount+=num;
    }
    
    /**
     * This method spoils the food. 
     * Case 1: Food amount is greater than 14 if(15% chance)
     * The spoiled food percent chance is
     * maxed out at 15%. This means upto a maximum of 15% of the food can
     * spoil at anytime. Usually from 0 to 15% of the food.
     * 
     * Case 2: Food amount less than 14 if (15% chance)
     * The food will only spoil 1 gram at a time at a around 14% of occuring.
     * This means the food will not spoil in batches like in Case 1. 
     * */
    public void spoilFood()
    {
        //Case 1
        if(maxSpoiledIncrease>1)
        {
            int increasedSpoiledFood = Randomizer.nextInt(maxSpoiledIncrease);
            consumeFood(increasedSpoiledFood);
            increaseSpoiledFoodAmount(increasedSpoiledFood);
        }
        //Case 2
        else if(amount <= WILL_NEVER_SPOIL_AMOUNT && amount >0)
        {
            int singleSpoiledGram = Randomizer.nextInt(SPOIL_CHANCE_IF_FOOD_AMOUNT13);
            if(singleSpoiledGram == 0)
            {
                consumeFood(1);
                increaseSpoiledFoodAmount(1);
            }
        }
    }
    
    /**
     * This method removes the spoiled food and takes ~1% of the food food. This 
     * method only performs the above mentionned action if there is spoiled food. 
     * */
    public void removeSpoiledFood()
    {
        if(hasSpoiledFood())
        {
            spoiledAmount =0;
            amount *=PERCENTAGE_FOOD_KEPT_AFTER_REMOVING_SPOILED;
        }
    }
    
    //Returns true if there is spoiled food, false if there is none
    public boolean hasSpoiledFood()
    {
        return spoiledAmount >0;
    }
    
    /**
     * Increase amount based on the inputted value
     * There is no check on the if num is positive or not,
     * because Npc.java can reduce the amount of food and 
     * it uses this method
     * */
    public void addFood(int num)
    {
        amount +=num;
        updateMaxSpoiledIncrease();
    }
    
    //Returns a boolean if food can be consumed or not
    public boolean canConsumeFood(int num)
    {
        return (hasFood() && amount-num>=0 && num>0);
    }
    
    //Reduces food amount if food can be eaten
    public void consumeFood(int num)
    {
        if(canConsumeFood(num))
        {
            amount -=num;
            updateMaxSpoiledIncrease();
        }
    }
    
    //Returns boolean if there is food or not
    public boolean hasFood()
    {
        return amount > 0;
    }
    
    //Gets the amount of spoiled food
    public int getSpoiledFoodAmount()
    {
        return spoiledAmount;
    }
    
    //Gets the amount of max spoiled increase
    public int getMaxSpoiledIncrease()
    {
        return maxSpoiledIncrease;
    }
    
    //Gets the amount of edible food
    public int getFoodAmount()
    {
        return amount;
    }
    
    //Returns the type of food. Human or Transport
    public int getType()
    {
        return type;
    }
    
    //Helper method to convert the type of food to a string
    private String convertTypeFoodToString()
    {
        if(type == HUMAN)
        {
            return "player";
        }
        else if(type == TRANSPORT)
        {
            return "transport";
        }
        return "Improper inputted parameter for type of food";
    }
    
    //String representation of food object
    public String toString()
    {
        return amount+" grames of edible food & "+ spoiledAmount +" grames of spoiled food for "+name;
    }
}