/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sgde.ini;

/**
 * Thrown when invalid flags are passed in.
 * @author kdsweenx
 */
public class InvalidFlagException extends Exception{
    public InvalidFlagException(String reason){
        super("Invalid flags passed. Reason: "+reason);
    }
}
