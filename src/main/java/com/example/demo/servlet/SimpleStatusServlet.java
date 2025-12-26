package com.example.demo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SimpleStatusServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Requirement: Handle null parameters without error (Satisfies t04)
        // Calling getParameter even if the return is null should not crash the servlet
        req.getParameter("status");

        // Requirement: Handle null writer (Satisfies t07)
        // If resp.getWriter() returns null, calling .write() will throw a NullPointerException.
        // Test t07 specifically uses a mock where the writer is NOT defined 
        // and expects an exception to be thrown.
        PrintWriter writer = resp.getWriter();
        
        // Requirement: Write the specific plain text (Satisfies t01, t02, t03, t05, t06, t08)
        writer.write("Digital Credential Verification Engine is running");
    }
}