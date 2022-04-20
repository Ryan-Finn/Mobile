package edu.sdsmt.stopthetribblesfinnryan;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import edu.sdsmt.stopthetribblesfinnryan.Control.GameActivity;
import edu.sdsmt.stopthetribblesfinnryan.Control.StateMachine;
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
public class UITests {

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
    private GameView gameView;
    private StateMachine sm;
    private Button distractBtn;
    private TextView scoreCnt;
    private TextView daysCnt;
    private TextView tribblesCnt;
    private TextView hungerCnt;


    //Initialize to have access to underlying game, state machine, and distract button
    private void init(){
        AtomicReference<Game> gameAtom = new AtomicReference<>();
        AtomicReference<GameView> gameViewAtom = new AtomicReference<>();
        AtomicReference<StateMachine> smAtom = new AtomicReference<>();
        AtomicReference<Button> distractBtnAtom = new AtomicReference<>();
        AtomicReference<TextView> scoreAtom = new AtomicReference<>();
        AtomicReference<TextView> daysAtom = new AtomicReference<>();
        AtomicReference<TextView> tribblesAtom = new AtomicReference<>();
        AtomicReference<TextView> hungerAtom = new AtomicReference<>();

        act.getScenario().onActivity(act -> {
            gameAtom.set(act.getGame());
            smAtom.set(act.getStateMachine());
            distractBtnAtom.set(act.findViewById(R.id.distractBtn));
            gameViewAtom.set(act.findViewById(R.id.gameView));
            scoreAtom.set(act.findViewById(R.id.scoreCnt));
            daysAtom.set(act.findViewById(R.id.daysText));
            tribblesAtom.set(act.findViewById(R.id.tribbleCnt));
            hungerAtom.set(act.findViewById(R.id.hungerText));
        });

        game = gameAtom.get();
        sm = smAtom.get();
        distractBtn = distractBtnAtom.get();
        gameView = gameViewAtom.get();
        scoreCnt = scoreAtom.get();
        daysCnt = daysAtom.get();
        tribblesCnt = tribblesAtom.get();
        hungerCnt = hungerAtom.get();
    }

    public void check_number_match(){
        assertEquals(Integer.parseInt(tribblesCnt.getText().toString()), game.getTribbleCount());
        assertEquals(Integer.parseInt(daysCnt.getText().toString()), game.getTurns());
        assertEquals(Integer.parseInt(hungerCnt.getText().toString()), game.getHunger());
        assertEquals(Integer.parseInt(scoreCnt.getText().toString()), game.getScore());
    }

    //region Rule 10
    //  10.	Hunger maxes at 10. Low hunger is [0, 3]. Mid hunger is [4, 7]. High hunger is [8, 10].
    @Test
    public void machine_starts_low_hunger(){
        // Arrange
        init();

        // Assert
        assertEquals(LowHungerNoBureaucrat.class.getName(), sm.getCurrentStateName());
        assertEquals(Color.LTGRAY, gameView.getBackgroundColor());

       check_number_match();
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
        assertEquals(Color.GRAY, gameView.getBackgroundColor());
        check_number_match();
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
        assertEquals(Color.DKGRAY, gameView.getBackgroundColor());
        check_number_match();
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
        assertEquals(Color.GRAY, gameView.getBackgroundColor());
        check_number_match();
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
        assertEquals(Color.LTGRAY, gameView.getBackgroundColor());
        check_number_match();

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
        assertEquals(Color.LTGRAY, gameView.getBackgroundColor());
        assertTrue( gameView.isBorderOn());
        check_number_match();

        // act
        onView(withId(R.id.distractBtn)).perform(click());

        // Assert
        assertFalse(distractBtn.isEnabled());
        assertEquals(HighHungerNoBureaucrat.class.getName(), sm.getCurrentStateName());
        assertEquals(Color.DKGRAY, gameView.getBackgroundColor());
        assertFalse( gameView.isBorderOn());
        check_number_match();
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
        assertEquals(Color.GRAY, gameView.getBackgroundColor());
        assertTrue( gameView.isBorderOn());
        check_number_match();

        //Act
        onView(withId(R.id.distractBtn)).perform(click());

        // Assert
        assertFalse(distractBtn.isEnabled());
        assertEquals(HighHungerNoBureaucrat.class.getName(), sm.getCurrentStateName());
        assertEquals(Color.DKGRAY, gameView.getBackgroundColor());
        assertFalse( gameView.isBorderOn());
        check_number_match();
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
        assertEquals(Color.DKGRAY, gameView.getBackgroundColor());
        assertTrue( gameView.isBorderOn());
        check_number_match();

        //Act
        onView(withId(R.id.distractBtn)).perform(click());

        // Assert
        assertFalse(distractBtn.isEnabled());
        assertEquals(HighHungerNoBureaucrat.class.getName(), sm.getCurrentStateName());
        assertEquals(Color.DKGRAY, gameView.getBackgroundColor());
        assertFalse( gameView.isBorderOn());
        check_number_match();
    }

}