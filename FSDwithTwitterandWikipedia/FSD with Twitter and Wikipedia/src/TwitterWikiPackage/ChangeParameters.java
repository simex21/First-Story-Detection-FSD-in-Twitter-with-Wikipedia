package TwitterWikiPackage;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;


public class ChangeParameters extends JFrame {

	private JFrame changeParams;
	private static JPanel contentPane;
	private static JTextField tweetSampleInterval;
	private static JTextField wikiSampleInterval;
	private static JTextField sizeWindow;
	private static JTextField delta;
	private static JTextField gamma1;
	private static JTextField gamma2;
	private static JTextField threshold;
	private static JTextField pageCount;
	private static JTextField zScore;
	private static JTextField debugMode;
	private static JTextField fetchDuration;
	private static JTextField tweetDate;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JLabel lblNewLabel_5;
	private JLabel lblNewLabel_6;
	private JButton btnOk;

	/**
	 * Launch the application.
	 */
	//public static void main(String[] args) {
	public static void changeParams(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChangeParameters frame = new ChangeParameters();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Set the parameters needed for running the algorithm otherwise the default parameter values will be used
	 */
	public static void setParameters(){
	 
		try {
			FileInputStream input = new FileInputStream("TweetWikiParameters.properties");
			Properties tweetWikiProperties = new Properties();
			tweetWikiProperties.load(input);
			input.close();

			FileOutputStream output = new FileOutputStream("TweetWikiParameters.properties");
			if(tweetSampleInterval.getText()!=null&&!tweetSampleInterval.getText().isEmpty()){
				tweetWikiProperties.setProperty("tweetSample", tweetSampleInterval.getText());
			}
			if(wikiSampleInterval.getText()!=null&&!wikiSampleInterval.getText().isEmpty()){
				tweetWikiProperties.setProperty("wikiSample", wikiSampleInterval.getText());
			}
			if(sizeWindow.getText()!=null&&!sizeWindow.getText().isEmpty()){
				tweetWikiProperties.setProperty("sizeWindow", sizeWindow.getText());
			}
			if(delta.getText()!=null&&!delta.getText().isEmpty()){
				tweetWikiProperties.setProperty("delta", delta.getText());
			}
			if(gamma1.getText()!=null&&!gamma1.getText().isEmpty()){
				tweetWikiProperties.setProperty("gamma1", gamma1.getText());
			}
			if(gamma2.getText()!=null&&!gamma2.getText().isEmpty()){
				tweetWikiProperties.setProperty("gamma2", gamma2.getText());
			}
			if(threshold.getText()!=null&&!threshold.getText().isEmpty()){
				tweetWikiProperties.setProperty("thresholdE", threshold.getText());
			}
			if(pageCount.getText()!=null&&!pageCount.getText().isEmpty()){
				tweetWikiProperties.setProperty("pageCount", pageCount.getText());
			}
			if(zScore.getText()!=null&&!zScore.getText().isEmpty()){
				tweetWikiProperties.setProperty("zScore", zScore.getText());
			}
			if(debugMode.getText()!=null&&!debugMode.getText().isEmpty()){
				tweetWikiProperties.setProperty("debugMode", debugMode.getText());
			}
			if(fetchDuration.getText()!=null&&!fetchDuration.getText().isEmpty()){
				tweetWikiProperties.setProperty("fetchDuration", fetchDuration.getText());
			}
			if(tweetDate.getText()!=null&&!tweetDate.getText().isEmpty()){
				tweetWikiProperties.setProperty("tweetDate", tweetDate.getText());
			}
			tweetWikiProperties.store(output, null);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Create the frame.
	 */
	public ChangeParameters() {
				
		setTitle("Change Algorithm Parameters");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 395, 423);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Tweet Sample Interval(sec)");
		lblNewLabel.setBounds(10, 10, 164, 27);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Wikipedia Sample Interval(hr)");
		lblNewLabel_1.setBounds(10, 37, 164, 33);
		contentPane.add(lblNewLabel_1);
		
		tweetSampleInterval = new JTextField();
		tweetSampleInterval.setBounds(184, 13, 86, 20);
		contentPane.add(tweetSampleInterval);
		tweetSampleInterval.setColumns(10);
		
		wikiSampleInterval = new JTextField();
		wikiSampleInterval.setBounds(184, 43, 86, 20);
		contentPane.add(wikiSampleInterval);
		wikiSampleInterval.setColumns(10);
		
		sizeWindow = new JTextField();
		sizeWindow.setBounds(184, 74, 86, 20);
		contentPane.add(sizeWindow);
		sizeWindow.setColumns(10);
		
		delta = new JTextField();
		delta.setBounds(184, 105, 86, 20);
		contentPane.add(delta);
		delta.setColumns(10);
		
		gamma1 = new JTextField();
		gamma1.setBounds(184, 136, 86, 20);
		contentPane.add(gamma1);
		gamma1.setColumns(10);
		
		gamma2 = new JTextField();
		gamma2.setBounds(184, 167, 86, 20);
		contentPane.add(gamma2);
		gamma2.setColumns(10);
		
		threshold = new JTextField();
		threshold.setBounds(184, 198, 86, 20);
		contentPane.add(threshold);
		threshold.setColumns(10);
		
		pageCount = new JTextField();
		pageCount.setBounds(184, 229, 86, 20);
		contentPane.add(pageCount);
		pageCount.setColumns(10);
		
		zScore = new JTextField();
		zScore.setBounds(184, 260, 86, 20);
		contentPane.add(zScore);
		zScore.setColumns(10);
		
		debugMode = new JTextField();
		debugMode.setBounds(184, 292, 86, 20);
		contentPane.add(debugMode);
		debugMode.setColumns(10);
		
		fetchDuration = new JTextField();
		fetchDuration.setBounds(184, 323, 86, 20);
		contentPane.add(fetchDuration);
		fetchDuration.setColumns(10);
		
		tweetDate = new JTextField();
		tweetDate.setBounds(184, 354, 86, 20);
		contentPane.add(tweetDate);
		tweetDate.setColumns(10);
		
		lblNewLabel_2 = new JLabel("EDCoW Size Window");
		lblNewLabel_2.setBounds(10, 68, 146, 33);
		contentPane.add(lblNewLabel_2);
		
		lblNewLabel_3 = new JLabel("EDCoW Delta");
		lblNewLabel_3.setBounds(10, 96, 154, 38);
		contentPane.add(lblNewLabel_3);
		
		lblNewLabel_4 = new JLabel("EDCoW gamma1");
		lblNewLabel_4.setBounds(10, 133, 146, 27);
		contentPane.add(lblNewLabel_4);
		
		lblNewLabel_5 = new JLabel("EDCoW gamma2");
		lblNewLabel_5.setBounds(10, 164, 154, 27);
		contentPane.add(lblNewLabel_5);
		
		lblNewLabel_6 = new JLabel("EDCoW Threshold");
		lblNewLabel_6.setBounds(10, 196, 146, 25);
		contentPane.add(lblNewLabel_6);
		
		btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setParameters();
			}
		});
		btnOk.setBounds(290, 166, 79, 23);
		contentPane.add(btnOk);
		
		JLabel lblNewLabel_7 = new JLabel("Wikipedia Page Count");
		lblNewLabel_7.setBounds(10, 232, 127, 14);
		contentPane.add(lblNewLabel_7);
		
		JLabel lblNewLabel_8 = new JLabel("Wikipedia Z-Score");
		lblNewLabel_8.setBounds(10, 263, 127, 14);
		contentPane.add(lblNewLabel_8);
		
		JLabel lblNewLabel_9 = new JLabel("EDCoW Debug Mode");
		lblNewLabel_9.setBounds(10, 295, 115, 14);
		contentPane.add(lblNewLabel_9);
		
		JLabel lblNewLabel_11 = new JLabel("Tweet Fetch Total Time(min)");
		lblNewLabel_11.setBounds(10, 326, 146, 14);
		contentPane.add(lblNewLabel_11);
		
		JLabel lblNewLabel_12 = new JLabel("Tweet Date (YYYYMMDD)");
		lblNewLabel_12.setBounds(10, 357, 146, 14);
		contentPane.add(lblNewLabel_12);
	}
}
