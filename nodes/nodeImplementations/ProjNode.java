package projects.DistSysProject.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import projects.DistSysProject.Path;
import projects.DistSysProject.nodes.messages.ProjMessage;
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

	public Path p;
	public Path neigh[];
	public int linkNb[];
	public int back;
	public int backNeigh[];
	public boolean cutNode = ((int) (Math.random() * 10000.0)) % 2 == 1;
	public boolean parentBridge;

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
		if (this.nbNeigh() > 0) {
			this.p = new Path(this.nbNodes());
			this.neigh = new Path[this.nbNeigh()];
			this.linkNb = new int[this.nbNeigh()];
			this.back = (int) (Math.random() * 10000.0);
			this.backNeigh = new int[this.nbNeigh()];
			this.parentBridge = ((int) (Math.random() * 10000.0)) % 2 == 1;
			for (int i = 0; i < this.nbNeigh(); i++) {
				this.neigh[i] = new Path(this.nbNodes());
				this.linkNb[i] = ((int) (Math.random() * 10000.0)) % this.nbNeigh();
				this.backNeigh[i] = (int) (Math.random() * 10000.0);
			}
			if (this.ID == 1)
				this.p.setRoot(this.nbNodes());
			(new waitTimer()).startRelative(20, this);
		}
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
			this.send(new ProjMessage(new Path(this.p.path), i, this.back), this.getNode(i));
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

	public boolean isChildren(int n) {
		for (int i = 0; i < this.p.path.length; i++) {
			if (this.p.path[i] == this.neigh[n].path[i])
				continue;
			if (this.p.path[i] == -2 && this.neigh[n].path[i] == n)
				return true;
			return false;
		}
		return false;
	}

	public boolean isParent(int n, int link) {
		for (int i = 0; i < this.p.path.length; i++) {
			if (this.p.path[i] == this.neigh[n].path[i])
				continue;
			if (this.p.path[i] == link && this.neigh[n].path[i] == -2)
				return true;
			return false;
		}
		return false;
	}

	public int backCompute() {
		int temp = this.p.height();
		for (int i = 0; i < this.nbNeigh(); i++) {
			if (isChildren(i) && this.backNeigh[i] < temp)
				temp = this.backNeigh[i];
			if (!isChildren(i) && !isParent(i, this.linkNb[i]) && this.neigh[i].height() < temp)
				temp = this.neigh[i].height();
		}
		for (int i = 0; i < this.neigh.length; i++) {
			System.out.println(
					this.ID + " - " + this.getNode(i).ID + " : " + this.neigh[i].print() + " | " + this.linkNb[i]);
		}
		System.out.println(this.ID + " : " + this.p.print());
		System.out.println("---------");

		return temp;
	}

	public Path pathCompute() {
		Path min = this.neigh[0];
		int minL = this.linkNb[0];
		for (int i = 1; i < this.nbNeigh(); i++) {
			if (min.compare(this.neigh[i], minL, this.linkNb[i]) == 1) {
				min = this.neigh[i];
				minL = this.linkNb[i];
			}
		}
		Path temp = new Path(min.path);
		temp.addOne(minL);

		return temp;
	}

	public boolean isCutNode() {
		if (this.ID == 1) {
			int count = 0;
			for (int i = 0; i < this.nbNeigh(); i++) {
				if (isChildren(i))
					count++;
			}
			return count >= 2;
		}
		for (int i = 0; i < this.nbNeigh(); i++) {
			if (isChildren(i) && this.backNeigh[i] >= this.p.height())
				return true;
		}
		return false;
	}

	public boolean bridgeCompute() {
		for (int i = 0; i < this.nbNeigh(); i++) {
			if (isParent(i, this.linkNb[i]) && this.back == this.p.height())
				return true;
		}
		return false;
	}

	public void Actions() {
		this.cutNode = isCutNode();
		if (this.ID == 1)
			return;
		this.p = pathCompute();
		this.back = backCompute();
		this.parentBridge = bridgeCompute();
	}

	public void handleMessages(Inbox inbox) {
		while (inbox.hasNext()) {
			Message m = inbox.next();

			if (m instanceof ProjMessage) {
				ProjMessage msg = (ProjMessage) m;
				this.neigh[this.getIndex(inbox.getSender())] = msg.p;
				this.linkNb[this.getIndex(inbox.getSender())] = msg.edgeNb;
				this.backNeigh[this.getIndex(inbox.getSender())] = msg.back;
			}
			this.Actions();
		}
	}

	public void neighborhoodChange() {
		if (this.nbNeigh() > 0) {
			this.p = new Path(this.nbNodes());
			this.neigh = new Path[this.nbNeigh()];
			this.linkNb = new int[this.nbNeigh()];
			this.back = (int) (Math.random() * 10000.0);
			this.backNeigh = new int[this.nbNeigh()];
			this.parentBridge = ((int) (Math.random() * 10000.0)) % 2 == 1;
			for (int i = 0; i < this.nbNeigh(); i++) {
				this.neigh[i] = new Path(this.nbNodes());
				this.linkNb[i] = ((int) (Math.random() * 10000.0)) % this.nbNeigh();
				this.backNeigh[i] = (int) (Math.random() * 10000.0);
			}
			if (this.ID == 1)
				this.p.setRoot(this.nbNodes());
		}
	}

	public void postStep() {
	}

	public void checkRequirements() throws WrongConfigurationException {
	}

	public Color couleur() {
		return this.cutNode ? Color.red : Color.green;
	}

	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		this.setColor(this.couleur());
		String text = "" + this.ID;
		super.drawNodeAsDiskWithText(g, pt, highlight, text, 20, Color.black);
	}

}
