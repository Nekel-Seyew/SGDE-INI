/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sgde.ini;

/**
 *
 * @author kdsweenx
 */
public class DataNotFoundException extends Exception{
    public DataNotFoundException(String dataName){
        super("Data with name: "+dataName+" not found!");
    }
}
