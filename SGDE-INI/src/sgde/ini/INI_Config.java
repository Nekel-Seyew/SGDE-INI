/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sgde.ini;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * A Database which holds all of the information stored in an INI file.
 * @author kdsweenx
 */
public class INI_Config {
    /**
     * Flag for ignoring whitespace in both the name and value fields.
     */
    public static final char IGNORE_WHITE_SPACE = 0b00000001;
    /**
     * Flag for creating extra sections with commas in the name.
     */
    public static final char COMMA_SECTIONS = 0b00000010;
    /**
     * Flag for indicating a Colon as the Delimeter between name value pairs.
     */
    public static final char COLON_DELIMETER =0b00000100;
    /**
     * Flag for indicating case sensitive names and values.
     */
    public static final char CASE_SENSITIVE = 0b00001000;
    /**
     * Flag for indicating non-case sensitive names and values.
     */
    public static final char CASE_INSENSITIVE=0b00010000;
    /**
     * Flag for indicating an Equals sign as the Delimeter between name value pairs.
     */
    public static final char EQUAL_DELIMETER= 0b00100000;
    /**
     * Flag for indicating to keep the whitespace between name fields and value fields.
     */
    public static final char KEEP_WHITE_SPACE=0b01000000;
    /**
     * The Default setting for a config file. Contains the flags CASE_SENSITIVE, EQUAL_DELIMETER, and KEEP_WHITE_SPACE.
     */
    public static final char DEFAULT_SETTINGS= CASE_SENSITIVE | EQUAL_DELIMETER | KEEP_WHITE_SPACE;
    /**
     * A different default setting, for slightly different ini files. Contains the flags CASE_INSENSITIVE, COLON_DELIMETER, and IGNORE_WHITE_SPACE.
     */
    public static final char DEFAULT2_SETTINGS= CASE_INSENSITIVE | COLON_DELIMETER| IGNORE_WHITE_SPACE;
    
    private Hashtable<String, Hashtable<String, Object>> configData;
    private char settings;
    
    /**
     * Default Constructor. Uses Default Settings.
     */
    public INI_Config(){
        configData=new Hashtable<>();
        settings=DEFAULT_SETTINGS;
    }
    /**
     * Constructor for custom settings.
     * @param setting custom flag for settings.
     */
    public INI_Config(char setting){
        configData=new Hashtable<>();
        settings=setting;
    }
    /**
     * Creates a new section inside of the database holding ini file information.
     * @param name of the new section.
     */
    public void newSection(String name){
        configData.put(name, new Hashtable<String, Object>());
    }
    /**
     * Puts data into the database for the section name passed in.
     * @param sec name of the section to put the data
     * @param dataName name of the name-value pair
     * @param data value of the name-value pair
     * @throws SectionNotFoundException if there is no section found for the specified section.
     */
    public void putSectionData(String sec, String dataName, Object data) throws SectionNotFoundException{
        Hashtable<String, Object> dataPlace=configData.get(sec);
        if(dataPlace==null){
            throw new SectionNotFoundException(sec);
        }
        dataPlace.put(dataName, data);
    }
    /**
     * Retrieves the entire specified section of data.
     * @param sec the section to be returned
     * @return A hashtable(String, Object) containing all data in the section. 
     * @throws SectionNotFoundException if there is no section found for the specified section.
     */
    public Hashtable<String, Object> getSection(String sec) throws SectionNotFoundException{
        Hashtable<String, Object> dataPlace=configData.get(sec);
        if(dataPlace==null){
            throw new SectionNotFoundException(sec);
        }
        return dataPlace;
    }
    /**
     * Obtains the object holding the data in the given section with the given name.
     * @param sec the section the data is held at.
     * @param name the name of the data
     * @return the value with the given name in the specified section
     * @throws SectionNotFoundException if there is no section found for the specified section.
     * @throws DataNotFoundException if there is no data found with the given name.
     */
    public Object getData(String sec, String name) throws SectionNotFoundException, DataNotFoundException{
        Hashtable<String, Object> dataPlace=configData.get(sec);
        if(dataPlace==null){
            throw new SectionNotFoundException(sec);
        }
        Object data=dataPlace.get(name);
        if(data == null){
            throw new DataNotFoundException(name,sec);
        }
        return data;
        
    }
    /**
     * A static function for constructing a config database based upon the ini file.
     * @param fileName path to the ini file.
     * @param settings flags for interpreting the ini file.
     * @return a new config database built from ini file
     * @throws FileNotFoundException if there was no file at the given path
     * @throws NoDelimeterFoundException if there are no delimeters found in a name-value pair
     * @throws SectionNotFoundException should never be thrown, but you never know. If it is thrown, probably due to comma based "sub"-sectioning
     * @throws NoDelimeterSpecifiedException if no flag is passed for the type of delimeter to be used
     * @throws InvalidFlagException general exception thrown if two opposing flags are used.
     */
    public static INI_Config getConfig(String fileName, char settings) throws FileNotFoundException, NoDelimeterFoundException, SectionNotFoundException, NoDelimeterSpecifiedException, InvalidFlagException{
        INI_Config conf=new INI_Config(settings);
        Scanner reader = new Scanner(new File(fileName));
        String currentSection=null;
        while(reader.hasNext()){
            String line=reader.nextLine();
            System.out.println(line);
            if(line.contains("[") && line.contains("]")){
                conf.newSection(line.substring(line.indexOf("[")+1,line.indexOf("]")));
                currentSection=new String(line.substring(line.indexOf("[")+1,line.indexOf("]")));
            }else if(line.length() <=0){
                continue;
            }else{
                //Ignore Comments;
                int comment=line.indexOf(';');
                String work=null;
                if(comment == -1){
                    work=line;
                }else{
                    work=line.substring(0,comment);
                }
                if(work.length()<=0){
                    continue;
                }
                String name=null;
                Object value=null;
                String subValue=null;
                //Get name
                if(!work.contains(":") && !work.contains("=")){
                    throw new NoDelimeterFoundException(currentSection, work);
                }
                if(((settings&COLON_DELIMETER) == COLON_DELIMETER) && ((settings&EQUAL_DELIMETER) == EQUAL_DELIMETER)){
                    throw new InvalidFlagException("Both Colon and Equal Delimeter flags used");
                }else if((settings&COLON_DELIMETER) == COLON_DELIMETER){
                    name=work.substring(0,work.indexOf(':'));
                    name=name.trim();
                    subValue=work.substring(work.indexOf(":")+1);
                }else if((settings&EQUAL_DELIMETER) == EQUAL_DELIMETER){
                    name=work.substring(0,work.indexOf('='));
                    name=name.trim();
                    subValue=work.substring(work.indexOf("=")+1);
                }else{
                    throw new NoDelimeterSpecifiedException();
                }
                //GetValue
                if(((settings&IGNORE_WHITE_SPACE)== IGNORE_WHITE_SPACE) && ((settings&KEEP_WHITE_SPACE)== KEEP_WHITE_SPACE)){   
                    throw new InvalidFlagException("Both Keep and Ignore white space flags passed");
                }else if((settings&IGNORE_WHITE_SPACE)== IGNORE_WHITE_SPACE){
                    name=new String(name.replace(" ",""));
                    subValue=new String(subValue.replace(" ",""));
                }
                if(((settings&CASE_INSENSITIVE)==CASE_INSENSITIVE) && ((settings&CASE_SENSITIVE)==CASE_SENSITIVE)){
                    throw new InvalidFlagException("Both Case sensitive and insensitive flags passed");
                }else if((settings&CASE_INSENSITIVE)==CASE_INSENSITIVE){
                    subValue=new String(subValue.toLowerCase());
                    name=new String(name.toLowerCase());
                }
                String section=currentSection;
                if((settings&COMMA_SECTIONS)==COMMA_SECTIONS){
                    if(name.contains(",")){
                        String Fullname=name.substring(name.lastIndexOf(",")+1);
                        section=currentSection+"."+name.substring(0,name.lastIndexOf(",")).replace(",", ".");
                        conf.newSection(section);
                        name=Fullname;
                    }
                }
                //finally, get the value of the field
                try{
                    value=new Integer(Integer.parseInt(subValue));
                }catch(NumberFormatException nfe){
                    try{
                        value=new Double(Double.parseDouble(subValue));
                    }catch(NumberFormatException e){
                        value=subValue;
                    }
                }
                
                //put
                conf.putSectionData(section, name, value);
            }
            
        }
        return conf;
    }
    
}
