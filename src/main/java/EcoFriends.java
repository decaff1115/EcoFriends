import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class EcoFriends extends JFrame implements Commons {

	private static final long serialVersionUID = -4905230094675077405L;

	private JButton start, help;

	private static final String TOP_MESSAGE = "ECOFRIENDS";
	private static final String INITIAL_MESSAGE = "Attention, Heroic Friend!!!!"
			+ "<br>Our buddy Bara is on the brink of a snack crisis!"
			+ "<br><br> Your mission, should you choose to accept it:"
			+ "<br><br>Join forces with Bara to fend off pesky insects and ruthless pests, ensuring a victorious feast!"
			+ "<br><br>Remember: snacks are life, so don't let Bara down!"
			+ "<br><br><br> 🌟 GOOD LUCK, Snack Savior! 🌟";


	private static final String HELP_TOP_MESSAGE = "Directions";
	private static final String HELP_MESSAGE = "Controls:"
			+ "<br><br>Move Left: <br>A"
			+ "<br><br>Move Right: <br>D"
			+ "<br><br>Shoot: <br>Spacebar";

	JFrame frame = new JFrame("EcoFriends");
	JFrame frame2 = new JFrame("EcoFriends");
	JFrame frame3 = new JFrame("How To Play");


	public EcoFriends() {
		String topMessage = "<html><br><br>" + TOP_MESSAGE + "</html>";
		String message = "<html>" + INITIAL_MESSAGE + "</html>";

		start = new JButton("Play");
		start.addActionListener(new ButtonListener());
		start.setBounds(800, 800, 200, 100);

		help = new JButton("How To Play");
		help.addActionListener(new HelpButton());

		JLabel textLabel = new JLabel(message, SwingConstants.CENTER);
		JLabel topTextLabel = new JLabel(topMessage, SwingConstants.CENTER);

		Font font = new Font("Helvetica", Font.BOLD, 12);
		textLabel.setFont(font);

		Font font2 = new Font("Helvetica", Font.BOLD, 20);
		topTextLabel.setFont(font2);

		frame2.setTitle("EcoFriends");

		frame2.add(textLabel);

		frame2.add(topTextLabel, BorderLayout.PAGE_START);
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(help);
		bottomPanel.add(start);

		frame2.add(bottomPanel, BorderLayout.PAGE_END);
		frame2.setSize(500, 500);
		frame2.setLocationRelativeTo(null);
		frame2.setVisible(true);
		frame2.setResizable(false);

	}

	public void closeIntro() {
		frame2.dispose();
		frame3.dispose();
	}

	public void closeHelp() {
		frame3.dispose();
	}

	/*
	 * Main
	 */
	public static void main(String[] args) {
		new EcoFriends();
	}

	private class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {

			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);
			frame.getContentPane().add(new Board());
			frame.setResizable(false);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			closeIntro();

		}
	}

	private class CloseHelp implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			closeHelp();
		}
	}

	private class HelpButton implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JButton close = new JButton("Close");
			close.addActionListener(new CloseHelp());

			String topMessage = "<html><br>" + HELP_TOP_MESSAGE + "</html>";
			String message = "<html>" + HELP_MESSAGE + "</html> ";
			JLabel textLabel = new JLabel(message, SwingConstants.CENTER);
			JLabel topTextLabel = new JLabel(topMessage, SwingConstants.CENTER);

			Font font = new Font("Helvetica", Font.BOLD, 12);
			textLabel.setFont(font);

			Font font2 = new Font("Helvetica", Font.BOLD, 20);
			topTextLabel.setFont(font2);

			frame3.add(textLabel);

			frame3.add(topTextLabel, BorderLayout.PAGE_START);

			frame3.add(close, BorderLayout.PAGE_END);
			frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame3.setSize(250, 290);
			frame3.setResizable(false);
			frame3.setLocationRelativeTo(null);
			frame3.setVisible(true);
		}
	}
}
