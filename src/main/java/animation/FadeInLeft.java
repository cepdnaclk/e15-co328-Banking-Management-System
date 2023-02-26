package animation;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

@SuppressWarnings("deprecation")
public class FadeInLeft {
    private static final Interpolator risith = Interpolator.SPLINE(0.25, 0.1, 0.25, 1);
    private static Timeline timeline;
    private static int LENGTH = 30;
    private static int FEEDBACK = 6;
    
    public FadeInLeft(final Node myNode) {
        this(myNode,LENGTH,FEEDBACK);
    }
    
    public FadeInLeft(final Node myNode, int length) {
        this(myNode,LENGTH,FEEDBACK);
    }
    
    public FadeInLeft(final Node myNode, int length, int feedBack) {
        timeline = new Timeline();
        
        timeline.getKeyFrames().addAll(
            new KeyFrame(Duration.millis(0),    
                new KeyValue(myNode.opacityProperty(), 0, risith),
                new KeyValue(myNode.translateXProperty(), -length, risith)
            ),
            new KeyFrame(Duration.millis(600),    
                new KeyValue(myNode.opacityProperty(), 1, risith),
                new KeyValue(myNode.translateXProperty(), 0, risith)
            ),
            new KeyFrame(Duration.millis(800),    
                new KeyValue(myNode.opacityProperty(), 1, risith),
                new KeyValue(myNode.translateXProperty(), -feedBack, risith)
            )
        );
        timeline.play();
        timeline.setAutoReverse(true);
    }
}
