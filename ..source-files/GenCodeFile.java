//TFG 2013-2014

package genCode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import files.ConfigFile;

/**
 *
 * @author Belen Bermejo Gonzalez
 */

/*Class that load the configuration of random file*/
public class GenCodeFile {

    private final Properties prop = new Properties();
    private InputStream input = null;

    private static int nLines;
    private static double Pload;
    private static double Pstore;
    private static double Paluim;
    private static double Palu;
    private static double Pshift;
    private static double Pjump;
    private static int nJumpsPoints;
    private static int nVariables;

    public GenCodeFile(String path) {
        try {
            input = new FileInputStream(path);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadConfig() throws IOException {
        prop.load(input);        
        nLines = Integer.parseInt(prop.getProperty("N_LINES"));
        Pload = Double.parseDouble(prop.getProperty("P_LOAD"));
        Pstore = Double.parseDouble(prop.getProperty("P_STORE"));
        Paluim = Double.parseDouble(prop.getProperty("P_ALUIM"));
        Palu = Double.parseDouble(prop.getProperty("P_ALU"));
        Pshift = Double.parseDouble(prop.getProperty("P_SHIFT"));      
        Pjump = Double.parseDouble(prop.getProperty("P_JUMP"));
        nJumpsPoints = Integer.parseInt(prop.getProperty("N_JUMPS_POINTS"));
        nVariables = Integer.parseInt(prop.getProperty("N_VARIABLES"));
    }

    public static int getnLines() {
        return nLines;
    }

    public static double getPload() {
        return Pload;
    }

    public static double getPstore() {
        return Pstore;
    }

    public static double getPaluim() {
        return Paluim;
    }

    public static double getPalu() {
        return Palu;
    }

    public static double getPshift() {
        return Pshift;
    }
    public static double getPjump() {
        return Pjump;
    }

    public static int getnJumpsPoints() {
        return nJumpsPoints;
    }

    public static int getnVariables() {
        return nVariables;
    }

}
