package swe2.co.sweprojectsht;

public class GameEngine {
    private String currentTurn = "BLACK";
    private boolean pieRuleActive = false;

    public void switchTurn() {
        currentTurn = currentTurn.equals("BLACK") ? "WHITE" : "BLACK";
    }

    public void activatePieRule() {
        this.pieRuleActive = true;
    }
}
