package com.cats.queue.handler.rabbitmq;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
//	final static String queueName = "operation";
	public static void main(String[] args) throws InterruptedException {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(RabbitMQConfig.class);
		ctx.refresh();
		System.out.println("---Message is being sent---");
		RabbitTemplate rabbitTemplate = (RabbitTemplate)ctx.getBean("rabbitTemplate");
		MessageReceiver receiver = (MessageReceiver)ctx.getBean("receiver");
//		rabbitTemplate.convertAndSend(queueName, "CATS Analytics confirms operation is saved!");
		receiver.getCountDownLatch().await(1, TimeUnit.SECONDS);

//	   	ctx.close();
	}
}
 