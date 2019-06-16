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
import newpackage.WarehouseLabelInterface;

/**
 *
 * @author Кирилл
 */
public class NewPartNumber extends HttpServlet {

    @EJB
    private OperationSelectLocal osl;
    private DataInterface resultData[];

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String operation = request.getParameter("Check").trim();
        if (operation.equals("AddNewPartNumber")) {
            System.out.println("Add new");
            resultData = osl.dbSelect(new Data(DataInterface.INSERT, getLabel(request), "add new"));
        } else if (operation.equals("ChangePartNumber")) {
            StringBuilder oldLabelData = new StringBuilder();
            WarehouseLabel oldLabel;
            oldLabel = (WarehouseLabel) session.getAttribute("OldLabel");
            oldLabelData.append(oldLabel.getType()).append("^")
                    .append(oldLabel.getPartNumber()).append("^")
                    .append(oldLabel.getPartName()).append("^")
                    .append(oldLabel.getALC()).append("^")
                    .append(oldLabel.getMinQty()).append("^")
                    .append(oldLabel.getMaxQty()).append("^")
                    .append(oldLabel.getPackedQty()).append("^")
                    .append(oldLabel.getLocation()).append("^")
                    .append(oldLabel.getQRrcode()).append("^")
                    .append(oldLabel.getPrintQty()).append("^");
            System.out.println(oldLabelData);
            resultData = osl.dbSelect(new Data(DataInterface.UPDATE, getLabel(request), oldLabelData.toString()));
        }

        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            response.sendRedirect("Done.html");
        }
    }

    private WarehouseLabelInterface getLabel(HttpServletRequest request) {
        System.out.println("Servlet packed is:"+request.getParameter("PackedQty"));
        return new WarehouseLabel(Integer.parseInt(request.getParameter("Type")),
                request.getParameter("Part#"),
                request.getParameter("PartName"),
                request.getParameter("ALC"),
                Integer.parseInt(request.getParameter("MinQty")),
                Integer.parseInt(request.getParameter("MaxQty")),
                Integer.parseInt(request.getParameter("PackedQty")),
                request.getParameter("Location"),
                null,
                null,
                Integer.parseInt(request.getParameter("PrintQty"))
        );
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
