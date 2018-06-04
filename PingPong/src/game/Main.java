package game;

import aurelienribon.tweenengine.Tween;
import slidinglayout.SLAnimator;

public class Main {
    public static void main(String[] args) {
        Tween.registerAccessor(PingPanel.class, new PingPanel.Accessor());
        SLAnimator.start();

        Main_Frame frame = new Main_Frame(750, 750);
        /*JFrame frame =new JFrame();
        game.Board board = new game.Board();
        frame.add(board);*/
        frame.setResizable(false);
        frame.setSize(765, 830);

        frame.setVisible(true);
    }
}
