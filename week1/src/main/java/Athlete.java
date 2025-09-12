public class Athlete extends Person {
    private final int number;
    private boolean isInjured;

    public Athlete(String name, double height, double weight, int number) {
        super(name, height, weight);
        this.number = number;
        this.isInjured = false;
    }

    public void injure() {
        this.isInjured = true;
    }

    public void recover() {
        this.isInjured = false;
    }

    public int getNumber() {
        return number;
    }

    public boolean isInjured() {
        return isInjured;
    }
}
