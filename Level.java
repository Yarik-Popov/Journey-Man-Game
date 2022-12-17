/**
 * May 14, 2021
 * This class handles the values related to a single level in the game.
 *The logic is handles by the MyProgram.java class 
 * */
public class Level
{
    //Class constants
    public static final int MONSTER_FIGHT = 0;
    public static final int INTERACTION_NPC = 1;
    
    //The chance to fight will be 1/CHANCE_OF_FIGHT; 
    public static final int CHANCE_OF_FIGHT = 3; 
    public static final int TRADER = 5;
    public static final int ALWAYS_MONSTER_FIGHT = 8;
 
    //Range of distances
    public static final int RANGE_OF_DISTANCES = 20;
    public static final int AVERAGE_DISTANCE = 100;
    
    //Variables related to single level
    private final int levelNumber;
    private int distance;
    private int type;
    
    //Constructor, the user inputs the number which the level corresponds to
    public Level(int num)
    {
        levelNumber = num;
        createDistance();
        createType();
    }
    
    /**
     * This method creates the distance based on the level. It take the
     * average distance and mulitplies it by the levelNumber and plus or minuses
     * the range of distances to get the min and max distance. Next the method
     * chooses a value between those 2 distances (inclusive)
     * */
    private void createDistance()
    {
        int minDistance = AVERAGE_DISTANCE*levelNumber-RANGE_OF_DISTANCES;
        int maxDistance = AVERAGE_DISTANCE*levelNumber+RANGE_OF_DISTANCES;
        distance = Randomizer.nextInt(minDistance, maxDistance);
    }
    
    /**
     * This method creates the type of event which will occur based on the
     * levelNumber. If the levelNumber is trader or always monster fight then
     * it sets the type to those values. If isn't those than it sets the type
     * to be npc interaction or monster fight.
     * */
    private void createType()
    {
        //Checks if the level number corresponds to a constant type
        if(levelNumber != TRADER && levelNumber != ALWAYS_MONSTER_FIGHT)
        {
            int value = Randomizer.nextInt(CHANCE_OF_FIGHT);
            if(value != MONSTER_FIGHT)
            {
                type = INTERACTION_NPC;
            }
            else
            {
                type = MONSTER_FIGHT;
            }
        }
        //The levelNumber is set to be trader
        else if(levelNumber == TRADER)
        {
            type = TRADER;
        }
        //The levelNumber is set to be always monster fight
        else {
            type = MONSTER_FIGHT;
        }
    }
    
    //Sets the type
    public void setType(int num)
    {
        type = num;
    }
    
    //Returns the type
    public int getType()
    {
        return type;
    }
    
    //Sets the type
    public void setDistance(int num)
    {
        distance = num;
    }
    
    //Returns the distance
    public int getDistance()
    {
        return distance;
    }
    
    //Returns if the distance is smaller or equal to input
    public boolean hasBeenReached(int num)
    {
        return distance<=num;
    }
    
    //Gets the type but in string format
    public String typeToString()
    {
        if(type == MONSTER_FIGHT)
        {
            return "Monster fight";
        }
        else if(type == INTERACTION_NPC)
        {
            return "Interaction with Npc";
        }
        else if(type == TRADER)
        {
            return "Interaction with Trader";
        }

        return "Unknown";
    }
    
    //String representation of level, prints the level num, distance and type event
    public String toString()
    {
        return "Level "+levelNumber+" is a "+typeToString()+" event with a distance of "+distance+" from start";
    }
}