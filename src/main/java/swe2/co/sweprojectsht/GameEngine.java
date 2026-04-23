package swe2.co.sweprojectsht;

import java.util.Stack;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.application.Platform;
import java.util.Optional;

public class GameEngine {
    private Board board;
    private Player humanPlayer;
    private Player botPlayer;
    private QuaxGUI quaxGUI;
    private boolean firstMoveMade = false;
    private boolean swapOffered = false;
    private int firstMoveRow = -1;
    private int firstMoveCol = -1;
    private boolean gameOver;

    public GameEngine(Board board, QuaxGUI quaxGUI) {
        this.board = board;
        this.humanPlayer = Player.BLACK;
        this.botPlayer = Player.WHITE;
        this.quaxGUI = quaxGUI;
        this.gameOver = false;
    }

    public QuaxGUI getQuaxGUI() {
        return quaxGUI;
    }

    public Player getHumanPlayer() {
        return humanPlayer;
    }

    public Player getBotPlayer() {
        return botPlayer;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean swapOffer() {
        return firstMoveMade && !swapOffered;
    }

    public void markSwapOffered() {
        swapOffered = true;
    }

    public void performSwap() {
        swapOffered = true;

        // Swap the player assignments
        if (humanPlayer == Player.BLACK) {
            humanPlayer = Player.WHITE;
            botPlayer = Player.BLACK;
        } else {
            humanPlayer = Player.BLACK;
            botPlayer = Player.WHITE;
        }

        // Get the first move cell (human's first move)
        OctagonalCell firstCell = board.getOctagon(firstMoveRow, firstMoveCol);

        // Transfer ownership of the first move to the other player
        if (firstCell.getOwner() == Player.BLACK) {
            firstCell.setOwner(Player.WHITE);
        } else {
            firstCell.setOwner(Player.BLACK);
        }


        // Find the bot's piece (the only other piece on board besides first move)
        //And swap it
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                OctagonalCell cell = board.getOctagon(r, c);
                if (cell.getOwner() != Player.NONE && (r != firstMoveRow || c != firstMoveCol)) {
                    // This is the bot's piece - transfer it to human's new color
                    if (cell.getOwner() == Player.BLACK) {
                        cell.setOwner(Player.WHITE);
                    } else {
                        cell.setOwner(Player.BLACK);
                    }
                }
            }
        }
    }

    public boolean placePiece(int r, int c) {
        if (gameOver) {
            return false;
        }

        OctagonalCell cell = board.getOctagon(r, c);

        if (cell.getOwner() != Player.NONE) {
            return false;
        }

        cell.setOwner(humanPlayer);

        if (checkWin(humanPlayer)) {
            gameOver = true;
            showWinnerPopup(humanPlayer);
            return true;
        }
        else if (checkWin(botPlayer)) {
            gameOver = true;
            showWinnerPopup(botPlayer);
            return true;
        }

        if (!firstMoveMade) {
            firstMoveMade = true;
            firstMoveRow = r;
            firstMoveCol = c;
        }

        return true;
    }

    public boolean botPlacePiece(int r, int c) {
        if (gameOver) {
            return false;
        }

        OctagonalCell cell = board.getOctagon(r, c);

        if (cell.getOwner() != Player.NONE) {
            return false;
        }

        cell.setOwner(botPlayer);

        if (checkWin(botPlayer)) {
            gameOver = true;
            showWinnerPopup(botPlayer);
            return true;
        }
        else if (checkWin(humanPlayer)) {
            gameOver = true;
            showWinnerPopup(humanPlayer);
            return true;
        }

        if (!firstMoveMade) {
            firstMoveMade = true;
            firstMoveRow = r;
            firstMoveCol = c;
        }

        return true;
    }

    public boolean botPlaceBridge(int r, int c) {
        if (gameOver) {
            return false;
        }

        RhombicCell diamond = board.getDiamond(r, c);

        if (diamond.getOwner() != Player.NONE) {
            return false;
        }

        diamond.setOwner(botPlayer);

        if (checkWin(botPlayer)) {
            gameOver = true;
            showWinnerPopup(botPlayer);
            return true;
        }
        else if (checkWin(humanPlayer)) {
            gameOver = true;
            showWinnerPopup(humanPlayer);
            return true;
        }

        return true;
    }

    public boolean placeBridge(int r, int c) {
        if (gameOver) {
            return false;
        }

        RhombicCell diamond = board.getDiamond(r, c);

        if (diamond.getOwner() != Player.NONE) {
            return false;
        }

        diamond.setOwner(humanPlayer);

        if (checkWin(humanPlayer)) {
            gameOver = true;
            showWinnerPopup(humanPlayer);
            return true;
        }

        return true;
    }

    protected boolean checkWin(Player player) {
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

                if (visited[r][c]) {
                    continue;
                }

                visited[r][c] = true;

                if (r == size - 1) {
                    return true;
                }

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

                if (visited[r][c]) {
                    continue;
                }

                visited[r][c] = true;

                if (c == size - 1) {
                    return true;
                }

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

        // check bridge connections
        if (r < Board.SIZE - 1 && c < Board.SIZE - 1) {
            RhombicCell bridge = board.getDiamond(r, c);

            if (bridge.getOwner() == player &&
                    board.getOctagon(r + 1, c + 1).getOwner() == player) {
                stack.push(new int[]{r + 1, c + 1});
            }
        }

        if (r < Board.SIZE - 1 && c > 0) {
            RhombicCell bridge = board.getDiamond(r, c - 1);

            if (bridge.getOwner() == player &&
                    board.getOctagon(r + 1, c - 1).getOwner() == player) {
                stack.push(new int[]{r + 1, c - 1});
            }
        }
    }

    private void showWinnerPopup(Player winner) {
        String winnerText;
        if (winner == humanPlayer) {
            winnerText = "HUMAN wins!";
        } else if (winner == botPlayer) {
            winnerText = "BOT wins!";
        } else {
            winnerText = winner.toString() + " wins!";
        }

        // Use Platform.runLater to show dialog safely
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(winnerText);
            alert.setContentText("Play again?");

            ButtonType playAgain = new ButtonType("Play Again");
            ButtonType exit = new ButtonType("Exit");

            alert.getButtonTypes().setAll(playAgain, exit);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == playAgain) {
                quaxGUI.resetGame();
            } else {
                Platform.exit();
            }
        });
    }
}