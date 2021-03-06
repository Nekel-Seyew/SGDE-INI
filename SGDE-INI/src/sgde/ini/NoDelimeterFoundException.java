/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sgde.ini;

/**
 * Thrown when no delimeter was found in a name-value pair.
 * @author kdsweenx
 */
public class NoDelimeterFoundException extends Exception{
    public NoDelimeterFoundException(String sec, String name){
        super("No Delimeter found in section: ["+sec+"] name: "+name);
    }
}
