package pt314.blocks.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GameBoard {

	private int width;
	private int height;
	private Block[][] blocks;
	
	public GameBoard(int width, int height) {
		this.width = width;
		this.height = height;
		blocks = new Block[height][width];
	}
	
	/**
	 * Place block at the specified location.
	 * 
	 * If there is a block at the location, it is replaced by the new block.
	 */
	public void placeBlockAt(Block block, int row, int col) {
		blocks[row][col] = block;
	}
	
	// TODO: Check for out of bounds
	public Block getBlockAt(int row, int col) {
		return blocks[row][col];
	}

	/**
	 * Move block at the specified location.
	 * 
	 * @param dir direction of movement.
	 * @param dist absolute movement distance.
	 * 
	 * @return <code>true</code> if and only if the move is possible.
	 */
	public boolean moveBlock(int row, int col, Direction dir, int dist) {
		
		// TODO: throw exception if move is invalid, instead of using return value
		
		Block block = blocks[row][col];

		// no block at specified location
		if (block == null)
			return false;
		
		// block cannot move in the specified direction
		if (!block.isValidDirection(dir))
			return false;
		
		// determine new location
		int newRow = row;
		int newCol = col;
		if (dir == Direction.UP)
			newRow -= dist;
		else if (dir == Direction.DOWN)
			newRow += dist;
		else if (dir == Direction.LEFT)
			newCol -= dist;
		else if (dir == Direction.RIGHT)
			newCol += dist;

		// destination out of bounds
		if (!isWithinBounds(newRow, newCol))
			return false;
		
		int dx = 0;
		int dy = 0;
		if (dir == Direction.UP)
			dy = -1;
		else if (dir == Direction.DOWN)
			dy = 1;
		else if (dir == Direction.LEFT)
			dx = -1;
		else if (dir == Direction.RIGHT)
			dx = 1;
		
		// check all cells from block location to destination
		int tmpRow = row;
		int tmpCol = col;
		for (int i = 0; i < dist; i++) {
			tmpRow += dy;
			tmpCol += dx;
			if (blocks[tmpRow][tmpCol] != null)
				return false; // another block in the way
		}
		
		blocks[newRow][newCol] = blocks[row][col];
		blocks[row][col] = null;
		return true;
	}
	
	/**
	 * Check if a location is inside the board.
	 */
	public boolean isWithinBounds(int row, int col) {
		if (row < 0 || row >= height)
			return false;
		if (col < 0 || col >= width)
			return false;
		return true;
	}
	
	/**
	 * Check if Target Block in Rightmost column
	 */
	public boolean isTargetAtExit() {
		for(int r=0; r<height; r++)
			if(blocks[r][width-1] instanceof TargetBlock)
				return true;
		
		return false;
	}
	
	/**
	 * Load puzzle according to given file
	 */
	public void loadPuzzle(File puzzleFile) throws NullPointerException, FileNotFoundException, IllegalStateException {
		if(puzzleFile == null)
			throw new NullPointerException("Error Loading File - Null Value Given.");
		
		try {
			String[][] newBoard = readPuzzle(puzzleFile);
			validateBoard(newBoard);
			initBoard(newBoard);
		}
		catch(IllegalStateException e) {
			throw new IllegalStateException (e.getMessage());
		}
		catch(FileNotFoundException e) {
			throw new FileNotFoundException(e.getMessage());
		}
		catch(Exception e) {
			throw new IllegalStateException("Error Loading Puzzle File.  Invalid File Format.");
		}
	}
	
	/**
	 * Read puzzle file
	 */
	private String[][] readPuzzle(File puzzleFile) throws IllegalStateException, FileNotFoundException {
		String[][] newBoard = null;
		Scanner fileScan = null, rowScan = null;
		try {
			fileScan = new Scanner(puzzleFile);
			String line1 = fileScan.nextLine();
			rowScan = new Scanner(line1);
			int rows = Integer.parseInt(rowScan.next());
			int cols = Integer.parseInt(rowScan.next());
			newBoard = new String[rows][cols];
			for(int r=0; r<rows; r++) {
				String boardLine = fileScan.nextLine();
				for(int c=0; c<cols; c++) {
					String block = boardLine.substring(c,c+1);
						newBoard[r][c] = block;
				}
			}
		}
		catch(FileNotFoundException e) {
			throw new FileNotFoundException("File Not Found.");
		}
		catch(Exception e) {
			throw new IllegalStateException("Error Reading Puzzle File.");
		}
		finally {
			if(fileScan != null)
				fileScan.close();
			if(rowScan != null)
				rowScan.close();
		}
		
		return newBoard;
	}
	
	/**
	 * Validate new board fits appropriate characteristics 
	 */
	private void validateBoard(String[][] newBoard) throws IllegalStateException {
		if(newBoard.length < 1 || newBoard[0].length < 1)
			throw new IllegalStateException("Invalid Board Size. Board must be at least 1x1.");
		
		if(!validateBlocks(newBoard))
			throw new IllegalStateException("Invalid Board.");
		
		if(!validateTargetBlock(newBoard, newBoard.length, newBoard[0].length))
			throw new IllegalStateException("Target Block is not the Right-most Block");
		
		if(!hasOneTarget(newBoard))
			throw new IllegalStateException("Invalid Board. One Target Block is needed for each board.");
	}
	
	/**
	 * Validate valid blocks
	 */
	private boolean validateBlocks(String[][] newBoard) throws IllegalStateException {
		boolean retVal = true;
		for(int r=0; r<newBoard.length; r++)
			for(int c=0; c<newBoard[0].length; c++) {
				switch(newBoard[r][c]) {
				case "H" :
					break;
				case "V" :
					break;
				case "T" :
					break;
				case "." :
					break;
				default :
					retVal = false;
				}
			}
		
		return retVal;
	}
	
	/**
	 * Validate target block can exit
	 */
	private boolean validateTargetBlock(String[][] newBoard, int row, int col) {
		boolean retVal = true;
		for(int c=col; c<newBoard[0].length; c++) {
			if(newBoard[row][c].equals("H")) {	//should not have a horizontal block right of target block
				retVal = false;
				break;
			}
		}
		
		return retVal;
	}
	
	/**
	 * Validate number of target blocks
	 */
	private boolean hasOneTarget(String[][] newBoard) {
		int targetCount = 0;
		
		for(int r=0; r<newBoard.length; r++)
			for(int c=0; c<newBoard[0].length; c++)
				if(newBoard[r][c].equals("T"))
					targetCount++;
		
		if(targetCount == 1)
			return true;
		else
			return false;
	}
	
	/**
	 * Initialize new board
	 */
	private void initBoard(String[][] newBoard) {
		int rows = newBoard.length;
		int cols = newBoard[0].length;
		blocks = new Block[rows][cols];
		for(int r=0; r<rows; r++)
			for(int c=0; c<cols; c++) {
				switch(newBoard[r][c]) {
				case "H" :
					blocks[r][c] = new HorizontalBlock();
					break;
				case "V" :
					blocks[r][c] = new VerticalBlock();
					break;
				case "T" :
					blocks[r][c] = new TargetBlock();
					break;
				case "." :
					blocks[r][c] = null;
					break;
				}
			}
		
		width = cols;
		height = rows;
	}

	/**
	 * Print the board to standard out.
	 */
	public void print() {
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				Block block = blocks[row][col];
				char ch = '.';
				if (block instanceof TargetBlock)
					ch = 'T';
				else if (block instanceof HorizontalBlock)
					ch = 'H';
				else if (block instanceof VerticalBlock)
					ch = 'V';
				System.out.print(ch);
			}
			System.out.println();
		}
		System.out.println();
	}
}
