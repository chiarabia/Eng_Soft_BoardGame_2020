package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.*;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
			gameTwoPlayers = new Game (2,gamePlayerTwo);
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
	void gameShouldCreateTheRightAmountOfGodPowers(){
		assertEquals(3,gameThreePlayers.getGodPowers().size());
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
	void gameInstanceShouldThrowWithIllegalNumberOfPlayer() {
		assertThrows(Exception.class, () -> {
			new Game(-1, gamePlayerTwo);
		});
	}


	@Test
	void gameInstanceShouldThrowWithNullList() {
		assertThrows(NullPointerException.class, () -> {
			new Game (1,null);
		});
	}




}
