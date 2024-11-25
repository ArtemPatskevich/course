package by.bsuir;

import by.bsuir.utils.fabrics.SpringBeanControllerFactory;
import by.bsuir.utils.tcp.ClientRequestHandler;
import by.bsuir.utils.tcp.ServerSocketInfo;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class Main extends Application {
	public static int activeUsers = 0;
	public static ConfigurableApplicationContext springContext;
	private static ServerSocket serverSocket;

	public static void startServer() {
		try {
			serverSocket = new ServerSocket(ServerSocketInfo.PORT);
			System.out.println("<=======> SERVER IS RUNNING <=======>");

			while (true) {
				try {
					Socket clientSocket = serverSocket.accept();
					System.out.println("Новое подключение " + clientSocket.toString() + "\nВсего " + (++activeUsers) + " активных пользователей\n");

					Thread clientThread = new Thread(new ClientRequestHandler(clientSocket));
					clientThread.start();

				} catch (IOException e) {
					System.err.println("Ошибка при подключении клиента: " + e.getMessage());
				}
			}
		} catch (IOException e) {
			System.err.println("Ошибка при запуске сервера: " + e.getMessage());
		} finally {
			try {
				if (serverSocket != null && !serverSocket.isClosed()) {
					serverSocket.close();
				}
			} catch (IOException e) {
				System.err.println("Ошибка при закрытии серверного сокета: " + e.getMessage());
			}
		}
	}


	@Override
	public void init() {
		springContext = SpringApplication.run(SpringBootApp.class);
		SpringBeanControllerFactory.setSpringContext(springContext);
//		springContext = new SpringApplicationBuilder(SpringBootApplicationJPA.class).run();
	}

	@Override
	public void stop() {
		springContext.close();
	}

	@Override
	public void start(Stage stage) {
		startServer();
	}
}