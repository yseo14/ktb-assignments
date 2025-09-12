import java.util.Random;

public class BasketballPlayer extends Athlete {
    private final double baseTwoFgp;
    private final double baseThreeFgp;
    private double currentTwoFgp;
    private double currentThreeFgp;

    private int wins = 0;
    private int losses = 0;
    private int gamePlayed = 0;
    private int consecutiveWins = 0;

    private final Team team;

    private static final Random random = new Random();

    public BasketballPlayer(String name, double height, double weight, int number,
                            double twoFgp, double threeFgp, Team team) {
        super(name, height, weight, number);
        this.baseTwoFgp = twoFgp;
        this.baseThreeFgp = threeFgp;
        this.currentTwoFgp = twoFgp;
        this.currentThreeFgp = threeFgp;
        this.team = team;
    }

    public boolean attemptTwoPoint() {
        return random.nextDouble() < currentTwoFgp;
    }

    public boolean attemptThreePoint() {
        return random.nextDouble() < currentThreeFgp;
    }

    public void resetConsecutiveWins() {
        this.consecutiveWins = 0;
    }

    public void addConsecutiveWin() {
        this.consecutiveWins++;
        if (this.consecutiveWins >= 2) {
            this.injure();
        }
    }

    @Override
    public void injure() {
        super.injure();
        this.currentTwoFgp *= 0.8;   // 능력치 20% 하락
        this.currentThreeFgp *= 0.8;
    }

    public void recoverIfInjured() {
        if (this.isInjured()) {
            this.recover();
            this.currentTwoFgp = baseTwoFgp;
            this.currentThreeFgp = baseThreeFgp;
        }
    }

    public void recordWin() {
        this.gamePlayed++;
        this.wins++;
    }

    public void recordLoss() {
        this.gamePlayed++;
        this.losses++;
    }

    public double getTwoFgp() {
        return currentTwoFgp;
    }

    public double getThreeFgp() {
        return currentThreeFgp;
    }

    public int getWins() {
        return wins;
    }

    public int getConsecutiveWins() {
        return consecutiveWins;
    }

    public int getGamePlayed() {
        return gamePlayed;
    }

    public double getWinRate() {
        return (gamePlayed == 0) ? 0.0 : (double) wins / gamePlayed;
    }

}
