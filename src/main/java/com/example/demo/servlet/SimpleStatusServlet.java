package com.example.demo.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/status")
public class SimpleStatusServlet extends HttpServlet {

    @Override
    // Must be public for the test package to access it
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getParameter("status");
        PrintWriter writer = resp.getWriter();
        resp.setContentType("text/plain");
        writer.write("Digital Credential Verification Engine is running");
    }
}