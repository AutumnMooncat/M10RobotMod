package M10Robot;

import M10Robot.characters.M10Robot;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Mainline;
import com.brashmonkey.spriter.Player;

public class CustomAnimationListener implements Player.PlayerListener {

    private final M10Robot character;

    public CustomAnimationListener(M10Robot character) {
        this.character = character;
    }

    public void animationFinished(Animation animation) {
        if (animation.name.equals("ko")) {
            character.playAnimation("ko");
        } else if (!animation.name.equals("idle")) {
            character.resetToIdleAnimation();
        }
    }

    @Override
    public void animationChanged(Animation animation, Animation animation1) {}

    @Override
    public void preProcess(Player player) {}

    @Override
    public void postProcess(Player player) {}

    @Override
    public void mainlineKeyChanged(Mainline.Key key, Mainline.Key key1) {}
}
