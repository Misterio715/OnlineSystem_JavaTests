package compilation.test.project.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Profile("send")
public class Send {
    private static Logger logger = LoggerFactory.getLogger(Send.class);
    private static ConnectionFactory factory;
    private static String queueName;

    public void setConnection(String hostName, String queueName) throws IOException, TimeoutException {
        this.queueName = queueName;
        factory = new ConnectionFactory();
        factory.setHost(hostName);
    }
    public void sendMessage(String message) throws IOException, TimeoutException {
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queueName, false,false,false,null);
        channel.basicPublish("", queueName,null, message.getBytes("utf-8"));
        logger.info("[!] Send '" + message +"'" );
        channel.close();
        connection.close();
    }
}