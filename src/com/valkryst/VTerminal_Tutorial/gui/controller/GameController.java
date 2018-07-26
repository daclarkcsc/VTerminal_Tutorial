package com.valkryst.VTerminal_Tutorial.gui.controller;

import com.valkryst.VTerminal.Screen;
import com.valkryst.VTerminal_Tutorial.entity.Player;
import com.valkryst.VTerminal_Tutorial.gui.model.GameModel;
import com.valkryst.VTerminal_Tutorial.gui.view.GameView;
import lombok.Getter;
import lombok.NonNull;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameController extends Controller<GameView, GameModel> {
    /** The timer which acts as an extremely basic game loop. */
    @Getter private final Timer timer;

    /**
     * Constructs a new GameController.
     *
     * @param screen
     *          The screen on which the view is displayed.
     *
     * @throws NullPointerException
     *          If the screen is null.
     */
    public GameController(final @NonNull Screen screen) {
        super(new GameView(screen), new GameModel(screen));
        initializeEventHandlers(screen);

        timer = new Timer(200, e -> {
            screen.draw();
        });
    }

    /**
     * Creates any event handlers required by the view.
     *
     * @param screen
     *          The screen on which the view is displayed.
     */
    private void initializeEventHandlers(final Screen screen) {
        final Player player = super.model.getPlayer();

        final KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP: {
                        player.move(0, -1);
                        break;
                    }

                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN: {
                        player.move(0, 1);
                        break;
                    }

                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT: {
                        player.move(-1, 0);
                        break;
                    }

                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT: {
                        player.move(1, 0);
                        break;
                    }
                }
            }
        };

        screen.addListener(keyListener);
        super.getModel().getEventListeners().add(keyListener);
    }
}
