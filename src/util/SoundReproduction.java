package util;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundReproduction {
	
	private File audiofile;
	int BUFFER_SIZE = 4096;
	
	public SoundReproduction(String pathFile) {
		this.audiofile = new File(pathFile);
	}
	
	public void reproduce() {
		AudioInputStream audioStream = null;
		try {
			audioStream = AudioSystem.getAudioInputStream(this.audiofile);
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		AudioFormat format = audioStream.getFormat();
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		SourceDataLine audioLine = null;
		try {
			audioLine = (SourceDataLine) AudioSystem.getLine(info);
			audioLine.open(format);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
		audioLine.start();
		 
		byte[] bytesBuffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;
		 
		try {
			while ((bytesRead = audioStream.read(bytesBuffer)) != -1) {
			    audioLine.write(bytesBuffer, 0, bytesRead);
			    audioLine.drain();
			    audioLine.close();
			    audioStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
