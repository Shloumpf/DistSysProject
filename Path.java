package projects.DistSysProject;

public class Path {

	public int path[];

	public Path(int n) {
		this.path = new int[n + 1];
		int l = (((int) (Math.random() * 10000.0)) % (n / 2)) + 1;
		for (int i = 0; i < l; i++)
			this.path[i] = (int) (Math.random() * n);
		for (int i = l; i < n + 1; i++)
			this.path[i] = -2;
	}

	public Path(int path[]) {
		this.path = new int[path.length];
		for (int i = 0; i < path.length; i++) {
			this.path[i] = path[i];
		}
	}

	public void setRoot(int n) {
		for (int i = 1; i < n; i++)
			this.path[i] = -2;
		this.path[0] = -1;
	}

	public int compare(Path p, int linkThis, int linkP) {
		for (int i = 0; i < this.path.length; i++) {
			if (this.path[i] == -2)
				return linkThis <= p.path[i] ? -1 : 1;
			if (p.path[i] == -2)
				return this.path[i] < linkP ? -1 : 1;
			if (this.path[i] < p.path[i])
				return -1;
			if (this.path[i] > p.path[i])
				return 1;
		}
		return 0;
	}

	public void addOne(int k) {
		int i = 0;
		while (i < this.path.length - 1 && this.path[i] != -2)
			i++;
		this.path[i] = k;
	}

	public int len() {
		int i = 0;
		while (i < this.path.length - 1 && this.path[i] != -2)
			i++;
		return i;
	}

	public boolean treeEdge(Path p) {
		if (this.len() == p.len() + 1) {
			for (int i = 0; i < p.len(); i++) {
				if (this.path[i] != p.path[i])
					return false;
			}
			return true;
		}
		if (this.len() + 1 == p.len()) {
			for (int i = 0; i < this.len(); i++) {
				if (this.path[i] != p.path[i])
					return false;
			}
			return true;
		}
		return false;
	}

	public String print() {
		String s = "";
		for (int i = 0; i < this.path.length; i++)
			s += " " + this.path[i];
		return s;
	}
}
