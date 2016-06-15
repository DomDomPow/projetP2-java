package networking;

import networking.KListeners.KListenerClientNewGame;
import networking.packets.*;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.EndPoint;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by diogo on 5/23/16.
 */
public class KClient extends KBaseApp {


    private KView view;

    private EntityInfo player = new EntityInfo();

    private Client client;

    public static long timeDelta;

    private boolean updateState = false;


    public static void main(String[] args) throws IOException {
        new KClient(5555,5559,null);
    }

    public KClient(int tcpPort, int udpPort,KView view) throws IOException {
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        this.view = view;
        init();
    }

    private void init() throws IOException {
        client = new Client(100 * 8192, 3*2048);
        kryoSerializer = client.getKryo();
        kryoSerializer.register(String.class);
        kryoSerializer.register(BasePacket.class);
        kryoSerializer.register(BlockChairPacket.class);
        kryoSerializer.register(EntityInfo.class);
        kryoSerializer.register(OnChairPacket.class);
        kryoSerializer.register(RoundInfo.class);
        kryoSerializer.register(StatePacket.class);
        kryoSerializer.register(EntityInfo[].class);
        kryoSerializer.register(StatePacket.states.class);
        kryoSerializer.register(NewPlayerPacket.class);
        kryoSerializer.register(NewPlayerPacket.action.class);
        kryoSerializer.register(int[].class);
        kryoSerializer.register(SimpleDateFormat.class);
        kryoSerializer.register(GregorianCalendar.class);
        kryoSerializer.register(Date.class);
        kryoSerializer.register(DateFormatSymbols.class);
        kryoSerializer.register(String[].class);
        kryoSerializer.register(Locale.class);
        client.addListener(new KListenerClientNewGame(this));
        client.start();
        client.connect(5000, JOptionPane.showInputDialog(null,"Entrez l'ip"), tcpPort, udpPort);
        endPoint = (EndPoint) client;
    }

    public void sendReady(){
        if(view != null){
            client.sendTCP(new StatePacket(view.getPlayerInfo(),StatePacket.states.READY));
        }
    }

    public void sendChairTaken(int index){
        OnChairPacket chairPacket = new OnChairPacket();
        chairPacket.chairIndex = index;

        chairPacket.playerOnChair = view.getPlayerInfo();

        Calendar timeout = Calendar.getInstance();
        timeout.add(Calendar.MILLISECOND,(int)-KClient.timeDelta);
        chairPacket.clientTime = timeout.getTime().toString();
        client.sendTCP(chairPacket);
    }

    public void disconnectMe(){
        StatePacket statePacket = new StatePacket(view.getPlayerInfo(), StatePacket.states.DISCONNECT_ME);
        client.sendTCP(statePacket);
    }

    public KView getView() {
        return view;
    }

    public void setView(KView view) {
        this.view = view;
    }

    public EntityInfo getPlayer() {
        return player;
    }

    public void setPlayer(EntityInfo player) {
        this.player = player;
    }

    public boolean isUpdateState() {
        return updateState;
    }

    public void setUpdateState(boolean updateState) {
        this.updateState = updateState;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
