/*
* FlashCards.java
* Phillip Ball
* 05/29/2020 - 06/04/2020
* This program creates, edits, deletes, displays, saves, and loads virtual flashcards.
* This program requires another .java file named Card.java, said file gives the attributes of the flashcards.
* All the cards are stored in a txt file that this program will create, this program will also create then update
* a backup of said txt file every other time the program is exited via the exit button on the main menu.
*/

// imports
import java.io.*; // used for saving and loading
import java.util.ArrayList; // used to store all the cards
import java.util.Collections; // used to shuffle the study deck
import javax.swing.*; // used to display the program 

public class FlashCards{
   // fields
   // variable for the file that stores all the cards AKA the deck
   private static String theDeck = "theDeck.txt";
   private static String theDeckBackup = "theDeckBackup.txt";
   
   // methods
   // saves the current card to the deck
   public static void save(ArrayList<Card> card, String allTheDecks){ // save(new card, place to save)
      // Serialization "saving"
      try{    
         //Saving of object in a file 
         FileOutputStream file = new FileOutputStream(allTheDecks); 
         ObjectOutputStream out = new ObjectOutputStream(file); 
           
         // Method for serialization of object 
         out.writeObject(card);  
         
         // remember to close
         out.close();
         file.close();
           
         //System.out.println("Object has been serialized"); 
           
      }catch(IOException ex){ 
         System.out.println("IOException is caught"); 
         System.out.println(ex);
      }
   }
   
   // loads the deck (all of the cards)
   public static ArrayList<Card> load(String allTheDecks){
      // Deserialization "loading"
      try{    
         File fileCheck = new File(allTheDecks);
         if(!fileCheck.exists()){
            return new ArrayList<Card>();
         }
         // Reading the object from a file 
         FileInputStream file = new FileInputStream(allTheDecks); 
         ObjectInputStream in = new ObjectInputStream(file); 
           
         // Method for deserialization of object 
         ArrayList<Card> deck = (ArrayList<Card>)in.readObject();
           
         // remember to close
         in.close(); 
         file.close(); 
           
         //System.out.println("Object has been deserialized "); 
         //System.out.println(deck); 
         return deck;
         
      }catch(IOException ex){ 
         System.out.println("IOException is caught"); 
         System.out.println(ex);
      }catch(ClassNotFoundException ex){ 
         System.out.println("ClassNotFoundException is caught"); 
         System.out.println(ex);
      }
      return null; // useless (hopefully)
   }
   
   // creates a new card
      // load all cards, add new card, save all cards.
   public static void newCard(String side1, String side2, String example, String tag){
      // creates the new card
      Card newCard;
      if(example.equals("")){
         newCard = new Card(side1, side2, "", tag.toLowerCase());
      }
      else{
         newCard = new Card(side1, side2, example, tag.toLowerCase());
      }

      ArrayList<Card> deckArray = load(theDeck); // loads the current deckArray
      deckArray.add(newCard); // updates the deckArray with the new card
      save(deckArray, theDeck); // overwrites theDeck with the updated deckArray
   }

   // view cards by tag
      // will show a card on side 1, with buttons for flip card, next card and exit
      // the "deck will be shuffled."
   public static ArrayList<Card> chosenTag(String tag){
      ArrayList<Card> deckArray = load(theDeck); // loads theDeck
      // checks if the tag chosen is in the deck or not
      boolean tagFound = false;
      for(int i = 0; i < deckArray.size(); i++){
         if(deckArray.get(i).getTag().equals(tag.toLowerCase())){
            tagFound = true;
            break;
         }
      }
      if(!tagFound){
         JOptionPane.showMessageDialog(null, "That tag does not exist");
         mainMenu();
         JOptionPane.getRootFrame().dispose(); // closes the window after opening the new one
         return new ArrayList<Card>(); // should never reach this code
      }
      // sets up an array for the cards with the chosen tag
      ArrayList<Card> studyDeck = new ArrayList<Card>();
      // goes through all the cards in theDeck and copies each card with the chosen tag into the studyDeck
      for(int i = 0; i < deckArray.size(); i++){
         if(deckArray.get(i).getTag().equals(tag.toLowerCase())){ // looks for cards with the chosen tag
            studyDeck.add(deckArray.get(i)); // checks to see if there's already a card in the random spot
         }
      }
      return studyDeck;
   }

   // looks for a specific card
      // searches for side1 and the tag
   public static boolean searchCard(String side1, String tag){
      ArrayList<Card> deckArray = load(theDeck);
      // goes through all the cards in theDeck until it finds the card thats being looked for
      for(int i = 0; i < deckArray.size(); i++){
         if(deckArray.get(i).getTag().equals(tag.toLowerCase()) && deckArray.get(i).getSide1().equals(side1.toLowerCase())){
            return true;
         }
      }
      return false;
   }

   // deletes a chosen card
      // uses "looks for a specific card" method to find the chosen card
   public static void deleteCard(String side1, String tag){
      ArrayList<Card> deckArray = load(theDeck);
      // goes through all the cards in theDeck until it finds the card thats being deleted
      for(int i = 0; i < deckArray.size(); i++){
         if(deckArray.get(i).getTag().equals(tag.toLowerCase()) && deckArray.get(i).getSide1().equals(side1.toLowerCase())){
            deckArray.remove(i);
            save(deckArray, theDeck); // overwrites theDeck with the updated deckArray
         }
      }
   }

   // views a chosen card
      // uses "looks for a specific card" method to find the chosen card
   public static void viewCard(){
      // sets up text fields for the users input
      JTextField side1 = new JTextField(10);
      JTextField tag = new JTextField(10);
      // adds the text fields to JOptionPane and labels them
      JPanel myPanel = new JPanel();
      myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS)); // displays the input fields set up by JPanel vertically
      myPanel.add(new JLabel("Please enter the information of the card you want to search for"));
      myPanel.add(new JLabel("Side 1: "));
      myPanel.add(side1);
      myPanel.add(new JLabel("Tag: "));
      myPanel.add(tag);
      // makes an array of objects that will be used as buttons for the menu
      Object[] options = {"Search", "Cancel"};
      Object[] options2 = {"Search again", "Edit", "Delete", "Exit"};
      Object[] options3 = {"Yes", "No"};
      // asks the user to confirm the card
      int input = JOptionPane.showOptionDialog(null, myPanel, "Searching for a card", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
      if(input == 0){
         if(searchCard(side1.getText(), tag.getText())){
            ArrayList<Card> deckArray = load(theDeck);
            // goes through all the cards in theDeck until it finds the card thats being deleted
            for(int i = 0; i < deckArray.size(); i++){
               if(deckArray.get(i).getTag().equals(tag.getText().toLowerCase()) && deckArray.get(i).getSide1().equals(side1.getText().toLowerCase())){
                  int input2 = JOptionPane.showOptionDialog(null, "Here is the card you were looking for \n" + deckArray.get(i), "Viewing a card", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options2, options2[3]);
                  if(input2 == 0){ // search again
                     viewCard();
                     JOptionPane.getRootFrame().dispose(); // closes the window after opening the new one
                  }
                  else if(input2 == 1){ // edit
                     editCard(side1.getText(), tag.getText());
                     JOptionPane.showMessageDialog(null, "The card has been edited", "Editing a card", JOptionPane.INFORMATION_MESSAGE);
                     mainMenu();
                     JOptionPane.getRootFrame().dispose(); // closes the window after opening the new one
                  }
                  else if(input2 == 2){ // delete
                     int input3 = JOptionPane.showOptionDialog(null, "Are you sure you want to delete this card?\n" + deckArray.get(i), "Deleting a card", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options3, options3[1]);
                     if(input3 == 0){
                        deleteCard(side1.getText(), tag.getText());
                        JOptionPane.showMessageDialog(null, "The card has been deleted", "Deleting a card", JOptionPane.INFORMATION_MESSAGE);
                        mainMenu();
                        JOptionPane.getRootFrame().dispose(); // closes the window after opening the new one
                     }
                     else{ // exit
                        mainMenu();
                        JOptionPane.getRootFrame().dispose(); // closes the window after opening the new one
                     }
                  }
                  else{
                     mainMenu();
                     JOptionPane.getRootFrame().dispose(); // closes the window after opening the new one
                  }
               }
            }
         }
         else{
            JOptionPane.showMessageDialog(null, "This card does not exist", "Searching for a card", JOptionPane.ERROR_MESSAGE);
            mainMenu();
            JOptionPane.getRootFrame().dispose(); // closes the window after opening the new one
         }
      }
      else{
         mainMenu();
         JOptionPane.getRootFrame().dispose(); // closes the window after opening the new one
      }
   }

   // edits a chosen card
      // edit name, side1, side2, example, tag
   public static void editCard(String oldSide1, String oldTag){
      ArrayList<Card> deckArray = load(theDeck);
      Object[] options = {"Edit", "Cancel"};
      String oldSide2 = "";
      String oldExample = "";
      // goes through all the cards in theDeck until it finds the card thats being editing
      for(int i2 = 0; i2 < deckArray.size(); i2++){
         if(deckArray.get(i2).getTag().equals(oldTag.toLowerCase()) && deckArray.get(i2).getSide1().equals(oldSide1.toLowerCase())){
            oldSide2 = deckArray.get(i2).getSide2();
            oldExample = deckArray.get(i2).getExample();
            break;
         }
      }
      // sets up text fields for the users input
      JTextField side1 = new JTextField(oldSide1, 10);
      JTextField side2 = new JTextField(oldSide2, 10);
      JTextField example = new JTextField(oldExample, 10);
      JTextField tag = new JTextField(oldTag, 10);
      // adds the text fields to JOptionPane and labels them
      JPanel myPanel = new JPanel();
      myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS)); // displays the input fields set up by JPanel
      myPanel.add(new JLabel("Enter the new information"));
      myPanel.add(new JLabel("Side 1: "));
      myPanel.add(side1);
      myPanel.add(new JLabel("Side 2: "));
      myPanel.add(side2);
      myPanel.add(new JLabel("Example: "));
      myPanel.add(example);
      myPanel.add(new JLabel("Tag: "));
      myPanel.add(tag);
      int input = JOptionPane.showOptionDialog(null, myPanel, "Editing a card", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
      if(input == 0){
         // goes through all the cards in theDeck until it finds the card thats being editing
         for(int i = 0; i < deckArray.size(); i++){
            if(deckArray.get(i).getTag().equals(oldTag.toLowerCase()) && deckArray.get(i).getSide1().equals(oldSide1.toLowerCase())){
               deleteCard(oldSide1, oldTag);
               if(side1.getText().equals("") || side1.getText() == null){
                  side1 = new JTextField(oldSide1);
               }
               if(side2.getText().equals("") || side2.getText() == null){
                  side2 = new JTextField(oldSide2);
               }
               if(example.getText().equals("") || example.getText() == null){
                  example = new JTextField(oldExample);
               }
               if(tag.getText().equals("") || tag.getText() == null){
                  tag = new JTextField(oldTag);
               }
               newCard(side1.getText(), side2.getText(), example.getText(), tag.getText());
            }
         }
      }
      else{
         mainMenu();
         JOptionPane.getRootFrame().dispose(); // closes the window after opening the new one
      }
   }

   // uses a card to check if there needs to be a backup made
   // will backup every other time the program is closed via the exit button
   public static void backup(){
      ArrayList<Card> deckArray = load(theDeck);
         if(searchCard("backup", "uawbfuoabofkascjopj102891y5")){
            for(int i = 0; i < deckArray.size(); i++){
               if(deckArray.get(i).getTag().equals("uawbfuoabofkascjopj102891y5".toLowerCase()) && deckArray.get(i).getSide1().equals("backup".toLowerCase())){
                  deleteCard("backup", "uawbfuoabofkascjopj102891y5");
                  save(deckArray, theDeckBackup);
               }
            }
         }
         else{
            newCard("backup", "just in case", "i'd be surprised if you found this", "uawbfuoabofkascjopj102891y5");
         }
   }

   // The main menu, the hub to get to all the other options
   public static void mainMenu(){
      // makes an array of objects that will be used as buttons for the menu
      Object[] options = {"Study", "Create", "View/Edit/Delete", "Exit"};
      // receives input from the user via button they push
      int userInput = JOptionPane.showOptionDialog(null, "Please select an option", "FlashCards", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[3]);
      // uses the users input to make the program do as requested
      if(userInput == 0){ // study
         studyMenu();
         JOptionPane.getRootFrame().dispose(); // closes the window after opening the new one
      }
      else if(userInput == 1){ // create
         newCardMenu();
         JOptionPane.getRootFrame().dispose(); // closes the window after opening the new one
      }
      else if(userInput == 2){ // view/edit/delete
         viewCard();
         JOptionPane.getRootFrame().dispose(); // closes the window after opening the new one
      }
      else{ // exit
         backup();
         System.exit(0);
      }
   }

   // menu for creating a new card
   public static void newCardMenu(){
      // sets up text fields for the users input
      JTextField side1 = new JTextField(10);
      JTextField side2 = new JTextField(10);
      JTextField example = new JTextField(10);
      JTextField tag = new JTextField(10);
      // adds the text fields to JOptionPane and labels them
      JPanel myPanel = new JPanel();
      myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS)); // displays the input fields set up by JPanel
      myPanel.add(new JLabel("Side 1: "));
      myPanel.add(side1);
      myPanel.add(new JLabel("Side 2: "));
      myPanel.add(side2);
      myPanel.add(new JLabel("Example: "));
      myPanel.add(example);
      myPanel.add(new JLabel("Tag: "));
      myPanel.add(tag);
      // asks the user to confirm the new card
      Object[] options = {"Create", "Cancel"};
      int input = JOptionPane.showOptionDialog(null, myPanel, "Creating a new card", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
      if(input == 0){
         if(searchCard(side1.getText(), tag.getText())){
            JOptionPane.showMessageDialog(null, "This card is already in the deck", "Creating a new card", JOptionPane.ERROR_MESSAGE);
            mainMenu();
            JOptionPane.getRootFrame().dispose(); // closes the window after opening the new one
         }
         else{
            if(tag.getText() == null || tag.getText().equals("")){
               JOptionPane.showMessageDialog(null, "This cards tag is not valid", "Creating a new card", JOptionPane.ERROR_MESSAGE);
               mainMenu();
               JOptionPane.getRootFrame().dispose(); // closes the window after opening the new one
            }
            else{
               newCard(side1.getText().toLowerCase(), side2.getText().toLowerCase(), example.getText().toLowerCase(), tag.getText().toLowerCase());
               JOptionPane.showMessageDialog(null, "The card has been created", "Creating a new card", JOptionPane.INFORMATION_MESSAGE);
               mainMenu();
               JOptionPane.getRootFrame().dispose(); // closes the window after opening the new one
            }
         }
      }
      else{
         mainMenu();
         JOptionPane.getRootFrame().dispose(); // closes the window after opening the new one
      }
   }

   // menu for studying
   public static void studyMenu(){
      // makes an array of objects that will be used as buttons for the menu
      Object[] options = {"Study", "Cancel"};
      Object[] options2 = {"Flip", "Next", "Exit"};
      // sets up a text field for the users input as well as adds it to the jpanel with a label
      JTextField tag = new JTextField(10);
      JPanel myPanel = new JPanel();
      myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS)); // displays the input fields set up by JPanel vertically
      myPanel.add(new JLabel("Please enter the tag of the cards you would like to study"));
      myPanel.add(tag);
      // gets the tag wanted to study from input from the user to put those cards into an arraylist
      int input = JOptionPane.showOptionDialog(null, myPanel, "Study", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
      if(input == 0){
         String tagRequest = tag.getText();
         ArrayList<Card> studyDeck = chosenTag(tagRequest);
         if(tagRequest == null || tagRequest.equals("")){
            JOptionPane.showMessageDialog(null, "That tag does not exist", "Study", JOptionPane.ERROR_MESSAGE);
            mainMenu();
            JOptionPane.getRootFrame().dispose(); // closes the window after opening the new one
         }
         else{
            // shuffles the deck
            Collections.shuffle(studyDeck);
            // sets up a few variables that will be used to keep track of some things
            int cardNumber = 0; // what card the user is looking at
            String flipFace = "Front: " + studyDeck.get(cardNumber).getSide1(); // the side the user is looking at
            int flipper = 0; // 0 means side1, 1 means side2 + example
            while(true){ // continues the studying
               // receives input from the user via button they push
               int input2 = JOptionPane.showOptionDialog(null, flipFace, "Viewing the tag: " + tagRequest, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,null, options2, options2[2]);
               // uses the users input to make the program do as requested
               if(input2 == 0){ // flip
                  if(flipper == 0){
                     flipFace = "Back: " + studyDeck.get(cardNumber).getSide2();
                     flipFace += "\nExample: " + studyDeck.get(cardNumber).getExample();
                     flipper++;
                  }
                  else{
                     flipFace = "Front: " + studyDeck.get(cardNumber).getSide1();
                     flipper--;
                  }
               }
               else if(input2 == 1){ // next
                  cardNumber++; // moves to the next card
                  if(cardNumber >= studyDeck.size()){ // cycles the deck back to the first card from the last one
                     Collections.shuffle(studyDeck);
                     cardNumber = 0;
                  }
                  flipFace = "Front: " + studyDeck.get(cardNumber).getSide1();
                  flipper = 0;
               }
               else{ // exit
                  mainMenu();
                  JOptionPane.getRootFrame().dispose(); // closes the window after opening the new one
               }
            }
         }
      }
      else{
         mainMenu();
         JOptionPane.getRootFrame().dispose(); // closes the window after opening the new one
      }
   }
   
   // the main method
   public static void main(String[] args){
      mainMenu();
   }
}