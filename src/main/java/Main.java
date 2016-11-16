import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.IOException;
import java.util.*;

/**
 * Created by sachin on 8/11/16.
 */

public class Main {

    public static String url="ws://localhos:8000/socketcluster/";

    public static void main(String arg[])  {

        Socket socket=new Socket(url);

        socket.setListener(new BasicListener() {

            public void onConnected(Map<String, List<String>> headers) {
                System.out.println("Connected to endpoint");
            }

            public void onDisconnected(WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
                System.out.println("Disconnected from end-point");
            }

            public void onConnectError(WebSocketException exception) {
                System.out.println("Got connect error");
            }

            public void onSetAuthToken(String token,Socket socket) {
                socket.setAuthToken(token);
            }

            public void onAuthentication(Boolean status) {
                if (status){
                    System.out.println("Socket is authenticated");
                }else{
                    System.out.println("Authentication is required (optional)");
                }
            }

        });

        socket.connect();

        socket.ackSubscribe("yell", new Emitter.Listener() {
            public void call(Object object) {
                System.out.println("Got publish :: " + object);
            }
        }, new Ack() {
            public void call(Object error, Object data) {
                if (error==null) {
                    System.out.println("Subscribed to channel successfully");
                }
            }
        });



        socket.on("chat", new Emitter.Listener() {
            public void call(Object object) {
                System.out.println("Got echo event :: " +object);
            }
        });

//        socket.on("ping", new Emitter.Listener() {
//            public void call(Object... args) {
//                System.out.println("Got the message"+args[0]);
//            }
//        });

//        socket.on("ping", new Emitter.AckListener() {
//            public void call(Object object, Ack ack) {
//                System.out.println("Got ping data "+object);
//                ack.call("sample error","sample object");
//            }
//        });

        while (true) {
            Scanner scanner=new Scanner(System.in);
//            socket.emit("chat", scanner.nextLine(), new Ack() {
//                public void call(Object error, Object data) {
//                    System.out.println("Error is "+error+" and data is "+ data);
//                }
//            });
            socket.ackPublish("yell", scanner.nextLine(), new Ack() {
                public void call(Object error, Object data) {
                    if (error==null){
                        System.out.println("Publish sent successfully");
                    }
                }
            });
        }

//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                socket.disconnect();
//            }
//        },2000);
//
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    socket.connect();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        },4000);
    }

}
