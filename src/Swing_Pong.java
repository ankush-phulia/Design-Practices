import aurelienribon.tweenengine.Tween;
import slidinglayout.SLAnimator;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class Swing_Pong {
	public static void main(String[] args) {
		Tween.registerAccessor(PingPanel.class, new PingPanel.Accessor());
		SLAnimator.start();

		PongFrame frame = new PongFrame();
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}