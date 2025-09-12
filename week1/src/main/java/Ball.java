public class Ball {
    private volatile boolean gameOver = false;
    private BasketballPlayer currentHolder;
    private final Object lock = new Object();

    public void setInitialHolder(BasketballPlayer first) {
        this.currentHolder = first;
    }

    public void twoPoint(BasketballPlayer shooter, PlayerThread thread) {
        synchronized (lock) {
            if (gameOver) { //  경기가 종료되었음에도 lock 획득으로 인한 슛 시도를 방지
                return;
            }

            while (shooter != currentHolder) {  //  공격권이 없다 == lock 획득 못함  -> 해제 기다림
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (gameOver) { //  기다리다가 경기 종료되면 슛 시도 없이 종료
                    return;
                }
            }

            boolean success = shooter.attemptTwoPoint();    //  2점 슛 시도
            if (success) {
                thread.addScore(2);
                System.out.printf("%s 2점슛 -> 성공! [현재 %d점]\n", shooter.getName(), thread.getScore());

                if (thread.getScore() >= PlayerThread.TARGET_SCORE && !gameOver) {  //  playerThread의 득점이 타겟 스코어에 도달하면
                    setGameOver();  //  경기 종료
                }
            } else {
                System.out.printf("%s 2점슛 -> 실패\n", shooter.getName());
                switchPossession();
            }

            lock.notifyAll();   //  슛 시도 후, 대기 중인 스레드에게 lock 해제를 알림
        }
    }

    public void threePoint(BasketballPlayer shooter, PlayerThread thread) {
        synchronized (lock) {
            if (gameOver) {
                return;
            }

            while (shooter != currentHolder) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (gameOver) {
                    return;
                }
            }


            boolean success = shooter.attemptThreePoint();
            if (success) {
                thread.addScore(3);
                System.out.printf("%s 3점슛 -> 성공! [현재 %d점]\n", shooter.getName(), thread.getScore());

                if (thread.getScore() >= PlayerThread.TARGET_SCORE && !gameOver) {
                    setGameOver();
                }
            } else {
                System.out.printf("%s 3점슛 -> 실패\n", shooter.getName());
                switchPossession();
            }

            lock.notifyAll();
        }
    }

    private void switchPossession() {   //  공격권 전환
        currentHolder = (currentHolder == GameManager.getUser())
                ? GameManager.getOpponent()
                : GameManager.getUser();
    }

    public void setGameOver() { //   경기 종료 상태로 설정
        synchronized (lock) {
            this.gameOver = true;
            lock.notifyAll();   //  lock 해제를 모든 스레드에 알림
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
