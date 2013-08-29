/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sgde.ini;

/**
 * A class which is thrown when the requested data cannot be found.
 * @author kdsweenx
 */
public class DataNotFoundException extends Exception{
    public DataNotFoundException(String dataName, String section){
        super("Data with name: "+dataName+" not found. Section ["+section+"]");
    }
}
