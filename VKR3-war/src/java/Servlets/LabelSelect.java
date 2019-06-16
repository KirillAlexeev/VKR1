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

import Classes.LabelOperationSelect;
import newpackage.Data;
import newpackage.DataInterface;



/**
 *
 * @author Кирилл
 */
public class LabelSelect extends HttpServlet {

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
 
        DataInterface data[] = (DataInterface[]) session.getAttribute("Data");
 
        LabelOperationSelect operation = getOperation(request, data);
         System.out.println(operation.getOperation());
        System.out.println(operation.getSeq());
        try (PrintWriter out = response.getWriter()) {
            if (operation.getOperation().equals("Print")) {
                System.out.println("Label selct print servlet");
                resultData = osl.dbSelect(new Data(DataInterface.UPDATE, data[operation.getSeq()].getLabel(), "+1"));
                response.sendRedirect("SentToPrint.html");

            } else if (operation.getOperation().equals("Check")) {
                session.setAttribute("OldLabel", data[operation.getSeq()].getLabel());
                out.println(PartnumberCheck.pageBegin
                        + "        <div>\n"
                        + "            <form action=\"NewPartNumber\" method=\"Post\">\n"
                        + "            <p>\n"
                        + "                Part#: <input type =\"text\" name = \"Part#\" size=\"20\"value = \"" + data[operation.getSeq()].getLabel().getPartNumber() + "\"/> <br/>\n"
                        + "                <br/>\n"
                        + "                Type: <input type =\"text\" name = \"Type\" size=\"1\"value = \"" + data[operation.getSeq()].getLabel().getType() + "\"/> <br/>\n"
                        + "                <br/>\n"
                        + "                Part name:<input type =\"text\" name = \"PartName\" size=\"20\"value = \"" + data[operation.getSeq()].getLabel().getPartName() + "\"/> <br/>\n"
                        + "                <br/>\n"
                        + "                ALC       <input type =\"text\" name = \"ALC\" size=\"20\"value = \"" + data[operation.getSeq()].getLabel().getALC() + "\"/> <br/>\n"
                        + "                <br/>\n"
                        + "                Min q-ty: <input type =\"text\" name = \"MinQty\" size=\"20\"value = \"" + data[operation.getSeq()].getLabel().getMinQty() + "\"/> <br/>\n"
                        + "                <br/>\n"
                        + "                Max q-ty <input type =\"text\" name = \"MaxQty\" size=\"20\"value = \"" + data[operation.getSeq()].getLabel().getMaxQty() + "\"/> <br/>\n"
                        + "                <br/>\n"
                        + "                Packed q-ty <input type =\"text\" name = \"PackedQty\" size=\"20\"value = \"" + data[operation.getSeq()].getLabel().getPackedQty() + "\"/> <br/>\n"
                        + "                <br/>\n"
                        + "                Location: <input type =\"text\" name = \"Location\" size=\"20\"value = \"" + data[operation.getSeq()].getLabel().getLocation() + "\"/> <br/>\n"
                        + "                <br/>\n"
                        + "                Print q-ty: <input type =\"text\" name = \"PrintQty\" size=\"20\"value = \"" + data[operation.getSeq()].getLabel().getPrintQty() + "\"/> <br/>\n"
                        + "                <br/>\n"
                        + "                <input type=\"submit\" name=\"Check\" value=\"ChangePartNumber\"/>\n"
                        + "            </p> \n"
                        + "\n"
                        + "\n"
                        + "            </form>\n"
                        + "        </div>\n"
                        + "    </body>\n"
                        + "</html>");

            } else if (operation.getOperation().equals("New label")) {

            }
        }
    }

    private LabelOperationSelect getOperation(HttpServletRequest request, DataInterface[] data) {
        String[] printFunction = new String[data.length];
        String[] checkFunction = new String[data.length];
        System.out.println(data.length);
        for (int i = 0; i < data.length; i++) {
            System.out.println(i);
            if (request.getParameter("Print_" + i) != null) {
                return new LabelOperationSelect("Print", i);
            } else if (request.getParameter("Check_" + i) != null) {
                return new LabelOperationSelect("Check", i);
            }

        }
        return null;
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
