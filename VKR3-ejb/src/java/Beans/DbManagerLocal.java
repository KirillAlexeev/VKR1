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
 * @author Кирилл
 */
@Local
public interface DbManagerLocal {
    public Data[] select (DataInterface data);
    public DataInterface[] update (DataInterface data);
    public DataInterface[] insert (DataInterface data);
    public DataInterface[] delete (DataInterface data);
    
    public void startConnection();
    public void disconnect();
}
