package ru.netology;
/**
 * @author Leonid Zulin
 * @date 11.03.2023 12:05
 */

import ru.netology.server.Server;

public class Main {
    // port
    final static int PORT = 9999;
    // and the size pool our threads
    final static int SIZE_OF_POOL = 64;

    public static void main(String[] args) {
        // starting the server
        Server server = new Server(SIZE_OF_POOL);
        server.listen(PORT);
    }
}
