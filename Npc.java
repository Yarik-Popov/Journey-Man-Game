/**
 * Yarik Popov
 * ICS3U1-16, Mr. Matchen
 * April 25, 2021
 * This class handles the variables and logic related to Npcs. 
 * */
import java.util.*;
public class Npc
{
    //Class constants
    public static final int CHANCE_TO_ASK_GIVE_STEAL = 3;
    //Types of Npcs 
    public static final int ASK = 0;
    public static final int GIVE = 1;
    public static final int STEAL = 2;
    public static final int NO_INTERACTION_WITH_RESOURCES = 3;
    
    //Values related to interaction with resources
    public static final int NUMBER_OF_RESOURCES = 6;
    public static final int PLAYER_FOOD = 0;
    public static final int TRANSPORT_FOOD = 1;
    public static final int STRENGTH_SPELLS = 2;
    public static final int REGENERATION_SPELLS = 3;
    public static final int HEAL_SPELLS = 4;
    public static final int RESISTANCE_SPELLS = 5;
    public static final double PERCENT_INTERACTION_WITH_RESOURCES = 0.1;
    
    //Persistence range used when asking for a player to give resources
    public static final int MIN_PERSISTENCE =2;
    public static final int MAX_PERSISTENCE =5;
    public static final int NO_PERSISTENCE = 0;
    
    //Possible names for NPCs based on the 10 most popular english names
    public static final String[] RANDOM_NAME = {"James", "Mary", "Robert", "Patricia", "John", "Jennifer", "Michael", "Linda", "William", "Elizabeth"};
    
    //Variables
    private String name;
    private int type;
    private int persistence;
    
    //Creates npc based on given name and type
    public Npc(String name, int type)
    {
        this.name = name;
        this.type = type;
    }
    
    //Creates a npc with the given name
    public Npc(String name)
    {
        this.name = name;
        giveRandomType();
    }
    
    //Creates a npc with the give type
    public Npc(int type)
    {
        this.type = type;
        giveRandomName();
    }
    
    //Creates a completely random npc
    public Npc()
    {
        giveRandomName();
        giveRandomType();
    }
    
    //Gives the npc a random type
    private void giveRandomType()
    {
        setTypeOfNpc(Randomizer.nextInt(CHANCE_TO_ASK_GIVE_STEAL));
    }
    
    //Gives the Npc a random name based on the 10 most common names
    private void giveRandomName()
    {
        name = RANDOM_NAME[Randomizer.nextInt(RANDOM_NAME.length)];
    }
    
    //Sets the type of the Npc (used in Trader and Monster classes)
    public void setTypeOfNpc(int num)
    {
        type = num;
        initializePersistence();
    }
    
    //Sets the value of the persistence depending on the type of npc
    private void initializePersistence()
    {
        if(type == ASK)
        {
            persistence = Randomizer.nextInt(MIN_PERSISTENCE, MAX_PERSISTENCE);
        }
        else
        {
            persistence = NO_PERSISTENCE;
        }
    }
    
    //Gets the persistence used when an ASK npc asks for resources
    public int getPersistence()
    {
        return persistence;
    }
    
    //Sets the persistence
    public void setPersistence(int num)
    {
        persistence = num;
    }
    
    //Gets if the Npc has persistence left
    public boolean isPersist()
    {
        return persistence > NO_PERSISTENCE;
    }
    
    /**
     * This method updates the persistence. This method happens
     * when the player refuses to give resources to the Npc
     * */
    public void updatePersistence()
    {
        persistence--;
    }
    
    /**
     * This method happens when the player chose to give
     * resources to an ask Npc.
     * */
    public void playerChoseToGive(Player user)
    {
        setTypeOfNpc(STEAL);
        affectPlayerResources(user);
    }
    
    //Gets the name
    public String getName()
    {
        return name;
    }
    
    //Gets the type of Npc
    public int getTypeOfNpc()
    {
        return type;
    }
    
    //Method to get the type of Npc
    public String typeOfNpcToString()
    {
        if(type == ASK)
        {
            return "asks for";
        }
        else if(type == GIVE)
        {
            return "gives";
        }
        else if(type == STEAL)
        {
            return "steals";
        }
        else
        {
            return "doesn't care about your";
        }
    }
//              Affect resources methods

//Takes or gives resources to the player
    public void affectPlayerResources(Player user)
    {
        int affectOnResources = getTypeOfNpc();
        if(affectOnResources == GIVE)
        {
            int num = 1;
            minusOrPlusResources(num, user);
        }
        else if(affectOnResources == STEAL)
        {
            int num =-1;
            minusOrPlusResources(num, user);
        }
    }
    
    /**
     * This is a helper method to add or substract food. The reason why
     * this method is in a loop-and-a-half is because sometimes the randomizer
     * can pick a value but the player doesn't have anything for that type of 
     * item.
     * @param num. Minus or plus
     * @param user. The player object gaining or losing items
     * */
    private void minusOrPlusResources(int num, Player user)
    {
        while(true)
        {
            int type = Randomizer.nextInt(NUMBER_OF_RESOURCES);
            
            //Substracts or adds food items for player
            if(type == PLAYER_FOOD)
            {
                changeFoodAmount(user.getFood(), num);
                break;
            }
            //Substracts or adds food items for transport
            else if(type == TRANSPORT_FOOD)
            {
                Food transportFood = user.getTransport().getFood();
                changeFoodAmount(transportFood, num);
                break;
            }
            //Removes or adds strength spells
            else if(type == STRENGTH_SPELLS && user.getStrength().size()>0)
            {
                changeSpellAmount(user, user.getStrength(), num);
                break;
            }
            //Removes or adds regeneration spells
            else if(type == REGENERATION_SPELLS && user.getRegeneration().size()>0)
            {
                changeSpellAmount(user, user.getRegeneration(), num);
                break;
            }
            //Removes or adds heal spells
            else if(type == HEAL_SPELLS && user.getHeal().size()>0)
            {
                changeSpellAmount(user, user.getHeal(), num);
                break;
            }
            //Removes or adds resistance spells
            else if(type == RESISTANCE_SPELLS && user.getResistance().size()>0)
            {
                changeSpellAmount(user, user.getResistance(), num);
                break;
            }
        }
        
    }
    
    /**
     * Adds or substracts spells from the player depending on the num.
     * @param user, Player. The player having spell items being added or substracted
     * @param spells, ArrayList<Spells>. The spell arraylist being affected
     * @param num, int. Plus or minus,
     * */
    private void changeSpellAmount(Player user, ArrayList<Spells> spells, int num)
    {
        //Get important values
        int size = spells.size();
        int index = Randomizer.nextInt(size);
        Spells singleSpell = spells.get(index);
        int maxResourceAmount = (int)(singleSpell.getAmount()*PERCENT_INTERACTION_WITH_RESOURCES);
        maxResourceAmount = makeSureIntCanProduceValueInRandomizer(maxResourceAmount);
        int amount = Randomizer.nextInt(1, maxResourceAmount);
        
        //Adds or substracts spell amount
        System.out.println("You had before: "+singleSpell);
        singleSpell.changeNumberOfSpells(amount*num);
        user.removeEmptySpellBatch(spells, index);
        System.out.println("You now have: "+singleSpell);
    }
    
    /**
     * Adds or substracts food from the player depending on the num.
     * @param food, Food. The food object being affected
     * @param num, int. Plus or minus,
     * */
    private void changeFoodAmount(Food food, int num)
    {
        //Gets important values
        int maxResourceAmount = (int)(food.getFoodAmount()*PERCENT_INTERACTION_WITH_RESOURCES);
        maxResourceAmount = makeSureIntCanProduceValueInRandomizer(maxResourceAmount);
        
        //Adds or substracts food amount
        System.out.println("You had before: "+food);
        int foodAmount = Randomizer.nextInt(1, maxResourceAmount);
        food.addFood(foodAmount*num);
        System.out.println("You now have: "+food);
    }
    
    /**
     * This method makes sure the the value is can produce a value (0 or 1) in the 
     * randomizer
     * */
    private int makeSureIntCanProduceValueInRandomizer(int num)
    {
        if(num <2)
        {
            return 1;
        }
        return num;
    }
    
    //String representation of a single Npc object
    public String toString()
    {
        return name+" "+typeOfNpcToString()+" resources";
    }
}