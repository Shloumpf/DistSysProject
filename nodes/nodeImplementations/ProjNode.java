package projects.DistSysProject.nodes.nodeImplementations;

import java.util.Iterator;

import projects.DistSysProject.Path;
import projects.DistSysProject.nodes.messages.DfsMessage;
import projects.DistSysProject.nodes.timers.initTimer;
import projects.DistSysProject.nodes.timers.waitTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

public class ProjNode extends Node {

	public Path p;
	public Path neigh[];

	public void preStep() {
	}

	public void init() {
		(new initTimer()).startRelative(1, this);
	}
	
	public int nbNodes() {
		return Tools.getNodeList().size();
	}

	public int nbNeigh() {
		return this.outgoingConnections.size();
	}

	public void start() {
		this.p = new Path(this.nbNodes());
		this.neigh = new Path[this.nbNeigh()];
		for (int i = 0; i < this.nbNeigh(); i++)
			this.neigh[i] = new Path(this.nbNodes());
		if (this.ID == 1)
			this.p.setRoot(this.nbNodes());
		(new waitTimer()).startRelative(20, this);
	}
	
	public Node getNode(int n) {
		int i = 0;
		Iterator<Edge> iter = this.outgoingConnections.iterator();
		while (i < n)
			iter.next();
		return iter.next().endNode;
	}
	
	public void envoi() {
		for (int i = 0; i < this.nbNeigh(); i++) {
			this.send(new DfsMessage(this.p, i), this.getNode(i));
		}
		(new waitTimer()).startRelative(20, this);
	}
	
	public int getIndex(Node sender) {
		int j = 0;
		for (Edge e : this.outgoingConnections) {
			if (e.endNode == sender)
				return j;
			j++;
		}
		return 0;
	}
	
	public void action(Node sender, DfsMessage msg) {
		
	}

	public void handleMessages(Inbox inbox) {
		while (inbox.hasNext()) {
			Message m = inbox.next();
			
			if (m instanceof DfsMessage) {
				DfsMessage msg = (DfsMessage) m;
				this.action(inbox.getSender(), msg);
			}
		}
	}

	public void neighborhoodChange() {
	}

	public void postStep() {
	}

	public void checkRequirements() throws WrongConfigurationException {
	}

}
