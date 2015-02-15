//TFG 2013-2014 

package files;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Belen Bermejo Gonzalez
 */
public class ConfigFile {

    private final Properties prop = new Properties();
    private InputStream input = null;

    /*Config params. to read*/
    private static String sourcePath;
    private static String randomFile;
    private static int nBits;
    private static int startAdress;
    private static int finishAdress;
    private static int nRegister;
    private static int fetchCycles;
    private static int decodeCycles;
    private static int executeCycles;
    private static int memoryCycles;
    private static int writeCycles;
    private static int commitCycles;
    private static String schedulerName;
    private static int nRenameRegister;
    private static int scalability;
    private static int sizeROB;
    private static int nFUGeneric;
    private static int nFUAdd;
    private static int nFUMult;
    private static int latencyGeneric;
    private static int latencyAdd;
    private static int latencyMult;
    private static int retirROB;
    private static String ROB;

    public ConfigFile(String path) {
        try {
            input = new FileInputStream(path);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadConfig() throws IOException {
        prop.load(input);
        sourcePath = normalizeKey(prop.getProperty("sourcePath"));
        randomFile = (normalizeKey(prop.getProperty("randomFile(y/n)")));
        nBits = Integer.parseInt(normalizeKey(prop.getProperty("nBits")));
        startAdress = Integer.parseInt(normalizeKey(prop.getProperty("startAdress")));
        finishAdress = Integer.parseInt(normalizeKey(prop.getProperty("finishAdress")));
        nRegister = Integer.parseInt(normalizeKey(prop.getProperty("nRegister")));
        fetchCycles = Integer.parseInt(normalizeKey(prop.getProperty("fetchCycles")));
        decodeCycles = Integer.parseInt(normalizeKey(prop.getProperty("decodeCycles")));
        executeCycles = Integer.parseInt(normalizeKey(prop.getProperty("executeCycles")));
        memoryCycles = Integer.parseInt(normalizeKey(prop.getProperty("memoryCycles")));
        writeCycles = Integer.parseInt(normalizeKey(prop.getProperty("writeCycles")));
        schedulerName = normalizeKey(prop.getProperty("schedulerName"));
        nRenameRegister = Integer.parseInt(normalizeKey(prop.getProperty("nRenameRegister")));
        scalability = Integer.parseInt(normalizeKey(prop.getProperty("scalable")));
        commitCycles = Integer.parseInt(normalizeKey(prop.getProperty("commitCycles")));
        sizeROB = Integer.parseInt(normalizeKey(prop.getProperty("sizeROB")));
        nFUGeneric = Integer.parseInt(normalizeKey(prop.getProperty("nFUGeneric")));
        nFUAdd = Integer.parseInt(normalizeKey(prop.getProperty("nFUAdd")));
        nFUMult = Integer.parseInt(normalizeKey(prop.getProperty("nFUMult")));
        latencyGeneric = Integer.parseInt(normalizeKey(prop.getProperty("latencyGeneric")));
        latencyAdd = Integer.parseInt(normalizeKey(prop.getProperty("latencyAdd")));
        latencyMult = Integer.parseInt(normalizeKey(prop.getProperty("latencyMult")));
        retirROB = Integer.parseInt(normalizeKey(prop.getProperty("retirementROB")));
        ROB = normalizeKey(prop.getProperty("ROB(y/n)"));
    }

    /*Method that ignore comments at config. file*/
    public String normalizeKey(String key) {
        String normalize = key;
        normalize = normalize.trim();
        int i = normalize.indexOf("#");
        if (i > 0) {
            normalize = normalize.substring(0, i);
        }
        return normalize;
    }

    public static boolean getROB() {
        boolean rob = false;
        if (ROB.equals("n")) {
            rob = false;
        } else if (ROB.equals("y")) {
            rob = true;
        }
        return rob;
    }

    public static int getRetirROB() {
        return retirROB;
    }

    public static int getScalability() {
        return scalability;
    }

    public static String getRandomFile() {
        return randomFile;
    }

    public static String getSourcePath() {
        return sourcePath;
    }

    public static int getnBits() {
        return nBits;
    }

    public static int getStartAdress() {
        return startAdress;
    }

    public static int getFinishAdress() {
        return finishAdress;
    }

    public static int getnRegister() {
        return nRegister;
    }

    public static int getFetchCycles() {
        return fetchCycles;
    }

    public static int getDecodeCycles() {
        return decodeCycles;
    }

    public static int getExecuteCycles() {
        return executeCycles;
    }

    public static int getMemoryCycles() {
        return memoryCycles;
    }

    public static int getWriteCycles() {
        return writeCycles;
    }

    public static String getSchedulerName() {
        return schedulerName;
    }

    public static int getnRenameRegister() {
        return nRenameRegister;
    }

    public static int getCommitCycles() {
        return commitCycles;
    }

    public static int getSizeROB() {
        return sizeROB;
    }

    public static int getnFUGeneric() {
        return nFUGeneric;
    }

    public static int getnFUAdd() {
        return nFUAdd;
    }

    public static int getnFUMult() {
        return nFUMult;
    }

    public static int getLatencyGeneric() {
        return latencyGeneric;
    }

    public static int getLatencyAdd() {
        return latencyAdd;
    }

    public static int getLatencyMult() {
        return latencyMult;
    }
}
