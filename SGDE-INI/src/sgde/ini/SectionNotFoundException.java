/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sgde.ini;

/**
 *
 * @author kdsweenx
 */
public class SectionNotFoundException extends Exception{
    public SectionNotFoundException(String secName){
        super("No Section Found called: "+secName);
    }
}
