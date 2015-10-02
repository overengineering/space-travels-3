package com.draga.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.draga.MaskBits;
import com.draga.entity.ship.ShipBox2dCollisionResolutionComponent;
import com.draga.manager.GravityManager;
import com.draga.manager.InputManager;

public class Ship extends GameEntity
{
    public static final  String  LOGGING_TAG                   = Ship.class.getSimpleName();
    private static final float   ROTATION_FORCE                = 1500;
    private static final float   SHIP_WIDTH                    = 10;
    private static final float   HALF_SHIP_WIDTH               = SHIP_WIDTH / 2f;
    private static final float   SHIP_HEIGHT                   = 10;
    private static final float   HALF_SHIP_HEIGHT              = SHIP_HEIGHT / 2f;
    private static final float   THRUSTER_MAX_WIDTH            = 5;
    private static final float   THRUSTER_MAX_HEIGHT           = 5;
    private static final float   BODY_GRAVITY_MULTIPLIER       = 3f;
    private static final float   SHIP_MASS                     = 1f;
    private static final float   TOTAL_THRUSTER_ANIMATION_TIME = 1f;
    private static final float   INPUT_GRAVITY_MULTIPLIER      = 100f;
    private static final Vector2 THRUSTER_OFFSET               = new Vector2(-5, 0);
    private Animation    thrusterAnimation;
    private FixtureDef   thrusterFixtureDef;
    private Fixture      shipFixture;
    private Fixture      thrusterFixture;
    private FixtureDef   shipFixtureDef;
    private Texture      shipTexture;
    private float        thrusterAnimationStateTime;
    private PolygonShape thrusterShape;
    private PolygonShape shipShape;

    public Ship(float x, float y, String shipTexturePath, String thrusterTextureAtlasPath)
    {
        collisionResolutionComponent = new ShipBox2dCollisionResolutionComponent(this);

        shipShape = new PolygonShape();
        shipShape.setAsBox(SHIP_WIDTH / 2f, SHIP_HEIGHT / 2f);

        shipFixtureDef = new FixtureDef();
        shipFixtureDef.shape = shipShape;
        float area = SHIP_WIDTH * SHIP_HEIGHT;
        shipFixtureDef.density = SHIP_MASS / area;
        shipFixtureDef.friction = 1f;
        shipFixtureDef.restitution = 1f;
        shipFixtureDef.filter.categoryBits = MaskBits.SHIP;
        shipFixtureDef.filter.maskBits = MaskBits.PLANET | MaskBits.BOUNDARIES;


        thrusterShape = new PolygonShape();
        thrusterShape.setAsBox(
            THRUSTER_MAX_WIDTH / 2f,
            THRUSTER_MAX_HEIGHT / 2f,
            THRUSTER_OFFSET,
            0);

        thrusterFixtureDef = new FixtureDef();
        thrusterFixtureDef.shape = thrusterShape;
        thrusterFixtureDef.density = 0;
        thrusterFixtureDef.friction = 1f;
        thrusterFixtureDef.restitution = 1f;
        thrusterFixtureDef.filter.categoryBits = MaskBits.THRUSTER;
        thrusterFixtureDef.filter.maskBits = 0;


        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.angle = 0;


        FileHandle fileHandle = Gdx.files.internal(shipTexturePath);
        shipTexture = new Texture(fileHandle);


        thrusterAnimationStateTime = 0f;
        TextureAtlas textureAtlas = new TextureAtlas(thrusterTextureAtlasPath);
        thrusterAnimation = new Animation(
            TOTAL_THRUSTER_ANIMATION_TIME / textureAtlas.getRegions().size,
            textureAtlas.getRegions(),
            Animation.PlayMode.LOOP);
    }

    @Override public void update(float deltaTime)
    {
        // Avoid overflow.
        thrusterAnimationStateTime += deltaTime;
        if (thrusterAnimationStateTime > TOTAL_THRUSTER_ANIMATION_TIME)
        {
            thrusterAnimationStateTime %= TOTAL_THRUSTER_ANIMATION_TIME;
        }

        Vector2 gravityForce = GravityManager.getForceActingOn(body);
        gravityForce.scl(BODY_GRAVITY_MULTIPLIER);
        body.applyForceToCenter(gravityForce, true);

        Vector2 inputForce = InputManager.getInputForce();
        rotateTo(inputForce, deltaTime);
        inputForce.scl(INPUT_GRAVITY_MULTIPLIER);
        body.applyForceToCenter(inputForce, true);
    }

    @Override public void draw(SpriteBatch spriteBatch)
    {
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

        TextureRegion textureRegion = thrusterAnimation.getKeyFrame(thrusterAnimationStateTime);
        Vector2 thrusterPosition = new Vector2(body.getPosition());
        Vector2 thrusterRotateOffset = new Vector2(THRUSTER_OFFSET).rotateRad(body.getAngle());
        thrusterPosition.add(thrusterRotateOffset);
        spriteBatch.draw(
            textureRegion,
            thrusterPosition.x - THRUSTER_MAX_WIDTH / 2f,
            thrusterPosition.y - THRUSTER_MAX_HEIGHT / 2f,
            THRUSTER_MAX_WIDTH / 2f,
            THRUSTER_MAX_HEIGHT / 2f,
            THRUSTER_MAX_WIDTH,
            THRUSTER_MAX_HEIGHT,
            1,
            1,
            body.getAngle() * MathUtils.radiansToDegrees);
    }

    @Override public void dispose()
    {
        shipShape.dispose();
        thrusterShape.dispose();
        shipTexture.dispose();
    }

    @Override public void createBody(World world)
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
        float nextAngle = body.getAngle() + body.getAngularVelocity();
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
}
