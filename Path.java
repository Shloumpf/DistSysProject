package projects.DistSysProject;

public class Path {

	public int path[];

	public Path(int n) {
		this.path = new int[n];
		int l = (((int) (Math.random() * 10000.0)) % (n-1)) + 1;
		for (int i = 0; i < l; i++)
			this.path[i] = (int) (Math.random() * n);
		for (int i = l; i < n; i++)
			this.path[i] = -2;
	}
	
	public void setRoot(int n) {
		for (int i = 1; i < n; i++)
			this.path[i] = -2;
		this.path[0] = -1;
	}
}
