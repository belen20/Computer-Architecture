//TFG 2013-2014

package architecture;

/**
 *
 * @author Belen Bermejo Gonzalez
 */

/*Class that implements the calculation of simulation results*/
public class Statistics {

    // number of inst
    private static int load = 0;
    private static int store = 0;
    private static int aluIm = 0;
    private static int alu = 0;
    private static int shift = 0;
    private static int mulDiv = 0;
    private static int jump = 0;

    //% of type ins
    private static double Pload = 0.0;
    private static double Pstore = 0.0;
    private static double PaluIm = 0.0;
    private static double Palu = 0.0;
    private static double Pshift = 0.0;
    private static double PmulDiv = 0.0;
    private static double Pjump = 0.0;

    private static final int nIns = Architecture.getListOfIntructions().size();

    public static void calculatePerc() {
        Pload = 100 * ((double) load / nIns);
        Pstore = 100 * ((double) store / nIns);
        PaluIm = 100 * ((double) aluIm / nIns);
        Palu = 100 * ((double) alu / nIns);
        Pshift = 100 * ((double) shift / nIns);
        PmulDiv = 100 * ((double) mulDiv / nIns);
        Pjump = 100 * ((double) jump / nIns);

    }

    //return %
    public static double getPload() {
        return Pload;
    }

    public static double getPstore() {
        return Pstore;
    }

    public static double getPaluIm() {
        return PaluIm;
    }

    public static double getPalu() {
        return Palu;
    }

    public static double getPshift() {
        return Pshift;
    }

    public static double getPmulDiv() {
        return PmulDiv;
    }

    public static double getPjump() {
        return Pjump;
    }

    public static void setLoad(int load) {
        Statistics.load += load;
    }

    public static void setStore(int store) {
        Statistics.store += store;
    }

    public static void setAluIm(int aluIm) {
        Statistics.aluIm += aluIm;
    }

    public static void setAlu(int alu) {
        Statistics.alu += alu;
    }

    public static void setShift(int shift) {
        Statistics.shift += shift;
    }

    public static void setMulDiv(int mulDiv) {
        Statistics.mulDiv += mulDiv;
    }

    public static void setJump(int jump) {
        Statistics.jump += jump;
    }

}
