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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

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

	/**
	 * Instantiates a new main frame.
	 */
	public MainFrame() {
		initialize();
		this.setVisible(true);
	}

	/**
	 * Initialize the graphical elements of the Frame;.
	 */
	private void initialize() {

		this.setBounds(100, 100, 900, 639);
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

		mapCanvas = new MapCanvas();
		mapPanel.setLayout(new BorderLayout(0, 0));
		mapPanel.add(mapCanvas, BorderLayout.CENTER);

		//Side Section
		JPanel sidePanel = new JPanel();
		getContentPane().add(sidePanel, BorderLayout.EAST);
		sidePanel.setLayout(new MigLayout("", "[117px]", "[15px][0.00px][][][][]"));

		JLabel lblSideTitle = new JLabel("Control Panel");
		lblSideTitle.setFont(new Font("Dialog", Font.BOLD, 14));
		lblSideTitle.setHorizontalAlignment(SwingConstants.CENTER);
		sidePanel.add(lblSideTitle, "cell 0 0,alignx left,aligny center");

		tglbtnAutoplay = new JToggleButton("Autoplay");
		sidePanel.add(tglbtnAutoplay, "cell 0 2,growx");

		btnNextMove = new JButton("Next Move");
		sidePanel.add(btnNextMove, "cell 0 3,growx,aligny center");

		btnPreviousMove = new JButton("Previous Move");
		sidePanel.add(btnPreviousMove, "cell 0 4");
	}

}
