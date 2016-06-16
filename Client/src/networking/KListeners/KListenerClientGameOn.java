package networking.KListeners;

import com.esotericsoftware.kryonet.Connection;
import networking.KBaseApp;
import networking.KClient;
import networking.packets.EntityInfo;
import networking.packets.StatePacket;
import networking.packets.BlockChairPacket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by diogo on 6/10/16.
 */
public class KListenerClientGameOn extends KAbstractListener {
    boolean chairShowed = false;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public KListenerClientGameOn(KBaseApp context) {
        super(context);
        client();
        clientContext.getClient().sendTCP(clientContext.getView().getPlayerInfo());
        clientContext.getView().onGameStart();
    }

    @Override
    public void connected(Connection connection) {
        super.connected(connection);
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
    }

    @Override
    public void received(Connection connection, Object o) {
        super.received(connection, o);
        if(o instanceof EntityInfo[]){
            EntityInfo[] players = (EntityInfo[])o;
            if(clientContext.getView() != null){
                clientContext.getView().onPlayersPosReceived(players);
            }
            //Sync
            Date parsedDate = null;
            try {
                parsedDate = formatter.parse(players[0].getServerTime());
                KClient.timeDelta = getDateDiff(parsedDate,new Date(), TimeUnit.MILLISECONDS);
                if(checkChairs(formatter.parse(clientContext.getCurrentRound().getShowChairsAt()))){
                    if(clientContext.getView() != null && !chairShowed){
                        clientContext.getView().onTimeToShowChairs(clientContext.getCurrentRound().getChairs());
                        chairShowed = true;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else if(o instanceof BlockChairPacket){
            if(clientContext.getView() != null){
                clientContext.getView().onChairTaken(((BlockChairPacket) o).chairIndex);
            }
        }else if(o instanceof StatePacket && ((StatePacket) o).state == StatePacket.states.GO_TO_END_STS){
            System.out.println("Etat fin");
            context.getEndPoint().removeListener(this);
            context.getEndPoint().addListener(new KListenerClientGameEnd(context));
        }else if(o instanceof StatePacket && ((StatePacket) o).state == StatePacket.states.DISCONNECT_ME){
            if(clientContext.getView() != null)
                clientContext.getView().onPlayerDisconnected(((StatePacket) o).player);
        }
        connection.sendTCP(clientContext.getView().getPlayerInfo());
    }

    private boolean checkChairs(Date popDate){
        Calendar timeout = Calendar.getInstance();
        timeout.add(Calendar.MILLISECOND,(int)-KClient.timeDelta);
        if(timeout.getTime().getTime() - popDate.getTime() > 0){
            return true;
        }else{
            return false;
        }
    }

    public long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}