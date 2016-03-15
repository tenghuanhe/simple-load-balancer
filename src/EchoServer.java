import eneter.messaging.endpoints.stringmessages.DuplexStringMessagesFactory;
import eneter.messaging.endpoints.stringmessages.IDuplexStringMessageReceiver;
import eneter.messaging.endpoints.stringmessages.IDuplexStringMessagesFactory;
import eneter.messaging.endpoints.stringmessages.StringRequestReceivedEventArgs;
import eneter.messaging.messagingsystems.messagingsystembase.IDuplexInputChannel;
import eneter.messaging.messagingsystems.messagingsystembase.IMessagingSystemFactory;
import eneter.messaging.messagingsystems.tcpmessagingsystem.TcpMessagingSystemFactory;
import eneter.net.system.EventHandler;

/**
 * Created by tenghuanhe on 2016/3/15.
 */
public class EchoServer {
    public static void main(String[] args) throws Exception {
        IMessagingSystemFactory iMessagingSystemFactory = new TcpMessagingSystemFactory();
        IDuplexInputChannel inputChannel = iMessagingSystemFactory.createDuplexInputChannel("tcp://127.0.0.1:8071");
        IDuplexStringMessagesFactory iDuplexStringMessagesFactory = new DuplexStringMessagesFactory();
        IDuplexStringMessageReceiver receiver = iDuplexStringMessagesFactory.<String, String>createDuplexStringMessageReceiver();
        receiver.requestReceived().subscribe(new EventHandler<StringRequestReceivedEventArgs>() {
            @Override
            public void onEvent(Object o, StringRequestReceivedEventArgs stringRequestReceivedEventArgs) {
                System.out.println(stringRequestReceivedEventArgs.getRequestMessage());
                IDuplexStringMessageReceiver receiver1 = (IDuplexStringMessageReceiver) o;
                try {
                    receiver1.sendResponseMessage(stringRequestReceivedEventArgs.getResponseReceiverId(), "8071");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        receiver.attachDuplexInputChannel(inputChannel);
        System.out.println("The Echo Server is running.\nPress Enter to stop...");
        System.in.read();

        receiver.detachDuplexInputChannel();
    }
}
