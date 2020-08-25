import java.util.Scanner;
import java.lang.Math;
import java.io.*;

public class Main {

 // here we go
 public static void main(String[] args) throws ClassNotFoundException, IOException {
  Scanner scan = new Scanner(System.in);
  PunnetMixer mendel = new PunnetMixer();
  Parser parser = new Parser();

  GameState state = new GameState();

  int randomThree = (int) (Math.random() * 3);
  for (int i = 0; i < 4; i++) {
   if (i != randomThree) {
    state.boxes.addFrog(new Frog(i), 0);
   }
  }

  System.out.println(
    "Welcome to PokeFrogs! You are a frog breeder who specializes in multicolored frogs.\nYour frogs have 2 attributes: a base color, and an accent color.\nYou can breed these frogs to create new color combinations.\nYou have 10 habitats for your frogs to live in.\nGet help at any time by typing the command \"Help\". Have fun!");
  for (int i = 0; i < 3; i++) {
   String[] phenotype = state.boxes.getFrog(state.currentBox, i).getPhenotype();
   state.dex.achieveFrog(phenotype);
  }
  String input;
  String command;

  // main game loop========================================================
  while (true) {
   System.out.print(
     "Money: " + state.money + "      Feed: " + state.feed + "\nInput a command...\n>>");
   input = scan.nextLine();
   command = parser.getCommand(input);

   switch (command) {
   case "MOVE":
    try {
     Frog tempFrog = state.boxes.remove(state.currentBox, parser.getIntArgs(input)[0]);
     try {
      state.boxes.addFrog(tempFrog, parser.getIntArgs(input)[1]);
     } catch (IndexOutOfBoundsException e) {
      System.out.println("That box doesn't exist! There are 10 boxes, labeled 0-9.");
     }
    } catch (IndexOutOfBoundsException e) {
     System.out.println("That frog doesn't exist!");
    }
    break;

   case "BREED":
    try {
     Frog frog1 = state.boxes.getFrog(state.currentBox, parser.getIntArgs(input)[0]);
     Frog frog2 = state.boxes.getFrog(state.currentBox, parser.getIntArgs(input)[1]);
     if (frog1.getMaturity() == 3 && frog2.getMaturity() == 3) {
      Frog newFrog = mendel.cross(frog1, frog2);
      state.boxes.addFrog(newFrog, state.currentBox);
      String[] phenotype = newFrog.getPhenotype();
      state.dex.achieveFrog(phenotype);
      System.out.println("Frog " + parser.getIntArgs(input)[0] + " bred with frog "
        + parser.getIntArgs(input)[1] + " to produce a new frog!");
     } else {
      System.out.println("Frogs need to be fully grown (Maturity 3) to breed!");
     }
    } catch (IndexOutOfBoundsException e) {
     System.out.println(
       "One or more of those frogs don't exist! Please refrain from breeding nonexistent frogs so as to minimize the risk of alternate reality formation.");
    }
    break;

   case "VIEW":
    System.out.println("Current Box: " + state.currentBox);
    for (int i = 0; i < state.boxes.getBoxSize(state.currentBox); i++) {
     System.out.print(i + state.boxes.getFrog(state.currentBox, i).toString());
     if (state.boxes.getFrog(state.currentBox, i).getShiny()) {
      System.out.print(" * ");
     }
     System.out.println();
    }
    break;

   case "HELP":
    System.out.println("Here are the recognized commands:");
    System.out.println(
      "Breed <index1> <index2>\nFeed <index>\nMove <index> <destination box>\nSwitch <box to switch to>\nView (This command lists all frogs in the currently selected box)\nRelease <index>\nFroggydex (shows all frogs unlocked)\nShop (enters the shop)\nSave\nLoad");
    break;

   case "FROGGYDEX":
    System.out.println(state.dex.frogsToString());
    break;

   case "FEED":
    int frogToFeed = 0;
    if (parser.getIntArgs(input)[0] != -1) {
     frogToFeed = parser.getIntArgs(input)[0];
    }
    if (state.feed > 0 && !state.boxes.getFrog(state.currentBox, frogToFeed).feed()) {
     System.out.println(
       ":yum:, feed frog " + parser.getIntArgs(input)[0] + " and increased its maturity by 1");

    } else {
     System.out.println("That frog is already fully grown!");
    }
    break;

   case "RELEASE":
    if (parser.getIntArgs(input)[0] < state.boxes.getBoxSize(state.currentBox)) {
     System.out.println("Are you sure? (Y/N)");
     if (scan.nextLine().toUpperCase().equals("Y")) {
      state.boxes.remove(state.currentBox, parser.getIntArgs(input)[0]);
      System.out.println("Done.");
     } else {
      System.out.println("Release Cancelled.");
     }
    } else {
     System.out.println("There isn't a frog with that index in this box!");
    }
    break;

   case "SWITCH":
    state.currentBox = parser.getIntArgs(input)[0];
    System.out.println("Switched to box " + parser.getIntArgs(input)[0]);
    break;

   case "SAVE":
    state.dex.saveSystemVars(state.money, state.feed);
    System.out.println("Saving...");
    System.out.println("Please input the save name.");
    String saveOutPath = scan.nextLine();
    state.save(saveOutPath);

    break;

   case "LOAD":
    System.out.println("Please type in your save name.");
    String savePath = scan.nextLine();
    state.load(savePath);
    break;

   case "SHOP":
    System.out.println("SHOP\n==========================================\nFeed or Frogs?");
    System.out.print(">>");
    String choice = scan.nextLine().toUpperCase();
    switch (choice) {
    case "FEED":
     System.out
       .print("How much? Grain feed costs 2 money each. You have " + state.money + " money.\n>>");
     int grain = scan.nextInt();
     if (state.money > grain * 2) {
      state.money -= grain * 2;
      state.feed += grain;
      System.out.println("Pleasure doing business with you! Back to the main menu.");
     } else {
      System.out.println("Not enough money! Back to the main menu.");
     }
     break;

    case "FROGS":
     System.out.print(
       "Please enter the coloring of frog you'd like to see, for example \"ORN PRP\" to see an orange frog with an accent of purple.\n>>");
     String whatToView = scan.nextLine();
     try {
      int[] phenoView = parser.getClrArgs(whatToView);
      int[] phenoReal = parser.parsePheno(phenoView);
      int cost = Constants.prices[phenoView[0]] * Constants.prices[phenoView[1]];

      System.out.println("That will cost " + cost + " money. Proceed to buy? (Y/N)");
      if (cost < state.money && scan.nextLine().toUpperCase().equals("Y")) {
       state.money -= cost;
       state.boxes.addFrog(new Frog(phenoReal), state.currentBox);
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