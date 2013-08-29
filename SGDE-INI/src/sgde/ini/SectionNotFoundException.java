/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sgde.ini;

/**
 * Thrown when a requested section could not be found.
 * @author kdsweenx
 */
public class SectionNotFoundException extends Exception{
    public SectionNotFoundException(String secName){
        super("No Section Found called: "+secName);
    }
}
