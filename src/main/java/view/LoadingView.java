package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JProgressBar;
import java.awt.Font;

public class LoadingView extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoadingView frame = new LoadingView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoadingView() {
		this.setTitle("Processing");
		JLabel lblLoading = new JLabel("Processing ...");
		lblLoading.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblLoading.setBounds(186, 11, 89, 26);
		lblLoading.setVisible(true);
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setBounds(24, 54, 389, 32);
		progressBar.setVisible(true);		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 141);
		contentPane = new JPanel();		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));		
		contentPane.setLayout(null);				
		contentPane.add(lblLoading);				
		contentPane.add(progressBar);
		setContentPane(contentPane);
		this.setVisible(true);
		
		
	}
}
