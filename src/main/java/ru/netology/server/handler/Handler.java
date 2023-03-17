package ru.netology.server.handler;

import ru.netology.server.request.Request;

import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * @author Leonid Zulin
 * @date 11.03.2023 15:15
 */
// Functional handler interface
@FunctionalInterface
public interface Handler {
    void handle(Request request, BufferedOutputStream out) throws IOException; // BufferedOutputStream for response


}
