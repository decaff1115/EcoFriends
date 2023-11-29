import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.imageio.ImageIO;

/**
 * Space Invaders Game Board
 * Author: [Your Name]
 */
public class Board extends JPanel implements Runnable, Commons {

	private static final long serialVersionUID = 1L;

	public static final int BOARD_HEIGHT = 600;

	private Dimension dimension;
	private ArrayList<Alien> aliens;
	private Player player;
	private Shot shot;
	private GameOver gameOverImage;
	private Won wonImage;

	private int alienX = 150;
	private int alienY = 25;
	private int direction = -1;
	private int deaths = 0;

	private boolean inGame = true;
	private boolean haveWon = true;
	private final String explosionImage = "/img/explosion.png";
	private final String alienImage = "/img/mushroom.png";
	private String message = "Oh no!!!The insects and pests took over :( ";

	private Thread animator;

	/*
	 * Constructor
	 */
	public Board() {
		addKeyListener(new KeyAdapterClass());
		setFocusable(true);
		dimension = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
		setBackground(Color.black);

		initializeGame();
		setDoubleBuffered(true);
	}

	public void addNotify() {
		super.addNotify();
		initializeGame();
	}

	public void initializeGame() {
		aliens = new ArrayList<>();

		ImageIcon alienIcon = new ImageIcon(this.getClass().getResource(alienImage));

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 6; j++) {
				Alien alien = new Alien(alienX + 18 * j, alienY + 18 * i);
				alien.setImage(alienIcon.getImage());
				aliens.add(alien);
			}
		}

		player = new Player();
		shot = new Shot();

		if (animator == null || !inGame) {
			animator = new Thread(this);
			animator.start();
		}
	}

	public void drawAliens(Graphics g) {
		Iterator<Alien> iterator = aliens.iterator();

		while (iterator.hasNext()) {
			Alien alien = iterator.next();

			if (alien.isVisible()) {
				g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
			}

			if (alien.isDying()) {
				alien.die();
			}
		}
	}

	public void drawPlayer(Graphics g) {
		if (player.isVisible()) {
			g.drawImage(player.getImage(), player.getX(), player.getY(), this);
		}

		if (player.isDying()) {
			player.die();
			haveWon = false;
			inGame = false;
		}
	}

	public void drawGameOverImage(Graphics g) {
		g.drawImage(gameOverImage.getImage(), 0, 0, this);
	}

	public void drawShot(Graphics g) {
		if (shot.isVisible())
			g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
	}

	public void drawBombing(Graphics g) {
		Iterator<Alien> iterator = aliens.iterator();

		while (iterator.hasNext()) {
			Alien alien = iterator.next();

			Bomb bomb = alien.getBomb();

			if (!bomb.isDestroyed()) {
				g.drawImage(bomb.getImage(), bomb.getX(), bomb.getY(), this);
			}
		}
	}

	public void paint(Graphics g) {
		super.paint(g);

		g.setColor(Color.black);
		g.fillRect(0, 0, dimension.width, dimension.height);
		g.setColor(Color.green);

		if (inGame) {

			g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
			drawAliens(g);
			drawPlayer(g);
			drawShot(g);
			drawBombing(g);
		}

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	public void gameOver() {
		Graphics g = this.getGraphics();

		gameOverImage = new GameOver();
		wonImage = new Won();

		g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
		if (haveWon) {
			g.drawImage(wonImage.getImage(), 0, 0, this);
		} else {
			g.drawImage(gameOverImage.getImage(), 0, 0, this);
		}
		g.setColor(new Color(0, 32, 48));
		g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
		g.setColor(Color.white);
		g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics fontMetrics = this.getFontMetrics(small);

		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(message, (BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
				BOARD_WIDTH / 2);
	}

	public void animationCycle() {
		if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {
			inGame = false;
			message = "Congratulations! Bara is now full!";
		}

		// player
		player.act();

		// shot
		if (shot.isVisible()) {
			Iterator<Alien> iterator = aliens.iterator();
			int shotX = shot.getX();
			int shotY = shot.getY();

			while (iterator.hasNext()) {
				Alien alien = iterator.next();
				int alienX = alien.getX();
				int alienY = alien.getY();

				if (alien.isVisible() && shot.isVisible()) {
					if (shotX >= (alienX) && shotX <= (alienX + ALIEN_WIDTH)
							&& shotY >= (alienY)
							&& shotY <= (alienY + ALIEN_HEIGHT)) {
						ImageIcon explosionIcon = new ImageIcon(getClass().getResource(explosionImage));
						alien.setImage(explosionIcon.getImage());
						alien.setDying(true);
						deaths++;
						shot.die();
					}
				}
			}

			int y = shot.getY();
			y -= 8;
			if (y < 0)
				shot.die();
			else
				shot.setY(y);
		}

		// aliens
		Iterator<Alien> iterator1 = aliens.iterator();

		while (iterator1.hasNext()) {
			Alien a1 = iterator1.next();
			int x = a1.getX();

			if (x >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
				direction = -1;
				Iterator<Alien> iterator2 = aliens.iterator();
				while (iterator2.hasNext()) {
					Alien a2 = iterator2.next();
					a2.setY(a2.getY() + GO_DOWN);
				}
			}

			if (x <= BORDER_LEFT && direction != 1) {
				direction = 1;

				Iterator<Alien> iterator3 = aliens.iterator();
				while (iterator3.hasNext()) {
					Alien a = iterator3.next();
					a.setY(a.getY() + GO_DOWN);
				}
			}
		}

		Iterator<Alien> iterator = aliens.iterator();

		while (iterator.hasNext()) {
			Alien alien = iterator.next();
			if (alien.isVisible()) {

				int y = alien.getY();

				if (y > GROUND - ALIEN_HEIGHT) {
					haveWon = false;
					inGame = false;
					message = "Aliens are invading the galaxy!";
				}

				alien.act(direction);
			}
		}

		// bombs
		Iterator<Alien> iterator4 = aliens.iterator();
		Random generator = new Random();

		while (iterator4.hasNext()) {
			int shot = generator.nextInt(15);
			Alien a = iterator4.next();
			Bomb b = a.getBomb();
			if (shot == CHANCE && a.isVisible() && b.isDestroyed()) {

				b.setDestroyed(false);
				b.setX(a.getX());
				b.setY(a.getY());
			}

			int bombX = b.getX();
			int bombY = b.getY();
			int playerX = player.getX();
			int playerY = player.getY();

			if (player.isVisible() && !b.isDestroyed()) {
				if (bombX >= (playerX) && bombX <= (playerX + PLAYER_WIDTH)
						&& bombY >= (playerY)
						&& bombY <= (playerY + PLAYER_HEIGHT)) {
					ImageIcon explosionIcon = new ImageIcon(this.getClass().getResource(explosionImage));
					player.setImage(explosionIcon.getImage());
					player.setDying(true);
					b.setDestroyed(true);
					;
				}
			}

			if (!b.isDestroyed()) {
				b.setY(b.getY() + 1);
				if (b.getY() >= GROUND - BOMB_HEIGHT) {
					b.setDestroyed(true);
				}
			}
		}
	}

	public void run() {
		long beforeTime, timeDiff, sleep;

		beforeTime = System.currentTimeMillis();

		while (inGame) {
			repaint();
			animationCycle();

			timeDiff = System.currentTimeMillis() - beforeTime;
			sleep = DELAY - timeDiff;

			if (sleep < 0)
				sleep = 1;
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				System.out.println("interrupted");
			}
			beforeTime = System.currentTimeMillis();
		}
		gameOver();
	}

	private class KeyAdapterClass extends KeyAdapter {

		public void keyReleased(KeyEvent e) {
			player.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {

			player.keyPressed(e);

			int x = player.getX();
			int y = player.getY();

			if (inGame) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_SPACE) {

					if (!shot.isVisible())
						shot = new Shot(x, y);
				}
			}
		}
	}
}
