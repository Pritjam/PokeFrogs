import java.util.ArrayList;
import java.io.Serializable;

public class Megabox implements Serializable {
	ArrayList<ArrayList<Frog>> megabox;

	public Megabox() {
		megabox = new ArrayList<ArrayList<Frog>>(10);
		for (int i = 0; i < 10; i++) {
			megabox.add(new ArrayList<Frog>());
		}
	}

	public Frog getFrog(int box, int index) {
		return (megabox.get(box).get(index));
	}

	public void addFrog(Frog newFrog, int box) {
		megabox.get(box).add(newFrog);
	}

	public Frog remove(int box, int index) {
		return megabox.get(box).remove(index);
	}

	public int getBoxSize(int box) {
		return megabox.get(box).size();
	}

}