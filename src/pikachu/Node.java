package pikachu;

import java.awt.Point;

public class Node implements Comparable<Node> {

	Node parent;
	Point point;
	int h;
	char operator;
	int n;

	public Node() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Node(Node currentNode, Point point2) {
		// TODO Auto-generated constructor stub
		this.parent = currentNode;
		this.point = point2;
	}

	public Node(Node parent, Point point, int h, char operator, int n) {
		this.parent = parent;
		this.point = point;
		this.h = h;
		this.operator = operator;
		this.n = n;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public char getOperator() {
		return operator;
	}

	public void setOperator(char operator) {
		this.operator = operator;
	}

	@Override
	public int compareTo(Node o) {
		// TODO Auto-generated method stub
		if (h < o.h)
			return 1;
		if (h > o.h)
			return -1;
		
		if(this.parent.operator == this.operator)
			return 1;
		
		return 0;
	}

}
