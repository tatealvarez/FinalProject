import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MovementAdapter implements KeyListener {

	GameObject object;

	public MovementAdapter(GameObject object) {
		this.object = object;
	}
	
	//ensures that the selected object uses it's own move method to move
	public void move(Canvas c) {
		if(object != null) {
			object.move(c);
		}
	}

	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (object != null) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				object.setDirection(Direction.UP);
			}
			else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				object.setDirection(Direction.DOWN);
			}
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				object.setDirection(Direction.RIGHT);
			}
			else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				object.setDirection(Direction.LEFT);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (object != null && object instanceof Type_D_GameObject) {
			object.setDirection(Direction.NONE);
		}
	}

}
