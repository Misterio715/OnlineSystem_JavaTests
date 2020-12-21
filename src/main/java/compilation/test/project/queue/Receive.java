package compilation.test.project.queue;

import compilation.test.project.program.Program;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Profile("receive")
public class Receive {
    private static Logger logger = LoggerFactory.getLogger(Receive.class);
    private Channel channel;
    private String queueName;

    public void setConnection(String hostName, String queueName) throws IOException, TimeoutException {
        this.queueName = queueName;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(hostName);
        Connection connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(queueName,
                false, false,false, null);
    }

    public void startReceive() throws IOException{
        logger.info("[!] Waiting for messages. To exit press Ctrl+C");
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                logger.info("[x] Message Recieved' " + message + "'");
                Program.run(message);
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}