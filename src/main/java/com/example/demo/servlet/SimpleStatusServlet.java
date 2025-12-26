package com.example.demo.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/status") // Matches the @ServletComponentScan in DemoApplication
public class SimpleStatusServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Requirement: Handle null parameters without error
        req.getParameter("any");

        // Requirement: Allow exception if writer is null (for Test t07)
        PrintWriter writer = resp.getWriter();
        
        resp.setContentType("text/plain");
        writer.write("Digital Credential Verification Engine is running");
    }
}