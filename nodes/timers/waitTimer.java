package projects.DistSysProject.nodes.timers;

import projects.DistSysProject.nodes.nodeImplementations.ProjNode;
import sinalgo.nodes.timers.Timer;

public class waitTimer extends Timer {

	public void fire() {
		ProjNode n = (ProjNode) this.node;
		n.envoi();
	}
}
