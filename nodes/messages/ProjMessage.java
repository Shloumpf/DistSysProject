package projects.DistSysProject.nodes.messages;

import projects.DistSysProject.Path;
import sinalgo.nodes.messages.Message;

public class ProjMessage extends Message {

	public Path p;
	public int edgeNb;
	public int back;

	public ProjMessage(Path p, int edgeNb, int back) {
		this.p = p;
		this.edgeNb = edgeNb;
		this.back = back;
	}

	public Message clone() {
		return new ProjMessage(this.p, this.edgeNb, this.back);
	}
}
