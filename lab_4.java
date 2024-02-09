import java.io.IOException;
import java.util.Scanner;

import com.rabbitmq.client.*;



public class Lab4 {




    public static void main(String[] args) throws Exception {
        
        System.out.println("Podaj swoje imie:");
        Scanner myObj = new Scanner(System.in);
        String name = myObj.nextLine();
        System.out.println("Witaj w czacie");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("");
        factory.setUsername("");
        factory.setPassword("");
        factory.setVirtualHost("");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("my_exchange", BuiltinExchangeType.FANOUT, false, false, null);
        // channel.queueDeclare("my_queue", false, false, true, null);
        String my_queue_name = channel.queueDeclare().getQueue();
        channel.queueBind(my_queue_name, "my_exchange", "");

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(
                String consumerTag,
                Envelope envelope, 
                AMQP.BasicProperties properties, 
                byte[] body) throws IOException {
        
                    String message = new String(body, "UTF-8");
                    System.out.println(message);
            }
        };
        channel.basicConsume(my_queue_name, true, consumer);
        
        
        String message = ""; 
        
        while(true){
            message = myObj.nextLine();
            message = name+": "+message;
            channel.basicPublish("my_exchange", "", null, message.getBytes());
        }
        




    }
}