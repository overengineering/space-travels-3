package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.manager.DebugManager;
import com.draga.manager.GameManager;
import com.draga.manager.SkinManager;

public class DebugMenuScreen implements Screen
{
    private Stage stage;

    public DebugMenuScreen()
    {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.pad(((stage.getHeight() + stage.getWidth()) / 2f) / 50f);

        table.add(GetButtonScrollPane());

        stage.addActor(table);
        stage.setDebugAll(DebugManager.debugDraw);
    }

    public ScrollPane GetButtonScrollPane()
    {
        final TextButton debugDrawTextButton = new TextButton(
            "Debug Draw",
            SkinManager.BasicSkin.get(TextButton.TextButtonStyle.class));
        debugDrawTextButton.setChecked(DebugManager.debugDraw);

        debugDrawTextButton.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    DebugManager.debugDraw = !DebugManager.debugDraw;
                    debugDrawTextButton.setChecked(DebugManager.debugDraw);
                    super.clicked(event, x, y);
                }
            });

        VerticalGroup verticalGroup = new VerticalGroup();

        verticalGroup.addActor(debugDrawTextButton);

        ScrollPane scrollPane = new ScrollPane(verticalGroup);

        return scrollPane;
    }

    @Override
    public void show()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void render(float delta)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            GameManager.getGame().setScreen(new MenuScreen());
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose()
    {
        stage.dispose();
        //skin.dispose();
    }
}
