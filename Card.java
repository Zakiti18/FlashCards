/*
* Card.java
* Phillip Ball
* 05/29/2020
* This file is the attributes of a flashcard and is used by another file "FlashCards.java"
*/

// imports
//import java.io.Serializable; // needed for saving and loading

public class Card implements java.io.Serializable{ // needed for saving and loading
   // fields
   private String side1; // will also be used as the "name" of the card for look ups
   private String side2;
   private String example; // shows an example of side2 being used
   private String tag; // labels which decks a card is in
   
   // methods
   // constructor method
   public Card(String side1, String side2, String example, String tag){
      this.side1 = side1;
      this.side2 = side2;
      this.example = example;
      this.tag = tag;
   }
   // constructor method 2 (no example)
   public Card(String side1, String side2, String tag){
      this.side1 = side1;
      this.side2 = side2;
      this.tag = tag;
   }

   // getters
   public String getSide1(){
      return this.side1;
   }
   public String getSide2(){
      return this.side2;
   }
   public String getExample(){
      return this.example;
   }
   public String getTag(){
      return this.tag;
   }

   // setters
   public void setSide1(String newSide1){
      side1 = newSide1;
   }
   public void setSide2(String newSide2){
      side2 = newSide2;
   }
   public void setExample(String newExample){
      example = newExample;
   }
   public void setTag(String newTag){
      tag = newTag;
   }
   
   // toString method for testing
   public String toString(){
      return "side1: " + side1 + "\nside2: " + side2 + "\nexample: " + example + "\ntag: " + tag;
   }
}