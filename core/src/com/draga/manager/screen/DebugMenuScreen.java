package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.Constants;
import com.draga.manager.DebugManager;
import com.draga.manager.GameManager;
import com.draga.manager.SkinManager;

public class DebugMenuScreen implements Screen
{
    private Skin        skin;
    private Stage       stage;
    //private SpriteBatch batch;

    public DebugMenuScreen()
    {
        //batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = SkinManager.BasicMenuSkin;

        Table table = new Table();
        table.setFillParent(true);
        table.pad(((stage.getHeight() + stage.getWidth()) / 2f) / 50f);

        table.add(GetButtonScrollPane());

        stage.addActor(table);
        stage.setDebugAll(DebugManager.debugDraw);
    }

    public ScrollPane GetButtonScrollPane()
    {
        final TextButton debugDrawTextButton = new TextButton("Debug Draw", skin.get("checkTextButton", TextButton.TextButtonStyle.class));
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
    public void render(float delta)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            GameManager.getGame().setScreen(new MenuScreen());
        }
/*
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);*/

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose()
    {
        stage.dispose();
        //skin.dispose();
    }

    @Override
    public void show()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide()
    {
        // TODO Auto-generated method stub

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
}