import java.lang.Math;
import java.io.Serializable;

public class Frog implements Serializable {
 int[] baseColors;
 int[] accColors;
 boolean shiny;
 int maturity;
 String nickname;

 public Frog(int[] alleles) {
  baseColors = new int[2];
  accColors = new int[2];
  if (((int) (Math.random() * 50)) == 42) {
   shiny = true;
  } else {
   shiny = false;
  }
  maturity = 0;
  baseColors[0] = alleles[0];
  baseColors[1] = alleles[1];
  accColors[0] = alleles[2];
  accColors[1] = alleles[3];
 }

 public Frog(int color) {
  baseColors = new int[2];
  accColors = new int[2];

  if (((int) (Math.random() * 50)) == 42) {
   shiny = true;
  }

  else {
   shiny = false;
  }
  maturity = 3;
  baseColors[0] = color;
  baseColors[1] = color;
  accColors[0] = color;
  accColors[1] = color;
 }

 public boolean feed() {
  if (maturity >= 3) {
   return true;
  } else {
   maturity++;
   return false;
  }
 }

 public void name(String nickName) {
  nickname = nickName;
 }

 public int getMaturity() {
  return maturity;
 }

 public int[] getGametes() {
  int[] gametes = new int[4];
  gametes[0] = baseColors[0];
  gametes[1] = baseColors[1];
  gametes[2] = accColors[0];
  gametes[3] = accColors[1];
  return gametes;
 }

 public int[] getRandGametes() {
  int[] gametes = new int[2];
  if ((int) (Math.random() * 2) == 0) {
   gametes[0] = baseColors[0];
  } else {
   gametes[0] = baseColors[1];
  }

  if ((int) (Math.random() * 2) == 0) {
   gametes[1] = accColors[0];
  } else {
   gametes[1] = accColors[1];
  }

  return gametes;
 }

 public String[] getPhenotype() {
  String[] phenotype = new String[3];
  String phenotypeString = "";
  int[] gametes = this.getGametes();
  // figure out base color
  phenotypeString += Constants.rybbw[gametes[0]][gametes[1]];
  phenotypeString += " with an accent of ";
  // figure out accent color
  phenotypeString += Constants.rybbw[gametes[2]][gametes[3]];
  // put it all together
  phenotype[0] = phenotypeString;
  phenotype[1] = Constants.colorsList.indexOf(
    Constants.rybbw[gametes[0]][gametes[1]]) + "";
  phenotype[2] = Constants.colorsList.indexOf(
    Constants.rybbw[gametes[2]][gametes[3]]) + "";

  // get the achievement
  return phenotype;
 }

 public boolean getShiny() {
  return this.shiny;
 }

 public String toString() {
  String str = ": ";
  str += this.getPhenotype()[0];
  str += " Maturity: ";
  str += maturity;
  return str;
 }
}