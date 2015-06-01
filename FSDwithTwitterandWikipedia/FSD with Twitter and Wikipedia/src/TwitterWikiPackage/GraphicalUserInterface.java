package TwitterWikiPackage;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.JTextArea;

import twitter4j.TwitterException;

import ch.epfl.lis.networks.NetworkException;

import Dwt.Main;


public class GraphicalUserInterface {

	private JFrame frmT;
	private JTextArea txtrConsole;
	private JTextArea txtrDetectedEventKeyWords;
	private JScrollPane scrollPane;
	Set<String> eventKeyWords = new HashSet<String> ();
	Set<String> fsd = new HashSet<String>();
	private JButton btnExit;
	private JPanel panel;
	private DefaultComboBoxModel model;
	private JComboBox comboBox;
	private JButton btnChangeParameters;
	private JButton btnFetchTweets;
	private JLabel lblDetectedEventKeywords;
	private JLabel lblGeneralInformation;
	private JTextArea wikiEvents;
	private JTextArea detectedFirstStories;
	private JLabel lblWikipediaEvents;
	private JLabel lblDetectedFirstStories;
	private JButton btnFetchTweets_1;
	private JLabel lblPic;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	private JScrollPane scrollPane_3;
	private JScrollPane scrollPane_4;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphicalUserInterface window = new GraphicalUserInterface();
					window.frmT.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GraphicalUserInterface() {
		initialize();
		run();
	}
	 
	 public void run()
	    {
	        redirectSystemStreams();
              
	    }
	     
	//The following codes set where the text get redirected  
	  private void updateTextArea(final String text) {
	    SwingUtilities.invokeLater(new Runnable() {
	      public void run() {
	    	  txtrConsole.append(text);
	      }
	    });
	  }
	 
	//Followings are The Methods that do the Redirect
	  private void redirectSystemStreams() {
	    OutputStream out = new OutputStream() {
	      @Override
	      public void write(int b) throws IOException {
	        updateTextArea(String.valueOf((char) b));
	      }
	 
	      @Override
	      public void write(byte[] b, int off, int len) throws IOException {
	        updateTextArea(new String(b, off, len));
	      }
	 
	      @Override
	      public void write(byte[] b) throws IOException {
	        write(b, 0, b.length);
	      }
	    };
	 
	    System.setOut(new PrintStream(out, true));
	    System.setErr(new PrintStream(out, true));
	  }

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmT = new JFrame();
		frmT.setTitle("First Story Detection (FSD) in Twitter with Wikipedia");
		frmT.setBounds(100, 100, 1021, 575);
		frmT.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmT.getContentPane().setLayout(null);
		
		JButton btnDetectevent = new JButton("Detect Events");
		btnDetectevent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtrDetectedEventKeyWords.setText(null);
				txtrConsole.setText(null);
				panel = new JPanel();
		        panel.add(new JLabel("Please select date to get events:"));
		        model = new DefaultComboBoxModel();
		        model.addElement("Default");
		        model.addElement("20111031");
		        model.addElement("20150521");
		        model.addElement("20150530");
		        model.addElement("20150531");
		        model.addElement("20150601");
		        comboBox = new JComboBox(model);
		        panel.add(comboBox);

		        int result = JOptionPane.showConfirmDialog(null, panel, "Date", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		        switch (result) {
		            case JOptionPane.OK_OPTION:
		                System.out.println("You selected " + comboBox.getSelectedItem());
		                
		                if(comboBox.getSelectedItem().toString().equalsIgnoreCase("Default")){
		                	try {
		                		new Thread(new Runnable() {
		                	        public void run() {
		                	        	try {
		                	        		Main.edcowMain("");
		                	        	} catch (Exception e) {
											e.printStackTrace();
										}
		                	        	eventKeyWords = Main.getEventKeyWords();
		                	        	if(eventKeyWords.isEmpty()||eventKeyWords == null){
		        							txtrDetectedEventKeyWords.append("No Events are detected!");
		        						}
		        						else{
		        							for(String event:eventKeyWords){
		        								txtrDetectedEventKeyWords.append(event+"\n");
		        							}
		        						}
		                	        }
		                	    }).start(); 
							} catch (Exception e) {
								e.printStackTrace();
							}
		                }else{
		                	try {
		                		new Thread(new Runnable() {
		                	        public void run() {
		                	        	try {
											Main.edcowMain(comboBox.getSelectedItem().toString());
										} catch (Exception e) {
											e.printStackTrace();
										}
										eventKeyWords = Main.getEventKeyWords();
										if(eventKeyWords.isEmpty()||eventKeyWords == null){
											txtrDetectedEventKeyWords.append("No Events keyword are detected!");
										}
										else{
											for(String event:eventKeyWords){
												txtrDetectedEventKeyWords.append(event+"\n");
												if(Main.keywordswithwiki(event))fsd.add(event);
											}
											if(fsd.size()>0){
							                	for(String s:fsd){
								                	detectedFirstStories.append(s);
								                }
							                }else{
							                	detectedFirstStories.append("No events detected after filering with Wikipedia");
							                }
										}
		                	            }
		                	    }).start();         
								
							} catch (Exception e) {
								e.printStackTrace();
							}
		                }
		                
		                break;
		        }
			}
		});
		btnDetectevent.setBounds(24, 466, 157, 23);
		frmT.getContentPane().add(btnDetectevent);
		
		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmT.dispose();
				System.exit(0);
			}
		});
		btnExit.setBounds(794, 466, 67, 23);
		frmT.getContentPane().add(btnExit);
		
		btnChangeParameters = new JButton("Change Parameters");
		btnChangeParameters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtrDetectedEventKeyWords.setText(null);
				txtrConsole.setText(null);
				ChangeParameters.changeParams();
			}
		});
		btnChangeParameters.setBounds(209, 466, 160, 23);
		frmT.getContentPane().add(btnChangeParameters);
		
		btnFetchTweets = new JButton("Generate KeyWords");
		btnFetchTweets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtrDetectedEventKeyWords.setText(null);
				txtrConsole.setText(null);
				try {
					InputToEDCoW.inputToEdcow();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Finished Generating keywords!");
			}
		});
		btnFetchTweets.setBounds(391, 466, 200, 23);
		frmT.getContentPane().add(btnFetchTweets);
		
		lblDetectedEventKeywords = new JLabel("Twitter Event KeyWords");
		lblDetectedEventKeywords.setBounds(24, 153, 156, 36);
		frmT.getContentPane().add(lblDetectedEventKeywords);
		
		lblGeneralInformation = new JLabel("Progress");
		lblGeneralInformation.setBounds(24, 379, 200, 28);
		frmT.getContentPane().add(lblGeneralInformation);
		
		lblWikipediaEvents = new JLabel("Wikipedia Events/Page Titles");
		lblWikipediaEvents.setBounds(181, 164, 223, 14);
		frmT.getContentPane().add(lblWikipediaEvents);
		
		lblDetectedFirstStories = new JLabel("Final Detected First Stories/Tweets");
		lblDetectedFirstStories.setBounds(492, 61, 323, 14);
		frmT.getContentPane().add(lblDetectedFirstStories);
		
		btnFetchTweets_1 = new JButton("Fetch Tweets");
		btnFetchTweets_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					TweetsFetcher.fetchTweets();
				} catch (TwitterException | IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnFetchTweets_1.setBounds(627, 466, 127, 23);
		frmT.getContentPane().add(btnFetchTweets_1);
		
		scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(24, 403, 406, 39);
		frmT.getContentPane().add(scrollPane_4);
		
		txtrConsole = new JTextArea();
		scrollPane_4.setViewportView(txtrConsole);
		
		lblPic = new JLabel("Pic");
		Image TwitterWikiPic = new ImageIcon(this.getClass().getResource("/TwitterWikiPic.jpg")).getImage();

		lblPic.setIcon(new ImageIcon(TwitterWikiPic));
		lblPic.setBounds(24, 11, 237, 145);
		frmT.getContentPane().add(lblPic);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(22, 184, 141, 184);
		frmT.getContentPane().add(scrollPane_1);
		
		txtrDetectedEventKeyWords = new JTextArea();
		scrollPane_1.setViewportView(txtrDetectedEventKeyWords);
		txtrDetectedEventKeyWords.setToolTipText("");
		
		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(181, 184, 249, 184);
		frmT.getContentPane().add(scrollPane_2);
		
		wikiEvents = new JTextArea();
		scrollPane_2.setViewportView(wikiEvents);
		
		scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(492, 83, 369, 285);
		frmT.getContentPane().add(scrollPane_3);
		
		detectedFirstStories = new JTextArea();
		scrollPane_3.setViewportView(detectedFirstStories);
	}
}