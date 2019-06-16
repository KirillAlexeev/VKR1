/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;


import java.io.File;
import java.io.IOException;

import java.net.ServerSocket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import newpackage.Data;
import newpackage.DataInterface;
import newpackage.GetConfig;
import newpackage.GetSettings;
import newpackage.WarehouseLabel;

/**
 *
 * @author Кирилл
 */
@Singleton
public class DbManager implements DbManagerLocal {

    private ServerSocket serverSocket;
    private Connection con;
    private Statement st;
    private ResultSet rs;
    private File file;
    private GetConfig getConfigInterface;
    private Properties cfg;
    private Data[] resultData;

    @PostConstruct
    public void startConnection() {
        file = new File("Server config.cfg");

        System.out.println(file.exists());
        if (file.exists()) {
            cfg = getConfig(file);
        } else {
            System.out.println("File doesnt exists");
        }
        System.out.println(""
                + cfg.getProperty("dbName")
                + "/n"
                + cfg.getProperty("User")
                + "/n"
                + cfg.getProperty("Password")
                + "");

        try {

            DriverManager.getDriver(cfg.getProperty("dbName"));
            con = DriverManager.getConnection(cfg.getProperty("dbName"), cfg.getProperty("User"), cfg.getProperty("Password"));
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        } catch (SQLException ex) {
            System.out.println("Error - " + ex.getMessage());
        }
    }


    @Override
    public Data[] select(DataInterface data) {
        resultData = null;
        rs = null;
        try {
            System.out.println("------------->>>>>>>>>>>> Try");
            if(data.getMessage().equals("UpdateList")){
                rs = st.executeQuery("select"
                    + " labeldata.LABELTYPE,"
                    + " labels.PARTNUMBER,"
                    + " labels.PARTNAME,"
                    + " labels.ALC,"
                    + " labels.MINQTY,"
                    + " labels.MAXQTY,"
                    + " labels.PACKEDQTY,"
                    + " labeldata.LOCATION,"
                    + " labeldata.QRCODE,"
                    + " labeldata.PRINTQTY"
                    + "  from labels inner join labeldata on labels.PARTNUMBER = labeldata.PARTNUMBER"
                    + " WHERE labeldata.Printqty >0 ");
            }else{          
            rs = st.executeQuery("select"
                    + " labeldata.LABELTYPE,"
                    + " labels.PARTNUMBER,"
                    + " labels.PARTNAME,"
                    + " labels.ALC,"
                    + " labels.MINQTY,"
                    + " labels.MAXQTY,"
                    + " labels.PACKEDQTY,"
                    + " labeldata.LOCATION,"
                    + " labeldata.QRCODE,"
                    + " labeldata.PRINTQTY"
                    + "  from labels inner join labeldata on labels.PARTNUMBER = labeldata.PARTNUMBER"
                    + " WHERE labels.PARTNUMBER = '" + data.getLabel().getPartNumber().trim().toUpperCase() + "'");
            }
            System.out.println("------------->>>>>>>>>>>> rs end");
            rs.last();

            if (rs.getRow() == 0) {
                System.out.println("RS is null");
                resultData = new Data[1];
                resultData[0] = new Data(DataInterface.SELECT, null, "No record");

                return resultData;
            } else {
                resultData = new Data[rs.getRow()];
                int i = 0;
                rs.beforeFirst();
                while (rs.next()) {
                    System.out.println("We are in the cycle");
                    resultData[i] = new Data(DataInterface.SELECT,
                            new WarehouseLabel(
                                    rs.getInt(1),
                                    rs.getString(2),
                                    rs.getString(3),
                                    rs.getString(4),
                                    rs.getInt(5),
                                    rs.getInt(6),
                                    rs.getInt(7),
                                    rs.getString(8),
                                    null,
                                    null,
                                    rs.getInt(10)), "Selected");
                    System.out.println(i);
                    i++;

                }
                System.out.println(resultData);
                return resultData;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        resultData[1] = new Data(DataInterface.SELECT, null, "Something gone wrong");
        return resultData;
    }

    @Override
    public DataInterface[] update(DataInterface data) {
        try {
            if (data.getMessage().equals("+1")) {

                st.execute("UPDATE LABELDATA SET PRINTQTY = " + (data.getLabel().getPrintQty() + 1) + " WHERE partnumber = '"
                        + data.getLabel().getPartNumber() + "' and location = '" + data.getLabel().getLocation() + "'");
                resultData = new Data[1];

            } else if(data.getMessage().equals("Printed")) {
                
                st.execute("UPDATE LABELDATA SET PRINTQTY = 0 WHERE partnumber = '"
                        + data.getLabel().getPartNumber() + "' and location = '" + data.getLabel().getLocation() + "'");
                resultData = new Data[1];
                
            }else{
                String[] oldLabelData = data.getMessage().split("\\^");
       
                
                WarehouseLabel oldLabel = new WarehouseLabel(Integer.parseInt(oldLabelData[0]),
                        oldLabelData[1],
                        oldLabelData[2],
                        oldLabelData[3],
                        Integer.parseInt(oldLabelData[4]),
                        Integer.parseInt(oldLabelData[5]),
                        Integer.parseInt(oldLabelData[6]),
                        oldLabelData[7],
                        null,
                        null,
                        Integer.parseInt(oldLabelData[9]));
                
                st.execute("update labels set partname = '"
                        + data.getLabel().getPartName() + "',"
                        + " alc = '" + data.getLabel().getALC() + "',"
                        + "MinQty = " + data.getLabel().getMinQty() + ","
                        + "MaxQty = " + data.getLabel().getMaxQty() + ","
                        + "packedQty = " + data.getLabel().getPackedQty()
                        + " where partnumber = '" + data.getLabel().getPartNumber() + "'");
                st.execute("update labelData set "
                        + "LabelType = "  + data.getLabel().getType() + ","
                        + " location = '" + data.getLabel().getLocation() + "',"
                        + "PrintQty = " + data.getLabel().getPrintQty() + ","
                        + "QRCode = '" + data.getLabel().getQRrcode() + "'"
                        + " where partnumber = '" + oldLabel.getPartNumber() + "' and "
                                + "labeltype = " + oldLabel.getType() + " and "
                + "Location = '" + oldLabel.getLocation() + "'");


            }
        } catch (SQLException ex) {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        resultData = new Data[1];
        resultData[0] = new Data(DataInterface.SELECT, null, "Send to print");
        return resultData;
    }

    @Override
    public DataInterface[] insert(DataInterface data) {
        try {
            rs = st.executeQuery("select * from labels where partnumber = '" + data.getLabel().getPartNumber().toUpperCase() + "' ");
            rs.last();
            System.out.println("Bean packed q-ty is:"+ data.getLabel().getPackedQty());
            if (rs.getRow() == 0) {
                System.out.println("Double value ------------->>>>>>");
                st.execute("INSERT INTO LABELS VALUES('"
                        + data.getLabel().getPartNumber().toUpperCase() + "', '"
                        + data.getLabel().getPartName() + "', '"
                        + data.getLabel().getALC() + "', "
                        + data.getLabel().getMinQty() + ", "
                        + data.getLabel().getMaxQty() + ", "
                        + data.getLabel().getPackedQty()
                        + ")"
                );
            }
            st.execute("INSERT INTO LABELDATA VALUES('"
                    + data.getLabel().getPartNumber().toUpperCase() + "', "
                    + data.getLabel().getType() + ", '"
                    + data.getLabel().getLocation() + "', "
                    + data.getLabel().getPrintQty() + ", '"
                    + data.getLabel().getQRrcode() + "')"
            );
        } catch (SQLException ex) {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        resultData[0] = new Data(DataInterface.SELECT, null, "Label added");
        return resultData;
    }

    @Override
    public DataInterface[] delete(DataInterface data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Properties getConfig(File file) {
        getConfigInterface = new GetSettings();
        return getConfigInterface.getGonfig(file);
    }

    @PreDestroy
    @Override
    public void disconnect() {
        try {
            serverSocket.close();
            con.close();
        } catch (IOException ex) {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
