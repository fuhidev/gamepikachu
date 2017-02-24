package pikachu;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.Stack;

public class Algorithm extends Observable {
	class Operator {
		private static final char LEFT = 'l';
		private static final char RIGHT = 'r';
		private static final char UP = 'u';
		private static final char DOWN = 'd';
	}

	private static final int IMG_NUMBER = 32;
	private static final int NO_PATCH = 3;

	private int row;
	private int col;

	private final int NOT_BARRIER = 0;
	private int[][] matrix ;
	public int[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}

	public Algorithm(int row, int col) {
		this.row = row;
		this.col = col;
		createMatrix();
	}

	public void createMatrix() {

		matrix = new int[row][col];

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				matrix[i][j] = 0;
			}
		}
		Random rand = new Random();
		int maxDouble = IMG_NUMBER;
		int imgArr[] = new int[IMG_NUMBER + 1];
		ArrayList<Point> listPoint = new ArrayList<Point>();
		for (int i = 1; i < row - 1; i++) {
			for (int j = 1; j < col - 1; j++) {
				listPoint.add(new Point(i, j));
			}
		}
		int i = 0;
		do {
			int imgIndex = rand.nextInt(IMG_NUMBER) + 1;
			if (imgArr[imgIndex] < maxDouble) {
				imgArr[imgIndex] += 2;
				for (int j = 0; j < 2; j++) {
					try {
						int size = listPoint.size();
						int pointIndex = rand.nextInt(size);
						matrix[listPoint.get(pointIndex).x][listPoint
								.get(pointIndex).y] = imgIndex;
						listPoint.remove(pointIndex);
					} catch (Exception e) {
					}
				}
				i++;
			}
		} while (i < row * col / 2);

	}

	public void showMatrix() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				System.out.printf("%3d", matrix[i][j]);
			}
			System.out.println();
		}
	}

	List<Point> patch;

	public List<Point> getPatch() {
		return patch;
	}

	public boolean isCorrect(Point p1, Point p2) {
		if ((p1.x != p2.x || p1.y != p2.y)
				&& matrix[p1.x][p1.y] == matrix[p2.x][p2.y]) {
			try {
				Node node = climbing(p1, p2);
				if (node != null) {
					matrix[p1.x][p1.y] = 0;
					matrix[p2.x][p2.y] = 0;
					patch = getPatch(node);
					return true;
				}
			} catch (Exception ex) {
				throw ex;
			}
		}
		return false;
	}

	private int[][] copyMatrix(int[][] m) {
		int[][] result = new int[row][col];
		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.col; j++) {
				result[i][j] = m[i][j];
			}
		}
		return result;
	}

	public void changeMatrix() {
		int[][] tmp = copyMatrix(this.matrix);
		Random rand = new Random();
		int maxDouble = IMG_NUMBER;
		int imgArr[] = new int[IMG_NUMBER + 1];
		ArrayList<Point> listPoint = new ArrayList<Point>();
		for (int i = 1; i < row - 1; i++) {
			for (int j = 1; j < col - 1; j++) {
				if (tmp[i][j] == NOT_BARRIER)
					continue;
				listPoint.add(new Point(i, j));
			}
		}
		int i = 0;
		do {
			int imgIndex = rand.nextInt(IMG_NUMBER) + 1;
			if (imgArr[imgIndex] < maxDouble) {
				imgArr[imgIndex] += 2;
				for (int j = 0; j < 2; j++) {
					try {
						int size = listPoint.size();
						int pointIndex = rand.nextInt(size);
						matrix[listPoint.get(pointIndex).x][listPoint
								.get(pointIndex).y] = imgIndex;
						listPoint.remove(pointIndex);
					} catch (Exception e) {
					}
				}
				i++;
			}
		} while (i < row * col / 2);
	}

	public void chuyenHinhSangBenPhai() {
		// int[][] tmp = copyMatrix(this.matrix);
		int i = 1;
		boolean flag = false;
		for (i = this.row - 2; i > 0; i--) {
			for (int j = this.col - 2; j > 0; j--) {
				if (matrix[i][j] == NOT_BARRIER) {
					for (int q = j; q > 0; q--) {
						if (flag)
							q++;
						if (q > 1 && matrix[i][q - 1] == NOT_BARRIER
								&& matrix[i][q - 2] != NOT_BARRIER) {
							flag = true;

							for (int k = q - 1; k > 0; k--) {
								matrix[i][k] = matrix[i][k - 1];
							}
							continue;
						}
						matrix[i][q] = matrix[i][q - 1];
						flag = false;

					}
				}
			}
		}
	}

	public void chuyenHinhSangBenTrai() {
		// int[][] tmp = copyMatrix(this.matrix);
		int i = 1;
		boolean flag = false;
		for (i = 1; i < this.row - 1; i++) {
			for (int j = 1; j < this.col - 1; j++) {
				if (matrix[i][j] == NOT_BARRIER) {
					for (int q = j; q < this.col - 1; q++) {
						if (flag)
							q--;
						if (q < this.col - 2 && matrix[i][q + 1] == NOT_BARRIER
								&& matrix[i][q + 2] != NOT_BARRIER) {
							flag = true;

							for (int k = q + 1; k < this.col - 1; k++) {
								matrix[i][k] = matrix[i][k + 1];
							}
							continue;
						}
						matrix[i][q] = matrix[i][q + 1];
						flag = false;

					}

				}
			}
		}
	}

	public void chuyenHinhLenTren() {
		int i = 1;
		boolean flag = false;
		for (i = 1; i < this.col - 1; i++) {
			for (int j = 1; j < this.row - 1; j++) {
				if (matrix[j][i] == NOT_BARRIER) {
					for (int q = j; q < this.row - 1; q++) {
						if (flag)
							q--;
						if (q < this.row - 2 && matrix[q + 1][i] == NOT_BARRIER
								&& matrix[q + 2][i] != NOT_BARRIER) {
							flag = true;

							for (int k = q + 1; k < this.row - 1; k++) {
								matrix[k][i] = matrix[k + 1][i];
							}
							continue;
						}
						matrix[q][i] = matrix[q + 1][i];
						flag = false;

					}

				}
			}
		}
	}

	public void chuyenHinhXuongDuoi() {
		int i = 1;
		boolean flag = false;
		for (i = this.col - 2; i > 0; i--) {
			for (int j = this.row - 2; j > 0; j--) {
				if (matrix[j][i] == NOT_BARRIER) {
					for (int q = j; q > 0; q--) {
						if (flag)
							q++;
						if (q > 1 && matrix[q - 1][i] == NOT_BARRIER
								&& matrix[q - 2][i] != NOT_BARRIER) {
							flag = true;

							for (int k = q - 1; k > 0; k--) {
								matrix[k][i] = matrix[k - 1][i];
							}
							continue;
						}
						matrix[q][i] = matrix[q - 1][i];
						flag = false;

					}

				}
			}
		}
	}

	private int calculatorH(Point p1, Point p2) {
		int h = Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
		return h;
	}

	private int calculatorN(Node node, char c) {
		int n = node.n;
		if (node.operator != c)
			n = node.n + 1;
		return n;
	}

	boolean check(int x, int y, Point p2) {
		return matrix[x][y] == NOT_BARRIER || (x == p2.x && y == p2.y);
	}

	private List<Node> getSucessors(Node currentNode, Point p2) {
		List<Node> result = new LinkedList<>();
		if (currentNode.point.y > 0
				&& check(currentNode.point.x, currentNode.point.y - 1, p2)) {
			Point p = new Point(currentNode.point.x, currentNode.point.y - 1);
			char c = Operator.LEFT;
			Node node = new Node(currentNode, p, calculatorH(p, p2), c,
					calculatorN(currentNode, c));
			result.add(node);
		}
		if (currentNode.point.y < col - 1
				&& check(currentNode.point.x, currentNode.point.y + 1, p2)) {
			Point p = new Point(currentNode.point.x, currentNode.point.y + 1);
			char c = Operator.RIGHT;
			Node node = new Node(currentNode, p, calculatorH(p, p2), c,
					calculatorN(currentNode, c));
			result.add(node);
		}
		if (currentNode.point.x > 0
				&& check(currentNode.point.x - 1, currentNode.point.y, p2)) {
			Point p = new Point(currentNode.point.x - 1, currentNode.point.y);
			char c = Operator.UP;
			Node node = new Node(currentNode, p, calculatorH(p, p2), c,
					calculatorN(currentNode, c));
			result.add(node);
		}
		if (currentNode.point.x < row - 1
				&& check(currentNode.point.x + 1, currentNode.point.y, p2)) {
			Point p = new Point(currentNode.point.x + 1, currentNode.point.y);
			char c = Operator.DOWN;
			Node node = new Node(currentNode, p, calculatorH(p, p2), c,
					calculatorN(currentNode, c));
			result.add(node);
		}
		return result;
	}

	public Node climbing(Point p1, Point p2) {
		Stack<Node> stack = new Stack<Node>();
		Node currentNode = new Node(null, p1, -1, 'h', 0);
		stack.add(currentNode);
		while (!stack.isEmpty()) {
			currentNode = stack.pop();
			if (currentNode.n > NO_PATCH)
				continue;
			if (currentNode.h == 0)
				return currentNode;
			List<Node> tmpSuccessor = getSucessors(currentNode, p2);
			List<Node> successor = new ArrayList<Node>();
			
			for (Node n : tmpSuccessor) {
				if (!isParent(n))
					successor.add(n);
			}
			Collections.sort(successor);
			stack.addAll(successor);
			/*
			 * for (int i = 0; i < successor.size(); i++) { Node n =
			 * successor.get(i); if (!isParent(n)) stack.add(n); }
			 */
		}
		return null;
	}

	List<Point> getPatch(Node node) {
		List<Point> points = new LinkedList<>();
		Node tmp = node;
		while (tmp != null) {
			points.add(tmp.point);
			tmp = tmp.parent;
		}
		return points;
	}

	private boolean isParent(Node current) {
		Node tmp = current.parent;
		while (tmp != null) {
			if (tmp.point.x == current.point.x
					&& tmp.point.y == current.point.y)
				return true;
			tmp = tmp.parent;
		}
		return false;
	}

	private boolean ktduongdi(Point p) {
		if (p.y > 0
				&& (matrix[p.x][p.y - 1] == NOT_BARRIER || matrix[p.x][p.y - 1] == matrix[p.x][p.y])) {

			return true;
		}
		if (p.y < col - 1 && matrix[p.x][p.y + 1] == NOT_BARRIER
				|| matrix[p.x][p.y + 1] == matrix[p.x][p.y]) {
			return true;
		}
		if (p.x > 0
				&& (matrix[p.x - 1][p.y] == NOT_BARRIER || matrix[p.x - 1][p.y] == matrix[p.x][p.y])) {

			return true;
		}
		if (p.x < row - 1 && matrix[p.x + 1][p.y] == NOT_BARRIER
				|| matrix[p.x + 1][p.y] == matrix[p.x][p.y]) {
			return true;
		}
		return false;

	}

	public boolean A() {
		List<Point> list = new LinkedList<>();
		for (int i = 1; i < row - 1; i++) {
			for (int j = 1; j < col - 1; j++) {

				int a = matrix[i][j];
				if (a == NOT_BARRIER)
					continue;
				Point p = new Point(i, j);
				if (ktduongdi(p)) {
					list.add(p);
				}
			}
		}
		Collections.sort(list, new Comparator<Point>() {

			@Override
			public int compare(Point o1, Point o2) {
				// TODO Auto-generated method stub
				if (matrix[o1.x][o1.y] > matrix[o2.x][o2.y])
					return 1;
				if (matrix[o1.x][o1.y] < matrix[o2.x][o2.y])
					return -1;
				return 0;
			}
		});
		/*for (int i = 0; i < list.size() - 1; i++) {
			Point _p1 = list.get(i);
			Point _p2 = list.get(i + 1);
			if (matrix[_p1.x][_p1.y] != matrix[_p2.x][_p2.y])
				list.remove(i);
		}*/
		int soLuong = 0;
		for (int i = 0; i < list.size(); i = i + soLuong) {
			soLuong = 0;
			while (soLuong +1  < list.size() &&getValue(list.get(soLuong)) == getValue(list
					.get(soLuong + 1))) {
				soLuong++;
			}
			++soLuong;
			for (int a = 0; a < soLuong-1; a++) {
				for (int b = a+1; b < soLuong;) {
					Point _p1 = list.get(a);
					Point _p2 = list.get(b);
					System.out.println(_p1);
					System.out.println(_p2);
					if (climbing(_p1, _p2) != null)
						System.out.println("true");
						return true;
				}
			}
		}
this.showMatrix();
		return false;

	}

	int getValue(Point p) {
		return matrix[p.x][p.y];
	}
}
