package projects.DistSysProject.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import projects.DistSysProject.Path;
import projects.DistSysProject.nodes.messages.DfsMessage;
import projects.DistSysProject.nodes.timers.initTimer;
import projects.DistSysProject.nodes.timers.waitTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

public class ProjNode extends Node {

	private boolean debug = false;

	public Path p;
	public Path neigh[];
	public int linkNb[];

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
		this.linkNb = new int[this.nbNeigh()];
		for (int i = 0; i < this.nbNeigh(); i++) {
			this.neigh[i] = new Path(this.nbNodes());
			this.linkNb[i] = ((int) (Math.random() * 10000.0)) % this.nbNeigh();
		}
		if (this.ID == 1)
			this.p.setRoot(this.nbNodes());
		(new waitTimer()).startRelative(20, this);
	}

	public Node getNode(int n) {
		int i = 0;
		Iterator<Edge> iter = this.outgoingConnections.iterator();
		while (i < n) {
			iter.next();
			i++;
		}
		return iter.next().endNode;
	}

	public void envoi() {
		for (int i = 0; i < this.nbNeigh(); i++) {
			this.send(new DfsMessage(new Path(this.p.path), i), this.getNode(i));
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

	public void Actions() {
		if (this.ID == 1) {
			this.p.setRoot(this.nbNodes());
			return;
		}
		Path min = this.neigh[0];
		int minL = this.linkNb[0];
		for (int i = 1; i < this.nbNeigh(); i++) {
			if (min.compare(this.neigh[i], minL, this.linkNb[i]) == 1) {
				min = this.neigh[i];
				minL = this.linkNb[i];
			}
		}
		this.p = new Path(min.path);
		this.p.addOne(minL);

		// -- DEBUG --
		if (this.debug) {
			for (int i = 0; i < this.neigh.length; i++) {
				System.out.println(
						this.ID + " - " + this.getNode(i).ID + " : " + this.neigh[i].print() + " | " + this.linkNb[i]);
			}
			System.out.println(this.ID + " : " + this.p.print());
			System.out.println("---------");
		}
		// -- --
	}

	public void handleMessages(Inbox inbox) {
		while (inbox.hasNext()) {
			Message m = inbox.next();

			if (m instanceof DfsMessage) {
				DfsMessage msg = (DfsMessage) m;
				this.neigh[this.getIndex(inbox.getSender())] = msg.p;
				this.linkNb[this.getIndex(inbox.getSender())] = msg.edgeNb;
			}
		}
		this.Actions();
	}

	public void neighborhoodChange() {
	}

	public void postStep() {
	}

	public void checkRequirements() throws WrongConfigurationException {
	}

	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		this.setColor(Color.magenta);
		String text = "" + this.ID;
		super.drawNodeAsDiskWithText(g, pt, highlight, text, 20, Color.black);
	}

}
