//TFG 2013-2014

package main;

import files.ConfigFile;
import architecture.Architecture;
import genCode.GenCode;
import genCode.GenCodeFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Belen Bermejo Gonzalez
 */

/*Class that implements the main program*/
public class LGMIPSV1 {

    static String path;       
    static String configPath = "config.properties";
    static String outFile;

    public static void main(String[] args) throws FileNotFoundException, IOException {
//        FileWriter fw;
//        File fileResult;
//        String aux = "////////////////////////////////////////////\r\n/ Description:/"
//                + "    Simple example program\r\n/\r\n/\r\n/ Name:\r\n/    Belén Bermejo\r\n"
//                + "////////////////////////////////////////////\r\n/ Main program\r\n"
//                + "/Variables for main	.data\r\nage_	.word	-256	/decimal\r\n"
//                + "gpa_	.half	0xA02	/hex\r\nnl_	.byte	0%12	/octal\r\n"
//                + "bin_	.word	0b11000111100001111010010101011111	/binary\r\n"
//                + "\r\n/Main body\r\n	.text";
//
//        for (int i = 4; i < 11; i++) {
//            fileResult = new File("test" + i + ".txt");
//            fw = new FileWriter(fileResult.getAbsoluteFile());
//            try (BufferedWriter bw = new BufferedWriter(fw)) {
//                bw.write(aux);
//            } catch (Exception e) {
//                System.out.println("Alala");
//            }
//        }

        ConfigFile conf = new ConfigFile(configPath);
        conf.loadConfig();
        // conf.showConfig();
        String option = ConfigFile.getRandomFile();
        switch (option) {
            case "y":
                GenCodeFile gcf = new GenCodeFile("genCode.properties");
                gcf.loadConfig();
                GenCode randomCode = new GenCode();
                File f = randomCode.getFile();
                path = f.getName();
                //     System.out.println("PATH: " + path);
                break;
            case "n":
                path = ConfigFile.getSourcePath();
                //  System.out.println("PATH: " + path);
                break;
            default:
                System.out.println("OPTION ERROR");
        }
        Architecture a = new Architecture(ConfigFile.getnBits(), path);
        a.simulateMIPS();
//        for (int i = 0; i < 50; i++) {
//            outFile = "SimResult" + i + ".txt";
//            path = "test" + ((i % 11)+1) + ".txt";
//           
//        }
    }
}
