/**
 * May 22, 2021
 * This is the transport class it is a way that the user can travel.
 * */
public class Transport
{
    //Class constants
    //Hunger constants
    public static final int HUNGRY = 30; //Changes
    public static final int STARVED = 0; //Can't move
    public static final int MAX_HUNGER = 100; 
    /* MAX_HUNGER is the initalized amount of hunger.
    The lower the hunger amount the more hungry the animal.
    Exactly the same as player. Hunger is distance until the
    animal can't move anymore
    */
    
    //Constants related to setting default transport object
    public static final String UNSET_NAME = "Unset Name";
    public static final String UNSET_TYPE = "Unset Type";
    
    //Instance variables
    private String name;
    private String type; //Similar to the animal species
    private int hunger;
    private Food food;
    
    //Constructor with a name and type
    public Transport(String type, String name)
    {
        this.type = type;
        this.name = name;
        createVariablesInBothConstructors();
    }
    
    //Constructor which will be used when Player is initalized
    public Transport()
    {
        type = UNSET_TYPE;
        name = UNSET_NAME;
        createVariablesInBothConstructors();
    }
    
    //Creates the variables used in both constructors
    private void createVariablesInBothConstructors()
    {
        hunger = MAX_HUNGER; 
        food = new Food(0, Food.TRANSPORT);
    }
    
    //Sets the name
    public void setName(String name)
    {
        this.name = name;
    }
    
    //Returns the name
    public String getName()
    {
        return name;
    }
    
    //Sets the type 
    public void setType(String type)
    {
        this.type = type;
    }
    
    //Returns the type
    public String getType()
    {
        return type;
    }
    
    //Returns the food variable object
    public Food getFood()
    {
        return food;
    }
    
    //Returns the hunger
    public int getHunger()
    {
        return hunger;
    }
    
    //Spoils the food of the transport
    public void spoilFood()
    {
        food.spoilFood();
    }
    
    //Adds food to the food object
    public void addFood(int num)
    {
        food.addFood(num);
    }
    
    /*This method returns the amount of food until the 
    animal is full, this assumes the animal is not transporting 
    the player
    */
    public int foodUntilFull()
    {
        return MAX_HUNGER - hunger;
    }
/*                  Changed Methods                     */
    
    
    //Returns if animal is hungry or not
    public boolean isHungry()
    {
        return hunger <=HUNGRY;
    }
    
    //Returns if animal is hungry or not with travel
    public boolean isHungry(int num)
    {
        return (hunger-num)<=HUNGRY && (hunger-num) >= STARVED;
    }
    
    //Returns if the animal is too hungry or not
    //False it can still transport player
    public boolean isStarved()
    {
        return hunger == STARVED;
    }
    
    //Returns if the animal is too hungry or not
    //False it can still transport player
    public boolean isStarved(int num)
    {
        return hunger <= num;
    }
    
    //This reduces the amount of hunger
    public void feed(int num)
    {
        if(((num+hunger)<=MAX_HUNGER) && food.canConsumeFood(num))
        {
            food.consumeFood(num);
            hunger +=num;
        }
    }
    
    /**
     * This method transports the player a certain distance and adds it
     * to the hunger value
     * 
     * @param int num. The distance
     * If the animal is too hungry it will not transport the player and
     * needs to be feed immediately
     * 
     * NOTES TO SELF:
     * Perhaps make this method return a boolean if player can be 
     * transported the set distance
     * */
    public void transportPlayer(int num)
    {
        if(!isStarved(num))
        {
            hunger -=num;
        }
        else
        {
            hunger = STARVED;
        }
    }
    
    //Returns the distance until the animal is hungry
    public int getDistanceUntilHungry()
    {
        if(hunger - HUNGRY <=0)
        {
            return 0;
        }
        return hunger - HUNGRY;
    }
    
    //Prints the transport object with the food
    public void printTransportWithFood()
    {
        System.out.println(toString());
        System.out.println(food);
    }
    
    //String representation of a single transport object
    public String toString()
    {
        return "Your "+type+" named "+name +" is "+hunger+" percent full";
    }
}