/**
 * April 26, 2021
 * This is the Trader class, it handles the random trading half way through
 * the journey
 * */
import java.util.*;
public class Trader extends Npc
{
    //Variables related to creating items
    public static final int MAX_NUM_ITEMS_PER_RESOURCES = 4;
    public static final int MAX_NUM_ITEMS_FOOD = 6;
    public static final int MAX_NUM_ITEMS_SPELLS = 14;
    
    //Values related to the maximum values of items
    public static final int MAX_PRICE_FOOD = 50;
    public static final int MAX_PRICE_SPELLS = 100;
    public static final int MAX_PRICE_SWORDS = 200;
    public static final int MAX_PRICE_BOWS = 250;
    
    //Constants related to trading
    public static final int FOOD_ITEMS = 0;
    public static final int SPELLS_ITEMS = 1;
    public static final int SWORD_ITEMS = 2;
    public static final int BOW_ITEMS = 3;
    
    //Is returned if player inputs invalid input
    public static final int INVALID_INPUT = -1;
    
    //Lists to store the items 
    private ArrayList<Food> foods;
    private ArrayList<Spells> spells;
    private ArrayList<Weapon> swords;
    private ArrayList<Bow> bows;
    
    /**
     * These variables help to determine if the trader should display/take buy
     * requests from the player. If one of these variables are true, it prevents
     * the player from buying an item of this type. (The player can only have 1 sword 
     * and bow)
     * */
    private boolean swordSold;
    private boolean bowSold;
    
    //Constructor for Trader with a set name
    public Trader(String name)
    {
        super(name, Npc.NO_INTERACTION_WITH_RESOURCES);
        createItems();
    }
    
    //Creates a random trader
    public Trader()
    {
        super(Npc.NO_INTERACTION_WITH_RESOURCES);
        createItems();
    }
    
    //Creates the items for each type of resources
    private void createItems()
    {
        //Setting these values to false allows the player to buy these items 
        swordSold = false;
        bowSold = false;
        
        //Creates the arraylist of foods
        foods = new ArrayList<>();
        for(int i=0; i< MAX_NUM_ITEMS_FOOD; i++)
        {
            //Alternates between human and transport food when creating items
            int num = i%2;
            int type = Food.FOOD_TYPES_LIST[num];
            Food food = new Food(type);
            
            //Sets the price and adds the item to the arraylist
            food.setPrice((int)(food.getValue()*MAX_PRICE_FOOD)+1);
            foods.add(food);
        }
        
        //Creates the arraylist of spells
        spells = new ArrayList<>();
        for(int i=0; i<MAX_NUM_ITEMS_SPELLS; i++)
        {
            //Creates a spell item, sets the price and adds the item to the arraylist
            Spells spell = new Spells();
            spell.setPrice((int)(spell.getValue()*MAX_PRICE_SPELLS)+1);
            spells.add(spell);
        }
        
        //Creates the arraylist of swords
        swords = new ArrayList<>();
        for(int i=0; i<MAX_NUM_ITEMS_PER_RESOURCES; i++)
        {
            //Creates a random weapon item, sets the price and adds the item to the arraylist
            Weapon sword = new Weapon(Weapon.RANDOM_WEAPON);
            sword.setPrice((int)(sword.getValue()*MAX_PRICE_SWORDS)+1);
            swords.add(sword);
        }
        
        //Creates the arraylist of bows
        bows = new ArrayList<>();
        for(int i=0; i<MAX_NUM_ITEMS_PER_RESOURCES; i++)
        {
            //Creates a random bow item, sets the price and adds the item to the arraylist
            Bow bow = new Bow(Weapon.RANDOM_WEAPON);
            bow.setPrice((int)(bow.getValue()*MAX_PRICE_BOWS)+1);
            bows.add(bow);
        }
    }
    
    //Returns false if a sword has not been purchased
    public boolean hasSwordSold()
    {
        return swordSold;
    }
    
    //Sets the swordSold boolean value
    public void setSwordSold(boolean val)
    {
        swordSold = val;
    }
    
    //Returns false if a bow has not been purchased
    public boolean hasBowSold()
    {
        return bowSold;
    }
    
    //Sets the bowSold boolean value
    public void setBowSold(boolean val)
    {
        bowSold = val;
    }
    
    //Gets the arraylist of food items
    public ArrayList<Food> getFoods()
    {
        return foods;
    }
    
    //Sets the arraylist for foods
    public void setFoods(ArrayList<Food> foods)
    {
        this.foods = foods;
    }
    
    //Returns true if there are food to sell
    public boolean hasFoodItems()
    {
        return foods.size()>0;
    }
    
    //Gets the arraylist of spell batches
    public ArrayList<Spells> getSpells()
    {
        return spells;
    }
    
    //Sets the arraylist for spells
    public void setSpells(ArrayList<Spells> spells)
    {
        this.spells = spells;
    }
    
    //Returns true if there are spells to sell
    public boolean hasSpellsItems()
    {
        return spells.size()>0;
    }
    
    //Gets the arraylist of food items
    public ArrayList<Weapon> getSwords()
    {
        return swords;
    }
    
    //Sets the arraylist for swords
    public void setSwords(ArrayList<Weapon> swords)
    {
        this.swords = swords;
    }
    
    //Returns true if there are sword to sell
    public boolean hasSwordItems()
    {
        return swords.size()>0;
    }
    
    //Gets the arraylist of food items
    public ArrayList<Bow> getBows()
    {
        return bows;
    }
    
    //Sets the arraylist for bows
    public void setBows(ArrayList<Bow> bows)
    {
        this.bows = bows;
    }
    
    //Returns true if there are food to sell
    public boolean hasBowItems()
    {
        return bows.size()>0;
    }
    
    /** NEEDS TO BE TESTED
     * Method to display all the items of a specific type of item
     * @param type, int. The type of item which is based on 
     * constants found in this class
     * */
    public void displayItems(int type)
    {
        //Displays the food items
        if(type == FOOD_ITEMS)
        {
            for(int i=0; i<foods.size();i++)
            {
                System.out.println((i+1)+". "+foods.get(i)+" with a price of "+foods.get(i).getPrice());
                printLineBreakOrSpace(i, foods.size());
            }
        }
        //Displays the spells items
        else if(type == SPELLS_ITEMS)
        {
            for(int i=0; i<spells.size(); i++)
            {
                System.out.println((i+1)+". "+spells.get(i)+ " with a price of "+spells.get(i).getPrice());
                printLineBreakOrSpace(i, spells.size());
            }
        }
        //Displays the sword items
        else if(type == SWORD_ITEMS)
        {
            for(int i=0; i<swords.size();i++)
            {
                System.out.println((i+1)+". "+swords.get(i)+" with a price of "+swords.get(i).getPrice());
                printLineBreakOrSpace(i, swords.size());
            }
        }
        //Displays the bow items
        else if(type == BOW_ITEMS)
        {
            for(int i=0; i<bows.size(); i++)
            {
                System.out.println((i+1)+". "+bows.get(i)+" with a price of "+bows.get(i).getPrice());
                printLineBreakOrSpace(i, bows.size());
            }
        }
    }
    
    /**
     * This is a helper method to print a line break or a space between items
     * when displaying items. 
     * @param index, int. The current index being printed
     * @param length, int. The length of the arraylist of items being printed
     * */
    private void printLineBreakOrSpace(int index, int length)
    {
        if(index == length-1)
        {
            Main.printLineBreak();
        }
        else
        {
            System.out.println();
        }
    }
    
    /**
     * This method gives the number of items of an inputted type of
     * item based on constants found in this class.
     * @param type, int. The type we want to know how many items are there for
     * @return the number of items, int. If the type inputted isn't defined
     * in this class, this method returns -1.
     * */
    public int getNumberOfItems(int type)
    {
        //Gets the number of food items
        if(type == FOOD_ITEMS)
        {
           return foods.size();
        }
        //Gets the number of spells items
        else if(type == SPELLS_ITEMS)
        {
            return spells.size();
        }
        //Gets the number of sword items
        else if(type == SWORD_ITEMS)
        {
            return swords.size();
        }
        //Gets the number of bow items
        else if(type == BOW_ITEMS)
        {
            return bows.size();
        }
        //Is an invalid input so returns -1
        return INVALID_INPUT;
    }
    
    /**
     * This method gives minimum price of items of an inputted type of
     * item based on constants found in this class.
     * @param type, int. The type of item we want to get minimum value for
     * @return the number of items, int. If the type inputted isn't defined
     * in this class, this method returns -1.
     * */
    public int getMinimumPrice(int type)
    {
        //Arraylist of ints to store price values
        ArrayList<Integer> priceValues = new ArrayList<>();
        
        //Gets the minimum price of the food items
        if(type == FOOD_ITEMS && hasFoodItems())
        {
            //Loops over entire list, gets prices and adds them into priceValues arraylist
            for (Food food : foods) {
                priceValues.add(food.getPrice());
            }
            
            //Sorts the priceValues arraylist by ascending order and gets the lowest value
            Collections.sort(priceValues);
            return priceValues.get(0);
        }
        //Gets the minimum price of the spells items
        else if(type == SPELLS_ITEMS && hasSpellsItems())
        {
            //Loops over entire list, gets prices and adds them into priceValues arraylist
            for (Spells spell : spells) {
                priceValues.add(spell.getPrice());
            }
            
            //Sorts the priceValues arraylist by ascending order and gets the lowest value
            Collections.sort(priceValues);
            return priceValues.get(0);
        }
        //Gets the minimum price of the sword items
        else if(type == SWORD_ITEMS && hasSwordItems())
        {
            //Loops over entire list, gets prices and adds them into priceValues arraylist
            for (Weapon sword : swords) {
                priceValues.add(sword.getPrice());
            }
            
            //Sorts the priceValues arraylist by ascending order and gets the lowest value
            Collections.sort(priceValues);
            return priceValues.get(0);
        }
        //Gets the minimum price of the bow items
        else if(type == BOW_ITEMS && hasBowItems())
        {
            //Loops over entire list, gets prices and adds them into priceValues arraylist
            for (Bow bow : bows) {
                priceValues.add(bow.getPrice());
            }
            
            //Sorts the priceValues arraylist by ascending order and gets the lowest value
            Collections.sort(priceValues);
            return priceValues.get(0);
        }
        //Is an invalid input so returns -1
        return INVALID_INPUT;
    }
    
    /**
     * String representation of a trader object. It prints out the name and 
     * the number of each type of item which the trader is selling
     * */
    public String toString()
    {
        return "Trader "+getName()+" has "+foods.size()+" food items, "+spells.size()+" spell batches, "+swords.size()+" sword items and "+bows.size()+" bow items";
    }
}