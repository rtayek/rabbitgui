package com.tayek.io.swing;
import static com.tayek.io.IO.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import javax.swing.*;
@SuppressWarnings("serial") public class TextAreaOutputStreamTest extends JPanel {
	private JTextArea textArea=new JTextArea(15,30);
	private TextAreaOutputStream taOutputStream=new TextAreaOutputStream(textArea,"Test");
	public TextAreaOutputStreamTest() {
		setLayout(new BorderLayout());
		add(new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		System.setOut(new PrintStream(taOutputStream));
		int timerDelay=1000;
		new Timer(timerDelay,new ActionListener() {
			int count=0;
			@Override public void actionPerformed(ActionEvent arg0) {
				// though this outputs via System.out.println, it actually
				// displays
				// in the JTextArea:
				p("Count is now: "+count+" seconds");
				count++;
			}
		}).start();
	}
	private static void createAndShowGui() {
		JFrame frame=new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new TextAreaOutputStreamTest());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGui();
			}
		});
	}
}