package swe2.co.sweprojectsht;

import jdk.dynalink.beans.StaticClass;

public class GameEngine {
    //Has to be static since were constantly creating this Class after every cell is created
    static private String currentTurn = "WHITE";
    static private int currentTurnNumber = 1;
    private static boolean pieRuleActive = false;

    public static void switchTurn() {
        setCurrentTurn(currentTurn.equals("BLACK") ? "WHITE" : "BLACK");
    }

    public static void activatePieRule() {
        GameEngine.pieRuleActive = true;
    }

    public static void setCurrentTurn(String currentTurn) {
        GameEngine.currentTurn = currentTurn;
    }

    public String getCurrentTurn() {
         return currentTurn;
    }

    public static int getCurrentTurnNumber() {
        return currentTurnNumber;
    }
    public static void incrementTurn() {
        currentTurnNumber++;
    }
}
