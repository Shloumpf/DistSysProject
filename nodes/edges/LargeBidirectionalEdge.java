package projects.DistSysProject.nodes.edges;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import projects.DistSysProject.nodes.nodeImplementations.ProjNode;
import sinalgo.gui.helper.Arrow;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Position;
import sinalgo.nodes.edges.BidirectionalEdge;

public class LargeBidirectionalEdge extends BidirectionalEdge {

	public void draw(Graphics g, PositionTransformation pt) {
		Position p1 = startNode.getPosition();
		pt.translateToGUIPosition(p1);
		int fromX = pt.guiX, fromY = pt.guiY; // temporarily store
		Position p2 = endNode.getPosition();
		pt.translateToGUIPosition(p2);
		ProjNode deb = (ProjNode) this.startNode;
		ProjNode fin = (ProjNode) this.endNode;

		if (deb.p.treeEdge(fin.p)) {
			if ((deb.p.height() < fin.p.height() && fin.parentBridge) || (deb.p.height() > fin.p.height() && deb.parentBridge)) {
				Graphics2D g2 = (Graphics2D) g;
				Stroke stroke = new BasicStroke(5f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f);
				g2.setStroke(stroke);
				Arrow.drawArrow(fromX, fromY, pt.guiX, pt.guiY, g2, pt, Color.red);
			} else {
				Graphics2D g2 = (Graphics2D) g;
				Stroke stroke = new BasicStroke(5f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f);
				g2.setStroke(stroke);
				Arrow.drawArrow(fromX, fromY, pt.guiX, pt.guiY, g2, pt, Color.blue);
			}
		} else {
			Graphics2D g2 = (Graphics2D) g;
			Stroke stroke = new BasicStroke(1f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f);
			g2.setStroke(stroke);
			Arrow.drawArrow(fromX, fromY, pt.guiX, pt.guiY, g2, pt, Color.GRAY);
		}
	}

}