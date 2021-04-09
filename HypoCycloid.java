
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Toolkit;

//
//Java animation: resonant orbits animation and tracking
//
//Sateesh R. Mane, copyright 2018
//

import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class HypoCycloid extends JFrame {

	public HypoCycloid() {
		add(new Screen());
		setResizable(false);
		pack();
		setTitle("HypoCycloid");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			JFrame ex = new HypoCycloid();
			ex.setVisible(true);

			// terminate the program when the top right "cross" of the frame is clicked
			ex.addWindowListener(new java.awt.event.WindowAdapter() {
				@Override
				public void windowClosing(java.awt.event.WindowEvent windowEvent) {
					System.exit(0);
				}
			});
		});
	}
}

final class Screen extends JPanel implements Runnable {
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private Thread animator;

	private static final int COUNT = 10000;

	double[] x = new double[COUNT];
	double[] y = new double[COUNT];

	Color display_color;
	Random rand = new Random();

	double p = 3 + 2 * ((int) (Math.random() * 3));
	double q = 1 + ((int) (Math.random() * 5));
	double k = 15;
	double r = 1.0 / k;
	int n = 0;

	private void setColors() {
		switch ((int) (Math.random() * 3)) {
		case 0:
			display_color = Color.CYAN;
			break;
		case 1:
			display_color = Color.YELLOW;
			break;
		case 2:
			display_color = Color.MAGENTA;
			break;
		case 3:
			display_color = Color.GREEN;
			break;
		}
	}

	private void update_xp() {
		if (n > COUNT)
			return;
		for (int i = 0; i < n - 1; i++) {
			double theta = (2.0 * Math.PI * i) / 1000;
			x[i] = r * (k - 1) * Math.cos(theta) + r * Math.cos(theta * (k - 1));
			y[i] = r * (k - 1) * Math.sin(theta) - r * Math.sin(theta * (k - 1));
		}
	}

	public Screen() {
		init();
	}

	private void init() {
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setDoubleBuffered(true);
		setFocusable(true);
		setColors();
		n = 0;
		double R = r * (k - 1);
		x[0] = R + r;
		y[0] = 0;
		for (int i = 1; i < COUNT; i++) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}

	}

	@Override
	public void addNotify() {
		super.addNotify();
		animator = new Thread(this);
		animator.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		updateDisplay(g);
	}

	private void updateDisplay(Graphics g) {
		if (n < COUNT) {
			++n;
			update_xp();
		}
		// draw the points
		for (int i = 1; i < n; ++i) {
			g.setColor(display_color);
			double x_display = (x[i]) * HEIGHT * 0.3 + HEIGHT * 0.5;
			double y_display = (y[i]) * HEIGHT * 0.3 + HEIGHT * 0.5;
			int int_x = (int) (x_display);
			int int_y = (int) (y_display);
			g.fillRect(int_x, int_y, 3, 2);
		}
		Toolkit.getDefaultToolkit().sync();
	}

	@Override
	public void run() {
		while (animator.interrupted() == false) {
			repaint();
		}
	}
}