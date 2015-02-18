package pt314.blocks.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class GameBoardTest {
	GameBoard board;
	
	@Before
	public void setUp() throws Exception {
		board = new GameBoard(5,5);	//create 5x5 game board
		board.placeBlockAt(new VerticalBlock(), 0, 1);
		board.placeBlockAt(new VerticalBlock(), 1, 2);
		board.placeBlockAt(new HorizontalBlock(), 2, 0); board.placeBlockAt(new TargetBlock(), 2, 2);
		board.placeBlockAt(new HorizontalBlock(), 3, 2); board.placeBlockAt(new HorizontalBlock(), 3, 4);
		board.placeBlockAt(new VerticalBlock(), 4, 4);
	}

	@Test
	public void testMoveBlock() {
		assertFalse(board.moveBlock(0, 0, Direction.UP, 1));	//attempt to move an empty cell
		testMoveOutsideBoard();
		testValidDirection();
		testMoveThroughBlock();
		testValidMoves();
	}
	
	@Test
	public void testMoveOutsideBoard() {
		assertFalse(board.moveBlock(0, 1, Direction.UP, 1));	//attempt to move above board
		assertFalse(board.moveBlock(2, 0, Direction.LEFT, 1));	//attempt to move left of board
		assertFalse(board.moveBlock(3, 4, Direction.RIGHT, 1));	//attempt to move right of board
		assertFalse(board.moveBlock(4, 4, Direction.DOWN, 1));	//attempt to move below board
	}
	
	@Test
	public void testValidDirection() {
		assertFalse(board.moveBlock(0, 1, Direction.RIGHT, 1));	//attempt to move vertical block horizontally
		assertFalse(board.moveBlock(2, 0, Direction.UP, 1));	//attempt to move horizontal block vertically
		assertFalse(board.moveBlock(2, 2, Direction.UP, 1));	//attempt to move target block vertically
	}
	
	@Test
	public void testMoveThroughBlock() {
		assertFalse(board.moveBlock(2, 2, Direction.LEFT, 2));	//attempt to move target block horizontally through another block
		assertFalse(board.moveBlock(2, 2, Direction.DOWN, 2));	//attempt to move target block vertically through another block
		assertFalse(board.moveBlock(3, 2, Direction.RIGHT, 2));	//attempt to move a horizontal block through another block
		assertFalse(board.moveBlock(1, 2, Direction.DOWN, 3));	//attempt to move a vertically block through another block
	}

	@Test
	public void testValidMoves() {
		assertTrue(board.moveBlock(0, 1, Direction.DOWN, 2));	//move vertical block down
		assertTrue(board.moveBlock(1, 2, Direction.UP, 1));		//move vertical block up
		assertTrue(board.moveBlock(2, 2, Direction.RIGHT, 2));	//move target block right
		assertTrue(board.moveBlock(3, 2, Direction.RIGHT, 1));	//move horizontal block right
		assertTrue(board.moveBlock(3, 3, Direction.LEFT, 2));	//move horizontal block left
		
		//check correct locations of blocks
		assertTrue(board.getBlockAt(2, 1) instanceof VerticalBlock);
		assertTrue(board.getBlockAt(0, 2) instanceof VerticalBlock);
		assertTrue(board.getBlockAt(4, 4) instanceof VerticalBlock);
		assertTrue(board.getBlockAt(2, 0) instanceof HorizontalBlock);
		assertTrue(board.getBlockAt(3, 1) instanceof HorizontalBlock);
		assertTrue(board.getBlockAt(3, 4) instanceof HorizontalBlock);
		assertTrue(board.getBlockAt(2, 4) instanceof TargetBlock);
	}
}
