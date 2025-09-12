public class Round {
    private BasketballPlayer user;
    private BasketballPlayer opponent;
    private BasketballPlayer winner;

    public Round(BasketballPlayer user, BasketballPlayer opponent) {
        this.user = user;
        this.opponent = opponent;
    }

    public void play() {
        System.out.println("\n1:1 대결 시작!");
        System.out.println("사용자: " + user.getName());
        System.out.println("상대 : " + opponent.getName());

        Ball ball = new Ball();
        boolean userFirst = Math.random() < 0.5;    //  선공 랜덤
        ball.setInitialHolder(userFirst ? user : opponent);

        PlayerThread userThread = new PlayerThread(user, ball);
        PlayerThread opponentThread = new PlayerThread(opponent, ball);

        System.out.println("============ 경기 시작 ============\n");
        userThread.start();
        opponentThread.start();

        try {
            userThread.join();
            opponentThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        int userScore = userThread.getScore();
        int opponentScore = opponentThread.getScore();

        System.out.printf("\n결과: %s [%d] vs %s [%d]\n",
                user.getName(), userScore, opponent.getName(), opponentScore);

        if (userScore > opponentScore) {
            winner = user;
            user.recordWin();
            user.addConsecutiveWin();
            opponent.recordLoss();
            opponent.resetConsecutiveWins();
            System.out.println("승자: " + user.getName());

            // 연승 부상 처리
            if (user.getConsecutiveWins() >= 2) {
                user.injure();
                System.out.println(user.getName() + " 체력 저하로 인한 부상!");
                System.out.println("부상으로 인해 슛 성공률이 20% 하락합니다.");
            }

        } else if (userScore < opponentScore) {
            winner = opponent;
            opponent.recordWin();
            user.recordLoss();
            user.resetConsecutiveWins();
            System.out.println("패배! " + user.getName() + "이 졌습니다.");

        } else {
            winner = null;
            System.out.println("무승부!");
            user.resetConsecutiveWins();
            opponent.resetConsecutiveWins();
        }

        // 경기 종료 후 자동 회복
        user.recoverIfInjured();
        opponent.recoverIfInjured();
    }

    public BasketballPlayer getWinner() {
        return winner;
    }
}
