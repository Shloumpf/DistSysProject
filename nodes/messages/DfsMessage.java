package projects.DistSysProject.nodes.messages;

import projects.DistSysProject.Path;
import sinalgo.nodes.messages.Message;

public class DfsMessage extends Message {

	public Path p;
	public int edgeNb;

	public DfsMessage(Path p, int edgeNb) {
		this.p = p;
		this.edgeNb = edgeNb;
	}

	public Message clone() {
		return new DfsMessage(this.p, this.edgeNb);
	}
}
