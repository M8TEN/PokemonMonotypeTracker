package Battle;

public class StatusCondition implements CombatElement {
    private int type;

    public int getType() {
        return type;
    }

    public StatusCondition() {}

    public StatusCondition(int type) {
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Battle.StatusCondition{" +
                "type=" + type +
                '}';
    }

    @Override
    public void effect() {
    }

    public boolean equals(CombatElement combatElement){
        if (!(combatElement instanceof StatusCondition)) {
            return false;
        }
        return this.type == ((StatusCondition)combatElement).getType();
    }
}
