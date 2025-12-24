package com.example.demo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/servlet/SimpleStatusServlet")
public class SimpleStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Must be PUBLIC so the test suite can call it
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        // Exact string required by PDF
        resp.getWriter().write("Digital Credential Verification Engine is running");
    }
}