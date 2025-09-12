import java.util.List;

public class Team {
    private final String name;
    private final List<BasketballPlayer> playerList;

    public Team(String name, List<BasketballPlayer> playerList) {
        this.name = name;
        this.playerList = playerList;
    }

    public List<BasketballPlayer> getPlayerList() {
        return playerList;
    }

    public String getName() {
        return name;
    }
}
