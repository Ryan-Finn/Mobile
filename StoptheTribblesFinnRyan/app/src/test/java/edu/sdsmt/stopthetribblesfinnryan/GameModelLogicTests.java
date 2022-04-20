package edu.sdsmt.stopthetribblesfinnryan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;

import android.widget.Button;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Random;

import edu.sdsmt.stopthetribblesfinnryan.Control.GameActivity;
import edu.sdsmt.stopthetribblesfinnryan.Model.Game;
import edu.sdsmt.stopthetribblesfinnryan.View.GameView;

public class GameModelLogicTests {
    /**
     * Some unit tests for the Stop the Tribbles Game and StateMachine Classes.
     * @author Jarod Osborn
     */
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

    // STATE MACHINE INDEPENDENT

    // region Rules 1-2
    //  1.	Start with 100 tribbles.
    //  2.	Start with hunger level 0.
    @Test
    public void default_parameters_are_correct(){
        // Arrange
        GameFixture gameFixture = new GameFixture();
        Game game = gameFixture.getGame();

        // Act

        // Assert
        assertEquals(TRIBBLES_START, game.getTribbleCount());
        assertEquals(HUNGER_START, game.getHunger());
        assertEquals(MEALS_START, game.getMeals());
        assertFalse(game.isBureaucratPresent());
        assertEquals(SCORE_START, game.getScore());
        assertEquals(1, game.getTurns());
    }
    // endregion Rules 1-2

    // region Rule 3
    //  3.	The Reset button restarts the game to its initial state.
    @Test
    public void reset_is_correct(){
        // Arrange
        GameFixture gameFixture = new GameFixture();
        Game game = gameFixture.getGame();

        game.eat();

        // Act
        game.reset();

        // Assert
        assertEquals(TRIBBLES_START, game.getTribbleCount());
        assertEquals(HUNGER_START, game.getHunger());
        assertEquals(MEALS_START, game.getMeals());
    }
    // endregion Rule 3

    // region Rule 4
    //  4.	Eating decreases the hunger level by 3 until 0 is reached.
    //      Tribbles increase by 10 each time the Eat button is clicked as they eat any crumbs.
    //      If the Eat button is clicked at hunger 0, the number of tribbles doubles as food was left out.
    @Test
    public void eat_no_hunger(){
        // Arrange
        GameFixture gameFixture = new GameFixture();
        Game game = gameFixture.getGame();

        // Act
        game.eat();

        // Assert
        assertEquals(HUNGER_START, game.getHunger());
        assertEquals(MEALS_START - MEALS_DECREMENT, game.getMeals());
    }

    @Test
    public void eat_meals_remain(){
        // Arrange
        GameFixture gameFixture = new GameFixture();
        Game game = gameFixture.getGame();

        game.collectTribbles(25);
        int pre_hunger = game.getHunger();

        // Act
        game.eat();

        // Assert
        assertEquals(COLLECT_HUNGER_INCREMENT, pre_hunger);
        assertEquals(HUNGER_START, game.getHunger());
        assertEquals(MEALS_START - MEALS_DECREMENT, game.getMeals());
    }

    @Test
    public void eat_big_hunger(){
        // Arrange
        GameFixture gameFixture = new GameFixture();
        Game game = gameFixture.getGame();

        game.collectTribbles(25);
        game.collectTribbles(25);
        int pre_hunger = game.getHunger();

        // Act
        game.eat();

        // Assert
        assertEquals(COLLECT_HUNGER_INCREMENT * 2, pre_hunger);
        assertEquals(COLLECT_HUNGER_INCREMENT * 2 - HUNGER_DECREMENT, game.getHunger());
        assertEquals(MEALS_START - MEALS_DECREMENT, game.getMeals());
    }

    @Test
    public void eat_no_meals_remain(){
        // Arrange
        GameFixture gameFixture = new GameFixture();
        Game game = gameFixture.getGame();
        game.eat();
        game.eat();
        game.eat();
        int pre_count = game.getTribbleCount();
        int pre_meals = game.getMeals();

        // Act
        game.eat();

        // Assert
        assertEquals(MEALS_START - MEALS_DECREMENT * 3, pre_meals);

        // No meals. No change.
        assertEquals(MEALS_START - MEALS_DECREMENT * 3, game.getMeals());
    }
    // endregion Rule 4

    // region Rule 5
    //  5.	You can eat only 3 times per day. Clicking the button decreases the meals until 0, and resets to 3 with the New day button.
    @Test
    public void eat_restore_on_new_day(){
        // Arrange
        GameFixture gameFixture = new GameFixture();
        Game game = gameFixture.getGame();

        game.collectTribbles(10);
        game.eat();
        game.collectTribbles(10);
        game.eat();
        game.collectTribbles(10);
        game.eat();

        int pre_meals = game.getMeals();

        // Act
        game.newTurn();

        // Assert
        assertEquals(MEALS_START - MEALS_DECREMENT * 3, pre_meals);

        // New day
        assertEquals(MEALS_START, game.getMeals());
    }
    // endregion Rule 5

    // region Rule 6
    //  6.	New day adds 25% more tribbles (rounded up).
    @Test
    public void tribbles_update_new_day(){
        // Arrange
        GameFixture gameFixture = new GameFixture();
        Game game = gameFixture.getGame();

        int collection = random.nextInt(25);

        game.collectTribbles(collection);
        int pre_count = game.getTribbleCount();

        // Act
        game.newTurn();

        // Assert
        assertEquals(TRIBBLES_START - collection, pre_count);
        assertEquals((int)(pre_count * TRIBBLES_NEW_DAY_MODIFIER + 0.5), game.getTribbleCount());
        assertEquals(2, game.getTurns());
    }

    // endregion Rule 6

    //region Rule 7-8
    //  7.	For every 2 time “New Day” is clicked, a bureaucrat shows up until distracted.
    //      This occurs from the start of the game or the last time the bureaucrat was present.
    @Test
    public void bureaucrat_appears_first(){
        // Arrange
        GameFixture gameFixture = new GameFixture();
        Game game = gameFixture.getGame();

        // Act
        game.newTurn();
        game.newTurn();

        // Assert
        assertTrue(game.isBureaucratPresent());
    }

    // 8. “Distract” removes the bureaucrat, but also makes hunger go straight to 10.
    @Test
    public void bureaucrat_distracted(){
        // Arrange
        GameFixture gameFixture = new GameFixture();
        Game game = gameFixture.getGame();

        game.newTurn();
        game.newTurn();
        boolean b_present_first = game.isBureaucratPresent();

        // Act
        game.distract();

        // Assert
        assertTrue(b_present_first);
        assertFalse(game.isBureaucratPresent());
        assertEquals(HUNGER_MAX, game.getHunger());
    }


    @Test
    public void bureaucrat_appears_again(){
        // Arrange
        GameFixture gameFixture = new GameFixture();
        Game game = gameFixture.getGame();

        game.newTurn();
        game.newTurn();
        boolean b_present_first = game.isBureaucratPresent();

        game.distract();
        game.eat();
        game.collectTribbles(100); // Makes sure we don't lose.

        // Act
        game.newTurn();
        game.newTurn();

        // Assert
        assertTrue(b_present_first);
        assertTrue(game.isBureaucratPresent());
    }

    //endregion Rule 7-8

    //region Rule 11
    //  11.	Collecting tribbles increases hunger by 2.
    @Test
    public void collect_increases_hunger(){
        // Arrange
        GameFixture gameFixture = new GameFixture();
        Game game = gameFixture.getGame();

        int collection = random.nextInt(25);

        // Act
        game.collectTribbles(collection);

        // Assert
        assertEquals(COLLECT_HUNGER_INCREMENT, game.getHunger());
        assertEquals(TRIBBLES_START-collection, game.getTribbleCount());
    }

    //endregion Rule 11

    //region Lost
    @Test
    public void tribbles_lost(){
        // Arrange
        GameFixture gameFixture = new GameFixture();
        Game game = gameFixture.getGame();

        game.newTurn(); // 125
        game.newTurn(); // 156
        game.newTurn(); // 195

        // Act
        game.newTurn(); // LOSE

        // Assert
        assertTrue(game.getTribbleCount() > TRIBBLES_LOSE);
        assertTrue(game.isLost());
    }

    //endregion Lost

    //  UI ELEMENTS  -  Can't mock out the alert dialog builder call since it's 'new'ed directly in the class.
    //      If only Dependency Injection
    //  13.	The game is lost if there are 200+ tribbles and is displayed with a dialog box that states the player lost. Your choice on message.
    //  14.	The game is won if there are 0 tribbles and is displayed with a dialog box that states the player won. Your choice on message.
    //  15.	The player may change the color of the tribbles with floating action buttons.


    // A snazy little testing class.
    private class GameFixture {
        private Game game;
        private StateMachine stateMachine;
        private GameActivity ma_mock;
        private GameView gv_mock;
        private Button db_mock;

        private boolean db_enabled = false;

        public Game getGame(){
            return game;
        }
        public StateMachine getStateMachine(){
            return stateMachine;
        }

        public GameFixture(){
            game = new Game();
            gv_mock = Mockito.mock(GameView.class);
            db_mock = Mockito.mock(Button.class);
            Mockito.when(db_mock.isEnabled()).thenReturn(is_db_enabled());
            Mockito.doAnswer(invocation -> {
                Object[] args = invocation.getArguments();
                db_enabled = (boolean) args[0];
                return null;
            }).when(db_mock).setEnabled(anyBoolean());

            ma_mock = Mockito.mock(GameActivity.class);
            Mockito.when(ma_mock.findViewById(R.id.gameView)).thenReturn(gv_mock);
            Mockito.when(ma_mock.findViewById(R.id.distractBtn)).thenReturn(db_mock);

            stateMachine = new StateMachine(game, ma_mock);
        }

        public void reset(){
            game.reset();
            stateMachine = new StateMachine(game, ma_mock);
        }

        public boolean is_db_enabled(){
            return db_enabled;
        }
    }
}