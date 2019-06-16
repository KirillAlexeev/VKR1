/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import Beans.OperationSelectLocal;
import newpackage.Data;
import newpackage.DataInterface;
import newpackage.WarehouseLabel;

/**
 *
 * @author Кирилл
 */
public class PartnumberCheck extends HttpServlet {
    public static String pageBegin = "<!DOCTYPE html>"
            + "<html>"
            + "<head>"
            + "<title> </title>"
            + "</head>"
            + "<body>";
    @EJB
    private OperationSelectLocal osl;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String partNumber = request.getParameter("Part#").trim();
        HttpSession session = request.getSession();

        session.setAttribute("Part#", partNumber);
        System.out.println("bean start");
        DataInterface data[] = osl.dbSelect(new Data(DataInterface.SELECT, new WarehouseLabel(0, partNumber.toUpperCase(), null, null, 0, 0, 0, null, null, null, 0), "ty"));
        System.out.println("Bean ended");

        if (data[0].getMessage().equals("No record")) {
            System.out.println("We are here");
            data = null;
            response.sendRedirect("No record.html");
        } else {
            session.setAttribute("Recieveded data", data);
            try (PrintWriter out = response.getWriter()) {
                out.println(pageBegin);
                out.println("<h1> "
                        + "<form action=\"LabelSelect\" method=\"Post\">\n"
                        + drawPage(data)
                        + "</form>"
                        + "</h1>");

                out.println("<h1> "
                        + "<form action=\"MainPage\" method=\"Post\">\n"
                        + "<input type=\"submit\" name=\"New Label\" value=\"New label\"/>"
                        + "</form>"
                        + "</h1>");
                out.println("</body>");
                out.println("</html>");

                session.setAttribute("Data", data);
                data = null;
            }
        }
    }

    private String drawPage(DataInterface[] data) {
        System.out.println("Draw pagexxx");
        StringBuilder sb = new StringBuilder();

        sb.append("<p>");
        for (int i = 0; i < data.length; i++) {
            sb.append("<input name=\"Part#_" + i + "\" readonly=\"readonly\" size=\"30\" type=\"text\" value = \"" + data[i].getLabel().getPartNumber() + " "
                    + data[i].getLabel().getType() + " " + data[i].getLabel().getLocation() + "\" />"
                    + "<input type=\"submit\" name=\"Check_" + i + "\" value=\"Check\"/>"
                    + "<input type=\"submit\" name=\"Print_" + i + "\" value=\"Print\"/></br>");

        }
        sb.append("<p>");

        return sb.toString();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
