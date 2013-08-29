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
 *
 * @author kdsweenx
 */
public class Config {
    public static final char IGNORE_WHITE_SPACE = 0b00000001;
    public static final char COMMA_SECTIONS = 0b00000010;
    public static final char COLON_DELIMETER =0b00000100;
    public static final char CASE_SENSITIVE = 0b00001000;
    public static final char CASE_INSENSITIVE=0b00010000;
    public static final char EQUAL_DELIMETER= 0b00100000;
    public static final char KEEP_WHITE_SPACE=0b01000000;
    
    public static final char DEFAULT_SETTINGS= CASE_SENSITIVE | EQUAL_DELIMETER | KEEP_WHITE_SPACE;
    public static final char DEFAULT2_SETTINGS= CASE_INSENSITIVE | COLON_DELIMETER| IGNORE_WHITE_SPACE;
    
    Hashtable<String, Hashtable<String, Object>> configData;
    
    public Config(){
        configData=new Hashtable<>();
    }
    
    public void newSection(String name){
        configData.put(name, new Hashtable<String, Object>());
    }
    
    public void putSectionData(String sec, String dataName, Object data) throws SectionNotFoundException{
        Hashtable<String, Object> dataPlace=configData.get(sec);
        if(dataPlace==null){
            throw new SectionNotFoundException(sec);
        }
        dataPlace.put(dataName, data);
    }
    
    public Hashtable<String, Object> getSection(String sec) throws SectionNotFoundException{
        Hashtable<String, Object> dataPlace=configData.get(sec);
        if(dataPlace==null){
            throw new SectionNotFoundException(sec);
        }
        return dataPlace;
    }
    
    public Object getData(String sec, String name) throws SectionNotFoundException, DataNotFoundException{
        Hashtable<String, Object> dataPlace=configData.get(sec);
        if(dataPlace==null){
            throw new SectionNotFoundException(sec);
        }
        Object data=dataPlace.get(name);
        if(data == null){
            throw new DataNotFoundException(name);
        }
        return data;
        
    }
    
    public static Config getConfig(String fileName, char settings) throws FileNotFoundException, NoDelimeterFoundException, SectionNotFoundException, NoDelimeterSpecifiedException{
        Config conf=new Config();
        Scanner reader = new Scanner(new File(fileName));
        String currentSection=null;
        while(reader.hasNext()){
            String line=reader.nextLine();
            if(line.contains("[") && line.contains("]")){
                conf.newSection(line.substring(line.indexOf("[")+1,line.indexOf("]")));
                currentSection=new String(line.substring(line.indexOf("[")+1,line.indexOf("]")));
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
                if(!work.contains(":")|| !work.contains("=")){
                    throw new NoDelimeterFoundException(currentSection, work);
                }
                if((settings&COLON_DELIMETER) == COLON_DELIMETER){
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
                
                if((settings&IGNORE_WHITE_SPACE)== IGNORE_WHITE_SPACE){
                    name=new String(name.replace(" ",""));
                    subValue=new String(subValue.replace(" ",""));
                }
                if((settings&CASE_INSENSITIVE)==CASE_INSENSITIVE){
                    subValue=new String(subValue.toLowerCase());
                    name=new String(name.toLowerCase());
                }
                String section=currentSection;
                if((settings&COMMA_SECTIONS)==COMMA_SECTIONS){
                    if(name.contains(",")){
                        String Fullname=name.substring(name.lastIndexOf(",")+1);
                        section=name.substring(0,name.lastIndexOf(",")).replace(",", ".");
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
