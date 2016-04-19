// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework;

// All of the extra includes here.
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import com.example.app.jason.ragerelease.app.Framework.Graphics.AnimatedSprite;
import com.example.app.jason.ragerelease.app.Framework.Maths.Vector2;
import com.example.app.jason.ragerelease.app.Framework.Network.ConnectionApplication;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkConstants;
import com.example.app.jason.ragerelease.app.Framework.Physics.StaticBody;
import com.example.app.jason.ragerelease.app.GameStates.Multiplayer.MultiplayerGame;
import com.example.app.jason.ragerelease.app.GameStates.SinglePlayer.SinglePlayerGame;
import com.example.app.jason.ragerelease.app.Framework.Physics.DynamicBody;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;

import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jason Mottershead on 17/07/2015.
 */

// Level USES Touch Listener, therefore implements it.
public class Level implements View.OnTouchListener
{
    // Attributes.
    // Public.
    public int levelNumber = 1;
    public SinglePlayerGame singlePlayerGame = null;
    public MultiplayerGame multiplayerGame = null;
    public LevelGenerator levelGenerator = null;
    public Player player = null;

    // Private.
    private Resources resources = null;
    private int interval = 1;
    private boolean multiplayerStatus = false;
    private ScheduledExecutorService scheduler = null;
    private ScheduledFuture<?> distanceIncrementer = null;
    private int playerMatchStatus = 0;
    private Activity activityReference = null;
    private ConnectionApplication connectionApplication = null;

    // Methods.
    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    //  This will set up access to common game      //
    //  properties, and set up the touch listener.  //
    //////////////////////////////////////////////////
    public void init(final Resources gameResources, final SinglePlayerGame singlePlayerGameView, final int gamePlayerImage, final int gameCompanionImage)
    {
        // Initialising local variables.
        resources = gameResources;
        singlePlayerGame = singlePlayerGameView;
        player = new Player(resources, this);
        levelGenerator = new LevelGenerator(resources, this, gamePlayerImage, gameCompanionImage);
        levelGenerator.buildLevel(1, 1);    // Builds the first level.
        levelGenerator.addToView();
        player.distanceText.bringToFront();
        player.setGameOver(false);
        player.setPaused(false);
        scheduler = Executors.newSingleThreadScheduledExecutor();
        multiplayerStatus = false;

        // This schedule is incrementing player distance every second.
        // Running on a new thread.
        distanceIncrementer = scheduler.scheduleAtFixedRate(new Runnable()
        {
            @Override
            public void run()
            {
                if(!player.isPaused() && !player.isGameOver())
                {
                    player.distance++;
                }
            }
            // Executing at a regular interval of 1 second.
        }, interval, interval, TimeUnit.SECONDS);

        // Listen out for touches in the level.
        resources.getBackground().setOnTouchListener(this);
    }

    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    //  This will set up access to common game      //
    //  properties, and set up the touch listener.  //
    //////////////////////////////////////////////////
    public void init(final Resources gameResources, final MultiplayerGame multiplayerGameView, final int gamePlayerImage, final int gamePlayerMatchStatus, final int gamePeerImage, final Activity gameActivity, final ConnectionApplication gameConnectionApplication)
    {
        // Initialising local variables.
        resources = gameResources;
        multiplayerGame = multiplayerGameView;
        player = new Player(resources, this);
        multiplayerStatus = true;
        playerMatchStatus = gamePlayerMatchStatus;
        //activityReference = activity;
        levelGenerator = new LevelGenerator(resources, this, gamePlayerImage, playerMatchStatus, gamePeerImage, gameActivity);
        levelGenerator.buildLevel(2, 1);    // Builds the first level.
        levelGenerator.addToView();
        player.distanceText.bringToFront();
        player.setGameOver(false);
        player.setPaused(false);
        scheduler = Executors.newSingleThreadScheduledExecutor();
        activityReference = gameActivity;
        levelNumber = 3;
        connectionApplication = gameConnectionApplication;

        // This schedule is incrementing player distance every second.
        // Running on a new thread.
        distanceIncrementer = scheduler.scheduleAtFixedRate(new Runnable()
        {
            @Override
            public void run()
            {
                if(!player.isPaused() && !player.isGameOver())
                {
                    player.distance++;
                }
            }
            // Executing at a regular interval of 1 second.
        }, interval, interval, TimeUnit.SECONDS);

        // Listen out for touches in the level.
        resources.getBackground().setOnTouchListener(this);
    }

    //////////////////////////////////////////////////
    //                  New Level                   //
    //==============================================//
    //  This will determine what happens when we go //
    //  to a new level.                             //
    //  In this case, we will clear the current     //
    //  level, and then build another level.        //
    //////////////////////////////////////////////////
    public void newLevel()
    {
        // Resetting the player's distance score.
        player.distance = 0;

        // Clear the current level.
        levelGenerator.clearLevel();

        // Increment the current level number.
        levelNumber++;

        if(levelNumber == 2)
        {
            // Builds the new level, with two characters and one obstacle.
            levelGenerator.buildLevel(2, 1);
        }
        else if(levelNumber == 3)
        {
            // Builds the new level, with one character and two obstacles.
            levelGenerator.buildLevel(1, 2);
        }
        else if(levelNumber >= 4)
        {
            // Builds the new level, with two characters and two obstacles.
            levelGenerator.buildLevel(2, 2);
        }

        if(multiplayerStatus)
        {
            multiplayerGame.render();
        }
        else
        {
            // Updates the UI thread to add all of the new level objects to the screen.
            singlePlayerGame.render();
        }
    }

    //////////////////////////////////////////////////
    //              Touch Collision Test            //
    //==============================================//
    //  This will allow us to check whether or not  //
    //  we have tapped on an animated sprite.       //
    //////////////////////////////////////////////////
    private boolean touchCollisionTest(AnimatedSprite object)
    {
        if(((player.touchPosition.getX() > object.getSpriteLeft()) && (player.touchPosition.getX() < object.getSpriteRight()))
                && ((player.touchPosition.getY() > object.getSpriteTop()) && (player.touchPosition.getY() < object.getSpriteBottom())))
        {
            return true;
        }

        return false;
    }

    //////////////////////////////////////////////////
    //                  On Touch                    //
    //==============================================//
    //  This will provide the input for the level.  //
    //  Tap onto a character to make them jump up!  //
    //////////////////////////////////////////////////
    public boolean onTouch(View v, MotionEvent event)
    {
        if(!player.isPaused())
        {
            // Touch response here.
            player.touchPosition.set(event.getX(), event.getY());
            int eventAction = event.getAction();

            switch (eventAction)
            {
                // If the player touches the screen.
                case MotionEvent.ACTION_DOWN:
                {
                    for (AnimatedSprite object : getLevelObjects())
                    {
                        if(multiplayerStatus)
                        {
                            // Processing our character input.
                            if (object.getID() == ObjectID.CHARACTERONE)
                            {
                                if (touchCollisionTest(object))
                                {
                                    tappedResponse(object);
                                }
                            }

//                            // Processing other player input.
//                            if(object.getID() == ObjectID.CHARACTERTWO)
//                            {
//                                peerTappedResponse(object);
//                            }
                        }
                        else
                        {
                            if ((object.getID() == ObjectID.CHARACTERONE) || (object.getID() == ObjectID.CHARACTERTWO))
                            {
                                if (touchCollisionTest(object))
                                {
                                    tappedResponse(object);
                                }
                            }
                        }
                    }

                    break;
                }

                // If the player keeps holding the touch on the screen.
                case MotionEvent.ACTION_MOVE:
                {
                    break;
                }

                // If the player releases their touch on the screen.
                case MotionEvent.ACTION_UP:
                {
                    if (player.tap)
                    {
                        for (AnimatedSprite object : getLevelObjects())
                        {
                            if ((object.getID() == ObjectID.CHARACTERONE) || (object.getID() == ObjectID.CHARACTERTWO))
                            {
                                if (!object.isUsingCameraImage())
                                {
                                    object.changeTexture(new Vector2(0.0f, 0.0f));
                                    object.setAnimationFrames(6);
                                }
                            }
                        }

                        player.tap = false;
                    }

                    break;
                }

                // If none of the above cases are met, then just default to not being touched.
                default:
                {
                    //player.tap = false;
                    break;
                }
            }

            return true;
        }

        return true;
    }

    private void tappedResponse(AnimatedSprite object)
    {
        player.tap = true;

        if (!object.isUsingCameraImage())
        {
            // Change to a jumping animation.
            object.changeTexture(new Vector2((5.0f / 7.0f), (1.0f / 3.0f)));
            object.setAnimationFrames(2);
        }

        // Make the object jump.
        object.getBody().applyLinearImpulse(new Vec2(0.0f, 4.0f), object.getBody().getWorldCenter());

//        activityReference.runOnUiThread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                DebugInformation.displayShortToastMessage(activityReference, "Tapped: " + player.tap);
//            }
//        });

//        // If we are using multiplayer.
//        if(multiplayerStatus)
//        {
//            // Send this boolean message to the other client.
//            // Receive the any tapped info.
//            if(playerMatchStatus == NetworkConstants.HOST_ID)
//            {
//                resources.getConnectionApplication().getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getServerTask().setTapped(player.tap);
//            }
//            else
//            {
//                resources.getConnectionApplication().getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getClientTask().setTapped(player.tap);
//            }
//        }
    }

    private void peerTappedResponse(final AnimatedSprite object, boolean peerTapped)
    {
        boolean doOnce = true;
        final Vec2 originalForce = new Vec2(0.0f, 4.0f);
        final Vec2 forceApplied = new Vec2(0.0f, 0.75f);

        if(playerMatchStatus == NetworkConstants.HOST_ID)
        {
            //final Vec2 forceMultiplied = new Vec2(0.0f, forceApplied.y * 3.0f);

            if(peerTapped)
            {
                // Make them jump.
                if (!object.isUsingCameraImage())
                {
                    // Change to a jumping animation.
                    object.changeTexture(new Vector2((5.0f / 7.0f), (1.0f / 3.0f)));
                    object.setAnimationFrames(2);
                }

                if(doOnce)
                {
                    // Make the object jump.
                    object.getBody().applyLinearImpulse(originalForce, object.getBody().getWorldCenter());

                    doOnce = false;
                }
            }
        }
        else if(playerMatchStatus == NetworkConstants.JOIN_ID)
        {
            if(peerTapped)
            {
                // Make them jump.
                if (!object.isUsingCameraImage())
                {
                    // Change to a jumping animation.
                    object.changeTexture(new Vector2((5.0f / 7.0f), (1.0f / 3.0f)));
                    object.setAnimationFrames(2);
                }

                if(doOnce)
                {
                    // Make the object jump.
                    object.getBody().applyLinearImpulse(forceApplied, object.getBody().getWorldCenter());

                    doOnce = false;
                }
            }
        }
    }

    //////////////////////////////////////////////////
    //              Handle Level Objects            //
    //==============================================//
    //  This will process all of the data that we   //
    //  can gather in the level, and provide        //
    //  appropriate responses to the condition.     //
    //////////////////////////////////////////////////
    private void handleLevelObjects(float dt, boolean peerTapped)
    {
        for (AnimatedSprite object : getLevelObjects())
        {
            if (object != null)
            {
                if(object.getID() == ObjectID.ANIMATEDSPRITE)
                {
                    object.animateSprite(0.002f * dt);
                }

                // Animates and moves all of the player characters.
                if (object.getID() == ObjectID.CHARACTERONE || object.getID() == ObjectID.CHARACTERTWO)
                {
                    DynamicBody playerSprite = (DynamicBody) object.getBody().getUserData();
                    playerSprite.updateBody();

                    if(!playerSprite.isUsingCameraImage())
                    {
                        playerSprite.animateSprite(0.01f * dt);
                    }

                    // If a player square needs to respawn.
                    if(playerSprite.isRespawning())
                    {
                        // Set the player square at the spawn location.
                        playerSprite.translateFramework(object.getSpawnLocation());
                        playerSprite.setRespawnState(false);
                    }

                    if(multiplayerStatus)
                    {
                        if (playerSprite.getID() == ObjectID.CHARACTERTWO)
                        {
                            peerTappedResponse(playerSprite, peerTapped);
                        }
                    }
                }

                // Moves all of the obstacles.
                if (object.getID() == ObjectID.OBSTACLE)
                {
                    StaticBody enemySprite = (StaticBody) object.getBody().getUserData();

                    if(enemySprite.getSpriteRight() > 0.0f)
                    {
                        enemySprite.updateBody(new Vector2(-0.02f, 0.0f));
                    }
                }
            }
        }
    }

    //////////////////////////////////////////////////
    //                  Add To View                 //
    //==============================================//
    //  This add all of the level objects to the    //
    //  screen.                                     //
    //////////////////////////////////////////////////
    public void addToView()
    {
        levelGenerator.addToView();
    }

    //////////////////////////////////////////////////
    //                Check Collisions              //
    //==============================================//
    //  This will cycle through and check he Box2D  //
    //  world to see if any collisions have         //
    //  occured.                                    //
    //  We also provide collision response in here. //
    //////////////////////////////////////////////////
    private void checkCollisions()
    {
        // Get the head of the contact list.
        Contact contact = resources.getWorld().getContactList();

        // Get the number of contacts in this world.
        int contactCount = resources.getWorld().getContactCount();

        // Cycle through the contacts.
        for(int contactNumber = 0; contactNumber < contactCount; contactNumber++)
        {
            if(contact.isTouching())
            {
                // Get the colliding bodies.
                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();

                // Converting the two colliding bodies into objects that we can work with.
                AnimatedSprite gameObjectA = (AnimatedSprite) bodyA.getUserData();
                AnimatedSprite gameObjectB = (AnimatedSprite) bodyB.getUserData();

                // Collision test.
                // If the player is in contact with an obstacle.
                if(((gameObjectA.getID() == ObjectID.CHARACTERONE) && (gameObjectB.getID() == ObjectID.OBSTACLE))
                    || ((gameObjectB.getID() == ObjectID.CHARACTERTWO) && (gameObjectA.getID() == ObjectID.OBSTACLE)))
                {
                    // Do collision response here...
                    // Change back to the hurt animation.
                    if(!gameObjectA.isUsingCameraImage())
                    {
                        gameObjectA.changeTexture(new Vector2((6.0f / 7.0f), 0.0f));
                        gameObjectA.setAnimationFrames(0);
                    }

                    player.setGameOver(true);
                }

                // Get the next contact point.
                contact = contact.getNext();
            }
        }
    }

    //////////////////////////////////////////////////
    //                   Update.                    //
    //==============================================//
    //  This will update the level every frame.     //
    //////////////////////////////////////////////////
    public void update(final float dt, boolean peerTapped)
    {
        // If the level has been completed.
        finishedLevel();

        // Incrementing player distance.
        updatePlayerDistance();

        // Local function calls.
        handleLevelObjects(dt, peerTapped);

        // Check any collisions in the level.
        checkCollisions();
    }

    //////////////////////////////////////////////////
    //            Update Player Distance            //
    //==============================================//
    //  In order to output an updated text view     //
    //  onto the screen from our main thread, we    //
    //  have to go back to the UI thread and make   //
    //  any text updates there. This is because we  //
    //  originally created the text on the UI       //
    //  thread.                                     //
    //////////////////////////////////////////////////
    private void updatePlayerDistance()
    {
        // Creating a new thread.
        new Thread()
        {
            // When this new thread runs.
            @Override
            public void run()
            {
                // Try to...
                try
                {
                    // Make sure that the new level objects are added to the background on the correct thread.
                    activityReference.runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            if (!player.isGameOver())
                            {
                                // Update the player score.
                                player.distanceText.setText("Distance: " + player.distance);
                            }
                            else
                            {
                                distanceIncrementer.cancel(true);
                            }
                        }
                    });

                    // Slight loading time, without this, below catch gives an error.
                    Thread.sleep(300);
                }
                // Catch any exceptions with this thread.
                catch (InterruptedException e)
                {
                    // Print a stack trace so we know where we went wrong.
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //////////////////////////////////////////////////
    //               Finished Level                 //
    //==============================================//
    //  This will check to see if the player has    //
    //  reached a certain distance.                 //
    //  If we have, we should go to a new level.    //
    //////////////////////////////////////////////////
    private void finishedLevel()
    {
        if(levelNumber == 1)
        {
            if (player.distance == 18)
            {
                newLevel();
            }
        }
        else if(levelNumber >= 2)
        {
            if(player.distance == 28)
            {
                newLevel();
            }
        }
    }

    // Getters.
    // This will return the current set of game objects in the level.
    public Vector<AnimatedSprite> getLevelObjects() { return levelGenerator.getObjects(); }
}