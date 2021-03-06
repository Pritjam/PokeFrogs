import java.io.Serializable;
import java.util.ArrayList;

public class Contract implements Serializable {
	ArrayList<Frog> goal;

	public Contract(ArrayList<Frog> target) {
		this.goal = target;
	}

	public boolean giveFrog(Frog tribute) {
		if (goal.indexOf(tribute) != -1) {
			goal.remove(tribute);
			return true;
		} else {
			return false;
		}
	}

	public boolean isComplete() {
		return (goal.size() == 0);
	}

	public String toString() {
		String str = "";
		str += "Frogs Remaining:" + goal.size();
		for (int i = 0; i < goal.size(); i++) {
			str += i + goal.get(i).toString();
		}
		return str;
	}
}
