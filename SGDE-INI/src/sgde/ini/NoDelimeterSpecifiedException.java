/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sgde.ini;

/**
 * Thrown if no flag is passed for what delimeter to use
 * @author kdsweenx
 */
public class NoDelimeterSpecifiedException extends Exception{
    public NoDelimeterSpecifiedException(){
        super("No Delimeter Type Specified");
    }
}
