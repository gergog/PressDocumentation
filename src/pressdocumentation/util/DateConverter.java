/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pressdocumentation.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.apache.james.mime4j.field.datetime.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author ETHGGY
 */
public class DateConverter {
 
    
    public static Date convert(String toParse) {
        
        Locale hungarian = Locale.ENGLISH;
        Locale[] locales = Locale.getAvailableLocales();
        for (int i = 0; i < locales.length; i++) {
            Locale locale = locales[i];
            if (locale.getCountry().equalsIgnoreCase("hu")) {
                hungarian = locale;
            }
            
        }
        
//        DateTime dt = dtf.parseDateTime("2012. március 21., szerda 9:43");
        
        

        ArrayList<DateTimeFormatter> formatterList = new ArrayList<DateTimeFormatter>();
        formatterList.add(DateTimeFormat.forPattern("YYYY. MMM dd").withLocale(hungarian));
        formatterList.add(DateTimeFormat.forPattern("YYYY. MM. dd").withLocale(hungarian));
        formatterList.add(DateTimeFormat.forPattern("YYYY.MM.dd").withLocale(hungarian));
        formatterList.add(DateTimeFormat.forPattern("YYYY-MM-dd").withLocale(hungarian));
        formatterList.add(DateTimeFormat.forPattern("YYYY/MM/dd").withLocale(hungarian));
        
        
        
        Logger.getLogger(DateConverter.class.getName()).log(Level.INFO, "date to convert : " + toParse);
        
        toParse = StringUtils.strip(toParse);
        
        LocalDate d = null;
        
        for (Iterator<DateTimeFormatter> it = formatterList.iterator(); it.hasNext();) {
            DateTimeFormatter dateTimeFormatter = it.next();
            

            String tempParse = toParse;
        
            try {
                d = dateTimeFormatter.parseLocalDate(tempParse);
                System.out.println("date : " + d.getYear() + "/" + d.getMonthOfYear() + "/" + d.getDayOfMonth());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());

                if (!StringUtils.endsWith(e.getMessage(), "too short")) {
                    String[] sa = StringUtils.splitByWholeSeparator(e.getMessage(), "malformed at ");

                    System.out.println(StringUtils.strip(sa[1], "\""));

                    tempParse = StringUtils.remove(tempParse, StringUtils.strip(sa[1], "\""));

                    try {
                        d = dateTimeFormatter.parseLocalDate(tempParse);
                        System.out.println("date : " + d.getYear() + "/" + d.getMonthOfYear() + "/" + d.getDayOfMonth());
                        break;
                    } catch (IllegalArgumentException e1) {
                        System.out.println(e1.getMessage());
                    }
                }

            }
        
        }
        
        
        return (d == null? null : d.toDate());
        
    }
    
}
