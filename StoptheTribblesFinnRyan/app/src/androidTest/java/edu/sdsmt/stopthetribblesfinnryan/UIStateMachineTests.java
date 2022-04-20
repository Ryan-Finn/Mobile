package edu.sdsmt.stopthetribblesfinnryan;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.widget.Button;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import edu.sdsmt.stopthetribblesfinnryan.Control.GameActivity;
import edu.sdsmt.stopthetribblesfinnryan.Model.Game;
import edu.sdsmt.stopthetribblesfinnryan.View.GameView;
import edu.sdsmt.stopthetribblesfinnryan.States.HighHungerNoBureaucrat;
import edu.sdsmt.stopthetribblesfinnryan.States.HighHungerWithBureaucrat;
import edu.sdsmt.stopthetribblesfinnryan.States.LowHungerNoBureaucrat;
import edu.sdsmt.stopthetribblesfinnryan.States.LowHungerWithBureaucrat;
import edu.sdsmt.stopthetribblesfinnryan.States.MidHungerNoBureaucrat;
import edu.sdsmt.stopthetribblesfinnryan.States.MidHungerWithBureaucrat;

/**
 * Instrumented test, which will execute on an Android device.
 * Some unit tests for the Stop the Tribbles Game and StateMachine Classes.
 *      * @author Jarod Osborn and Dr Lisa Rebenitsch
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UIStateMachineTests {

    @Rule
    public ActivityScenarioRule<GameActivity> act = new ActivityScenarioRule<>(GameActivity.class);

    static final Random random = new Random();
    static final int SCORE_START = 0;

    static final int TRIBBLES_START = 100;
    static final int TRIBBLES_LOSE = 200;
    static final int TRIBBLES_WIN = 0;
    static final float TRIBBLES_NEW_DAY_MODIFIER = 1.25f;

    static final int MEALS_START = 3;
    static final int MEALS_DECREMENT = 1;

    static final int HUNGER_START = 0;
    static final int HUNGER_DECREMENT = 3;
    static final int HUNGER_MAX = 10;

    static final float EAT_NO_HUNGER_T_MODIFIER = 2.0f;
    static final int EAT_T_INCREMENT = 10;

    static final int COLLECT_HUNGER_INCREMENT = 2;
    static final int COLLECT_T_LH_NB_DECREMENT = 25;
    static final int COLLECT_T_MH_NB_DECREMENT = 15;
    static final int COLLECT_T_LH_B_DECREMENT = 10;
    static final int COLLECT_T_MH_B_DECREMENT = 5;
    static final int COLLECT_T_HH_DECREMENT = 0;

    private Game game;
    private StateMachine sm;
    private Button distractBtn;

    //Initialize to have access to underlying game, state machine, and distract button
    private void init(){
        AtomicReference<Game> gameAtom = new AtomicReference<>();
        AtomicReference<StateMachine> smAtom = new AtomicReference<>();
        AtomicReference<Button> distractBtnAtom = new AtomicReference<>();
        act.getScenario().onActivity(act -> {
            gameAtom.set(act.getGame());
            smAtom.set(act.getStateMachine());
            distractBtnAtom.set(act.findViewById(R.id.distractBtn));
        });

        game = gameAtom.get();
        sm = smAtom.get();
        distractBtn = distractBtnAtom.get();
    }
    // Unit Testable
    //  1.	Start with 100 tribbles.
    //  2.	Start with hunger level 0.
    //  3.	The Reset button restarts the game to its initial state.
    //  4.	Eating decreases the hunger level by 3 until 0 is reached.
    //      Tribbles increase by 10 each time the Eat button is clicked as they eat any crumbs.
    //      If the Eat button is clicked at hunger 0, the number of tribbles doubles as food was left out.
    //  5.	You can eat only 3 times per day. Clicking the button decreases the meals until 0, and resets to 3 with the New day button.
    //  6.	New day adds 25% more tribbles (rounded up).
    //  7.	For every 2 time “New Day” is clicked, a bureaucrat shows up until distracted.
    //      This occurs from the start of the game or the last time the bureaucrat was present.
    //  8.	“Distract” removes the bureaucrat, but also makes hunger go straight to 10.
    //  9.	The Distract button must be disabled if the bureaucrat is not present and enabled if present.
    //      This must be controlled with the state machine.
    //  10.	Hunger maxes at 10. Low hunger is [0, 3]. Mid hunger is [4, 7]. High hunger is [8, 10].
    //  11.	Collecting tribbles increases hunger by 2.
    //  12.	Collecting tribbles removes a certain number of tribbles based on the current state.
    //      a.	If “low hunger and no bureaucrat”, 25 tribbles are collected
    //      b.	If “mid hunger and no bureaucrat”, 15 tribbles are collected
    //      c.	If “low hunger and bureaucrat”, 10 tribbles are collected
    //      d.	If “mid hunger and bureaucrat”, 5 tribbles are collected
    //      e.	If “high hunger and with or without bureaucrat”, 0 tribbles are collected
    //  13.	The game is lost if there are 200+ tribbles and is displayed with a dialog box that states the player lost. Your choice on message.
    //  14.	The game is won if there are 0 tribbles and is displayed with a dialog box that states the player won. Your choice on message.
    //  15.	The player may change the color of the tribbles with floating action buttons.

    // STATE MACHINE DEPENDENT

    //region Rule 9
    //  9.	The Distract button must be disabled if the bureaucrat is not present and enabled if present.
    //      This must be controlled with the state machine.
    @Test
    public void distract_disabled_initially(){
        init();

        // Assert
        assertFalse(game.isBureaucratPresent());
        assertFalse(distractBtn.isEnabled());
        assertEquals(LowHungerNoBureaucrat.class.getName(), sm.getCurrentStateName());


    }

    @Test
    public void distract_enabled_correctly(){
        // Arrange
        init();

        //Act
        onView(withId(R.id.dayBtn)).perform(click());
        onView(withId(R.id.dayBtn)).perform(click());

        // Assert
        assertTrue(game.isBureaucratPresent());
        assertTrue(distractBtn.isEnabled());
        assertEquals(LowHungerWithBureaucrat.class.getName(), sm.getCurrentStateName());
    }


    @Test
    public void distract_disabled_correctly(){
        // Arrange
        init();

        onView(withId(R.id.dayBtn)).perform(click());
        onView(withId(R.id.dayBtn)).perform(click());

        assertTrue(distractBtn.isEnabled());

        // Act
        onView(withId(R.id.distractBtn)).perform(click());

        // Assert
        assertFalse(game.isBureaucratPresent());
        assertFalse(distractBtn.isEnabled());
        assertEquals(HighHungerNoBureaucrat.class.getName(), sm.getCurrentStateName());
    }

    //endregion Rule 9

    //region Rule 10
    //  10.	Hunger maxes at 10. Low hunger is [0, 3]. Mid hunger is [4, 7]. High hunger is [8, 10].
    @Test
    public void machine_starts_low_hunger(){
        // Arrange
        init();

        // Assert
        assertEquals(LowHungerNoBureaucrat.class.getName(), sm.getCurrentStateName());
    }


    @Test
    public void machine_reaches_mid_hunger(){
        // Arrange
       init();

        // Act
        onView(withId(R.id.collectBtn)).perform(click());
        onView(withId(R.id.collectBtn)).perform(click());

        // Assert
        assertEquals(MidHungerNoBureaucrat.class.getName(), sm.getCurrentStateName());
    }

    @Test
    public void machine_reaches_high_hunger(){
        // Arrange
        init();
        
        // Act
        onView(withId(R.id.collectBtn)).perform(click());
        onView(withId(R.id.collectBtn)).perform(click());
        onView(withId(R.id.collectBtn)).perform(click());
        onView(withId(R.id.collectBtn)).perform(click());

        // Assert
        assertFalse(distractBtn.isEnabled());
        assertEquals(HighHungerNoBureaucrat.class.getName(), sm.getCurrentStateName());
    }

     
    @Test
    public void machine_returns_mid_hunger(){
        // Arrange
        init();

        onView(withId(R.id.collectBtn)).perform(click());
        onView(withId(R.id.collectBtn)).perform(click());
        onView(withId(R.id.collectBtn)).perform(click());
        onView(withId(R.id.collectBtn)).perform(click());

        // Act
        onView(withId(R.id.eatBtn)).perform(click());

        // Assert
        assertFalse(distractBtn.isEnabled());
        assertEquals(MidHungerNoBureaucrat.class.getName(), sm.getCurrentStateName());
    }

    @Test
    public void machine_returns_low_hunger(){
        // Arrange
       init();

        onView(withId(R.id.collectBtn)).perform(click());
        onView(withId(R.id.collectBtn)).perform(click());
        onView(withId(R.id.collectBtn)).perform(click());
        onView(withId(R.id.collectBtn)).perform(click());

        // Act
        onView(withId(R.id.eatBtn)).perform(click());
        onView(withId(R.id.eatBtn)).perform(click());

        // Assert
        assertFalse(distractBtn.isEnabled());
        assertEquals(LowHungerNoBureaucrat.class.getName(), sm.getCurrentStateName());
    }

    @Test
    public void machine_returns_low_hunger_with_bureaucrat(){
        // Arrange
        init();

        onView(withId(R.id.dayBtn)).perform(click());
        onView(withId(R.id.dayBtn)).perform(click());
        // Assert
        assertTrue(distractBtn.isEnabled());
        assertEquals(LowHungerWithBureaucrat.class.getName(), sm.getCurrentStateName());

        onView(withId(R.id.distractBtn)).perform(click());

        // Assert
        assertFalse(distractBtn.isEnabled());
        assertEquals(HighHungerNoBureaucrat.class.getName(), sm.getCurrentStateName());
    }

    @Test
    public void machine_returns_mid_hunger_with_bureaucrat(){
        // Arrange
        init();

        onView(withId(R.id.dayBtn)).perform(click());
        onView(withId(R.id.collectBtn)).perform(click());
        onView(withId(R.id.dayBtn)).perform(click());
        onView(withId(R.id.collectBtn)).perform(click());

        // Assert
        assertTrue(distractBtn.isEnabled());
        assertEquals(MidHungerWithBureaucrat.class.getName(), sm.getCurrentStateName());

        onView(withId(R.id.distractBtn)).perform(click());

        // Assert
        assertFalse(distractBtn.isEnabled());
        assertEquals(HighHungerNoBureaucrat.class.getName(), sm.getCurrentStateName());
    }

    @Test
    public void machine_returns_high_hunger_with_bureaucrat(){
        // Arrange
        init();

        onView(withId(R.id.dayBtn)).perform(click());
        onView(withId(R.id.collectBtn)).perform(click());
        onView(withId(R.id.collectBtn)).perform(click());
        onView(withId(R.id.dayBtn)).perform(click());
        onView(withId(R.id.collectBtn)).perform(click());
        onView(withId(R.id.collectBtn)).perform(click());

        // Assert
        assertTrue(distractBtn.isEnabled());
        assertEquals(HighHungerWithBureaucrat.class.getName(), sm.getCurrentStateName());

        onView(withId(R.id.distractBtn)).perform(click());

        // Assert
        assertFalse(distractBtn.isEnabled());
        assertEquals(HighHungerNoBureaucrat.class.getName(), sm.getCurrentStateName());
    }


    //endregion Rule 10

    //region Rule 12
    //  12.	Collecting tribbles removes a certain number of tribbles based on the current state.
    //      a.	If “low hunger and no bureaucrat”, 25 tribbles are collected
    //      b.	If “mid hunger and no bureaucrat”, 15 tribbles are collected
    //      c.	If “low hunger and bureaucrat”, 10 tribbles are collected
    //      d.	If “mid hunger and bureaucrat”, 5 tribbles are collected
    //      e.	If “high hunger and with or without bureaucrat”, 0 tribbles are collected
    @Test
    public void machine_updates_collection_no_bureaucrat(){
        // Arrange
        init();
        int low_collect = sm.getCollection();
        int mid_collect;
        String low_state = sm.getCurrentStateName();
        String mid_state;

        // Act
        onView(withId(R.id.collectBtn)).perform(click());
        onView(withId(R.id.collectBtn)).perform(click());
        mid_collect = sm.getCollection();
        mid_state = sm.getCurrentStateName();

        onView(withId(R.id.collectBtn)).perform(click());
        onView(withId(R.id.collectBtn)).perform(click());

        // Assert
        assertEquals(COLLECT_T_LH_NB_DECREMENT, low_collect);
        assertEquals(COLLECT_T_MH_NB_DECREMENT, mid_collect);
        assertEquals(COLLECT_T_HH_DECREMENT, sm.getCollection());
        assertEquals(LowHungerNoBureaucrat.class.getName(), low_state);
        assertEquals(MidHungerNoBureaucrat.class.getName(), mid_state);
        assertEquals(HighHungerNoBureaucrat.class.getName(), sm.getCurrentStateName());
    }

    @Test
    public void machine_updates_collection_with_bureaucrat(){
        // Arrange
        init();

        onView(withId(R.id.dayBtn)).perform(click());
        onView(withId(R.id.dayBtn)).perform(click());

        int low_collect = sm.getCollection();
        int mid_collect;
        String low_state = sm.getCurrentStateName();
        String mid_state;

        // Act
        onView(withId(R.id.collectBtn)).perform(click());
        onView(withId(R.id.collectBtn)).perform(click());
        mid_collect = sm.getCollection();
        mid_state = sm.getCurrentStateName();

        onView(withId(R.id.collectBtn)).perform(click());
        onView(withId(R.id.collectBtn)).perform(click());

        // Assert
        assertEquals(COLLECT_T_LH_B_DECREMENT, low_collect);
        assertEquals(COLLECT_T_MH_B_DECREMENT, mid_collect);
        assertEquals(COLLECT_T_HH_DECREMENT, sm.getCollection());
        assertEquals(LowHungerWithBureaucrat.class.getName(), low_state);
        assertEquals(MidHungerWithBureaucrat.class.getName(), mid_state);
        assertEquals(HighHungerWithBureaucrat.class.getName(), sm.getCurrentStateName());
    }
    //endregion Rule 12


}