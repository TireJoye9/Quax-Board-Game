package swe2.co.sweprojectsht;

import java.util.Stack;

public class GameEngine {
    private Board board;
    private Player currentPlayer;
    private boolean firstMoveMade = false;
    private boolean swapOffered = false;
    private boolean gameOver;

    public GameEngine(Board board) {
        this.board = board;
        this.currentPlayer = Player.BLACK;
        this.gameOver = false;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean swapOffer() {
        return firstMoveMade && !swapOffered;
    }

    public void markSwapOffered() {
        swapOffered = true;
    }

    public void performSwap() {

        swapOffered = true;

        if (currentPlayer == Player.BLACK)
            currentPlayer = Player.WHITE;
        else
            currentPlayer = Player.BLACK;
    }

    public boolean placePiece(int r, int c) {
        OctagonalCell cell = board.getOctagon(r, c);

        if(cell.getOwner() != Player.NONE) {
            return false;
        }

        cell.setOwner(currentPlayer);

        if(checkWin(currentPlayer)) {
            gameOver = true;
        }

        if (!firstMoveMade) {
            firstMoveMade = true;
        }

        switchPlayer();

        return true;
    }

    public boolean placeBridge(int r, int c) {

        RhombicCell diamond = board.getDiamond(r, c);

        if (diamond.getOwner() != Player.NONE) {
            return false;
        }

        OctagonalCell topLeft = board.getOctagon(r, c);
        OctagonalCell topRight = board.getOctagon(r, c + 1);
        OctagonalCell bottomLeft = board.getOctagon(r + 1, c);
        OctagonalCell bottomRight = board.getOctagon(r + 1, c + 1);

        boolean diag1 = topLeft.getOwner() == currentPlayer && bottomRight.getOwner() == currentPlayer;

        boolean diag2 = topRight.getOwner() == currentPlayer && bottomLeft.getOwner() == currentPlayer;

        if (!diag1 && !diag2) {
            return false;
        }

        diamond.setOwner(currentPlayer);

        switchPlayer();

        return true;
    }

    private void switchPlayer() {

        if (currentPlayer == Player.BLACK) {
            currentPlayer = Player.WHITE;
        } else {
            currentPlayer = Player.BLACK;
        }
    }

    private boolean checkWin(Player player) {

        int size = Board.SIZE;

        boolean[][] visited = new boolean[size][size];
        Stack<int[]> stack = new Stack<>();

        if (player == Player.BLACK) {

            for (int c = 0; c < size; c++) {
                if (board.getOctagon(0, c).getOwner() == player) {
                    stack.push(new int[]{0, c});
                }
            }

            while (!stack.isEmpty()) {

                int[] pos = stack.pop();
                int r = pos[0];
                int c = pos[1];

                if (visited[r][c])
                    continue;

                visited[r][c] = true;

                if (r == size - 1)
                    return true;

                exploreNeighbours(player, stack, r, c);
            }
        }

        if (player == Player.WHITE) {

            for (int r = 0; r < size; r++) {
                if (board.getOctagon(r, 0).getOwner() == player) {
                    stack.push(new int[]{r, 0});
                }
            }

            while (!stack.isEmpty()) {

                int[] pos = stack.pop();
                int r = pos[0];
                int c = pos[1];

                if (visited[r][c])
                    continue;

                visited[r][c] = true;

                if (c == size - 1)
                    return true;

                exploreNeighbours(player, stack, r, c);
            }
        }

        return false;
    }

    private void exploreNeighbours(Player player, Stack<int[]> stack, int r, int c) {

        int[][] dirs = {
                {-1,0},
                {1,0},
                {0,-1},
                {0,1}
        };

        for (int[] d : dirs) {

            int nr = r + d[0];
            int nc = c + d[1];

            if (nr >= 0 && nr < Board.SIZE && nc >= 0 && nc < Board.SIZE) {

                if (board.getOctagon(nr, nc).getOwner() == player) {

                    stack.push(new int[]{nr, nc});
                }
            }
        }
    }
}
