import java.io.Serializable;

public class FroggyDex implements Serializable{
  boolean[][] frogs;
  boolean[] achievements;
  public int[] systemVars;
  
  public FroggyDex() {
    frogs = new boolean[9][9];
    achievements = new boolean[25];
    systemVars = new int[10];
  }
  
  public String frogsToString() {
    String dexStr = "";
    dexStr += "Printing FroggyDex: \n";
    dexStr += "    BASE RED ORN YLW GRN BLU PRP WHT BLK GLS\n";
    dexStr += "ACCENT\n";
    
    for (int i = 0; i < 9; i++) {
      dexStr += Constants.colorsList.get(i) + "      ";
      for (int j = 0; j < 9; j++) {
        if(frogs[i][j]) {
          dexStr += " +  ";
        }
        else {
          dexStr += " -  ";
        }
      }
      dexStr += "\n";
    }
    return dexStr;
  }
  
  public void achieveFrog(String[] phenotype) {
    int base = Integer.parseInt(phenotype[1]);
    int acc = Integer.parseInt(phenotype[2]);
    frogs[base][acc] = true;
  }
  
  public void saveSystemVars (int money, int feed) {
    systemVars[0] = money;
    systemVars[1] = feed;
  }
  
}