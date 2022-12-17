/**
 * Yarik Popov
 * ICS3U1-16, Mr. Matchen
 * May 18, 2021,
 * This class is the main class it has the run method, and it takes in user inputs.
 * Refactored
 * */
import java.util.*;

public class Main
{
    //Constants related to game not contained in any subclass
    public static final int EXIT_VALUE = -1;
    public static final int LENGTH_OF_LINE_BREAK = 30;

    private final Scanner scanner;

    public static void main(String[] args)
    {
        Main mainObject = new Main();
        System.out.println("Hello there, this is a message. Whenever you see 3 dots (...) at the end of a message press enter to continue");

//      Similar to a title screen
        mainObject.readLine("This is a resource management game called the Journey...");
        mainObject.readLine("It was created by Yarik...");

        //Loop where user plays until they exit the game
        while(true)
        {
            Player player = mainObject.initializeGame();

            mainObject.journey(player);

            printLineBreak();
            if(!mainObject.endOfJourney(player))
            {
                System.out.println("Thank you for playing");
                break;
            }
        }

        mainObject.scanner.close();
    }

    public Main()
    {
        scanner = new Scanner(System.in);
    }

    private String readLine(String s)
    {
        System.out.print(s);
        return scanner.nextLine();
    }

    public int readInt(String prompt){

        while(true){
            String input = readLine(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException ignored){

            }
        }
    }


    //Prints a line break can be useful through the game
    public static void printLineBreak()
    {
        for(int i=0; i<LENGTH_OF_LINE_BREAK; i++)
        {
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * This method asks for a yes or no answer based
     * on the given input and returns a boolean.
     * @param text The yes or no question.
     * @return boolean.
     * True if the user entered yes
     * False if the user entered no
     * */
    private boolean askForYesOrNoFromUser(String text)
    {
        while(true)
        {
            //Asks for a yes or no answer
            String input = readLine(text+" Input Yes or No: ");

            if(input.equalsIgnoreCase("yes"))
            {
                return true;
            }
            else if(input.equalsIgnoreCase("no"))
            {
                return false;
            }
            System.out.println("Please enter yes or no.");
        }
    }

    /**
     * This method prints out a message when the
     * journey is over. If they made it to the end
     * it congratulates them. If they didn't it asks
     * if they want to play again.
     * @param user The player playing
     * @return boolean. Returns true if the player
     * wants to play again
     */
    private boolean endOfJourney(Player user)
    {
        if(user.hasFinishedJourney())
        {
            System.out.println("Congratulations! You finished the game!");
        }
        else
        {
            System.out.println("Sorry, you didn't finish the game.");
        }
        return askForYesOrNoFromUser("Do you want to play again?");
    }

    /**
     * This method makes the user go onto the journey
     * @param user The player going on the journey
     * */
    private void journey(Player user)
    {
        Transport animal = user.getTransport();
        int excessDistanceAdded = 0;

        while(!user.hasFinishedJourney() && animal.getFood().hasFood() && user.getFood().hasFood())
        {
            //Gets the values required to calculate where the player is and where they should be
            int distanceOfPlayer = user.getDistance();
            int time = user.getTime();


            if(time == Player.NUMBER_LEVELS)
            {
                int distanceUntilEnd = user.getDistanceUntilEnd();

                printLineBreak();
                System.out.println("You are "+distanceUntilEnd+" away from the end of your journey.");
                eatFood(user);
                excessDistanceAdded += updateDistanceAndFeedAnimal(user, distanceUntilEnd);
                excessDistanceAdded = user.calculateExcessDistanceAdded(excessDistanceAdded, distanceUntilEnd);

            }
            else
            {
                int distanceLevel = user.getALevel(time).getDistance();
                int distanceUntilLevel = distanceLevel-distanceOfPlayer;
                //System.out.println("Absolute distance until next level: "+distanceLevel);

                //Print out distance to next level
                printLineBreak();
                System.out.println("You are "+distanceUntilLevel+" away from the "+(time+1)+" level.");


                //Does level event
                if(distanceUntilLevel == 0)
                {
                    printLineBreak();
                    //Prints out important stats related to player
                    user.printHealth();
                    user.printSicknessStatus();

                    //Does a level
                    doLevel(user);

                    //The user died
                    if(!user.isAlive())
                    {
                        System.out.println("You died");
                        break;
                    }
                    user.doSingleDay();
                    askToRemoveSpoiledFood(user);

                    /*
                      The following deals with the case of when the user inputs too much food into
                      transport and to prevent the user from passing a level
                      */
                    time = user.getTime();
                    if(time < Player.NUMBER_LEVELS)
                    {
                        distanceUntilLevel = user.getALevel(time).getDistance() - user.getDistance();

                    }
                    else if(time == Player.NUMBER_LEVELS)
                    {
                        distanceUntilLevel = user.getDistanceUntilEnd();
                    }
                    excessDistanceAdded = user.calculateExcessDistanceAdded(excessDistanceAdded, distanceUntilLevel);
                    eatFood(user);
                }
                else
                {
                    excessDistanceAdded += updateDistanceAndFeedAnimal(user, distanceUntilLevel);
                    eatFood(user);
                }
            }

        }
    }

    /**
     * This method asks for the player to eat food if they are hungry
     * @param user The player on the journey
     * */
    private void eatFood(Player user)
    {
        int hunger = user.getHunger();

        //Check if player is hunger
        if(hunger <= Player.NO_HEAL)
        {
            while(true)
            {
                //Get values used in calculating the max and min amount of food which can be eaten
                hunger = user.getHunger();
                int amountUntilNoHeal = Player.NO_HEAL - user.getHunger();
                int amountUntilFull = user.getAmountUntilFull();

                //Ask the player to eat
                printLineBreak();
                System.out.println(user.getName()+", you are "+hunger+" percent full. You must eat food and you can eat until "+amountUntilFull +" food amount and must eat a minimum of "+amountUntilNoHeal+" to continue");
                System.out.println(user.getFood());
                int amount = readInt("Enter the food amount: ");

                int foodAmount = user.getFood().getFoodAmount();

                //Player finishes all the food
                if(foodAmount < amountUntilNoHeal)
                {
                    user.eat(foodAmount);
                    System.out.println("You finished all of your food and don't have anymore food left to play the game");
                    user.printHunger();
                    break;
                }
                //Player eats food based on amount inputted
                else if(amount >0 && amount <=amountUntilFull && amount <= foodAmount && amount >=amountUntilNoHeal)
                {
                    user.eat(amount);
                    user.printHunger();
                    break;
                }
                //Prints out the amount enter is outside amountUntilNoHeal and amountUntilFull
                else if(amount < amountUntilNoHeal)
                {
                    System.out.println("You entered an amount of food less than "+amountUntilNoHeal);
                }
                else if(amount > amountUntilFull)
                {
                    System.out.println("You entered an amount of food greater than "+amountUntilFull);
                }

                System.out.println("You entered an invalid amount of food, please try again");
            }
        }
    }

    /**
     * This method updates the distance and asks/forces the player to feed if
     * necessary .
     *
     * @param user The player going on the journey
     * @param distanceUntilPoint The distance until a level or the end
     *
     * @return excessDistance. This occurs when the player gives too much food to
     * the animal, and it would make the animal go paste the point under normal
     * circumstances.
     * */
    private int updateDistanceAndFeedAnimal(Player user, int distanceUntilPoint)
    {
        Transport animal = user.getTransport();
        int distanceUntilHungry = animal.getDistanceUntilHungry();

        if(animal.isStarved())
        {
            return animalIsStarved(user, distanceUntilPoint);
        }
        else if(distanceUntilHungry < distanceUntilPoint)
        {
            hungerBeforePoint(user, distanceUntilPoint);
        }
        else
        {
            user.addDistance(distanceUntilPoint);
        }
        return 0;
    }

    /**
     * This method deals with the case of when the transport is starved
     * @param user The player on the journey
     * @param distanceUntilPoint The distance until the next level or end of
     * game.
     * @return excessDistance. This occurs when the player gives too much food to
     * the animal, and it would make the animal go paste the point under normal
     * circumstances.
     */
    private int animalIsStarved(Player user, int distanceUntilPoint)
    {
        while(true)
        {
            //Demands the user to feed the animal
            Transport animal = user.getTransport();
            printLineBreak();
            animal.printTransportWithFood();
            int foodAmount = animal.getFood().getFoodAmount();
            System.out.println("Your animal can't move anymore you must feed it");
            int amount = readInt("Enter the amount of food you want to feed: ");

            //Feeds the animal
            if(amount >0 && amount <= Transport.MAX_HUNGER && amount <= foodAmount)
            {
                if(amount > distanceUntilPoint)
                {
                    user.feedAndTransport(distanceUntilPoint);
                    return (amount - distanceUntilPoint);
                }
                else
                {
                    user.feedAndTransport(amount);
                    break;
                }
            }
            //The value was invalid
            else
            {
                System.out.println("You enter an amount that is 0 or negative or that is too high");
            }
        }
        return 0;
    }

    /**
     * This method deals with the case of when the transport will be hungry
     * before reaching a level or the end of the game
     *
     * @param user The player on the journey
     * @param distanceUntilPoint The distance until the next level or end of
     * game.
     */
    private void hungerBeforePoint(Player user, int distanceUntilPoint)
    {
        while(true)
        {
            //Ask the user to feed the animal
            Transport animal = user.getTransport();
            printLineBreak();
            animal.printTransportWithFood();
            System.out.println("You can input 0 to move until the transport can't move anymore or until the next level or end, which ever is faster");

            int foodAmount = animal.getFood().getFoodAmount();
            int amount = readInt("Enter the amount of food you want to feed: ");


            if(amount >=0)
            {
                //Player choose to feed their animal
                if(amount <= distanceUntilPoint && amount >0 && amount <= Transport.MAX_HUNGER && amount <= foodAmount)
                {
                    //Possibly change (amount <= distanceUntilPoint) to something else
                    user.feedAndTransport(amount);
                }
                else
                {
                    int hunger = animal.getHunger();

                    //Animal moves until they reach the next level
                    //Animal moves until it can't move anymore
                    //Switched to Math.min() from
                    user.addDistance(Math.min(hunger, distanceUntilPoint));
                }
                break;
            }
        }
    }

    /**
     * This method asks the user if they want to remove the spoiled food
     * @param user The player going on the journey
     * */
    private void askToRemoveSpoiledFood(Player user)
    {
        //Gets food objects
        Food userFood = user.getFood();
        Food transportFood = user.getTransport().getFood();

        //Makes sure a food object has spoiled food
        if(userFood.hasSpoiledFood() || transportFood.hasSpoiledFood())
        {
            //Print out foods
            printLineBreak();
            System.out.println(userFood);
            System.out.println(transportFood);

            //Ask the user if they want to remove food
            boolean toRemoveSpoiledFood = askForYesOrNoFromUser("Do you want to remove the spoiled food?");

            //Removes the spoiled food if user agrees
            if(toRemoveSpoiledFood)
            {
                userFood.removeSpoiledFood();
                transportFood.removeSpoiledFood();
            }
        }

    }

    /**
     * This method does a single level depending on the day
     * @param user The player doing a level
     * */
    private void doLevel(Player user)
    {
        //Get important values
        int time = user.getTime();
        Level currentLevel = user.getALevel(time);
        int currentLevelType = currentLevel.getType();

        //Print out the current level message
        System.out.println(currentLevel);


        //Does the interact with npc event
        if(currentLevelType == Level.INTERACTION_NPC)
        {
            printLineBreak();
            Npc villager = new Npc();
            if(villager.getTypeOfNpc() == Npc.ASK)
            {
                askForItems(user, villager);
            }
            else
            {
                villager.affectPlayerResources(user);
            }
        }
        //Makes the player fight a monster
        else if(currentLevelType == Level.MONSTER_FIGHT)
        {
            printLineBreak();
            fight(user, new Monster());
        }
        //Makes the player interact with the trader
        else if(currentLevelType == Level.TRADER)
        {
            tradeItems(user);
        }
    }

    /**
     * This method makes the player fight a monster.
     * @param user Player object.
     * @param monster Monster object.
     * */
    private void fight(Player user, Monster monster)
    {
        //Print the monster and both your weapons

        System.out.println(monster);
        System.out.println();
        System.out.println("Your weapons:");
        System.out.println(user.getSword());
        System.out.println(user.getBow());

        //Loops until one of them is not alive anymore
        while(user.isAlive() && monster.isAlive())
        {
            //Asks the user for spell use and choice of weapon
            askForSpellUse(user);
            int choice = askForWeaponUse(user);

            //Updates the values related to combat
            user.doSingleCombatStep(monster, choice);
        }
        //Sets the values related to spells to 0 to be used during next fight
        user.setSpellsToZero();
        printLineBreak();
    }

    /**
     * This method makes sure the user inputs the correct choice for the use of a
     * weapon
     * @param user The player fighting
     */
    private int askForWeaponUse(Player user)
    {
        while(true)
        {
            //Asks for choice of weapon
            printLineBreak();
            System.out.println("You have "+user.getBow().getArrows()+" arrows");
            int choice = readInt("Enter 0 to use a sword or 1 to use a bow: ");

            //User enter 0 or 1 for their choice
            if(choice == Player.SWORD_CHOICE)
            {
                System.out.println("You choose to use your "+user.getSwordName());
                printLineBreak();
                return choice;
            }
            else if(choice == Player.BOW_CHOICE)
            {
                System.out.println("You choose to use your "+user.getBowName());
                printLineBreak();
                return choice;
            }
        }
    }

    /**
     * This method asks the user which type of spell
     * they want to use when fighting a monster.
     * It prints out the spells of that type and the user
     * selects which spell to use. This method only prints
     * out the type of spell if the player can use that spell
     * type. For example, can't have 2 strength spells active
     * at the same time.
     * @param user The player fighting
     * */
    private void askForSpellUse(Player user)
    {
        while(true)
        {
            //Ask for spell choice
            printLineBreak();
            System.out.println("Enter 0 for Strength spells, 1 for Regeneration spells, 2 for Heal spells, 3 for Resistance spells or "+EXIT_VALUE+" to exit.");
            int selection = readInt("Enter the type of spell you want to use here: ");

            //User choose to exit
            if(selection == EXIT_VALUE)
            {
                System.out.println("You chose not to use a spell or you finished with your selection");
                break;
            }
            //User choose to use a strength spell
            if(selection == Spells.STRENGTH)
            {
                askForSpecificSpell(user, Spells.STRENGTH);
            }
            //User choose to use a resistance spell
            else if(selection == Spells.RESISTANCE)
            {
                askForSpecificSpell(user, Spells.RESISTANCE);
            }
            //User choose to use a heal spell
            else if(selection == Spells.HEAL)
            {
                askForSpecificSpell(user, Spells.HEAL);
            }
            //User choose to use a regeneration spell
            else if(selection == Spells.REGENERATION)
            {
                askForSpecificSpell(user, Spells.REGENERATION);
            }
        }
    }

    /**
     * This method helps askForSpellUse() method by asking for a specific spell from the
     * given type of spells.
     * @param user The player object
     * @param type which type of spell they want
     * */
    private void askForSpecificSpell(Player user, int type)
    {
        ArrayList<Spells> spells = user.getSpecificSpellsArray(type);

        if(user.canSpecificSpellBeUsed(type))
        {
            while(true)
            {
                printLineBreak();
                //Prints the health if the type of spell selected
                user.printHealth(type);
                if(type == Spells.HEAL)
                {
                    System.out.println("You can heal by "+user.getHealthToMax()+" to get to full health");
                }

                //Prints out the spells and asks for the choice
                user.displaySpells(type);
                int num = readInt("Input your choice starting from 1 to "+spells.size()+" or "+EXIT_VALUE+" to use a different spell: ");

                //User chose to exit
                if(num == EXIT_VALUE)
                {
                    break;
                }
                //They chose a valid number in the range
                if(num >0 && num <= spells.size())
                {
                    user.useSpells(type, num);
                    user.printHealth(type);
                    break;
                }
            }
        }
        else
        {
            printLineBreak();
            int length = user.getLengthOfSpell(type);
            //Prints out the length of the current active spell
            if(length > 0)
            {
                System.out.println("This spell is already in use");
                System.out.println("Your "+Spells.typeOfSpellToString(type)+" has "+length+" uses left");

            }
            //Tells the user the
            else
            {
                System.out.println("You don't have any spells of this type");
            }
        }
    }

    /** NEEDS MORE INSTRUCTIONS
     This method begins the game. The game asks for a name and
     creates a player object. The player chooses the resources
     from the trader.
     @return player. Player object which has everything purchased
     */
    private Player initializeGame()
    {
        //Asks for name and creates player object with that name
        String name = readLine("Enter your name, traveler: ");
        Player player = new Player(name);
        player.getFood().setName(name);

        //Insturctions
        readLine("On your journey, you will need many different and useful items...");
        readLine("You should try to buy as many of each item as possible...");
        readLine("Food is very important on your journey, because without it you won't get far...");
        readLine("Also, a good sword and bow are very important for your survival as there will be foes on this journey...");
        readLine("Spells are also incrediably valuable, because they give a strong advantage to you when fighting...");
        readLine("When a trader is selling an item, the more expensive an item, the better that item is...");
        readLine("Here you can buy useful items, you will need on your journey...");

        //Trades for items
        tradeItems(player);

        //Renames transport, sword and bow items
        renameAllItems(player);

        return player;
    }

    /**
     * This method makes the player rename every item the player has: Transport, Sword and Bow.
     * For the transport, this method asks to change the name and type.
     * Their is no return type, because an object is an mutable type.
     * */
    private void renameAllItems(Player user)
    {
        readLine("Along your journey an animal will help you...");

        //Tells player to rename the name of their animal
        String nameTransport = renameSingleItem("Rename your animal. Enter a new name of your animal: ", Player.RENAME_ITEM_TRANSPORT_NAME, user);
        user.setTransportName(nameTransport);

        user.getTransport().getFood().setName(nameTransport);

        //Tells player to rename the type of their animal
        String typeTransport = renameSingleItem("Enter the type or species which you want your animal to be: ", Player.RENAME_ITEM_TRANSPORT_TYPE, user);
        user.setTransportType(typeTransport);
        System.out.println(user.getTransport());

        readLine("All travelers like to name their weapons, you should too...");

        //Tells player to rename the sword
        String nameSword = renameSingleItem("Rename your sword: Enter a new name for your sword: ", Player.RENAME_ITEM_SWORD_NAME, user);
        user.setSwordName(nameSword);
        System.out.println(user.getSword());

        //Tells player to rename the bow
        String bowName = renameSingleItem("Rename your bow: Enter a new name for your bow: ", Player.RENAME_ITEM_BOW_NAME, user);
        user.setBowName(bowName);
        System.out.println(user.getBow());
    }

    /**
     * This method helps renameAllItems method by renaming a
     * single item. It makes sure the player can keep the
     * original name of the item
     * */
    private String renameSingleItem(String text, int type, Player user)
    {
        //Asks for a new name or to press enter
        printLineBreak();
        System.out.println("You can press enter to keep the original name");
        String renameText = readLine(text);

        //User pressed enter and the name wasn't changed
        if(renameText.equals(""))
        {
            if(type == Player.RENAME_ITEM_TRANSPORT_NAME)
            {
                return user.getTransportName();
            }
            else if(type == Player.RENAME_ITEM_TRANSPORT_TYPE)
            {
                return user.getTransportType();
            }
            else if(type == Player.RENAME_ITEM_SWORD_NAME)
            {
                return user.getSwordName();
            }
            else if(type == Player.RENAME_ITEM_BOW_NAME)
            {
                return user.getBowName();
            }
        }
        //Name was changed
        return renameText;
    }

    /**
     * This method makes an Npc ask for resources from the player.
     * The player can refuse to give the Npc resources until the Npc gives up.
     * @param user Player. The player object playing
     * @param villager Npc which is asking for resources
     * */
    private void askForItems(Player user, Npc villager)
    {
        while(villager.isPersist())
        {
            //Asks the player if they want to give resources
            boolean input = askForYesOrNoFromUser("Do you want to give a random resources to "+villager.getName()+"?");

            //The player choose to give
            if(input)
            {
                villager.playerChoseToGive(user);
                break;
            }

            //The player didn't choose to give
            villager.updatePersistence();
        }
    }

    /**
     * The method to trade items with user.
     * @param user The player object.
     * */
    private void tradeItems(Player user)
    {
        Trader villager = new Trader();
        while(user.hasMoney())
        {
            printLineBreak();
            //Makes sure the player can purchase items
            if(!villager.hasFoodItems() && !villager.hasSpellsItems() && villager.hasSwordSold() && villager.hasBowSold())
            {
                System.out.println("Trader "+villager.getName()+ " has no items available for purchase");
                break;
            }
            //Exits player if they don't have enough money for anything
            else if(!user.canBuyItems(villager, Trader.FOOD_ITEMS) && !user.canBuyItems(villager, Trader.SPELLS_ITEMS) && !user.canBuyItems(villager, Trader.SWORD_ITEMS) && !user.canBuyItems(villager, Trader.BOW_ITEMS))
            {
                System.out.println("You can't afford to buy any items");
                break;
            }

            //Prints out user's money, trader and asks for which type of item they want to buy
            System.out.println("You have "+user.getMoney()+" money");
            System.out.println(villager);
            System.out.println("Enter 0 for Food, 1 for Spells, 2 for Swords, 3 for Bows or "+EXIT_VALUE+" to exit");
            int selection = readInt("Enter the type of item: ");

            //Player chooses to leave
            if(selection == EXIT_VALUE)
            {
                break;
            }
            //Player chose to trade for food
            else if(selection == Trader.FOOD_ITEMS)
            {
                tradeFood(villager, user);
                //Make into method
                if(!villager.hasFoodItems())
                {
                    System.out.println(villager.getName()+" has no food items");
                }
                else
                {
                    System.out.println("You finished trading food items");
                }
            }
            //Player chose to trade for spells
            else if(selection == Trader.SPELLS_ITEMS)
            {
                tradeSpells(villager, user);
                if(!villager.hasSpellsItems())
                {
                    System.out.println(villager.getName()+" has no spell items");
                }
                else
                {
                    System.out.println("You finished trading spell items");
                }
            }
            //Player chose to trade for sword
            else if(selection == Trader.SWORD_ITEMS)
            {
                if(villager.hasSwordSold())
                {
                    System.out.println("You already bought a sword");
                }
                else
                {
                    tradeSword(villager, user);
                    System.out.println("You finished trading for a sword");
                }
            }
            //Player chose to trade for bow
            else if(selection == Trader.BOW_ITEMS)
            {
                if(villager.hasBowSold())
                {
                    System.out.println("You already bought a bow");
                }
                else
                {
                    tradeBow(villager, user);
                    System.out.println("You finished trading for a bow");
                }
            }
        }
        //Sets the weapons to worst if they are unset
        user.setSwordToWorst();
        user.setBowToWorst();

        //Prints out weapons
        printLineBreak();
        System.out.println(user.getSword());
        System.out.println();
        System.out.println(user.getBow());
    }

    /**
     * This method has the player trade food items.
     * @param villager, Trader. The trader trading items.
     * @param user, Player. The player buying items
     * */
    private void tradeFood(Trader villager, Player user)
    {
        while(villager.hasFoodItems())
        {
            //Ask for selection
            int selection = displayItemsAndGetUserInput(user, villager, Trader.FOOD_ITEMS);

            //Player chooses to exit
            if(selection == EXIT_VALUE)
            {
                break;
            }

            //Buys item
            selection--;
            user.buyFood(villager, selection);

            //Print food objects
            printLineBreak();
            System.out.println("Your food item: "+user.getFood());
            printLineBreak();
            System.out.println("Your transport's food item: "+user.getTransport().getFood());
        }
    }

    /**
     * This method has the player trade spells items.
     * @param villager Trader. The trader trading items.
     * @param user Player. The player buying items
     * */
    private void tradeSpells(Trader villager, Player user)
    {
        while(villager.hasSpellsItems())
        {
            //Ask for selection
            int selection = displayItemsAndGetUserInput(user, villager, Trader.SPELLS_ITEMS);

            //Player chooses to exit
            if(selection == EXIT_VALUE)
            {
                break;
            }

            //Buys item
            selection--;
            user.buySpells(villager, selection);

            //Print out the users spells
            printLineBreak();
            user.displaySpells(Spells.STRENGTH);
            user.displaySpells(Spells.REGENERATION);
            user.displaySpells(Spells.HEAL);
            user.displaySpells(Spells.RESISTANCE);
        }
    }

    /**
     * This method has the player trade sword items.
     * Buys 1 sword item. Can't have more than 1 sword
     * @param villager Trader. The trader trading items.
     * @param user Player. The player buying items
     * */
    private void tradeSword(Trader villager, Player user)
    {
        while(!villager.hasSwordSold())
        {
            //Prints out the current sword if it isn't unset
            if(!user.isSwordUnset())
            {
                System.out.println("Your current sword: "+user.getSword());
            }

            //Ask for selection
            int selection = displayItemsAndGetUserInput(user, villager, Trader.SWORD_ITEMS);

            //Player choose to exit
            if(selection == EXIT_VALUE)
            {
                //The user doesn't have enough money to buy anything, so the trade gave worst weapon
                if(!user.canBuyItems(villager, Trader.SWORD_ITEMS))
                {
                    System.out.println("You don't have enough money to buy any sword. So the trader gave you their worst sword.");
                    user.setSwordToWorst();
                }

                //Prints sword and exists
                System.out.println("Your sword: "+user.getSword());
                break;
            }

            //Buys and prints sword
            selection--;
            user.buySword(villager, selection);
            System.out.println("Your sword: "+user.getSword());
        }

    }

    /**
     * This method has the player trade food items.
     * Buys 1 bow item. Can't have more than 1 bow
     * @param villager Trader. The trader trading items.
     * @param user Player. The player buying items
     * */
    private void tradeBow(Trader villager, Player user)
    {
        while(!villager.hasBowSold())
        {
            //Prints out the current bow if it isn't unset
            if(!user.isBowUnset())
            {
                System.out.println("Your current bow: "+user.getBow());
            }

            //Ask for selection
            int selection = displayItemsAndGetUserInput(user, villager, Trader.BOW_ITEMS);

            //Player chooses to exit
            if(selection == EXIT_VALUE)
            {
                //The user doesn't have enough money to buy anything, so the trade gave worst weapon
                if(!user.canBuyItems(villager, Trader.BOW_ITEMS))
                {
                    System.out.println("You don't have enough money to buy any bow. So the trader gave you their worst bow.");
                    user.setBowToWorst();
                }

                //Prints bow and exists
                System.out.println("Your bow: "+user.getBow());
                break;
            }

            //Buys and print bow
            selection--;
            user.buyBow(villager, selection);
            System.out.println("Your bow: "+user.getBow());
        }
    }

    /**
     * This method makes sure the user inputs the a valid input for the corresponding trade.
     * This method is used in every type of trade
     * @param user Player object related to the trading user
     * @param villager Trader object
     * @param type int. The type of object
     * */
    private int displayItemsAndGetUserInput(Player user, Trader villager, int type)
    {
        while(true)
        {
            //Display items, player's money and ask for input
            printLineBreak();
            villager.displayItems(type);
            System.out.println("You currently have "+user.getMoney()+" dollars");
            int selection = readInt("Input the corresponding number related to the item you wish to purchase (Input "+EXIT_VALUE+" to exit): ");

            //Returns input in correct range
            if(selection== EXIT_VALUE || (selection <=villager.getNumberOfItems(type) && selection >=1))
            {
                return selection;
            }
            System.out.println("You entered a invalid value, please try again");
        }
    }

}
