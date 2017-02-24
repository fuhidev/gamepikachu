package pikachu;

import java.awt.event.*;

import sun.audio.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import com.sun.media.jfxmedia.Media;

import java.io.*;
import java.net.URL;

public class MySound {
	private static final String PATCH = "audio";
	private static final String EXTENSION = ".mp3";
	public static final String AM_THANH_DAU_TIEN = "beep1";
	public static final String AM_THANH_DUNG = "beep2";
	public static final String AM_THANH_SAI = "beep3";

	public static synchronized void music(String fileName) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String f = PATCH + "\\" + fileName + EXTENSION;
				try {
					Player player = new Player(new FileInputStream(f));
					player.play();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JavaLayerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

	}
}
