package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.event.PurchasedEvent;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.manager.level.LevelPack;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.spaceTravels3.ui.BeepingClickListener;
import com.draga.spaceTravels3.ui.BeepingImageTextButton;
import com.draga.spaceTravels3.ui.BeepingTextButton;
import com.draga.spaceTravels3.ui.Screen;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;

public class LevelPackScreen extends Screen
{
    private final LevelPack levelPack;
    private final Actor purchaseButton;
    private       Stage stage;
    private       float levelIconsSize;

    public LevelPackScreen(LevelPack levelPack)
    {
        super(true, true);
        this.levelPack = levelPack;

        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.spriteBatch);
        this.levelIconsSize = this.stage.getWidth() / 10f;

        Table table = UIManager.addDefaultTableToStage(this.stage);

        // Header label.
        Label headerLabel = getHeaderLabel();
        table
            .add(headerLabel)
            .top();

        // Level list.
        table.row();
        table
            .add(getLevelList())
            .expand()
            .center();

        // Buttons.
        table.row();
        this.purchaseButton = getPurchaseButton();
        table.add(this.purchaseButton);

        table.row();
        table.add(getBackButton());

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
        Constants.General.EVENT_BUS.register(this);
    }

    public Label getHeaderLabel()
    {
        Label headerLabel = new Label(this.levelPack.getName(), UIManager.skin, "large", Color.WHITE);

        return headerLabel;
    }

    private ScrollPane getLevelList()
    {
        ArrayList<SerialisableLevel> serialisableLevels =
            this.levelPack.getSerialisableLevels();

        final Table outerTable = UIManager.getDefaultTable();

        for (final SerialisableLevel serialisableLevel : serialisableLevels)
        {
            Table innerTable = UIManager.getDefaultTable();

            // If the level icon is not loaded in the ass man then add to an map to load them async.
            Image image = loadTextureAsync(
                serialisableLevel.serialisedDestinationPlanet.texturePath,
                AssMan.getAssMan());

            innerTable
                .add(image)
                .size(this.levelIconsSize);
            innerTable.row();

            innerTable.add(serialisableLevel.name);
            innerTable.row();

            innerTable.addListener(BeepingClickListener.BEEPING_CLICK_LISTENER);
            innerTable.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    LevelScreen levelScreen = new LevelScreen(LevelPackScreen.this.levelPack, serialisableLevel);
                    ScreenManager.addScreen(levelScreen);
                }
            });

            outerTable.add(innerTable);
        }

        ScrollPane scrollPane = new ScrollPane(outerTable, UIManager.skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(false, true);

        return scrollPane;
    }
    
    private Actor getPurchaseButton()
    {
        BeepingImageTextButton button =
            new BeepingImageTextButton("Purchase", UIManager.skin, "unlock");

        button.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    SpaceTravels3.services.purchaseSku(LevelPackScreen.this.levelPack.getGoogleSku());
                }
            });

        button.setVisible(!this.levelPack.isFree()
            && !SpaceTravels3.services.hasPurchasedSku(this.levelPack.getGoogleSku()));

        return button;
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float deltaTime)
    {
        loadAsyncImages(AssMan.getAssMan());

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            ScreenManager.removeScreen(this);
        }

        this.stage.getViewport().apply();

        this.stage.act(deltaTime);
        this.stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        this.stage.getViewport().update(width, height);
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

    }

    @Override
    public void dispose()
    {
        this.stage.dispose();
        Constants.General.EVENT_BUS.unregister(this);
    }

    @Subscribe
    public void purchased(PurchasedEvent purchasedEvent)
    {
        if (!this.levelPack.isFree()
            && this.levelPack.getGoogleSku().equals(purchasedEvent.sku))
        {
            this.purchaseButton.setVisible(false);
        }
    }
}
