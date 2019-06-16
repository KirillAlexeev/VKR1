/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;


import RemoteBeans.OperationSelectRemote;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import newpackage.Data;

import newpackage.DataInterface;

/**
 *
 * @author kalex
 */
@Stateless
public class OperationSelect implements OperationSelectRemote, OperationSelectLocal {
    @EJB
    private DbManagerLocal db;

    @PostConstruct
    void temp() {
        System.out.println("DBSelect constructed");
    }


    @Override
    public DataInterface[] dbSelect(Data data) {
                System.out.println("DBSelect");
        System.out.println(data.getOperation());
        switch (data.getOperation()) {
            case DataInterface.SELECT:

                System.out.println("DB select");
                return db.select(data);

            case DataInterface.UPDATE:
                System.out.println("DB Update");
                return db.update(data);

            case DataInterface.INSERT:

                System.out.println("DB insert");
                return db.insert(data);
            case DataInterface.DELETE:

                break;
        }
        return null;
    }
    


}
