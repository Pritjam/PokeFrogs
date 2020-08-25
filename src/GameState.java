import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameState {
  FroggyDex dex;
  Megabox boxes;
  int money, currentBox, feed;
  public GameState() {
    dex = new FroggyDex();
    boxes = new Megabox();
    money = 50;
    currentBox = 0;
    feed = 20;
  }
  
  public void load(String path) throws ClassNotFoundException, IOException{
    try {
      FileInputStream fileIn = new FileInputStream(
        "saves/" + path.toUpperCase() + ".sav");
      ObjectInputStream ois = new ObjectInputStream(fileIn);
      dex = (FroggyDex) ois.readObject(); //deserialize the array
      boxes = (Megabox) ois.readObject();
      ois.close();
      money = dex.systemVars[0];
      feed = dex.systemVars[1];
      System.out.println("Loaded Successfully!");
    } catch (FileNotFoundException e) {
      System.out.println(
        "No such save exists! Check the \"saves\" folder in the game directory.");
    }
  }
  
  public void save(String path) throws FileNotFoundException, IOException{
    FileOutputStream fileStream = new FileOutputStream(
      "saves/" + path.toUpperCase() + ".sav");
    ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
    objectStream.writeObject(dex);
    objectStream.writeObject(boxes);
    objectStream.close();
    System.out.println("Saved Successfully as \"" + path + "\"");
  }
}
