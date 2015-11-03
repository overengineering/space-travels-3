package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.Constants;
import com.draga.manager.GameManager;
import com.draga.manager.asset.FontManager;
import com.draga.manager.level.LevelManager;

public class WinScreen implements Screen
{
    public static final float FADE_PER_SECOND = 0.7f;
    private final Stage stage;
    private final Color fadeToColour     = new Color(0, 0, 0, 0.7f);
    private final Color backgroundColour = new Color(0, 0, 0, 0);
    private final ShapeRenderer shapeRenderer;
    private       String        levelPath;

    public WinScreen(String levelPath)
    {
        this.levelPath = levelPath;
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Label headerLabel = getHeaderLabel();
        TextButton retryButton = getRetryButton();

        Table table = new Table();
        table.add(headerLabel);

        table.row();
        table.add(retryButton);

        FileHandle[] levels = LevelManager.getLevels();
        int currentLevelIndex = -1;
        for (int i = 0; i < levels.length && currentLevelIndex == -1; i++)
        {
            if (levels[i].path().equals(levelPath))
            {
                currentLevelIndex = i;
            }
        }
        if (currentLevelIndex < levels.length - 1)
        {
            String nextLevelPath = levels[currentLevelIndex + 1].path();
            TextButton nextTextButton = getNextButton(nextLevelPath);
            table.row();
            table.add(nextTextButton);
        }

        table.setFillParent(true);
        stage.addActor(table);

        stage.setDebugAll(Constants.IS_DEBUGGING);
        shapeRenderer = new ShapeRenderer();
    }

    public Label getHeaderLabel()
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        BitmapFont bigFont = FontManager.getBigFont();
        labelStyle.font = bigFont;

        Label headerLabel = new Label("You won!", labelStyle);
        headerLabel.setColor(new Color(1, 1, 1, 0));
        headerLabel.addAction(Actions.color(new Color(1, 1, 1, 1), 3, Interpolation.pow2In));

        return headerLabel;
    }

    public TextButton getRetryButton()
    {
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = FontManager.getBigFont();

        TextButton retryButton = new TextButton("Try Again?", buttonStyle);

        retryButton.setColor(new Color(1, 1, 1, 0));
        retryButton.addAction(Actions.color(new Color(1, 1, 1, 1), 3, Interpolation.pow2In));
        retryButton.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    Retry();
                    super.clicked(event, x, y);
                }
            });

        return retryButton;
    }

    private void Retry()
    {
        GameManager.getGame().setScreen(new LoadingScreen(levelPath));
    }

    public TextButton getNextButton(final String levelPath)
    {
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = FontManager.getBigFont();

        TextButton retryButton = new TextButton("Next level", buttonStyle);

        retryButton.setColor(new Color(1, 1, 1, 0));
        retryButton.addAction(Actions.color(new Color(1, 1, 1, 1), 3, Interpolation.pow2In));
        retryButton.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    GameManager.getGame().setScreen(new LoadingScreen(levelPath));
                    super.clicked(event, x, y);
                }
            });

        return retryButton;
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            GameManager.getGame().setScreen(new MenuScreen());
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            Retry();
            return;
        }

        update(delta);
        draw(delta);
    }

    private void update(float delta)
    {
        stage.act(delta);
    }

    private void draw(float delta)
    {
        backgroundColour.lerp(fadeToColour, FADE_PER_SECOND * delta);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setColor(backgroundColour);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, 0, stage.getWidth(), stage.getHeight());
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

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


    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {
        this.dispose();
    }

    @Override
    public void dispose()
    {
        shapeRenderer.dispose();
        stage.dispose();
    }
}
