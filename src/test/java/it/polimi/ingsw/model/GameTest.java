package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;


public class GameTest {
	List<String> gamePlayerTwo = new ArrayList<>();
	List<String> gamePlayerThree = new ArrayList<>();
	Game gameTwoPlayers, gameThreePlayers;
	Player playerOne = new Player ("pippo",1);
	Player playerTwo = new Player ("pluto",2);
	Player playerThree = new Player ("paperino",3);


	@BeforeEach
	void setUp(){
		gamePlayerTwo.add("pippo");
		gamePlayerTwo.add("pluto");
		gamePlayerThree.add("pippo");
		gamePlayerThree.add("pluto");
		gamePlayerThree.add("paperino");
		try {
			gameTwoPlayers = new Game(2,gamePlayerTwo);
			gameThreePlayers = new Game (3, gamePlayerThree);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterEach
	void reset() {
		gamePlayerThree = new ArrayList<>();
		gamePlayerTwo = new ArrayList<>();
	}

	@Test
	void gameShouldSetTheRightAmountOfPlayers(){
		assertEquals(2, gameTwoPlayers.getNumOfPlayers());
	}

	@Test
	void gameShouldStartWithoutGodPowers(){
		assertEquals(0,gameThreePlayers.getGodPowers().size());
	}

	@Test
	void gameShouldCreateTheRightListOfPlayers(){
		List players = new ArrayList();
		players.add(playerOne);
		players.add(playerTwo);
		assertEquals(players, gameTwoPlayers.getPlayers());
	}

	@Test
	void gameShouldSetTheRightBoard(){
		assertNotNull(gameTwoPlayers.getBoard());
	}

	@Test
	void gameInstanceShouldThrowWithNullList() {
		assertThrows(NullPointerException.class, () -> {
			new Game (1,null);
		});
	}




}
