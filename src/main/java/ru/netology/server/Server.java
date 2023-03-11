package ru.netology.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Leonid Zulin
 * @date 11.03.2023 12:24
 */
public class Server {

    // using ExecutorService
    private final ExecutorService executorService;

    // the size of the thread pool is set via the constructor parameters
    public Server(int sizeOfPool) {
        // Creating a thread pool
        this.executorService = Executors.newFixedThreadPool(sizeOfPool);
    }

    // method - the server listens on a specific port
    public void listen(int port) {
        try (final var serverSocket = new ServerSocket(port)) {
            while (true) {
                final Socket socket = serverSocket.accept();
                // using our class MyRunnable
                MyRunnable myRunnable = new MyRunnable(socket);
                // Let's set the task for execution, pass an instance of our MyRunnable class
                executorService.submit(myRunnable);
            }

        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}
