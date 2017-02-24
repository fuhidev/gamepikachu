package pikachu.main;

import pikachu.Pikachu;
import pikachu.gui.MyFrame;

public class MyMain {
	MyFrame frame;
	public MyMain(){
		Pikachu pikachu = new Pikachu();
		frame = new MyFrame(pikachu);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new MyMain();

	}

}
