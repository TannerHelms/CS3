/*
Very simple object to contain steps and id. The board does the heavy lifting.
 */

public class State {
    private String steps;
    private String id;

    State(String id, String steps) {
        this.steps = steps;
        this.id = id;
    }

    /**
     * @return last move made
     */
    public char getLast(){
        if (steps.equals("")) return '*';
        int last = steps.length();
        return steps.charAt(last-1);
    }
    public String getId() {
        return id;
    }

    public String getSteps() {
        return steps;
    }

    public int getNumSteps() {
        return steps.length();
    }
    public String toString(){
        return "State " + id + " steps: " + steps;

    }
}