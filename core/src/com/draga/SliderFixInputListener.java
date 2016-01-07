package com.draga;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Fix a bug that makes the slider jump back to zero when dragging.
 * Ref. http://badlogicgames.com/forum/viewtopic.php?f=11&t=12612
 */
public class SliderFixInputListener extends InputListener
{
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
    {
        event.stop();
        return false;
    }
}
