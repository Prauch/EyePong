package pl.prazuch.wojciech.pong;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by wojciechprazuch on 14.10.2017.
 */
public interface Movable {

    public void Move();

    public void Draw(GraphicsContext gc);

    public void Update();

}
