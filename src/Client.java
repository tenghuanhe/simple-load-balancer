import eneter.messaging.endpoints.stringmessages.DuplexStringMessagesFactory;
import eneter.messaging.endpoints.stringmessages.IDuplexStringMessageSender;
import eneter.messaging.endpoints.stringmessages.IDuplexStringMessagesFactory;
import eneter.messaging.endpoints.stringmessages.StringResponseReceivedEventArgs;
import eneter.messaging.messagingsystems.messagingsystembase.IDuplexOutputChannel;
import eneter.messaging.messagingsystems.messagingsystembase.IMessagingSystemFactory;
import eneter.messaging.messagingsystems.tcpmessagingsystem.TcpMessagingSystemFactory;
import eneter.net.system.EventHandler;

/**
 * Created by tenghuanhe on 2016/3/15.
 */
public class Client {
    public static void main(String[] args) throws Exception {

        IMessagingSystemFactory iMessagingSystemFactory = new TcpMessagingSystemFactory();
        IDuplexOutputChannel outputChannel = iMessagingSystemFactory.createDuplexOutputChannel("tcp://127.0.0.1:8060");
        IDuplexStringMessagesFactory iDuplexStringMessagesFactory = new DuplexStringMessagesFactory();
        IDuplexStringMessageSender sender = iDuplexStringMessagesFactory.<String, String>
                createDuplexStringMessageSender();
        sender.responseReceived().subscribe(new EventHandler<StringResponseReceivedEventArgs>() {
            @Override
            public void onEvent(Object o, StringResponseReceivedEventArgs stringResponseReceivedEventArgs) {
                System.out.println(stringResponseReceivedEventArgs.getResponseMessage());
            }
        });
        sender.attachDuplexOutputChannel(outputChannel);

        int i = 0;
        while (i++ < 10) {
            sender.sendMessage("Hello World");
        }
        System.in.read();
        sender.detachDuplexOutputChannel();
    }
}
