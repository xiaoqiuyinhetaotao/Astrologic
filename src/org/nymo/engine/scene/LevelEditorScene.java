package org.nymo.engine.scene;

import java.awt.event.*;

import org.nymo.engine.Window;
import org.nymo.engine.listeners.KeyListener;

public class LevelEditorScene extends Scene {

    private boolean changingScene = false;
    private float timeToChangeScene = 5.0f; // 1 second = 1.0f

    public LevelEditorScene() {
        System.out.println("Inside LevelEditorScene.");
    }

    @Override
    public void update(float dt) {
        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }

        if (changingScene && timeToChangeScene > 0) {
            timeToChangeScene -= dt;
            Window.get().r -= dt * 5.0f;
            Window.get().g -= dt * 5.0f;
            Window.get().b -= dt * 5.0f;
        } else if (changingScene) {
            Window.changeScene(1);
        }
    }
    
}
