public class PunnetMixer {

	public PunnetMixer() {

	}

	public Frog cross(Frog f1, Frog f2) {
		int[] newFrogArray = { 0, 0, 0, 0 };
		int[] gametes1 = f1.getRandGametes();
		int[] gametes2 = f2.getRandGametes();
		newFrogArray[0] = gametes1[0];
		newFrogArray[1] = gametes2[0];
		newFrogArray[2] = gametes1[1];
		newFrogArray[3] = gametes2[1];
		Frog newFrog = new Frog(newFrogArray);
		return newFrog;
	}
}