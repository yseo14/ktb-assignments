public class PlayerThread extends Thread {
    private final BasketballPlayer player;
    private final Ball ball;
    private int score = 0;
    public final static int TARGET_SCORE = 11;

    public PlayerThread(BasketballPlayer player, Ball ball) {
        this.player = player;
        this.ball = ball;
    }

    @Override
    public void run() {
        while (!ball.isGameOver()) {    //  경기 종료까지 슛 시도 반복
            boolean isTwoPoint = Math.random() < 0.5;   //  2점, 3점 슛 랜덤 선택
            if (isTwoPoint) {
                ball.twoPoint(player, this);
            } else {
                ball.threePoint(player, this);
            }

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public int getScore() {
        return score;
    }

    public void addScore(int s) {
        this.score += s;
    }
}
