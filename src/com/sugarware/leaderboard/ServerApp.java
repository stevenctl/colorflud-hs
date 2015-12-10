package com.sugarware.leaderboard;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ServerApp extends JFrame implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerApp frame = new ServerApp();
					
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	JTextArea textArea ;
	 Leaderboard l ;
	public ServerApp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(51, 162, 117, 29);
		contentPane.add(btnAdd);
		btnAdd.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Socket sock = new Socket("localhost",RECEPTION);
					 PrintStream ps = new PrintStream(sock.getOutputStream());
					ps.println(textField.getText());
					ps.close();
					sock.close();
					textField.setText("");
					
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		});
		
		
		textField = new JTextField();
		textField.setBounds(6, 124, 185, 26);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textArea = new JTextArea();
		textArea.setBounds(207, 20, 172, 206);
		contentPane.add(textArea);
		l =   new Leaderboard();
		new Thread(this).start();
	}
	
	static final int RECEPTION = 9799;
	static final int REPLY = 8888;
	private JTextField textField;
	
	@Override
	public void run() {
		
		
		try{
		while(true){
			ServerSocket ssocket = new ServerSocket(RECEPTION);
			textArea.append(ssocket.getLocalSocketAddress() + "\n");
			Socket socket = ssocket.accept();
			InputStreamReader ir = new InputStreamReader(socket.getInputStream());
			BufferedReader r = new BufferedReader(ir);
			String in = r.readLine();
			System.out.println("Recieved Packet: " + in);
			//boolean = in.split(" ")[0].equals("score");
			boolean score = false;
		
			
				try{
					score  = in.split(" ")[0].equals("score");
					
				}catch(Exception e){
				
					e.printStackTrace();
				}
				
			
			if(score)
				try{
				l.add(in.split(" ")[1], Integer.parseInt(in.split(" ")[2]));
				
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("Reading From File...");
			textArea.setText("");
			File f = new File("scores.dat");
		//	FileWriter fw = new FileWriter(f);
			PrintWriter pw = new PrintWriter(f);
			PrintStream ps = new PrintStream(socket.getOutputStream());
			ps.println(l.getScores().size());
			for(Score s : l.getScores()){
				textArea.append(s.name + ", " + s.score + '\n' );
				pw.println(s.name + " " + s.score);
				ps.println(s.name + " " + s.score);
			}
			pw.close();
			ssocket.close();
		}
		
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
	}
}
