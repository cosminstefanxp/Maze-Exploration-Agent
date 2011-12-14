/*
 * Robot Explorer
 * 
 * Stefan-Dobrin Cosmin
 * 342C4
 */

package explorer.gui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import explorer.Cell;
import explorer.MainLauncher;
import explorer.Position;

/**
 * The Class MainFrame that contains the main gui of the application.
 */
public class MainFrame extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2706974349380517130L;

	/** The tglbtn autoplay. */
	private JToggleButton tglbtnAutoplay;

	/** The btn next move. */
	private JButton btnNextMove;

	/** The btn previous move. */
	private JButton btnPreviousMove;

	/** The map canvas. */
	private Canvas mapCanvas;

	private JTextArea textAreaDescription;

	private JScrollPane scrollPane;

	/**
	 * Instantiates a new main frame.
	 */
	public MainFrame(HashMap<Position, Cell> cells) {
		initialize(cells);
		this.setVisible(true);
	}

	/**
	 * Initialize the graphical elements of the Frame;.
	 */
	private void initialize(HashMap<Position, Cell> cells) {

		this.setBounds(100, 100, 1100, 639);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));

		// Header section
		JPanel headerPanel = new JPanel();
		headerPanel.setBorder(new EmptyBorder(5, 0, 25, 0));
		getContentPane().add(headerPanel, BorderLayout.NORTH);
		headerPanel.setLayout(new BorderLayout(0, 0));

		JLabel lblRobotExplorer = new JLabel("Robot Explorer");
		headerPanel.add(lblRobotExplorer, BorderLayout.NORTH);
		lblRobotExplorer.setFont(new Font("Dialog", Font.BOLD, 21));
		lblRobotExplorer.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblStefandobrinCosmin = new JLabel("Stefan-Dobrin Cosmin");
		lblStefandobrinCosmin.setHorizontalAlignment(SwingConstants.CENTER);
		headerPanel.add(lblStefandobrinCosmin, BorderLayout.SOUTH);

		//Map Section
		JPanel mapPanel = new JPanel();
		mapPanel.setBorder(new TitledBorder(null, "Map", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		getContentPane().add(mapPanel, BorderLayout.CENTER);

		mapCanvas = new MapCanvas(cells);
		mapPanel.setLayout(new BorderLayout(0, 0));
		mapPanel.add(mapCanvas, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Legend", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		mapPanel.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		//Legend section
		Canvas legendCanvas=new LegendCanvas();
		panel.add(legendCanvas);
		legendCanvas.setSize(800, 50);

		//Side Section
		JPanel sidePanel = new JPanel();
		getContentPane().add(sidePanel, BorderLayout.EAST);
		sidePanel.setLayout(new MigLayout("", "[117px,grow]", "[15px][0.00px][][][][grow]"));

		JLabel lblSideTitle = new JLabel("Control Panel");
		lblSideTitle.setFont(new Font("Dialog", Font.BOLD, 14));
		lblSideTitle.setHorizontalAlignment(SwingConstants.CENTER);
		sidePanel.add(lblSideTitle, "cell 0 0,growx,aligny center");

		tglbtnAutoplay = new JToggleButton("Autoplay");
		tglbtnAutoplay.setSelected(true);
		tglbtnAutoplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainLauncher.autoplay)
				{
					MainLauncher.debug("Autoplay OFF");
					MainLauncher.autoplay=false;
					MainLauncher.lock.lock();
				}
				else
				{
					MainLauncher.debug("Autoplay ON");
					MainLauncher.autoplay=true;
					MainLauncher.lock.unlock();
				}
			}
		});
		sidePanel.add(tglbtnAutoplay, "cell 0 2,growx");

		btnNextMove = new JButton("Next Move");
		btnNextMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainLauncher.debug("Manual next move!");
				MainLauncher.lock.lock();
				MainLauncher.nextMove();
				MainLauncher.lock.unlock();
			}
		});
		sidePanel.add(btnNextMove, "cell 0 3,growx,aligny center");

		btnPreviousMove = new JButton("Previous Move");
		btnPreviousMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainLauncher.debug("Manual previoues move!");
				MainLauncher.lock.lock();
				MainLauncher.previousMove();
				MainLauncher.lock.unlock();
			}
		});
		sidePanel.add(btnPreviousMove, "cell 0 4");
		
		//Move Description section 
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sidePanel.add(scrollPane, "cell 0 5,grow");
		
		JPanel panelDescription = new JPanel();
		scrollPane.setViewportView(panelDescription);
		panelDescription.setLayout(new BorderLayout(0, 0));
		
		textAreaDescription = new JTextArea();
		textAreaDescription.setColumns(30);
		panelDescription.add(textAreaDescription);
		
	}
	
	/**
	 * Repaints the map canvas.
	 */
	public void repaintMap()
	{
		this.mapCanvas.repaint();
	}

	private int descCount=0;
	/**
	 * Adds the move description.
	 *
	 * @param text the text
	 */
	public void addMoveDescription(String text)
	{
		textAreaDescription.setText(textAreaDescription.getText()+(++descCount)+". "+text+"\n");
	}
}
