package networking.packets;

/**
 * Created by diogo on 5/23/16.
 */
public class StatePacket extends BasePacket {

    public EntityInfo player;

    public enum states {
        HELLO,
        READY,
        GO_TO_ON_STS,
        GO_TO_END_STS,
        GO_TO_NEW_STS,
        SRV_FULL,
        SRV_ALREADY_IN_GAME
    }

    public states state;

    public RoundInfo roundInfo;

    public StatePacket(){}

    public StatePacket(EntityInfo player, states state) {
        super();
        this.player = player;
        this.state = state;
    }

    @Override
    public String toString() {
        return "StatePacket{" +
                "state=" + state +
                '}';
    }
}
