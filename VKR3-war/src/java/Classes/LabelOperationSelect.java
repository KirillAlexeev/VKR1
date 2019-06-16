/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

/**
 *
 * @author Кирилл
 */
public class LabelOperationSelect {
    private String Operation;
    private int seq;

    public LabelOperationSelect(String Operation, int seq) {
        this.Operation = Operation;
        this.seq = seq;
    }

    public String getOperation() {
        return Operation;
    }

    public int getSeq() {
        return seq;
    }

    public void setOperation(String Operation) {
        this.Operation = Operation;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
    
}
