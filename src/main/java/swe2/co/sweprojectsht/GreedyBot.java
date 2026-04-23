package swe2.co.sweprojectsht;

import java.util.*;

/*
 GreedyBot — builds the shortest chain from its starting wall to its goal wall.

  BLACK: connects TOP row (row 0) → BOTTOM row (row SIZE-1), moving downward.
  WHITE: connects LEFT col (col 0) → RIGHT col (col SIZE-1), moving rightward.

  Each "node" in the search is an octagon (r, c).
  Cost to move between two octagons:
    0  if both are already owned by the bot (already connected — free).
    1  if the target octagon is empty (place a piece here).
    ∞  if the target octagon is owned by the opponent (blocked).

  A diamond (bridge) between two diagonal octagons costs 1 extra if it is empty,
  or 0 if the bot already owns it.

  Dijkstra finds the minimum-cost path. The bot then plays the first empty cell
  on that path — either an octagon or a diamond.

  If no path exists (fully blocked), the bot falls back to blocking the human's
  most advanced piece, then to any empty octagon.
 */
public class GreedyBot {

    private static final int INF = Integer.MAX_VALUE / 2;

    // move[2] type flags returned to QuaxGUI
    private static final int OCTAGON = 0;
    private static final int DIAMOND = 1;
    private Board board;
    private final GameEngine engine;

    public GreedyBot(Board board, GameEngine engine) {
        this.board = board;
        this.engine = engine;
    }

    /*Called by QuaxGUI after every move so the bot always sees the latest board. */
    public void updateBoard(Board board) {
        this.board = board;
    }

    /*
     * Returns the best move as int[]{row, col, type}
     * where type=0 means octagon, type=1 means diamond (bridge).
     * Returns null only if the board is completely full (should never happen).
     */
    public int[] getBestMove() {
        Player bot = engine.getBotPlayer();

        // 1 Try to find the shortest path to the goal and play the next step.
        int[] pathMove = findShortestPathMove(bot);
        if (pathMove != null) {
            return pathMove;
        }

        // 2  Fully blocked — try to block the human's best octagon.
        int[] blockMove = findBlockingMove();
        if (blockMove != null) {
            return blockMove;
        }

        // 3 Last resort: any empty octagon.
        return findAnyEmptyOctagon(bot);
    }

    /**
     * Runs Dijkstra on the octagon grid (with bridge shortcuts) and returns
     * the first empty cell on the cheapest path to the goal wall.
     */
    private int[] findShortestPathMove(Player bot) {
        int size = Board.SIZE;

        // dist[r][c] = cheapest cost to reach octagon (r,c)
        int[][] dist = new int[size][size];
        // prev[r][c] = predecessor octagon on the cheapest path
        int[][][] prev = new int[size][size][2];
        // bridgeUsed[r][c] = {dr, dc} of the diamond used to reach (r,c), or null
        int[][][][] bridgeUsed = new int[size][size][][];

        for (int[] row : dist) Arrays.fill(row, INF);
        for (int[][] row : prev) for (int[] cell : row) Arrays.fill(cell, -1);

        // Priority queue: {cost, row, col}
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

        // Seed from the starting wall
        if (bot == Player.BLACK) {
            // BLACK starts at top row
            for (int c = 0; c < size; c++) {
                int cost = costToEnterOctagon(0, c, bot);
                if (cost < INF) {
                    dist[0][c] = cost;
                    pq.offer(new int[]{cost, 0, c});
                }
            }
        } else {
            // WHITE starts at left column
            for (int r = 0; r < size; r++) {
                int cost = costToEnterOctagon(r, 0, bot);
                if (cost < INF) {
                    dist[r][0] = cost;
                    pq.offer(new int[]{cost, r, 0});
                }
            }
        }

        // Dijkstra main loop
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int curCost = cur[0], r = cur[1], c = cur[2];

            if (curCost > dist[r][c]) continue; // stale entry

            // Expand orthogonal neighbours (direct octagon adjacency)
            int[][] orthoDirs = {{-1,0},{1,0},{0,-1},{0,1}};
            for (int[] d : orthoDirs) {
                int nr = r + d[0], nc = c + d[1];
                if (!inBounds(nr, nc)) continue;

                int edgeCost = costToEnterOctagon(nr, nc, bot);
                if (edgeCost == INF) continue;

                int newCost = dist[r][c] + edgeCost;
                if (newCost < dist[nr][nc]) {
                    dist[nr][nc] = newCost;
                    prev[nr][nc] = new int[]{r, c};
                    bridgeUsed[nr][nc] = null;
                    pq.offer(new int[]{newCost, nr, nc});
                }
            }

            // Expand diagonal neighbours via bridge (diamond)
            // Possible bridge directions from (r,c): down-right, down-left, up-right, up-left
            int[][] diagDirs = {{1,1},{1,-1},{-1,1},{-1,-1}};
            for (int[] d : diagDirs) {
                int nr = r + d[0], nc = c + d[1];
                if (!inBounds(nr, nc)) continue;

                // The diamond that bridges (r,c) and (nr,nc):
                // diamond(min(r,nr), min(c,nc))
                int dr = Math.min(r, nr);
                int dc = Math.min(c, nc);
                if (dr >= size - 1 || dc >= size - 1) continue;

                RhombicCell diamond = board.getDiamond(dr, dc);
                int bridgeCost = costToEnterDiamond(diamond, bot);
                if (bridgeCost == INF) continue;

                int octCost = costToEnterOctagon(nr, nc, bot);
                if (octCost == INF) continue;

                int newCost = dist[r][c] + bridgeCost + octCost;
                if (newCost < dist[nr][nc]) {
                    dist[nr][nc] = newCost;
                    prev[nr][nc] = new int[]{r, c};
                    bridgeUsed[nr][nc] = new int[][]{{dr, dc}};
                    pq.offer(new int[]{newCost, nr, nc});
                }
            }
        }

        // Find the goal cell with the lowest cost
        int bestCost = INF;
        int[] goalCell = null;

        if (bot == Player.BLACK) {
            // Goal: bottom row
            for (int c = 0; c < size; c++) {
                if (dist[size - 1][c] < bestCost) {
                    bestCost = dist[size - 1][c];
                    goalCell = new int[]{size - 1, c};
                }
            }
        } else {
            // Goal: right column
            for (int r = 0; r < size; r++) {
                if (dist[r][size - 1] < bestCost) {
                    bestCost = dist[r][size - 1];
                    goalCell = new int[]{r, size - 1};
                }
            }
        }

        if (goalCell == null || bestCost == 0) {
            // Already won or no path exists
            return null;
        }

        // Reconstruct path from goal back to start, find the first empty move
        return reconstructFirstMove(goalCell, prev, bridgeUsed, bot);
    }

    /*
     * Walks the predecessor chain from the goal back to the start wall,
     * collecting the full path. Then scans forward from the start to find
     * the first cell that is empty (needs to be placed).
     */
    private int[] reconstructFirstMove(int[] goal, int[][][] prev,
                                       int[][][][] bridgeUsed, Player bot) {
        // Rebuild the path as a list of steps from start to goal
        List<int[]> path = new ArrayList<>(); // each entry: {r, c} octagon
        List<int[][]> bridges = new ArrayList<>(); // bridge used to reach path[i], or null

        int[] cur = goal;
        while (cur != null) {
            int r = cur[0], c = cur[1];
            path.add(0, new int[]{r, c});
            bridges.add(0, bridgeUsed[r][c]);
            int[] p = prev[r][c];
            cur = (p[0] == -1) ? null : p;
        }

        // Walk forward along the path; return the first empty octagon or bridge
        for (int i = 0; i < path.size(); i++) {
            int r = path.get(i)[0], c = path.get(i)[1];

            // Check if the bridge used to reach this octagon needs placing first
            if (bridges.get(i) != null) {
                int dr = bridges.get(i)[0][0];
                int dc = bridges.get(i)[0][1];
                if (board.getDiamond(dr, dc).getOwner() == Player.NONE) {
                    return new int[]{dr, dc, DIAMOND};
                }
            }

            // Check if this octagon itself needs placing
            if (board.getOctagon(r, c).getOwner() == Player.NONE) {
                return new int[]{r, c, OCTAGON};
            }
        }

        return null; // entire path already owned by bot
    }

    /*
     * Blocks the human's most advanced empty octagon (closest to their goal).
     */
    private int[] findBlockingMove() {
        Player human = engine.getHumanPlayer();

        int bestScore = -1;
        int[] bestMove = null;

        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                if (board.getOctagon(r, c).getOwner() != Player.NONE) continue;

                // Score = how close this empty cell is to the human's goal
                int score;
                if (human == Player.BLACK) {
                    score = r; // higher row = closer to bottom goal
                } else {
                    score = c; // higher col = closer to right goal
                }

                // Only consider blocking if the human has a neighbour here
                if (hasAdjacentOwner(r, c, human) && score > bestScore) {
                    bestScore = score;
                    bestMove = new int[]{r, c, OCTAGON};
                }
            }
        }

        return bestMove;
    }

    private boolean hasAdjacentOwner(int r, int c, Player player) {
        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (inBounds(nr, nc) && board.getOctagon(nr, nc).getOwner() == player) {
                return true;
            }
        }
        return false;
    }

    private int[] findAnyEmptyOctagon(Player bot) {
        // Prefer cells closer to the goal wall
        if (bot == Player.BLACK) {
            for (int r = Board.SIZE - 1; r >= 0; r--) {
                for (int c = 0; c < Board.SIZE; c++) {
                    if (board.getOctagon(r, c).getOwner() == Player.NONE) {
                        return new int[]{r, c, OCTAGON};
                    }
                }
            }
        } else {
            for (int c = Board.SIZE - 1; c >= 0; c--) {
                for (int r = 0; r < Board.SIZE; r++) {
                    if (board.getOctagon(r, c).getOwner() == Player.NONE) {
                        return new int[]{r, c, OCTAGON};
                    }
                }
            }
        }
        return null;
    }


    /*
     * Cost for the bot to "enter" (occupy) an octagon:
     0  — already owned by the bot
       1  — empty
      INF — owned by the opponent
     */
    private int costToEnterOctagon(int r, int c, Player bot) {
        Player owner = board.getOctagon(r, c).getOwner();
        if (owner == bot)       return 0;
        if (owner == Player.NONE) return 1;
        return INF; // opponent owns it
    }

    /*
      Cost for the bot to "enter" (own) a diamond bridge:
        0  — already owned by the bot.
        1  — empty.
        INF — owned by the opponent.
     */
    private int costToEnterDiamond(RhombicCell diamond, Player bot) {
        Player owner = diamond.getOwner();
        if (owner == bot)         return 0;
        if (owner == Player.NONE) return 1;
        return INF;
    }

    private boolean inBounds(int r, int c) {
        return r >= 0 && r < Board.SIZE && c >= 0 && c < Board.SIZE;
    }
}