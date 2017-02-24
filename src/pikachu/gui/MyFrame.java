package pikachu.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import pikachu.MySound;
import pikachu.Pikachu;
import pikachu.ngonngu.English;
import pikachu.ngonngu.Language;

public class MyFrame extends JFrame implements Observer {
	/**
	 * 
	 */

	private static final String EXTENSION = ".jpg";
	private static final String PATCH = "icon";

	private static final long serialVersionUID = 1L;
	private static int frame_width;
	private static int frame_height;
	private static final int frame_origin_x = 300;
	private static final int frame_origin_y = 50;
	public static final int IMAGE_SIZE = 70;

	JProgressBar bar = new JProgressBar(JProgressBar.HORIZONTAL, 0,
			Pikachu.THOIGIAN);
	int counter = 1;

	Timer timer = new Timer(1000, new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			bar.setValue(++counter);
			if (counter > Pikachu.THOIGIAN) {
				int result = JOptionPane.showConfirmDialog(MyFrame.this,
						language.getNotify_Deadline(), language.getNotify(),
						JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					pikachu.Lose();
					newGame();
				} else {
					timer.stop();
					setVisible(false);
					dispose();
				}
				// timer.stop();

			}
		}
	});

	JLabel labelScore = null;
	JLabel labelThoiGian = null;

	Pikachu pikachu;
	JButton[][] btn;
	Point diemClickLanDau = null;
	boolean isFirstClicked = true;

	Language language;
	MyPanel panelPikachu;
	
	private void setBackGroundImage() {
		Random random = new Random();
		int indexImageBackground = random.nextInt(5);
		try {
			BufferedImage img = ImageIO.read(new File("background\\"
					+ indexImageBackground + ".jpg"));
			panelPikachu.setImg(img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		panelPikachu.repaint();
	}

	public MyFrame(Pikachu pikachu) {
		// add(CreateStatusPanel());

		this.pikachu = pikachu;
		this.pikachu.addObserver(this);
		language = new English();
		setLayout(new BorderLayout());
		setTitle(language.getTITLE());
		frame_width = IMAGE_SIZE * Pikachu.MAX_COL+ 200;
		frame_height = IMAGE_SIZE * Pikachu.MAX_ROW ;

		setSize(frame_width, frame_height);
		setResizable(false);
		setLocation(frame_origin_x, frame_origin_y);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panelPikachu = new MyPanel();
		Image image = new ImageIcon("background\\1.jpg").getImage();
		this.setIconImage(image);
		panelPikachu
				.setLayout(new GridLayout(Pikachu.MAX_ROW, Pikachu.MAX_COL));
		bar.setValue(0);
		btn = new JButton[Pikachu.MAX_ROW][Pikachu.MAX_COL];
		for (int i = 0; i < Pikachu.MAX_ROW; i++) {
			for (int j = 0; j < Pikachu.MAX_COL; j++) {
				btn[i][j] = new JButton();
				panelPikachu.add(btn[i][j]);
			}
		}
		loadMatrix(pikachu.getMap());
		JPanel panelControl = new JPanel();
		panelControl.setSize(1000, frame_height);
		JPanel panelLeft = new JPanel(new GridLayout(0, 1, 5, 5));
		labelScore = new JLabel(language.getSCORE() + ":0");
		labelThoiGian = new JLabel(language.getTIME() + ":");
		timer.start();
		panelLeft.add(labelScore);
		panelLeft.add(bar);
		JButton btnNewGame = new JButton(language.getNEWGAME());
		btnNewGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				newGame();
			}

		});

		JButton btnChange = new JButton(language.getChange());
		btnChange.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadMatrix(pikachu.reloadMap());
			}

		});
		setBackGroundImage();
		panelLeft.add(btnChange);
		panelLeft.add(btnNewGame);
		panelControl.add(panelLeft);
		this.add(panelPikachu, BorderLayout.CENTER);
		this.add(panelControl, BorderLayout.EAST);

	}

	
	private void newGame() {
		updateScore();
		loadMatrix(pikachu.newGame());
		counter = 1;
		setBackGroundImage();

	}

	private Icon getIcon(int value) {
		String patch = PATCH + "\\pikachu (" + value + ")" + EXTENSION;
		Image image = new ImageIcon(patch).getImage();
		Icon icon = new ImageIcon(image.getScaledInstance(IMAGE_SIZE,
				IMAGE_SIZE, Image.SCALE_SMOOTH));
		return icon;
	}

	void updateScore() {
		labelScore.setText(language.getSCORE() + ": " + pikachu.getScore());
	}

	Point p1, p2;

	private void loadMatrix(int[][] map) {
		// TODO Auto-generated method stub
		for (int i = 0; i < Pikachu.MAX_ROW; i++) {
			for (int j = 0; j < Pikachu.MAX_COL; j++) {
				int x = i;
				int y = j;
				int value = map[i][j];
				if (value == 0) {
					btn[i][j].setVisible(false);
				} else {
					btn[i][j].setVisible(true);
					btn[i][j].setIcon(getIcon(value));

					if (btn[i][j].getActionListeners().length == 0) {
						btn[i][j].addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent arg0) {
								if (p1 == null) {
									p1 = new Point(x, y);
									btn[x][y].setBorder(new LineBorder(
											Color.green));
									MySound.music(MySound.AM_THANH_DAU_TIEN);
									return;
								} else {
									Point tmp = new Point(x, y);
									if (tmp.x == p1.x && tmp.y == p1.y) {
										return;
									}
									p2 = tmp;
									boolean isCorrect = pikachu.isCorrect(p1,
											p2);
									if (isCorrect) {
										resetBorderButton(p1);
										MySound.music(MySound.AM_THANH_DUNG);
										List<Point> patch = pikachu.getPatch();
										drawPatch(patch);
										updateScore();
										loadMatrix(pikachu.getMap());
										isWin();
										p1 = null;
										p2 = null;
									} else {
										resetBorderButton(p1);
										MySound.music(MySound.AM_THANH_SAI);
										
										p1 = null;
										p2 = null;
									}
								}
							}

						});
					}
				}
			}
		}
	}

	void isWin() {
		if (pikachu.isWin()) {
			int result = JOptionPane.showConfirmDialog(MyFrame.this,
					language.getWINNER(), language.getNotify(),
					JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				upLevel();

			} else {
				setVisible(false);
				dispose();
			}
		}
	}

	void drawPatch(List<Point> patch) {
		panelPikachu.setPatch(patch);
		panelPikachu.repaint();
	}

	private void upLevel() {
		pikachu.upLevel();
		counter = 1;
		loadMatrix(pikachu.getMap());
		panelPikachu.repaint();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Pikachu) {
			int result = JOptionPane.showConfirmDialog(MyFrame.this,
					"Bạn đã chiến thắng. Nhấn Yes để chơi lại.",
					language.getNotify(), JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				newGame();

			} else {
				setVisible(false);
				dispose();
			}
		}
	}

	private void resetBorderButton(Point p) {
		btn[p.x][p.y].setBorder(UIManager.getBorder("Button.border"));
	}

	private Point getLocate(Point p) {
		Point tmpLocation = btn[p.x][p.y].getLocation();
		Point location = new Point(tmpLocation.x + IMAGE_SIZE / 2,
				tmpLocation.y + IMAGE_SIZE / 2);
		return location;
	}

	private class MyPanel extends JPanel {
		List<Point> patch = null;
		private BufferedImage img;

		public void setPatch(List<Point> patch) {
			this.patch = patch;
		}

		public void setImg(BufferedImage img) {
			this.img = img;
		}

		private static final long serialVersionUID = 1L;

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
			if (patch != null) {
				for (int i = 0; i < patch.size() - 1; i++) {
					Point p1 = patch.get(i);
					Point p2 = patch.get(i + 1);

					Point location1 = getLocate(p1);
					Point location2 = getLocate(p2);
					Graphics2D g2 = (Graphics2D) g;
					g2.setStroke(new BasicStroke(4));
					g2.setColor(Color.green);
					g2.drawLine(location1.x, location1.y, location2.x,
							location2.y);
					g2.setStroke(new BasicStroke(0));
				}

				_timer.start();

			}
		}
	}

	Timer _timer = new Timer(10, new ActionListener() {
		int _counter = 0;

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			++_counter;
			if (_counter > 10) {
				panelPikachu.setPatch(null);
				panelPikachu.repaint();
				_timer.stop();
				_counter = 0;
			} else {
			}
		}
	});
}
