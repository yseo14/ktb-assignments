import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class GameManager {
    private static BasketballPlayer user;
    private static BasketballPlayer opponent;

    public static void startGame() {
        // 기본 정보 초기화
        List<Team> teams = initTeams();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Random random = new Random();

        while (true) {
            System.out.println("\n==================== Welcome to NBA ====================\n");
            printTeamAndPlayers(teams);

            BasketballPlayer[] allPlayers = teams.stream()
                    .flatMap(t -> t.getPlayerList().stream())
                    .toArray(BasketballPlayer[]::new);

            // 팀 선택
            Team userTeam = selectTeam(br, teams);

            // 선수 선택
            List<BasketballPlayer> selectable = userTeam.getPlayerList().stream()
                    .collect(Collectors.toList());

            if (selectable.isEmpty()) {
                System.out.println("선택 가능한 선수가 없습니다. 다시 시작합니다.");
                continue;
            }

            System.out.println("\n 출전할 선수를 선택하세요:");
            for (int i = 0; i < selectable.size(); i++) {
                BasketballPlayer p = selectable.get(i);
                System.out.printf(
                        "  [%d] #%d %s (2FG%%: %.2f, 3FG%%: %.2f, 연승: %d, 경기수: %d, 승: %d, 승률: %.2f, 부상: %s)\n",
                        i, p.getNumber(), p.getName(), p.getTwoFgp(), p.getThreeFgp(),
                        p.getConsecutiveWins(), p.getGamePlayed(), p.getWins(), p.getWinRate(),
                        p.isInjured() ? "Yes" : "No");
            }

            while (true) {
                System.out.print(" 입력 >>> ");
                try {
                    int selected = Integer.parseInt(br.readLine());
                    if (selected >= 0 && selected < selectable.size()) {
                        user = selectable.get(selected);
                        break;
                    }
                } catch (Exception ignored) {
                }
                System.out.println("잘못된 번호입니다. 다시 입력하세요.");
            }

            System.out.printf(" 출전 선수: %s\n", user.getName());

            // 상대 선택 및 경기 진행
            List<BasketballPlayer> opponents = Arrays.stream(allPlayers)
                    .filter(p -> p != user && !p.isInjured())
                    .collect(Collectors.toList());

            boolean userLost = false;
            while (!userLost && !opponents.isEmpty()) {
                opponent = opponents.remove(random.nextInt(opponents.size()));
                Round round = new Round(user, opponent);
                round.play();

                if (round.getWinner() != user) {
                    userLost = true;
                    break;
                }

                if (!opponents.isEmpty()) {
                    System.out.print("\n✔️ 다음 상대와 계속 대결합니다. 계속하려면 Enter를 누르세요...");
                    try {
                        br.readLine();
                    } catch (Exception ignored) {
                    }
                }
            }

            // 결과 출력 및 선택지
            if (userLost) {
                System.out.println("\n사용자가 패배했습니다.");
                System.out.print("게임을 다시 시작하려면 y, 종료하려면 아무 키 > ");
                try {
                    String again = br.readLine().trim();
                    if (!again.equalsIgnoreCase("y")) {
                        System.out.println("게임 종료!");
                        break;
                    }
                } catch (Exception ignored) {
                    break;
                }
            } else {
                System.out.println("\n모든 상대를 이겼습니다!");
                System.out.print("게임을 계속하려면 y, 종료하려면 아무 키 > ");
                try {
                    String again = br.readLine().trim();
                    if (!again.equalsIgnoreCase("y")) {
                        System.out.println("게임 종료!");
                        break;
                    }
                } catch (Exception ignored) {
                    break;
                }
            }

            // 부상 회복
            user.recoverIfInjured();
        }
    }

    private static Team selectTeam(BufferedReader br, List<Team> teams) {
        System.out.println("\n 플레이할 팀을 선택하세요:");
        for (int i = 0; i < teams.size(); i++) {
            System.out.printf("  [%d] %s\n", i, teams.get(i).getName());
        }

        while (true) {
            try {
                System.out.print(" 입력 >>> ");
                int selected = Integer.parseInt(br.readLine());
                if (selected >= 0 && selected < teams.size()) {
                    return teams.get(selected);
                }
            } catch (Exception ignored) {
            }
            System.out.println(" 잘못된 번호입니다. 다시 입력하세요.");
        }
    }

    private static void printTeamAndPlayers(List<Team> teams) {
        System.out.println("========== 🏀 전체 팀 및 선수 목록 🏀 ==========\n");
        for (Team team : teams) {
            System.out.println("팀: " + team.getName());
            System.out.println("---------------------------------------------------------------");
            System.out.printf("%-3s %-20s %-6s %-6s %-8s %-8s %-6s %-4s %-6s\n",
                    "No", "이름", "키", "몸무게", "2FG%", "3FG%", "경기수", "승", "승률");
            for (BasketballPlayer p : team.getPlayerList()) {
                System.out.printf("#%-3d %-20s %-6.1f %-6.1f %-8.2f %-8.2f %-6d %-4d %-6.2f\n",
                        p.getNumber(), p.getName(), p.getHeight(), p.getWeight(),
                        p.getTwoFgp(), p.getThreeFgp(),
                        p.getGamePlayed(), p.getWins(), p.getWinRate());
            }
            System.out.println();
        }
        System.out.println("================================================================\n");
    }


    private static List<Team> initTeams() {
        List<Team> teams = new ArrayList<>();

        List<BasketballPlayer> gswPlayers = new ArrayList<>();
        Team goldenState = new Team("gsw", gswPlayers);
        gswPlayers.add(new BasketballPlayer("Stephen Curry", 188.0, 84.0, 30, 0.52, 0.43, goldenState));
        gswPlayers.add(new BasketballPlayer("Klay Thompson", 198.0, 98.0, 11, 0.50, 0.40, goldenState));
        gswPlayers.add(new BasketballPlayer("Draymond Green", 198.0, 104.0, 23, 0.53, 0.30, goldenState));
        teams.add(goldenState);

        List<BasketballPlayer> lakersPlayers = new ArrayList<>();
        Team lakers = new Team("lakers", lakersPlayers);
        lakersPlayers.add(new BasketballPlayer("LeBron James", 206.0, 113.0, 6, 0.55, 0.35, lakers));
        lakersPlayers.add(new BasketballPlayer("Kobe Bryant", 198.0, 96.0, 24, 0.52, 0.34, lakers));
        lakersPlayers.add(new BasketballPlayer("Shaquille O'Neal", 216.0, 147.0, 34, 0.62, 0.00, lakers));
        teams.add(lakers);

        List<BasketballPlayer> okcPlayers = new ArrayList<>();
        Team okc = new Team("okc", okcPlayers);
        okcPlayers.add(new BasketballPlayer("Shai Gilgeous-Alexander", 198.0, 88.0, 2, 0.53, 0.35, okc));
        okcPlayers.add(new BasketballPlayer("Kevin Durant", 208.0, 109.0, 35, 0.54, 0.38, okc));
        okcPlayers.add(new BasketballPlayer("Chet Holmgren", 216.0, 88.0, 7, 0.52, 0.38, okc));
        teams.add(okc);

        return teams;
    }

    public static BasketballPlayer getUser() {
        return user;
    }

    public static BasketballPlayer getOpponent() {
        return opponent;
    }
}
