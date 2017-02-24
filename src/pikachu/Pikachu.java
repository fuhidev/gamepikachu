package pikachu;

import java.awt.Point;
import java.util.List;
import java.util.Observable;


public class Pikachu extends Observable {
	Algorithm algorithm;
	public static final int MAX_COL = 12;
	public static final int MAX_ROW = 10;
	private int soCapHinhDaAn = 0;
	int level = 1;
	public static final int THOIGIAN = 240;
	public static final long DIEM_DAT_DUOC = 20;
	public static final int MAX_RELOAD = 10;
	private int noReload=0;
	long score;

	public Pikachu() {
		// TODO Auto-generated constructor stub
		algorithm = new Algorithm(MAX_ROW, MAX_COL);
		score=0;
	}

	public Pikachu(Algorithm algorithm) {
		super();
		this.algorithm = algorithm;
	}

	public int[][] newGame() {
		score=0;
		level = 1;
		algorithm.createMatrix();
		return algorithm.getMatrix();
	}

	public int[][] getMap() {
		return algorithm.getMatrix();
	}

	public int[][] reloadMap() {
		if(noReload < MAX_RELOAD){
		algorithm.changeMatrix();
		noReload++;
		}
		return algorithm.getMatrix();
	}
	
	public long getScore() {
		return score;
	}

	public void upLevel() {
		algorithm.createMatrix();
		++level;
		soCapHinhDaAn=0;
		switch (level) {
		case 1:
			score+=100;
			break;
		case 2:
			score+=200;
			break;
		case 3:
			score+=300;
			break;
		case 4:
			score+=400;
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
		default:
			break;
		}
		
	}

	public boolean isWin() {
		int num = ((MAX_ROW - 2) * (MAX_COL - 2)) / 2;
		// num = 3;
		if (soCapHinhDaAn == num) {
			soCapHinhDaAn = 0;
			return true;
		}
		return false;
	}

	public void Lose() {
		soCapHinhDaAn=0;
	}
	private List<Point> patch;
	public boolean isCorrect(Point p1,Point p2) {
		boolean result = algorithm.isCorrect(p1,p2);
		if(!result)
			return false;
		 setPatch(algorithm.getPatch());
		switch (level) {
		case 1:
			break;
		case 2:
			algorithm.chuyenHinhLenTren();
			break;
		case 3:
			algorithm.chuyenHinhXuongDuoi();
			break;
		case 4:
			algorithm.chuyenHinhSangBenPhai();
			break;
		case 5:
			algorithm.chuyenHinhSangBenTrai();
			break;
		case 6:
			algorithm.chuyenHinhLenTren();
			algorithm.chuyenHinhXuongDuoi();
			break;
		case 7:
		default:
			break;
		}
		score +=DIEM_DAT_DUOC;
		soCapHinhDaAn++;
		/*boolean conDuongDi = algorithm.A();
		if(!conDuongDi && !isWin()){
			reloadMap();
		}*/
		return result;
	}

	public List<Point> getPatch() {
		return patch;
	}

	public void setPatch(List<Point> lst) {
		this.patch = lst;
	}
}
