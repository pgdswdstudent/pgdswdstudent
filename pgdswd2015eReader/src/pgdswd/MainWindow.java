package pgdswd;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * @author Sipho Maleka
 * @version 1.0
 */
public class MainWindow extends JFrame implements ActionListener, ChangeListener  {
	private JButton load,nextPage,prePage;
	private JTextPane tPane;
	private JScrollPane sPane;
	private JPanel pnlFontName,pnlFontStyle,pnlFontSize,pnlBotBtnHold,pnlBot,pnlTopOFBot,pnlBotOFBot;
	private ButtonGroup bgFontName,bgFontStyle;
	private JRadioButton rbArial,rbCourier,rbTNRoman,rbStandard,rbBold,rbItalic;
	private JSlider sFontSize;
	private Font theFont;
	private JMenu mainMenu;
	private JMenuBar mmBar;
	private JMenuItem miPrePage,miNextPage,miLoadBook;
	//update some functionalities for lab10a, add menu to show multiple book and "close all" menu item
	private JMenu mainMenuWindow;
	//add a string array to store the books' name, add another String array to store the books' content
	private String[] booksName,booksContent;
	//add a variable to track how many books have you opened
	private int bookCnt;

	
/*	private JComboBox cbFontName;
*/	
	
	private final JFileChooser fc=new JFileChooser();
	
	public MainWindow(){
		this.setLayout(new GridLayout(2,1));
		this.setSize(600, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("EReader");
		//for lab10a, initialize the String array
		booksName=new String[10];
		booksContent=new String[10];
		bookCnt=0;

		theFont=new Font("Arial", Font.PLAIN, 12);
		tPane=new JTextPane();
		tPane.setFont(theFont);
		tPane.setText("No book currently loaded");
		tPane.setEditable(false);		
		sPane=new JScrollPane(tPane);
		
		//------menu
		mainMenu=new JMenu("Actions");
		mmBar=new JMenuBar();
		miPrePage= new JMenuItem("Previous Page");
		miNextPage=new JMenuItem("Next Page");
		miLoadBook=new JMenuItem("Load Book");
		miPrePage.addActionListener(this);
		miNextPage.addActionListener(this);
		miLoadBook.addActionListener(this);	
		mainMenu.add(miPrePage);
		mainMenu.add(miNextPage);
		mainMenu.addSeparator();
		mainMenu.add(miLoadBook);
		mmBar.add(mainMenu);
		this.setJMenuBar(mmBar);
		//create window menu and add it to menu bar
		mainMenuWindow=new JMenu("Window");
		mmBar.add(mainMenuWindow);
		
		//------first group, font name 	
		//pnlFontName hold 3 font name radio buttons
		bgFontName=new ButtonGroup();
		pnlFontName=new JPanel();
		pnlFontName.setBorder(BorderFactory.createTitledBorder("Font Name"));
		pnlFontName.setLayout(new GridLayout(3,1));
		
		rbArial=new JRadioButton("Arial");		
		rbCourier=new JRadioButton("Courier");
		rbTNRoman=new JRadioButton("Times New Roman");
		
		rbArial.addActionListener(this);
		rbCourier.addActionListener(this);
		rbTNRoman.addActionListener(this);	
		bgFontName.add(rbArial);
		bgFontName.add(rbCourier);
		bgFontName.add(rbTNRoman);
		
		pnlFontName.add(rbArial);
		pnlFontName.add(rbCourier);
		pnlFontName.add(rbTNRoman);
		rbArial.setSelected(true);
			
		// another group, font style
		//pnlFontStyle hold there style radio buttons
		bgFontStyle=new ButtonGroup();
		pnlFontStyle=new JPanel();
		pnlFontStyle.setBorder(BorderFactory.createTitledBorder("Font Style"));
		pnlFontStyle.setLayout(new GridLayout(3,1));
		
		rbStandard=new JRadioButton("Standard");		
		rbBold=new JRadioButton("Bold");
		rbItalic=new JRadioButton("Italic");
		
		rbStandard.addActionListener(this);
		rbBold.addActionListener(this);
		rbItalic.addActionListener(this);	
		bgFontStyle.add(rbStandard);
		bgFontStyle.add(rbBold);
		bgFontStyle.add(rbItalic);
		
		rbStandard.setSelected(true);
		pnlFontStyle.add(rbStandard);
		pnlFontStyle.add(rbBold);
		pnlFontStyle.add(rbItalic);
		

		//font size
		sFontSize=new JSlider(JSlider.HORIZONTAL,10,68,12);
		sFontSize.addChangeListener(this);	
		
		//pnlFontSize, it hold JSlider
		pnlFontSize=new JPanel();
		pnlFontSize.setBorder(BorderFactory.createTitledBorder("Font Size"));
		pnlFontSize.setLayout(new GridLayout(1,1));
		pnlFontSize.add(sFontSize);
		
		//panel pnlBotOFBot, it hold pnlFontSize,pnlBotBtnHold
		pnlBotOFBot=new JPanel();
		pnlBotOFBot.setBorder(BorderFactory.createEmptyBorder());
		pnlBotOFBot.setLayout(new BorderLayout());		
		pnlBotOFBot.add(pnlFontSize,BorderLayout.CENTER);
		
		nextPage=new JButton("Next Page");
		prePage=new JButton("Previous Page");
		load=new JButton("Load Book");
		
		//pnlBotBtnHold hold 3 buttons
		pnlBotBtnHold =new JPanel();
		pnlBotBtnHold.setBorder(BorderFactory.createEmptyBorder());
		pnlBotBtnHold.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pnlBotBtnHold.add(prePage);
		pnlBotBtnHold.add(nextPage);
		pnlBotBtnHold.add(load);
		
		pnlBotOFBot.add(pnlBotBtnHold,BorderLayout.SOUTH);
		
		//pnlBot hold pnlBotOFBot,pnlTopOFBot
		pnlBot=new JPanel();
		pnlBot.setBorder(BorderFactory.createEmptyBorder());
		pnlBot.setLayout(new GridLayout(2,1));
		
		//pnlTopOFBot hold pnlFontName,pnlFontStyle
		pnlTopOFBot=new JPanel();
		pnlTopOFBot.setBorder(BorderFactory.createEmptyBorder());
		pnlTopOFBot.setLayout(new GridLayout(1,2));	
		pnlTopOFBot.add(pnlFontName);
		pnlTopOFBot.add(pnlFontStyle);
		
		nextPage.addActionListener(this);
		prePage.addActionListener(this);
		load.addActionListener(this);
		
		this.getContentPane().add(sPane);
		this.getContentPane().add(pnlBot);
		
		pnlBot.add(pnlTopOFBot);
		pnlBot.add(pnlBotOFBot);
		
		
	}
	
	public void stateChanged(ChangeEvent cE){
		JSlider sldSource=(JSlider)cE.getSource();
		if (sldSource.getValueIsAdjusting()){
			theFont=theFont.deriveFont((float)sldSource.getValue());
			tPane.setFont(theFont);		
		}
	}
	
	public void processEvent(AWTEvent e){
		if(e.toString().contains("WINDOW_CLOSING")){
			int rVal=JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit EReader", 
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(rVal==0)	{
				System.exit(0);
				super.processEvent(e);
			}		
		}
	}
	
	public void actionPerformed(ActionEvent aE)
	{
		if(aE.getActionCommand().equals("Load Book")){
			//add for lab10a. here we first look into how many books have been opened already, if it is already 10, then show message box 
			if(bookCnt==10){
				JOptionPane.showMessageDialog(this, "You cannot open any more books", "Maximum books", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			int rVal=fc.showOpenDialog(this);
			if(rVal==JFileChooser.APPROVE_OPTION){
				this.setTitle("Loading File");
				String myFile=fc.getSelectedFile().getAbsolutePath();
				String myPureFileName=fc.getSelectedFile().getName();
				FileReader fr;
				try {
					fr = new FileReader(myFile);
					BufferedReader br=new BufferedReader(fr);
					String curLine="",allLines="";
					curLine=br.readLine();
					while(curLine!=null){
						allLines=allLines+curLine+"\n";
						curLine=br.readLine();
					}
					
					br.close();
					//modify for lab10a, here we store the book's name and content to correspond array.
					booksName[bookCnt]=myPureFileName;
					booksContent[bookCnt]=allLines;
					tPane.setText(booksContent[bookCnt]);					
					this.setTitle(booksName[bookCnt]);
					//here we add a new menu item
					JMenuItem miTemp;
					if(bookCnt==0){
						miTemp=new JMenuItem("Close All");
						miTemp.addActionListener(this);
						mainMenuWindow.add(miTemp);
						mainMenuWindow.addSeparator();
					}
					miTemp=new JMenuItem(bookCnt+":"+booksName[bookCnt]);
					miTemp.addActionListener(this);
					mainMenuWindow.add(miTemp);
					bookCnt++;
					
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else if(aE.getActionCommand().equals("Arial")){
			theFont=new Font("Arial",theFont.getStyle(),theFont.getSize());
			tPane.setFont(theFont);
		}
		else if(aE.getActionCommand().equals("Courier")){
			theFont=new Font("Courier",theFont.getStyle(),theFont.getSize());
			tPane.setFont(theFont);
		}
		else if(aE.getActionCommand().equals("Times New Roman")){
			theFont=new Font("Times New Roman",theFont.getStyle(),theFont.getSize());
			tPane.setFont(theFont);
		}
		else if(aE.getActionCommand().equals("Standard")){
			theFont=theFont.deriveFont(Font.PLAIN);
			tPane.setFont(theFont);
		}
		else if(aE.getActionCommand().equals("Bold")){
			theFont=theFont.deriveFont(Font.BOLD);
			tPane.setFont(theFont);
		}	
		else if(aE.getActionCommand().equals("Italic")){
			theFont=theFont.deriveFont(Font.ITALIC);
			tPane.setFont(theFont);
		}			
		else if(aE.getActionCommand().equals("Next Page")){
			sPane.getVerticalScrollBar().setValue(sPane.getVerticalScrollBar().getValue()+sPane.getHeight());
		}	
		else if(aE.getActionCommand().equals("Previous Page")){
			sPane.getVerticalScrollBar().setValue(sPane.getVerticalScrollBar().getValue()-sPane.getHeight());
		}
		else if (aE.getActionCommand().equals("Close All")){
			int rVal=JOptionPane.showConfirmDialog(this, "Are you sure you want to close all the book windows?", "Close Books", 
													JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(rVal==0){
				//sure to close all then we delete menu items on the "Window" menu
				mainMenuWindow.removeAll();
				booksContent=new String[10];
				booksName=new String[10];
				tPane.setText("No book currently loaded");
				bookCnt=0;
				this.setTitle("EReader");
			}
		}
		else if (aE.getActionCommand().substring(0, aE.getActionCommand().indexOf(':')).equals("0")){
			tPane.setText(booksContent[0]);
			this.setTitle(booksName[0]);
		}
		else if (aE.getActionCommand().substring(0, aE.getActionCommand().indexOf(':')).equals("1")){
			tPane.setText(booksContent[1]);
			this.setTitle(booksName[1]);
		}	
		else if (aE.getActionCommand().substring(0, aE.getActionCommand().indexOf(':')).equals("2")){
			tPane.setText(booksContent[2]);
			this.setTitle(booksName[2]);
		}
		else if (aE.getActionCommand().substring(0, aE.getActionCommand().indexOf(':')).equals("3")){
			tPane.setText(booksContent[3]);
			this.setTitle(booksName[3]);
		}
		else if (aE.getActionCommand().substring(0, aE.getActionCommand().indexOf(':')).equals("4")){
			tPane.setText(booksContent[4]);
			this.setTitle(booksName[4]);
		}
		else if (aE.getActionCommand().substring(0, aE.getActionCommand().indexOf(':')).equals("5")){
			tPane.setText(booksContent[5]);
			this.setTitle(booksName[5]);
		}
		else if (aE.getActionCommand().substring(0, aE.getActionCommand().indexOf(':')).equals("6")){
			tPane.setText(booksContent[6]);
			this.setTitle(booksName[6]);
		}
		else if (aE.getActionCommand().substring(0, aE.getActionCommand().indexOf(':')).equals("7")){
			tPane.setText(booksContent[7]);
			this.setTitle(booksName[7]);
		}
		else if (aE.getActionCommand().substring(0, aE.getActionCommand().indexOf(':')).equals("8")){
			tPane.setText(booksContent[8]);
			this.setTitle(booksName[8]);
		}
		else if (aE.getActionCommand().substring(0, aE.getActionCommand().indexOf(':')).equals("9")){
			tPane.setText(booksContent[9]);
			this.setTitle(booksName[9]);
		}
	}
	

}
