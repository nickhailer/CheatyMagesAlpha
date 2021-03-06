package edu.up.cs301.game.cheatymages;

import android.widget.Button;

import java.util.ArrayList;

import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.gameConfiguration.GameConfig;
import edu.up.cs301.game.GameFramework.gameConfiguration.GamePlayerType;
import edu.up.cs301.game.GameFramework.infoMessage.GameState;
import edu.up.cs301.game.GameFramework.players.GamePlayer;
import edu.up.cs301.game.R;
import edu.up.cs301.game.cheatymages.Players.*;


public class CMMainActivity extends GameMainActivity {

    /**
     * Creates a default configuration of the game
     *
     * @return default configuration
     */
    @Override
    public GameConfig createDefaultConfig() {

        ArrayList<GamePlayerType> playerTypes = new ArrayList<>();

        playerTypes.add(new GamePlayerType("Human Player") {
            @Override
            public GamePlayer createPlayer(String name) {
                return new CMHumanPlayer(name, R.layout.cm_human_player);
            }
        });

        playerTypes.add(new GamePlayerType("Computer Player Dumb") {
            @Override
            public GamePlayer createPlayer(String name) {
                return new CMComputerPlayerDumb(name);
            }
        });

        playerTypes.add(new GamePlayerType("Computer Player Smart") {
            @Override
            public GamePlayer createPlayer(String name) {
                return new CMComputerPlayerSmart(name);
            }
        });

        //TODO ONCE IMPLEMENTED ADD OTHER PLAYER TYPES HERE

        GameConfig defaultConfig = new GameConfig(playerTypes, 3, 6, "Cheaty Mages", 5213);

        //TODO CHANGE THIS TO BE 1 HUMAN PLAYER AND 2 SMART AIS ONCE IMPLEMENTED
        defaultConfig.addPlayer("P0", 0);
        defaultConfig.addPlayer("P1", 1);
        defaultConfig.addPlayer("P2", 1);

        defaultConfig.setRemoteData("Remote Player", "", 0);

        return defaultConfig;
    }

    /**
     * @param gameState The desired gameState to start at or null for new game
     * @return
     */
    @Override
    public LocalGame createLocalGame(GameState gameState) {
        //TODO FIX THIS I DONT KNOW HOW BUT IT WILL ALWAYS ASSUME NEW GAMES HAVE 3 PLAYERS
        if (gameState == null) {
            return new CMLocalGame(3);
        }
        return new CMLocalGame(((CMGameState) gameState).getNumPlayers());
    }
}