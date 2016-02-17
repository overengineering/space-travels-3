package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.event.ChangeLevelPackEvent;
import com.draga.spaceTravels3.event.PurchasedEvent;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.level.LevelPack;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.spaceTravels3.ui.BeepingClickListener;
import com.draga.spaceTravels3.ui.BeepingImageTextButton;
import com.draga.spaceTravels3.ui.Screen;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;

public class LevelPackScreen extends Screen
{
    private final Cell<ScrollPane> levelListCell;
    private final Cell<Label>      headerCell;
    private       LevelPack        levelPack;
    private       Actor            purchaseButton;

    public LevelPackScreen(LevelPack levelPack)
    {
        super(true, true);
        this.levelPack = levelPack;

        Table table = UIManager.addDefaultTableToStage(this.stage);

        // Header label.
        this.headerCell = table
            .add(getHeaderLabel())
            .top();

        // Level list.
        table.row();
        this.levelListCell = table
            .add(getLevelList())
            .expand()
            .center();

        // Buttons.
        table.row();
        this.purchaseButton = getPurchaseButton();
        table
            .add(this.purchaseButton);

        table.row();
        table
            .add(getBackButton());

        Constants.General.EVENT_BUS.register(this);
    }

    public Label getHeaderLabel()
    {
        Label headerLabel =
            new Label(this.levelPack.getName(), UIManager.skin, "large", Color.WHITE);

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
                serialisableLevel.serialisedDestinationPlanet.texturePath
            );

            innerTable
                .add(image)
                .size(Constants.Visual.LEVEL_ICON_SIZE);
            innerTable.row();

            innerTable.add(serialisableLevel.name);
            innerTable.row();

            innerTable.addListener(BeepingClickListener.BEEPING_CLICK_LISTENER);
            innerTable.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    LevelScreen levelScreen =
                        new LevelScreen(LevelPackScreen.this.levelPack, serialisableLevel);
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
        Gdx.input.setInputProcessor(new InputMultiplexer(this.stage, getBackInputAdapter()));
    }

    @Override
    public void dispose()
    {
        Constants.General.EVENT_BUS.unregister(this);
        super.dispose();
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

    @Subscribe
    public void levelPackChanged(ChangeLevelPackEvent changeLevelPackEvent)
    {
        this.levelPack = changeLevelPackEvent.levelPack;

        this.levelListCell.setActor(getLevelList());
        this.headerCell.setActor(getHeaderLabel());
    }
}
