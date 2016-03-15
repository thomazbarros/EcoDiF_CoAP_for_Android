package br.ufrj.nce.ubicomp.utils;

/**
 * Created by thomaz on 28/02/16.
 */
public class MyXML {

    public static String myToXML(String dataId, double current_value){
        String formatt =
                "<eeml xmlns=\"http://www.eeml.org/xsd/0.5.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" version=\"0.5.1\" xsi:schemaLocation=\"http://www.eeml.org/xsd/0.5.1 http://www.eeml.org/xsd/0.5.1/0.5.1.xsd\">\n" +
                        "<environment>\n" +
                        "<data id='%s\'>\n" +
                        "<current_value>%f</current_value>\n" +
                        "</data>\n" +
                        "</environment>\n" +
                        "</eeml>\n";
        return String.format(formatt, dataId, current_value);
    }
}