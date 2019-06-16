/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import javax.ejb.Local;
import newpackage.Data;
import newpackage.DataInterface;

/**
 *
 * @author kalex
 */
@Local
public interface OperationSelectLocal {

    public DataInterface[] dbSelect(Data data);
}
