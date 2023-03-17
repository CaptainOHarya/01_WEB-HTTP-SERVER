package ru.netology.server;

import ru.netology.server.handler.Handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Leonid Zulin
 * @date 11.03.2023 12:24
 */
public class Server {

    // using ExecutorService
    private final ExecutorService executorService;
    // Store our handler, via nested Map {method -> {path -> handler, path 2 -> handler 2,...}}
    private final Map<String, Map<String, Handler>> handlers;


    // the size of the thread pool is set via the constructor parameters
    public Server(int sizeOfPool) {
        // Creating a thread pool
        this.executorService = Executors.newFixedThreadPool(sizeOfPool);
        // It is logical to use ConcurrentHashMap for our implementation, because it is thread-safe
        this.handlers = new ConcurrentHashMap<>();

    }
    // method add handlers
    public void addHandler(String method, String path, Handler handler) {
        // if there are already handlers along the way
        if (this.handlers.get(method) == null) {
            //add empty map for our method
            this.handlers.put(method, new ConcurrentHashMap<>());
        }

        this.handlers.get(method).put(path, handler);
    }


    // method - the server listens on a specific port
    public void listen(int port) {
        try (final var serverSocket = new ServerSocket(port)) {
            while (true) {
                final Socket socket = serverSocket.accept();
                // using our class MyRunnable
                MyRunnable myRunnable = new MyRunnable(socket, handlers);
                // Let's set the task for execution, pass an instance of our MyRunnable class
                executorService.submit(myRunnable);
            }

        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}
