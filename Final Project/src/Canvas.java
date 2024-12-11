
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class Canvas extends JComponent implements ActionListener, KeyListener {
	// DEFAULT SERIAL NUMBER
	private static final long serialVersionUID = 1L;

	// GAME LOOP USES A FRAME AND TIMER
	private JFrame frame;
	private Timer gameLoopTimer;

	private List<GameObject> gameObjectList;
	private int highlighted = 0;
	private MovementAdapter moveAdapter;

	public Canvas() {
		// TASK 1: CREATE A LIST OF CHARACTERS THAT WILL APPEAR ON THE CANVAS
		gameObjectList = new LinkedList<GameObject>();

		// TASK 2: CREATE A WINDOW FOR THE APPLICATION
		frame = new JFrame("Tate's Final Project");
		frame.setSize(800, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);

		// TASK 3: CONSTRUCT A TIMER FOR GAME LOOP
		gameLoopTimer = new Timer(25, this);
		gameLoopTimer.start();

		frame.addKeyListener(this);
		frame.setFocusable(true);
		setFocusTraversalKeysEnabled(false);

		setupKeyBindings(frame.getRootPane());

		// TASK 4: MAKE THE WINDOW VISIBLE
		frame.setVisible(true);
		frame.requestFocusInWindow();

		System.out.println("Frame focused: " + frame.isFocusOwner());

	}

	/**
	 * Adds GameObjects to the List, which are latter added to the Canvas
	 */
	public synchronized void addGameObject(GameObject sprite) {
		gameObjectList.add(sprite);
		System.out.println("Added object: " + sprite);

		if (gameObjectList.size() == 1) {
			highlighted = 0;
			moveAdapter = new MovementAdapter(sprite);
			System.out.println("First object initialized as active");
		}
	}

	/**
	 * Draws the GameObject graphic onto the Canvas
	 */
	public synchronized void paint(Graphics g) {
		for (GameObject s : gameObjectList) {
			s.draw(this, g);
			if (s == getActiveObject()) {
				s.setImage();
			}
		}
		drawHighlight(g);
	}

	//couldn't get key released to work with tab or space when trying to cycle
		//the active object, used a key bind with spacebar to solve this problem.
	private synchronized void setupKeyBindings(JComponent comp) {
		InputMap inputMap = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = comp.getActionMap();

		inputMap.put(KeyStroke.getKeyStroke("released SPACE"), "switchObject");
		actionMap.put("switchObject", new AbstractAction() {
			@Override
			public synchronized void actionPerformed(ActionEvent e) {
				cycleActiveObject();
				//System.out.println("Active object switched.");
			}
		});
	}

	// ****************************************************
	// Canvas must implement the inherited abstract method
	// ActionListener.actionPerformed(ActionEvent)

	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		for (GameObject gameObject : gameObjectList) {
			if (gameObject == getActiveObject()) {
				moveAdapter.move(this);
			} else {
				gameObject.move(this);
			}
			gameObject.setImage();
		}
		repaint();
	}

	// ****************************************************
	// Canvas must implement the inherited abstract methods
	// for KeyListener
	public void keyTyped(KeyEvent e) {
	}

	//passing all movement to the adapter
	public void keyPressed(KeyEvent e) {
		if (moveAdapter != null) {
			moveAdapter.keyPressed(e);
		}
	}

	//passing all movement to the adapter
	public void keyReleased(KeyEvent e) {
		if (moveAdapter != null) {
			moveAdapter.keyReleased(e);
		}
	}

	//returns the currently highlighted object
	public GameObject getActiveObject() {
		if (!gameObjectList.isEmpty()) {
			return gameObjectList.get(highlighted);
		}
		return null;
	}

	
	//to cycle the to the next instantiated object in the list
	public void cycleActiveObject() {
		if (!gameObjectList.isEmpty()) {
			highlighted = (highlighted + 1) % gameObjectList.size();
			//System.out.println("New highlighted index: " + highlighted);
			moveAdapter = new MovementAdapter(getActiveObject());
			System.out.println("moveAdapter updated to control: " + getActiveObject());
			frame.requestFocusInWindow();
		} else {
			System.out.println("No objects to cycle through");
		}
	}

	//to create the highlight around the selected object
	public void drawHighlight(Graphics g) {
		GameObject activeObject = getActiveObject();
		if (activeObject != null) {
			g.setColor(Color.RED);
			int x = activeObject.getX();
			int y = activeObject.getY();
			int width = activeObject.getCurrentImage().getIconWidth();
			int height = activeObject.getCurrentImage().getIconHeight();
			g.drawRect(x - 5, y - 5, width + 10, height + 10);
		}
	}	

}