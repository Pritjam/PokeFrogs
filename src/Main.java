import java.util.Scanner;
import java.lang.Math;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.*;

public class Main {
  
  //here we go---THIS IS A COMMENT 
  public static void main(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException {
    Scanner scan = new Scanner(System.in);
    
    PunnetMixer mendel = new PunnetMixer();
    FroggyDex dex = new FroggyDex();
    Megabox boxes = new Megabox();
    Parser parser = new Parser();
    int money = 50;
    int currentBox = 0;
    int feed = 20;
    
    int randomThree = (int)(Math.random() * 3);
    for (int i = 0; i < 4; i++) {
      if (i != randomThree) {
        boxes.addFrog(new Frog(i), 0);
      }
    }
    
    System.out.println("Welcome to PokeFrogs! You are a frog breeder who specializes in multicolored frogs.\nYour frogs have 2 attributes: a base color, and an accent color.\nYou can breed these frogs to create new color combinations.\nYou have 10 habitats for your frogs to live in.\nGet help at any time by typing the command \"Help\". Have fun!");
    for (int i = 0; i < 3; i++) {
      String[] phenotype = boxes.getFrog(currentBox, i).getPhenotype();
      dex.achieveFrog(phenotype);
    }
    String input;
    String command;
    
    //main game loop========================================================
    while (true) {
      System.out.print("Money: " + money + "      Feed: " + feed + "\nInput a command...\n>>");
      input = scan.nextLine();
      command = parser.getCommand(input);
      
      switch (command) {
        case "MOVE":
          try {
          Frog tempFrog = boxes.remove(currentBox, parser.getIntArgs(input)[0]);
          try {
            boxes.addFrog(tempFrog, parser.getIntArgs(input)[1]);
          } catch (IndexOutOfBoundsException e) {
            System.out.println("That box doesn't exist! There are 10 boxes, labeled 0-9.");
          }
        } catch (IndexOutOfBoundsException e) {
          System.out.println("That frog doesn't exist!");
        }
        break;
        
        case "BREED":
          try {
          Frog frog1 = boxes.getFrog(currentBox, parser.getIntArgs(input)[0]);
          Frog frog2 = boxes.getFrog(currentBox, parser.getIntArgs(input)[1]);
          if (frog1.getMaturity() == 3 && frog2.getMaturity() == 3) {
            Frog newFrog = mendel.cross(frog1, frog2);
            boxes.addFrog(newFrog, currentBox);
            String[] phenotype = newFrog.getPhenotype();
            dex.achieveFrog(phenotype);
            System.out.println("Frog " + parser.getIntArgs(input)[0] + " bred with frog " + parser.getIntArgs(input)[1] + " to produce a new frog!");
          } else {
            System.out.println("Frogs need to be fully grown (Maturity 3) to breed!");
          }
        } catch (IndexOutOfBoundsException e) {
          System.out.println("One or more of those frogs don't exist! Please refrain from breeding nonexistent frogs so as to minimize the risk of alternate reality formation.");
        }
        break;
        
        case "VIEW":
          System.out.println("Current Box: " + currentBox);
          for (int i = 0; i < boxes.getBoxSize(currentBox); i++) {
            System.out.print(i + ": " + boxes.getFrog(currentBox, i).getPhenotype()[0] + ", Maturity: " + boxes.getFrog(currentBox, i).getMaturity());
            if (boxes.getFrog(currentBox, i).getShiny()) {
              System.out.println(" * ");
            } else {
              System.out.println();
            }
          }
          break;
          
        case "HELP":
          System.out.println("Here are the recognized commands:");
          System.out.println("Breed <index1> <index2>\nFeed <index>\nMove <index> <destination box>\nSwitch <box to switch to>\nView (This command lists all frogs in the currently selected box)\nRelease <index>\nFroggydex (shows all frogs unlocked)\nShop (enters the shop)\nSave\nLoad");
          break;
          
        case "FROGGYDEX":
          System.out.println(dex.frogsToString());
          break;
          
        case "FEED":
          int frogToFeed = 0;
          if (parser.getIntArgs(input)[0] != -1) {
            frogToFeed = parser.getIntArgs(input)[0];
          }
          if (feed > 0 && !boxes.getFrog(currentBox, frogToFeed).feed()) {
            System.out.println(":yum:, feed frog " + parser.getIntArgs(input)[0] + " and increased its maturity by 1");
            
          } else {
            System.out.println("That frog is already fully grown!");
          }
          break;
          
        case "RELEASE":
          if (parser.getIntArgs(input)[0] < boxes.getBoxSize(currentBox)) {
          System.out.println("Are you sure? (Y/N)");
          if (scan.nextLine().toUpperCase().equals("Y")) {
            boxes.remove(currentBox, parser.getIntArgs(input)[0]);
            System.out.println("Done.");
          } else {
            System.out.println("Release Cancelled.");
          }
        } else {
          System.out.println("There isn't a frog with that index in this box!");
        }
        break;
        
        case "SWITCH":
          currentBox = parser.getIntArgs(input)[0];
          System.out.println("Switched to box " + parser.getIntArgs(input)[0]);
          break;
          
        case "SAVE":
          dex.saveSystemVars(money, feed);
          System.out.println("Saving...");
          System.out.println("Please input the save name.");
          String saveOutPath = scan.nextLine();
          FileOutputStream fileStream = new FileOutputStream("saves/" + saveOutPath.toUpperCase() + ".sav");
          ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
          objectStream.writeObject(dex);
          objectStream.writeObject(boxes);
          objectStream.close();
          System.out.println("Saved Successfully as \"" + saveOutPath + "\"");
          break;
          
        case "LOAD":
          System.out.println("Please type in your save name.");
          String savePath = scan.nextLine();
          try {
            FileInputStream fileIn = new FileInputStream("saves/" + savePath.toUpperCase() + ".sav");
            ObjectInputStream ois = new ObjectInputStream(fileIn);
            dex = (FroggyDex) ois.readObject(); //deserialize the array
            boxes = (Megabox) ois.readObject();
            ois.close();
            money = dex.systemVars[0];
            feed = dex.systemVars[1];
            System.out.println("Loaded Successfully!");
          } catch (FileNotFoundException e) {
            System.out.println("No such save exists! Check the \"saves\" folder in the game directory.");
          }
          break;
          
        case "SHOP":
          System.out.println("SHOP\n==========================================\nFeed or Frogs?");
          System.out.print(">>");
          String choice = scan.nextLine().toUpperCase();
          switch (choice) {
            case "FEED":
              System.out.print("How much? Grain feed costs 2 money each. You have " + money + " money.\n>>");
              int grain = scan.nextInt();
              if (money > grain * 2) {
                money -= grain * 2;
                feed += grain;
                System.out.println("Pleasure doing business with you! Back to the main menu.");
              } else {
                System.out.println("Not enough money! Back to the main menu.");
              }
              break;
              
            case "FROGS":
              System.out.print("Please enter the coloring of frog you'd like to see, for example \"ORN PRP\" to see an orange frog with an accent of purple.\n>>");
              String whatToView = scan.nextLine();
              try {
                int[] phenoView = parser.getClrArgs(whatToView);
                int[] phenoReal = parser.parsePheno(phenoView);
                int cost = Constants.prices[phenoView[0]] * Constants.prices[phenoView[1]];
                
                System.out.println("That will cost " + cost + " money. Proceed to buy? (Y/N)");
                if (cost < money && scan.nextLine().toUpperCase().equals("Y")) {
                  money -= cost;
                  boxes.addFrog(new Frog(phenoReal), currentBox);
                } else {
                  System.out.println("Ok, back to main menu!");
                }
                
              } catch (ArrayIndexOutOfBoundsException a) {
                System.out.println("Gotta specify exactly 2 colors!");
                break;
              }
              break;
              
            default:
              System.out.println("We don't carry that. Back to the main menu!");
          }
          break;
          
        default:
          System.out.println("Not a recognized command! Type \"Help\" for a list of commands.");
      }
      System.out.println();
    }
    
  }
}