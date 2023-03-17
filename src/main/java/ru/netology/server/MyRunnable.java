package ru.netology.server;

import ru.netology.server.handler.Handler;
import ru.netology.server.request.Request;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

/**
 * @author Leonid Zulin
 * @date 11.03.2023 12:50
 */

// Let's create our own MyRunnable class - the successor of the Runnable interface, where we implement our own run method
public class MyRunnable implements Runnable{
    // private static final List<String> VALID_PATHS = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");
    private final Socket socket;
    private final Map<String, Map<String, Handler>> handlers;
    private final Handler handlerNotFound = new Handler() {
        @Override
        public void handle(Request request, BufferedOutputStream out) throws IOException {
            // handling error 404
            out.write((
                    "HTTP/1.1 404 Not Found\r\n" +
                            "Content-Length: 0\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            out.flush();
        }
    };

        public MyRunnable(Socket socket, Map<String, Map<String, Handler>> handlers) {
        this.socket = socket;
        this.handlers = handlers;

    }


    // redefine the run method to work with the client by simply calling the handler method
    @Override
    public void run() {
        processConnection(socket);
    }

    private void processConnection(Socket socket) {
        try (
                //
                socket;
                // close resources
                final var in = socket.getInputStream();
                final var out = new BufferedOutputStream(socket.getOutputStream());
        ) {
            Request request = Request.fromInputStream(in);

            // work with handlers
            Map<String, Handler> handlerMap = handlers.get(request.getMethod());
            // if this method does not have a handler
            if (handlerMap == null) {
                handlerNotFound.handle(request, out);
                return;
            }
            // On the way
            Handler handler = handlerMap.get(request.getPath());
            if (handler == null) {
                handlerNotFound.handle(request, out);
                return;
            }

            // OK
            handler.handle(request, out);


        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }
}
