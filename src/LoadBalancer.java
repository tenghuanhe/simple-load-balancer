import eneter.messaging.messagingsystems.messagingsystembase.IDuplexInputChannel;
import eneter.messaging.messagingsystems.messagingsystembase.IMessagingSystemFactory;
import eneter.messaging.messagingsystems.tcpmessagingsystem.TcpMessagingSystemFactory;
import eneter.messaging.nodes.loadbalancer.ILoadBalancer;
import eneter.messaging.nodes.loadbalancer.ILoadBalancerFactory;
import eneter.messaging.nodes.loadbalancer.RoundRobinBalancerFactory;

/**
 * Created by tenghuanhe on 2016/3/15.
 */
public class LoadBalancer {
    public static void main(String[] args) throws Exception {
        IMessagingSystemFactory tcpMessagingSystemFactory = new TcpMessagingSystemFactory();
        ILoadBalancerFactory roundRobinBalancerFactory = new RoundRobinBalancerFactory(tcpMessagingSystemFactory);
        ILoadBalancer loadBalancer = roundRobinBalancerFactory.createLoadBalancer();
        String[] availableServices = {"tcp://127.0.0.1:8071", "tcp://127.0.0.1:8072", "tcp://127.0.0.1:8073"};
        for (String service : availableServices) {
            loadBalancer.addDuplexOutputChannel(service);
        }

        IDuplexInputChannel inputChannel = tcpMessagingSystemFactory.createDuplexInputChannel("tcp://127.0.0.1:8060");
        loadBalancer.attachDuplexInputChannel(inputChannel);
        System.out.println("Load Balancer is running.\nPress Enter to stop...");
        System.in.read();
        loadBalancer.detachDuplexInputChannel();
    }
}
