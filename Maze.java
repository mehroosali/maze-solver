import java.util.Random;
import java.util.Scanner;

/**
@author Mehroos Ali (mxa200089)
 */
public class Maze {

     int rows;
     int columns;
     int totalCells;
     DisjSets ds;
     Cell[][] maze;

     /**
      * constructor method to initialize the maze and disjunction set.
      * @param rows total number of rows for the maze.
      * @param columns total number of columns for the maze.
      */
    public Maze(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        totalCells = rows * columns;
        maze = createMaze(rows, columns);
        ds = new DisjSets(totalCells);
    }

    /**
     * private helper method to initialize and create 2-D array maze based on number of rows and columns.
     * @param rows total number of rows for the maze.
     * @param columns total number of columns for the maze.
     * @return maze created 2-D maze array .
     */
    private Cell[][] createMaze(int rows, int columns) {
        Cell[][] maze = new Cell[rows][columns];
        int count = 0;
        for(int i=0; i<rows; i++) {
            for(int j=0; j<columns; j++) {
                // initial values of down and right values of a cell is true to show no walls are broken.
                maze[i][j] = new Cell(count++, true,true);
            }
        }
        return maze;
    }

    /**
     * public method to loop through the 2-D maze array and solve it using the algorithm described in the method.
     */
    public void solveMaze() {

        Random rand = new Random();

        // main loop which ends the maze completion when root of the first and last element cell is same.
        while (ds.find(0) != ds.find(totalCells - 1)){

            // generating a random row and column number.
            int currRow = rand.nextInt(rows);
            int currCol = rand.nextInt(columns);

            // get the current cell and its value based on random row and column number.
            Cell currCell = maze[currRow][currCol];
            int currValue = currCell.value;

            // find the root of the current cell by calling find on the current value.
            int root1 = ds.find(currValue);
            // root2 is the root of the adjacent cell from the current cell. adjacent cell is determined based on the current
            // position of the cell.
            int root2;
            // this flag is used to track if we are to remove the down wall of the current cell.
            boolean removeDownWall = false;

            // if the current cell is the last cell ie bottom-rightmost cell, we don't do anything and continue.
            if(currValue == totalCells - 1) {
                continue;
            }

            if(currRow == rows - 1) {
                //If the current cell is on the last row we can only break the right wall and can't break the bottom wall.
                // hence the adjacent cell is chosen to be the next right cell.
                root2 = ds.find(currValue + 1);
            } else if(currCol == columns - 1) {
                //If the current cell is on the last column we can only break the bottom wall and can't break the right wall.
                // hence the adjacent cell is chosen to be the next bottom cell.
                root2 = ds.find(currValue + columns);
                removeDownWall = true;
            } else {
                //If the current cell is neither last row nor last column.
                //We randomly select either next right wall or next bottom wall to break.
                boolean selectRight;
                selectRight = rand.nextBoolean();

                if(selectRight) {
                    root2 = ds.find(currValue + 1);
                } else {
                    root2 = ds.find(currValue + columns);
                    removeDownWall = true;
                }
            }
            // Compare the roots of the current and chosen adjacent cell.
            if(root1 != root2) {
                ds.union(root1, root2); //If roots are different, we take union of the two sets.
                // Based on the removeDownWall flag set while choosing the adjacent cell,
                // we break the next right wall or next bottom wall.
                if(removeDownWall) {
                    currCell.down = false;
                } else {
                    currCell.right = false;
                }
            }
        }
        //We Leave the walls of the last cell ie bottom right corner to be open.
        Cell lastCell = maze[rows - 1][columns - 1];
        lastCell.down = false;
        lastCell.right = false;
        printMaze();
    }

    /**
     * Private helper method to print the 2D maze array.
     */
    private void printMaze() {
        System.out.print("   "); //Always leave the top wall of the first cell ie top left corner open.
        for(int j=1; j<columns; j++) {
            System.out.print(" __"); //Print the topmost line
        }
        System.out.println();
        for(int i=0; i<rows; i++) {
            if(i == 0) {
                System.out.print(" "); //Always leave the right wall of the first cell ie top left corner open.
            } else {
                System.out.print("|"); //Print the right wall line
            }
            for(int j=0; j<columns; j++) {
                if(maze[i][j].down) {
                    System.out.print("__"); //print the bottom maze wall that is not broken.
                } else {
                    System.out.print("  "); //print the bottom maze wall that is broken.
                }
                if(maze[i][j].right) {
                    System.out.print("|"); //print the right maze wall that is not broken.
                } else {
                    System.out.print(" "); //print the right maze wall that is broken.
                }
            }
            System.out.println();
        }
    }

    /**
     * Private class to store a particular cell of the maze. Contains the following variables :
     * value - data value of each cell in the maze.
     * down - boolean flag value to represent horizontal down wall with respect to a cell. a true value signifies that the
     * wall below a cell is not broken and false value signifies it is broken.
     * right - boolean flag value to represent vertical right wall with respect to a cell. a true value signifies that the
     * * wall right next a cell is not broken and false value signifies it is broken.
     */
    private static class Cell {
        int value;
        boolean down;
        boolean right;

       Cell(int value, boolean right, boolean down) {
           this.value = value;
           this.right = right;
           this.down = down;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of rows: ");
        int rows = sc.nextInt();
        System.out.println("Enter the number of columns: ");
        int columns = sc.nextInt();
        Maze maze = new Maze(rows, columns);
        System.out.println("Solved Maze: ");
        maze.solveMaze();
    }
}
