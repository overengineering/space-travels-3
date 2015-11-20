package com.draga.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Pools;
import com.draga.Constants;
import com.draga.MaskBits;
import com.draga.MiniMap;
import com.draga.entity.ship.ShipBox2dCollisionResolutionComponent;
import com.draga.event.FuelChangeEvent;
import com.draga.manager.SettingsManager;
import com.draga.manager.GravityManager;
import com.draga.manager.InputManager;
import com.draga.manager.asset.AssMan;

public class Ship extends GameEntity
{
    public static final String LOGGING_TAG = Ship.class.getSimpleName();

    // Fuel.
    public static final float MAX_FUEL        = 1f;
    public static final float FUEL_PER_SECOND = 0.3f;

    // Size.
    private static final float SHIP_WIDTH       = 10;
    private static final float HALF_SHIP_WIDTH  = SHIP_WIDTH / 2f;
    private static final float SHIP_HEIGHT      = 10;
    private static final float HALF_SHIP_HEIGHT = SHIP_HEIGHT / 2f;

    // Physic.
    private static final float ROTATION_FORCE         = 2000;
    private static final float SHIP_MASS              = 1f;
    private static final float INPUT_FORCE_MULTIPLIER = 100f;

    // Thruster.
    private static final float   THRUSTER_MAX_WIDTH            = 5;
    private static final float   THRUSTER_MAX_HEIGHT           = 5;
    private static final float   TOTAL_THRUSTER_ANIMATION_TIME = 1f;
    private static final Vector2 THRUSTER_OFFSET               =
        new Vector2(-HALF_SHIP_HEIGHT / 2f, 0);
    private float        thrusterWidth;
    private float        thrusterHeight;
    private Animation    thrusterAnimation;
    private FixtureDef   thrusterFixtureDef;
    private Fixture      thrusterFixture;
    private float        thrusterAnimationStateTime;
    private PolygonShape thrusterShape;
    private TextureAtlas thrusterTextureAtlas;

    private Sound thrusterSound;
    private long thrusterSoundInstance;

    private Fixture      shipFixture;
    private FixtureDef   shipFixtureDef;
    private PolygonShape shipShape;

    private Texture shipTexture;

    // State.
    private float fuel;

    public Ship(float x, float y, String shipTexturePath, String thrusterTextureAtlasPath)
    {
        collisionResolutionComponent = new ShipBox2dCollisionResolutionComponent(this);

        shipShape = new PolygonShape();
        shipShape.setAsBox(SHIP_WIDTH / 2f, SHIP_HEIGHT / 2f);

        shipFixtureDef = new FixtureDef();
        shipFixtureDef.shape = shipShape;
        float area = SHIP_WIDTH * SHIP_HEIGHT;
        shipFixtureDef.density = SHIP_MASS / area;
        shipFixtureDef.friction = 0;
        shipFixtureDef.restitution = 0;
        shipFixtureDef.filter.categoryBits = MaskBits.SHIP;
        shipFixtureDef.filter.maskBits = MaskBits.PLANET | MaskBits.BOUNDARIES | MaskBits.STAR;


        thrusterShape = new PolygonShape();
        thrusterShape.setAsBox(
            THRUSTER_MAX_WIDTH / 2f, THRUSTER_MAX_HEIGHT / 2f, THRUSTER_OFFSET, 0);

        thrusterFixtureDef = new FixtureDef();
        thrusterFixtureDef.shape = thrusterShape;
        thrusterFixtureDef.density = 0;
        thrusterFixtureDef.friction = 0;
        thrusterFixtureDef.restitution = 0;
        thrusterFixtureDef.filter.categoryBits = MaskBits.THRUSTER;
        thrusterFixtureDef.filter.maskBits = 0;


        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.angle = 0;


        this.shipTexture = AssMan.getAssMan().get(shipTexturePath);


        thrusterAnimationStateTime = 0f;
        this.thrusterTextureAtlas = AssMan.getAssMan().get(thrusterTextureAtlasPath);
        thrusterAnimation = new Animation(
            TOTAL_THRUSTER_ANIMATION_TIME / this.thrusterTextureAtlas.getRegions().size,
            this.thrusterTextureAtlas.getRegions(),
            Animation.PlayMode.LOOP);

        thrusterSound = AssMan.getAssMan().get(AssMan.getAssList().thrusterSound);
        // TODO: check if this sound is loopable.
        thrusterSoundInstance = thrusterSound.loop(0);

        fuel = MAX_FUEL;

        this.physicsComponent = new PhysicsComponent(new Circle(10));
    }
    
    @Override
    public void update(float deltaTime)
    {
        // Avoid overflow.
        thrusterAnimationStateTime += deltaTime;
        if (thrusterAnimationStateTime > TOTAL_THRUSTER_ANIMATION_TIME)
        {
            thrusterAnimationStateTime %= TOTAL_THRUSTER_ANIMATION_TIME;
        }

        Vector2 gravityForce;
        if (SettingsManager.noGravity)
        {
            gravityForce = new Vector2();
        }
        else
        {
            gravityForce = GravityManager.getForceActingOn(body);
        }
        body.applyForceToCenter(gravityForce, true);
        //this.physicsComponent.getVelocity().add(gravityForce);

        Vector2 inputForce = InputManager.getInputForce();

        // TODO: apply the last part of acceleration properly and maybe then stop updating the
        // thrusters?
        if (fuel <= 0)
        {
            inputForce.setZero();
        }

        thrusterSound.setVolume(thrusterSoundInstance, inputForce.len());
        rotateTo(inputForce, deltaTime);
        updateFuel(inputForce, deltaTime);

        updateThruster(inputForce);

        //inputForce.scl(INPUT_FORCE_MULTIPLIER);
        body.applyForceToCenter(inputForce, true);

        this.physicsComponent.getVelocity().add(inputForce);
    }

    @Override
    public void drawMiniMap()
    {
        MiniMap.getShapeRenderer().set(ShapeRenderer.ShapeType.Filled);
        MiniMap.getShapeRenderer().setColor(Color.WHITE);

        Vector2 p1 = new Vector2(8, 0);
        Vector2 p2 = new Vector2(-5, -5);
        Vector2 p3 = new Vector2(-5, 5);
        p1.rotate(body.getAngle() * MathUtils.radiansToDegrees);
        p2.rotate(body.getAngle() * MathUtils.radiansToDegrees);
        p3.rotate(body.getAngle() * MathUtils.radiansToDegrees);

        MiniMap.getShapeRenderer().triangle(
            p1.x + getX(), p1.y + getY(),
            p2.x + getX(), p2.y + getY(),
            p3.x + getX(), p3.y + getY());
    }

    @Override
    public float getX()
    {
        return physicsComponent.getPosition().x;
    }

    @Override
    public float getY()
    {
        return physicsComponent.getPosition().y;
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        float halfThrusterHeight = thrusterHeight / 2f;
        float halfThrusterWidth = thrusterWidth / 2f;

        TextureRegion textureRegion = thrusterAnimation.getKeyFrame(thrusterAnimationStateTime);
        Vector2 thrusterOffsetFromCentre = THRUSTER_OFFSET
            .cpy()
            .sub(thrusterWidth / 2f, 0);
        Vector2 thrusterPosition = new Vector2(body.getPosition());
        Vector2 thrusterRotateOffset =
            new Vector2(thrusterOffsetFromCentre).rotateRad(body.getAngle());
        thrusterPosition.add(thrusterRotateOffset);
        spriteBatch.draw(
            textureRegion,
            thrusterPosition.x - halfThrusterWidth,
            thrusterPosition.y - halfThrusterHeight,
            halfThrusterWidth,
            halfThrusterHeight,
            thrusterWidth,
            thrusterHeight,
            1,
            1,
            body.getAngle() * MathUtils.radiansToDegrees);

        spriteBatch.draw(
            shipTexture,
            getX() - HALF_SHIP_WIDTH,
            getY() - HALF_SHIP_HEIGHT,
            HALF_SHIP_WIDTH,
            HALF_SHIP_HEIGHT,
            SHIP_WIDTH,
            SHIP_HEIGHT,
            1,
            1,
            body.getAngle() * MathUtils.radiansToDegrees,
            0,
            0,
            shipTexture.getWidth(),
            shipTexture.getHeight(),
            false,
            false);
    }

    @Override
    public void dispose()
    {
        thrusterSound.stop();
        thrusterSound.dispose();
        shipShape.dispose();
        thrusterShape.dispose();
        shipTexture.dispose();
        thrusterTextureAtlas.dispose();
    }

    @Override
    public void createBody(World world)
    {
        body = world.createBody(bodyDef);
        body.setUserData(this);
        shipFixture = body.createFixture(shipFixtureDef);
        thrusterFixture = body.createFixture(thrusterFixtureDef);
    }

    /**
     * Rotate the ship towards the given vector smoothly
     *
     * @param inputForce The input force, should be long between 0 and 1.
     */
    private void rotateTo(Vector2 inputForce, float elapsed)
    {
        // Ref. http://www.iforce2d.net/b2dtut/rotate-to-angle
        float nextAngle = body.getAngle() + body.getAngularVelocity() / 2f;
        float directionAngle = inputForce.angleRad();

        float diffRotation = directionAngle - nextAngle;

        // Brings the desired rotation between -180 and 180 degrees to find the closest way to turn.
        // E.g.: rotates - 10 degrees instead of 350.
        while (diffRotation < -180 * MathUtils.degreesToRadians)
        {
            diffRotation += 360 * MathUtils.degreesToRadians;
        }
        while (diffRotation > 180 * MathUtils.degreesToRadians)
        {
            diffRotation -= 360 * MathUtils.degreesToRadians;
        }

        float scale = inputForce.len() / 1;

        body.applyAngularImpulse(diffRotation * ROTATION_FORCE * elapsed * scale, true);
    }

    private void updateFuel(Vector2 inputForce, float deltaTime)
    {
        float oldFuel = fuel;

        if (SettingsManager.infiniteFuel)
        {
            fuel = MAX_FUEL;
        }
        else
        {
            fuel -= inputForce.len() * FUEL_PER_SECOND * deltaTime;
        }

        FuelChangeEvent fuelChangeEvent = Pools.obtain(FuelChangeEvent.class);
        fuelChangeEvent.set(oldFuel, fuel, MAX_FUEL);
        Constants.EVENT_BUS.post(fuelChangeEvent);
        Pools.free(fuelChangeEvent);
    }

    private void updateThruster(Vector2 inputForce)
    {
        float thrusterScale = inputForce.len();
        thrusterWidth = THRUSTER_MAX_WIDTH * thrusterScale;
        thrusterHeight = THRUSTER_MAX_HEIGHT * thrusterScale;
        Vector2 thrusterOffsetFromCentre = THRUSTER_OFFSET
            .cpy()
            .sub(thrusterWidth / 2f, 0);
        thrusterShape.setAsBox(
            thrusterWidth / 2f, thrusterHeight / 2f, thrusterOffsetFromCentre, 0);
        thrusterFixtureDef.shape = thrusterShape;
        body.destroyFixture(thrusterFixture);
        thrusterFixture = body.createFixture(thrusterFixtureDef);
    }

    public float getFuel()
    {
        return fuel;
    }
}
