package com.example.demo.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Requirement 7.1: SimpleStatusServlet
 * Must extend HttpServlet and return plain text.
 */
@WebServlet("/servlet/SimpleStatusServlet")
public class SimpleStatusServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Requirement: Produce consistent text across multiple invocations
        String statusMessage = "Digital Credential Verification Engine is running";

        // Requirement: Avoid HTML tags and set as plain text
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");

        try {
            // Requirement: Write exact string to the response writer
            // Requirement: Handle null parameters without error (req/resp are handled by container)
            if (resp.getWriter() != null) {
                resp.getWriter().write(statusMessage);
            }
        } catch (IllegalStateException | IOException e) {
            // Requirement: Allow an exception when no writer is available
            throw e;
        }
    }
}