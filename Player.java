/**
 * May 28, 2021
 * This class handles the logic and values related
 * to a single player object. It doesn't handle user
 * inputs, this is done by MyProgram.java.
 * */
import java.util.*;
public class Player
{
    //Constants related to initalizing a player object
    public static final int MAX_HEALTH = 200;
    public static final int MAX_HUNGER = 100;
    
    //Constants related to money in the game
    public static final int STARTING_MONEY_AMOUNT = 1000; //1000
    public static final double CHANGE_IN_MONEY = 0.1;
    public static final int INCREASE_IN_MONEY = 2;
    public static final int ADD_MONEY_WHEN_UNABLE = 10;
    public static final int CHANCE_TO_LOSE = 3;
    // The chance to lose money is 1/CHANCE_TO_LOSE

    //Constants related to journey
    public static final int NUMBER_LEVELS = 9;
    public static final int DISTANCE_OF_JOURNEY = 1000;
    
    //Constants related to spells
    public static final int SPELL_CAN_BE_USED = 0;
    public static final double DEFAULT_STRENGTH_RESISTANCE = 1;
    public static final double NO_REGENERATION = 0;
    
    //Constants related to healing a player outside of combat
    public static final int NO_HEAL = 50; //The maximum value of the no heal range
    public static final int SMALL_HEAL = 80; // The maximum value of the small heal range
    public static final int SMALL_HEAL_AMOUNT = 5;
    public static final int LARGE_HEAL_AMOUNT = 10;
    
    //Constants related to sickness
    public static final int NO_SICKNESS = 0;
    //Makes the player take more damage when sick
    public static final double DAMAGE_DECREASE_WHEN_SICK = 0.8;
    public static final double DAMAGE_INCREASE_OF_OPPONENT_WHEN_SICK = 1.2;
    public static final int MIN_SICKNESS_LENGTH = 1;
    public static final int MAX_SICKNESS_LENGTH = 2;
    
    //Constants related to naming
    public static final int RENAME_ITEM_TRANSPORT_NAME =0;
    public static final int RENAME_ITEM_TRANSPORT_TYPE = 1;
    public static final int RENAME_ITEM_SWORD_NAME = 2;
    public static final int RENAME_ITEM_BOW_NAME = 3;
    
    //Constants related to combat
    public static final int SWORD_CHOICE = 0;
    public static final int BOW_CHOICE = 1;
    public static final double DEFENDER_REDUCED_DAMAGE = 0.5;
    public static final int PLAYER_ATTACK = 0;
    public static final int OPPONENT_ATTACK = 1;
    
    //Variables related to a single Player object
    private int health;
    private int hunger;
    private final String name;
    private int money;
    private int range;
    private int distance;
    private int time;
    private int sicknessLength;
    
    //Objects which the player can use
    private final Transport transport;
    private final Food food;
    private Weapon sword;
    private Bow bow;
    
    //Instantes variables related to spells
    private final ArrayList<Spells> strength;
    private int lengthOfStrength;
    private double effectOfStrength;
    
    private final ArrayList<Spells> regeneration;
    private int lengthOfRegeneration;
    private double effectOfRegeneration;
    
    private final ArrayList<Spells> heal;
    
    private final ArrayList<Spells> resistance;
    private int lengthOfResistance;
    private double effectOfResistance;
    
    //Arraylist of levels
    private final ArrayList<Level> levels;
    
    //Creates a player object
    public Player(String name)
    {
        this.name = name;
        health = MAX_HEALTH;
        money = STARTING_MONEY_AMOUNT;
        hunger = MAX_HUNGER;
        range = Monster.BOTH_WEAPONS;
        distance = 0;
        time = 0;
        sicknessLength = NO_SICKNESS;
        
        transport = new Transport();
        food = new Food(0, Food.HUMAN);
        sword = new Weapon();
        bow = new Bow();
        
        strength = new ArrayList<>();
        regeneration = new ArrayList<>();
        heal = new ArrayList<>();
        resistance = new ArrayList<>();
        
        lengthOfStrength = SPELL_CAN_BE_USED;
        lengthOfRegeneration = SPELL_CAN_BE_USED;
        lengthOfResistance = SPELL_CAN_BE_USED;
        
        effectOfStrength = DEFAULT_STRENGTH_RESISTANCE;
        effectOfResistance = DEFAULT_STRENGTH_RESISTANCE;
        effectOfRegeneration = NO_REGENERATION;
        
        levels = new ArrayList<>();
        createLevels();
    }
    
    /**
     * This method creates the levels used in the game to trigger certain
     * events when a set distance has been reached
     * */
    private void createLevels()
    {
        for(int i=0; i<NUMBER_LEVELS; i++)
        {
            Level singleLevel = new Level(i+1);
            levels.add(singleLevel);
        }
    }
    
    /**
     * This method buys the chosen food item
     * @param villager The trader
     * @param index The index of the food item.
     * 
     * This method checks which type of food the user chosen,
     * adds the amount to the corresponding food object and 
     * removes the food object from the trader and
     * subtracts the cost of item from player's money
     * */
    public void buyFood(Trader villager, int index)
    {
        //Gets the useful values related to food item
        ArrayList<Food> foods = villager.getFoods();
        Food foodItem = foods.get(index);
        int amount = foodItem.getFoodAmount();
        int price = foodItem.getPrice();
        
        if(price <= money)
        {
            //Adds the food amount to the specific food object depending on type
            if(foodItem.getType() == Food.HUMAN)
            {
                food.addFood(amount);
                removeFoodAndMinusMoney(foods, index, price);
            }
            
            else if(foodItem.getType() == Food.TRANSPORT)
            {
                transport.addFood(amount);
                removeFoodAndMinusMoney(foods, index, price);
            } 
        }
    }
    
    /**
     * This method removes the corresponding food item from the
     * trader's arraylist of food and minus the price from the
     * player's money
     * */
    private void removeFoodAndMinusMoney(ArrayList<Food> foods,int index ,int price)
    {
        foods.remove(index);
        money-=price;
    }
    
    /**
     * This method buys the chosen sword item
     * @param villager The trader
     * @param index The index of the sword item.
     * 
     * Adds the sword item to the player and 
     * removes the sword object from the trader and
     * substracts the cost of item from player's money
     * */
    public void buySword(Trader villager, int index)
    {
        ArrayList<Weapon> swords = villager.getSwords();
        Weapon swordItem = swords.get(index);
        int price = swordItem.getPrice();
        
        if(price<=money)
        {
            setSword(swordItem);
            money -=price;
            swords.remove(index);
            villager.setSwordSold(true);
        }
    }
    
    /**
     * This method buys the chosen bow item
     * @param villager The trader
     * @param index The index of the bow item.
     * 
     * Adds the bow item to the player and 
     * removes the bow object from the trader and
     * substracts the cost of item from player's money
     * */
    public void buyBow(Trader villager, int index)
    {
        ArrayList<Bow> bows = villager.getBows();
        Bow bowItem = bows.get(index);
        int price = bowItem.getPrice();
        if(price<=money)
        {
            setBow(bowItem);
            money -=price;
            bows.remove(index);
            villager.setBowSold(true);
        }
    }
    
    /**
     * This method buys the chosen spell item
     * @param villager The trader
     * @param index The index of the spells item.
     * 
     * Adds the spell item to the player and 
     * removes the spell object from the trader and
     * subtracts the cost of item from player's money
     * */
    public void buySpells(Trader villager, int index)
    {
        ArrayList<Spells> spells = villager.getSpells();
        Spells singleSpell = spells.get(index);
        int price = singleSpell.getPrice();
        if(price<=money)
        {
            addSpells(singleSpell);
            spells.remove(index);
            money -=price;
        }
    }
    
    //Returns the entire arraylist of levels
    public ArrayList<Level> getLevels()
    {
        return levels;
    }
    
    //Returns a single level based on the index
    public Level getALevel(int index)
    {
        return levels.get(index);
    }
    
    //Sets the name of the transport
    public void setTransportName(String name)
    {
        transport.setName(name);
    }
    
    //Returns the transport name
    public String getTransportName()
    {
        return transport.getName();
    }
    
    //Sets the name of the transport
    public void setTransportType(String name)
    {
        transport.setType(name);
    }
    
    //Returns the transport name
    public String getTransportType()
    {
        return transport.getType();
    }
    
    /**
     * Returns true if the sword is unset,
     * meaning all the values related to this
     * sword are set to 0
     * */
    //Returns true if the sword is unset
    public boolean isSwordUnset()
    {
        return sword.isUnset();
    }
    
    //Sets the sword item to a new sword
    public void setSword(Weapon item)
    {
        sword = item;
    }
    
    //Sets the name of the sword
    public void setSwordName(String name)
    {
        sword.setName(name);
    }
    
   /**
    * This method sets the sword to the worst possible sword.
    * This method only does that when the sword is unset
    * */
    public void setSwordToWorst()
    {
        if(sword.isUnset())
        {
            setSword(new Weapon(Weapon.WORST_WEAPON)); 
        }
    }
    
    //Gets the name of the sword
    public String getSwordName()
    {
        return sword.getName();
    }
    
    /**
     * Returns true if the bow is unset.
     * Meaning all the values related to this
     * bow are set to 0
     * */
    public boolean isBowUnset()
    {
        return bow.isUnset();
    }
    
    //Sets the bow item to a new bow
    public void setBow(Bow item)
    {
        bow = item;
    }
    
    //Sets the name of the sword
    public void setBowName(String name)
    {
        bow.setName(name);
    }
    
    /**
    * This method sets the bow to the worst possible bow.
    * This method only does that when the bow is unset
    * */
    public void setBowToWorst()
    {
        if(bow.isUnset())
        {
            setBow(new Bow(Weapon.WORST_WEAPON));
        }
    }
    
    //Gets the name of the sword
    public String getBowName()
    {
        return bow.getName();
    }
    
    //Feeds the transports and transports player the distance inputted
    //LOOK AT THIS METHOD
    public void feedAndTransport(int distance)
    {
        int hunger = transport.getHunger();
        if(distance > hunger)
        {
            int difference = distance - hunger;
            /*
              The reason feedAndMove() is written twice is the feed() in
              Transport.java makes sure the amount can't exceed total hunger when
              feeding an animal
              */
            feedAndMove(hunger);
            feedAndMove(difference);
        }
        else
        {
            feedAndMove(distance);
        }
    }
    
    /**
     * Feeds animal and moves the player by the distance
     * @param distance The distance to move the player and
     * the amount to feed the animal
    * */
    private void feedAndMove(int distance)
    {
        transport.feed(distance);
        addDistance(distance);
    }
    
    /**
     * Single day in the game, this method handles all the random events which 
     * could occur, such as spoiling food, reducing the length of sickness, 
     * making the player sick, adding 1 to the time, increasing or decreasing money
     * */
    public void doSingleDay()
    {
        updateSickness();
        makeSick();
        food.spoilFood();
        transport.spoilFood();
        healDependingOnHunger();
        time++;
        updateMoney();
    }
    
    /**
     * This method increases or decreases the 
     * amount of money the player
     * 
     *  NOTE TO SELF:
     * Add in a section related to if the player has less than
     * a certain amount of money. If the player reaches 0 money 
     * this method can't change the amount anymore
     * */
    private void updateMoney()
    {
        int chance = Randomizer.nextInt(CHANCE_TO_LOSE);
        int changeInMoney = (int)(money*CHANGE_IN_MONEY);
        
        //Player loses money
        if(chance == 0 && money>0)
        {
            //If the amount of money is less than 10 and can't be changed by changeInMoney
            if(money < ADD_MONEY_WHEN_UNABLE)
            {
                changeInMoney = 1;
            }
            
            System.out.println("You lost "+changeInMoney+" money this level");
            spendMoney(changeInMoney);
        }
        //Player gains money
        else
        {
            //If the amount of money is less than 10 and can't be changed by changeInMoney
            if(money < ADD_MONEY_WHEN_UNABLE)
            {
                changeInMoney = INCREASE_IN_MONEY;
            }
            changeInMoney *=INCREASE_IN_MONEY;
            System.out.println("You gained "+changeInMoney+" money this level");
            addMoney(changeInMoney);
        }
        System.out.println("You now have "+money+" money");
    }
    
    //This method prints out if the player is sick or not with a message
    public void printSicknessStatus()
    {
        if(isSick())
        {
            System.out.println(name+", you are currently sick");
        }
        else
        {
            System.out.println(name+", you are not currently sick");
        }
    }
    
    //Reduces the length of the sickness by 1 for each day until no longer sick
    private void updateSickness()
    {
        if(isSick())
        {
            sicknessLength--;
        }
    }
    
    //Makes the player sick depending on the percentage of spoiled food
    private void makeSick()
    {
        double spoiledPercentage = food.getSpoiledPercentage();
        if(!isSick() && Randomizer.nextBoolean(spoiledPercentage))
        {
            sicknessLength = Randomizer.nextInt(MIN_SICKNESS_LENGTH, MAX_SICKNESS_LENGTH);
        }
    }
    
    /**
     * This method heals the player depending on the hunger
     * and it removes a set amount of SMALL_HEAL_AMOUNT from
     * the hunger. This incentivizes the player to keep their
     * hunger at a high value. The player can only be healed
     * when they aren't sick and their health is less than 
     * the max health
     * */
    private void healDependingOnHunger()
    {
        if(!isSick() && health<MAX_HEALTH)
        {
            /*
              The hunger is between NO_HEAL (non-inclusive) and SMALL_HEAL,
              so it adds only the small amount to their health
              */
            if(hunger >NO_HEAL && hunger <=SMALL_HEAL)
            {
                healByHunger(SMALL_HEAL_AMOUNT);
            }
            //Hunger is bigger than SMALL_HEAL, so add a large amount to their health
            else if(hunger >SMALL_HEAL)
            {
                healByHunger(LARGE_HEAL_AMOUNT);
            }
        }
    }
    
    /**
     * This method heals the player based on the hunger
     * and substracts a constants amount from the hunger.
     * @param num int. The amount of the heal
     * */
    private void healByHunger(int num)
    {
        healPlayer(num);
        hunger -=(SMALL_HEAL_AMOUNT);
    }

    //Prints the health of the player in a message
    public void printHealth()
    {
        System.out.println(name+", your health is currently "+health);
    }
    
    //Prints the health of the player when the player choses to use heal
    public void printHealth(int type)
    {
        if(type == Spells.HEAL)
        {
            printHealth();
        }
    }
    
    //Returns the health increase to the maximum
    public int getHealthToMax()
    {
        return MAX_HEALTH - health;
    }
    
    /**
     * This method performs all of the interactions during combat
     * which the player has no input over.
     * @param monster, int choice
     * */
    public void doSingleCombatStep(Monster monster, int choice)
    {
        //Initialize damages
        int damageOfPlayer =0;
        int damageOfMonster = 0;
        
        //Still needs some testing to make sure the range is accurate and
        //correct values are being found
        //Heavily nerf spells or buff monster/weapons 
        
        //Get damages depending on the choice and the range
        if(choice == BOW_CHOICE)
        {
            damageOfPlayer = (int)(bow.shoot()*effectOfStrength);
            if(range == Monster.ONLY_SWORD)
            {
                damageOfMonster = (int)(monster.getDamage(range)*effectOfResistance*DEFENDER_REDUCED_DAMAGE);
            }
        }
        else if(choice == SWORD_CHOICE)
        {
            damageOfPlayer = (int)(sword.getDamage()*effectOfStrength);
            damageOfMonster = (int)(monster.getDamage(range)*effectOfResistance*DEFENDER_REDUCED_DAMAGE);
        }
        
        //Gets the new damage if the player is sick
        //The player does less damage and the monster does more
        damageOfPlayer = setDamageWhenPlayerSick(damageOfPlayer, PLAYER_ATTACK);
        damageOfMonster = setDamageWhenPlayerSick(damageOfMonster, OPPONENT_ATTACK);
        
        //Updates the range and damages the monster
        System.out.println(name+", you dealt "+damageOfPlayer+" damage in the main attack");
        updateRangeDependingOnUserInput(choice);
        monster.takeDamage(damageOfPlayer);
        
        //Makes sure monster is alive and can deal damage to player
        if(monster.isAlive())
        {
            updateRangeBasedOnMonster(monster);
            takeDamage(damageOfMonster);
            System.out.println("The monster, "+monster.getName()+" dealt "+damageOfMonster+" damage in the counter attack");
        }
        //Makes sure the player is alive and can deal damage, and monster is alive to take damage
        if(isAlive() && monster.isAlive())
        {
            damageOfPlayer =(int)(sword.getDamage()*effectOfStrength*DEFENDER_REDUCED_DAMAGE);
            damageOfPlayer = setDamageWhenPlayerSick(damageOfPlayer, PLAYER_ATTACK);
            
            monster.takeDamage(damageOfPlayer);
            System.out.println(name+", you dealt "+damageOfPlayer+" damage in the counter attack");
        }
        //Makes sure the monster is alive and can deal damage, and player is alive to atake damage
        if(monster.isAlive() && isAlive())
        {
            damageOfMonster = (int)(monster.getDamage(range)*effectOfResistance);
            damageOfMonster = setDamageWhenPlayerSick(damageOfMonster, OPPONENT_ATTACK);
            takeDamage(damageOfMonster);
            System.out.println("The monster, "+monster.getName()+" dealth "+damageOfMonster+" damage in the main attack");
        }
        //Heals the player and reduces length of spells
        if(isAlive())
        {
            healPlayer((int)effectOfRegeneration);
            reduceTimeOfSpells();
        }
        //Print healths after the combat step
        Main.printLineBreak();
        printHealth();
        System.out.println("The monster, "+monster.getName()+"'s health is currently "+monster.getHealth());
    }
    
    /**
    * This method increases the opponent damage and decreases the player damage when the player is sick.
    * 
    * @param damage The amount of damage.
    * @param typeOfDamage PLAYER_ATTACK to reduce damage or OPPONENT_ATTACK to increase
    * damage.
    * 
    * @return damage. The increased or decreased damage.
    * */
    private int setDamageWhenPlayerSick(int damage, int typeOfDamage)
    {
        if(isSick())
        {
            if(typeOfDamage == PLAYER_ATTACK)
            {
                return (int)(damage* DAMAGE_DECREASE_WHEN_SICK);
            }
            else if(typeOfDamage == OPPONENT_ATTACK)
            {
                return (int)(damage*DAMAGE_INCREASE_OF_OPPONENT_WHEN_SICK);
            }
        }
        return damage;
    }
    
    /**
     * This method updates the range which determines what the opponent
     * can do.
     * Case 1. If the choice is a bow and the range less than the max, than add 1 to range
     * Case 2. If the choice is a sword and the range is the max, decrease range by 1
     * @param choice The choice used to determine which condition is performed above
     * */
    public void updateRangeDependingOnUserInput(int choice)
    {
        //Case 1
        if(choice == BOW_CHOICE && range < Monster.HIGHER_CHANCE_TO_USE_BOW)
        {
            range++;
        }
        //Case 2
        else if(choice == SWORD_CHOICE && range == Monster.HIGHER_CHANCE_TO_USE_BOW)
        {
            range--;
        }
        
    }
    
    //Decreases the range if the monster used a sword when not maxed range
    private void updateRangeBasedOnMonster(Monster monster)
    {
        if(monster.getRangeShouldBeDecreased())
        {
            range--;
        }
    }
    
    /**
     * Reduces the length of an active spell. Also, it
     * sets the effect value to the default value if the spell
     * is no longer active
     * */
    private void reduceTimeOfSpells()
    {
        //Deals with strength spell
        if(lengthOfStrength>SPELL_CAN_BE_USED)
        {
            lengthOfStrength--;
            
            if(lengthOfStrength == SPELL_CAN_BE_USED)
            {
                effectOfStrength = DEFAULT_STRENGTH_RESISTANCE;
            }
        }
        //Deals with resistance spell
        if(lengthOfResistance>SPELL_CAN_BE_USED)
        {
            lengthOfResistance--;
            
            if(lengthOfResistance == SPELL_CAN_BE_USED)
            {
                effectOfResistance = DEFAULT_STRENGTH_RESISTANCE;
            }
        }
        //Deals with regeneration spell
        if(lengthOfRegeneration>SPELL_CAN_BE_USED)
        {
            lengthOfRegeneration--;
            
            if(lengthOfRegeneration == SPELL_CAN_BE_USED)
            {
                effectOfRegeneration = NO_REGENERATION;
            }
        }
    }
    
    /**
     * This method sets the spells to zero so the next
     * time there is combat the user can select new
     * spells
     * */
    public void setSpellsToZero()
    {
        lengthOfStrength = SPELL_CAN_BE_USED;
        lengthOfRegeneration = SPELL_CAN_BE_USED;
        lengthOfResistance = SPELL_CAN_BE_USED;
        
        effectOfStrength = DEFAULT_STRENGTH_RESISTANCE;
        effectOfRegeneration = NO_REGENERATION;
        effectOfResistance = DEFAULT_STRENGTH_RESISTANCE;
    }
    
    /**
     * This method checks if the player has reached
     * a certain level. 
     * @param level The level we want to check (1 is the min value inputted)
     * @return boolean. Whether the player has reached the level inputted
     * */
    public boolean hasReachedLevel(int level)
    {
        return levels.get(level-1).hasBeenReached(distance);
    }
    
    //Returns if the player has reached the end of their journey
    public boolean hasFinishedJourney()
    {
        return distance >= DISTANCE_OF_JOURNEY;
    }
    
    //Gets the distance until the end
    public int getDistanceUntilEnd()
    {
        return DISTANCE_OF_JOURNEY - distance;
    }
    
    //Adds the inputted num to the distance traveled for player and transport
    public void addDistance(int num)
    {
        distance +=num;
        getTransport().transportPlayer(num);
        int hungerMinus = num/3+1;
        hunger -= hungerMinus;
    }
    
    //Returns the distance the player has traveled
    public int getDistance()
    {
        return distance;
    }
    
    //Adds time
    public void addTime(int num)
    {
        time +=num;
    }
    
    //Sets the time
    public void setTime(int num)
    {
        time = num;
    }
    
    //Gets the time
    public int getTime()
    {
        return time;
    }
    
    /**
     * This method calculates the excess distance added when the player inputs too
     * much food and the animal would normal paste the point (level or ending)
     * 
     * @param excessDistanceAdded The excess distance which the player could travel
     * @param distanceUntilPoint The distance until the next point in the game
     * 
     * @return 0 or new excessDistanceAdded value.
     * */
    public int calculateExcessDistanceAdded(int excessDistanceAdded, int distanceUntilPoint)
    {
        if(excessDistanceAdded > distanceUntilPoint)
        {
            excessDistanceAdded -= distanceUntilPoint;
            feedAndTransport(distanceUntilPoint);
            return excessDistanceAdded;
        }
        feedAndTransport(excessDistanceAdded);
        return 0;
    }
    
    //Gets whether the player is sick
    public boolean isSick()
    {
        return sicknessLength > NO_SICKNESS;
    }
    
    //Sets the length of the sickness
    public void setSicknessLength(int numDays)
    {
        if(numDays >=0)
            sicknessLength = numDays;
    }
    
    //Returns the length of the sickness
    public int getSicknessLength()
    {
        return sicknessLength;
    }
    
    //Gets the name of the player
    public String getName()
    {
        return name;
    }
    
    //Returns the range
    public int getRange()
    {
        return range;
    }
    
    //Gets health
    public int getHealth()
    {
        return health;
    }
    
    //Gets the length of the strength effect
    public int getLengthOfStrength()
    {
        return lengthOfStrength;
    }
    
    //Gets the effect of the strength spell used currently
    public double getEffectOfStrength()
    {
        return effectOfStrength;
    }
    
    //Gets the length of the regeneration effect
    public int getLengthOfRegeneration()
    {
        return lengthOfRegeneration;
    }
    
    //Gets the effect of the regeneration effect
    public double getEffectOfRegeneration()
    {
        return effectOfRegeneration;
    }
    
    //Gets the length of the resistance effect
    public int getLengthOfResistance()
    {
        return lengthOfResistance;
    }
    
    //Gets the effect of the resistance spell used currently
    public double getEffectOfResistance()
    {
        return effectOfResistance;
    }
    
    //Sets the length of strength
    public void setLengthOfStrength(int len)
    {
        lengthOfStrength = len;
    }
    
    //Sets the length of strength
    public void setLengthOfResistance(int len)
    {
        lengthOfResistance = len;
    }
    
    //Sets the length of strength
    public void setLengthOfRegeneration(int len)
    {
        lengthOfRegeneration = len;
    }
    
    //Takes damage
    public void takeDamage(int num)
    {
        if(num >0 && health >=num)
            health -=num;
        else if(num > health)
            health = 0;
    }
    
    //Heals player
    public void healPlayer(int num)
    {
        if(num > 0)
        {
            if(num + health <= MAX_HEALTH)
                health +=num;
            else
                health = MAX_HEALTH;
        }
    }
    
    //Makes sure the player is alive
    public boolean isAlive()
    {
        return health > 0;
    }
    
    //Gets money
    public int getMoney()
    {
        return money;
    }
    
    //Gets the transport
    public Transport getTransport()
    {
        return transport;
    }
    
    //Gets the food
    public Food getFood()
    {
        return food;
    }
    
    //Gets the sword
    public Weapon getSword()
    {
        return sword;
    }
    
    //Gets the bow
    public Bow getBow()
    {
        return bow;
    }
//Spell getters    
    
    //Gets the strength spells
    public ArrayList<Spells> getStrength()
    {
        return strength;
    }
    
    //Gets the strength spells
    public ArrayList<Spells> getRegeneration()
    {
        return regeneration;
    }
    
    //Gets the strength spells
    public ArrayList<Spells> getHeal()
    {
        return heal;
    }
    
    //Gets the strength spells
    public ArrayList<Spells> getResistance()
    {
        return resistance;
    }
    /**
     * This method gets the arraylist of the specific spell inputted
     * @param type The type of spell based on the constants set in
     * Spells.java.
     * @return ArrayList<Spells> depending on the inputted value
     * 
     * If a value is inputted which is out of the constants set in 
     * Spells.java, this method creates a blank ArrayList<Spells> and
     * returns it to not cause an error or accessing on improper type
     * */
    public ArrayList<Spells> getSpecificSpellsArray(int type)
    {
        if(type == Spells.STRENGTH)
        {
            return strength;
        }
        else if(type == Spells.RESISTANCE)
        {
            return resistance;
        }
        else if(type == Spells.HEAL)
        {
            return heal;
        }
        else if(type == Spells.REGENERATION)
        {
            return regeneration;
        }
        return (new ArrayList<>());
    }
    
    //Adds the spell to the corresponding ArrayList
    public void addSpells(Spells spell)
    {
        int typeOfSpell = spell.getType();
        
        if(typeOfSpell == Spells.STRENGTH)
        {
            strength.add(spell);
        }
        else if(typeOfSpell == Spells.REGENERATION)
        {
            regeneration.add(spell);
        }
        else if(typeOfSpell == Spells.HEAL)
        {
            heal.add(spell);
        }
        else if(typeOfSpell == Spells.RESISTANCE)
        {
            resistance.add(spell);
        }
    }
     
    /**
     * This method uses a single spell depending on the inputted type and 
     * index. Also, this method makes sure the spell isn't currently active before
     * using
     * @param typeOfSpell The type of spell
     * @param index The index of the spell in the ArrayList of
     * the specified type (Min value is 1)
     * */
    public void useSpells(int typeOfSpell, int index)
    {
        index--;
        //Deals with the strength spell
        if(typeOfSpell == Spells.STRENGTH && canSpecificSpellBeUsed(Spells.STRENGTH))
        {
            Spells spell = strength.get(index);
            effectOfStrength = spell.use();
            lengthOfStrength = spell.getTime();
            removeEmptySpellBatch(strength, index);
        }
        //Deals with the regeneration spell
        else if(typeOfSpell == Spells.REGENERATION && canSpecificSpellBeUsed(Spells.REGENERATION))
        {
            Spells spell = regeneration.get(index);
            effectOfRegeneration = spell.use();
            lengthOfRegeneration = spell.getTime();
            removeEmptySpellBatch(regeneration, index);
        }
        //Deals with heal spell
        else if(typeOfSpell == Spells.HEAL)
        {
            Spells spell = heal.get(index);
            healPlayer((int)spell.use());
            removeEmptySpellBatch(heal, index);
        }
        //Deals with the resistance spell
        else if(typeOfSpell == Spells.RESISTANCE && canSpecificSpellBeUsed(Spells.RESISTANCE))
        {
            Spells spell = resistance.get(index);
            effectOfResistance = spell.use();
            lengthOfResistance = spell.getTime();
            removeEmptySpellBatch(resistance, index);
        }
    }
    
    //Removes a spell from the arraylist if the spell can't be used anymore
    public void removeEmptySpellBatch(ArrayList<Spells> list, int index)
    {
        if(!list.get(index).hasSpells())
            list.remove(index);
    }
    
    /**
     * This method checks if an inputted spell type can be used based on if the
     * length of the spell is SPELL_CAN_BE_USED and the arraylist of spells of the inputted
     * type is greater than 0
     * @param typeOfSpell int. The type of spell based on constants set in Spells.java
     * @return boolean. 
     * True: if the type of spell pass all the above-mentioned checks
     * False: if the type of spell fails any of the above-mentioned checks
     */
    public boolean canSpecificSpellBeUsed(int typeOfSpell)
    {
        //Checks if strength can be used
        if(typeOfSpell == Spells.STRENGTH && lengthOfStrength == SPELL_CAN_BE_USED && strength.size()>0)
        {
            return true;
        }
        //Checks if regeneration can be used
        else if(typeOfSpell == Spells.REGENERATION && lengthOfRegeneration == SPELL_CAN_BE_USED && regeneration.size()>0)
        {
            return true;
        }
        //Checks if heal can be used
        else if(typeOfSpell == Spells.HEAL && heal.size()>0)
        {
            return true;
        }
        //Checks if resistance can be used
        return typeOfSpell == Spells.RESISTANCE && lengthOfResistance == SPELL_CAN_BE_USED && resistance.size() > 0;
        //Fails any of the checks
    }
    
    /**
     * Displays the spells based on the type inputed based on the 
     * constants set in Spells.java
     * @param type The type of spell
     * */
    public void displaySpells(int type)
    {
        //Displays strength
        if(type == Spells.STRENGTH)
        {
            displayASpellList(strength);
        }
        //Displays resistance
        else if(type == Spells.RESISTANCE)
        {
            displayASpellList(resistance);
        }
        //Displays regeneration
        else if(type == Spells.REGENERATION)
        {
            displayASpellList(regeneration);
        }
        //Displays heal
        else if(type == Spells.HEAL)
        {
            displayASpellList(heal);
        }
    }
    
    /**
     * This method is a helper method which prints out the inputted spells
     * to the screen.
     * @param list, ArrayList<Spells>, the inputted list of spells
     * */
    private void displayASpellList(ArrayList<Spells> list)
    {
        //Prints out the type of spells
        if(list.size()>0)
        {
            System.out.println("Your "+list.get(0).typeToString()+" spells: \n");
        }
        //Prints out the entire arraylist of spells
        for(int i=0; i<list.size();i++)
        {
            System.out.println((i+1)+". "+list.get(i)+"\n");
            //MyProgram.printLineBreak();
        }
    }
    
    /**
     * This method gets the length of an spell
     * which is active. If the spell isn't active it
     * returns 0. If the type inputted is a value which
     * is not defined in Spells.java, it returns -1.
     * @param type, int. The type of spell
     * @return lengthOfSpell, int. The length of a spell
     * */
    public int getLengthOfSpell(int type)
    {
        //Returns the length of the strength if active
        if(type == Spells.STRENGTH)
        {
            return lengthOfStrength;
        }
        //Returns the length of the resistance if active
        else if(type == Spells.RESISTANCE)
        {
            return lengthOfResistance;
        }
        //Returns the length of the regeneration if active
        else if(type == Spells.REGENERATION)
        {
            return lengthOfRegeneration;
        }
        //Always returns 0
        else if(type == Spells.HEAL)
        {
            return SPELL_CAN_BE_USED;
        }
        //Invalid input
        return -1;
    }
    
    //This method returns true if the player can buy an item of the type
    public boolean canBuyItems(Trader villager, int type)
    {
        int minimumAmount = villager.getMinimumPrice(type);
        return minimumAmount != Trader.INVALID_INPUT && money >= minimumAmount;
    }
    
    /** 
     * This method reduces the amount of money the player has depending on the
     * inputted amount
     * @param amount, int, The amount of money to be spent
    * */
    public void spendMoney(int amount)
    {
        if(money >= amount && amount >0)
        {
            money -=amount;
        }
    }
    
    //Returns if the player has money or not
    public boolean hasMoney()
    {
        return money>0;
    }
    
    /**
     * This method adds money to the player, if the amount is a positive number
     * @param amount, int. The amount of money
     * */
    public void addMoney(int amount)
    {
        if(amount >0)
        {
            money +=amount;
        }
    }
    
    /**
     * This method makes the player eat food based on the amount input. If the 
     * amount + hunger is less than or equal to max hunger and the amount is less
     * than or equal to the amount of food the player has. If the amount pass these
     * 2 conditions the player eats the amount.
     * @param amount, int. The amount of food the player wants to eat and adds the
     * same amount to the hunger. 
     * */
    public void eat(int amount)
    {
        if(food.canConsumeFood(amount) && canEat(amount))
        {
            food.consumeFood(amount);
            hunger +=amount;
        }
    }

    //Returns true if the amount of food can be eaten
    public boolean canEat(int amount)
    {
        return amount+hunger <=MAX_HUNGER;
    }

    //Returns the hunger
    public int getHunger()
    {
        return hunger;
    }
    
    //Sets the value of the hunger
    public void setHunger(int hunger)
    {
        this.hunger = hunger;
    }
    
    /**
     * This method returns the amount of food
     * needed make the player full
     * */
    public int getAmountUntilFull()
    {
        return MAX_HUNGER - hunger;
    }
    
    //Prints a message representing the hunger
    public void printHunger()
    {
        System.out.println(name+", you are "+hunger+" percent full");
    }
    
    //Still needs work on
    public String toString()
    {
        return "Your name is "+name+" with "+health+" health, "+hunger+" percent full and "+money+" money, with "+sword.getName()+" and "+bow.getName()+" with "+strength.size()+" strength, "+resistance.size()+" resistance, "+heal.size()+" heal, "+regeneration.size()+" regeneration spell batches";
    }
}