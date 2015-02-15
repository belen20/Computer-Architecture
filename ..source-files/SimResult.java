//TFG 2013-2014
package files;

import architecture.MIPS;
import architecture.SourceFile;
import architecture.Pipeline;
import architecture.Architecture;
import architecture.Scheduler;
import architecture.BranchManager;
import architecture.Statistics;
import functionalUnit.FuGeneric;
import functionalUnit.FUManager;
import functionalUnit.FunctionalUnit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Belen Bermejo Gonzalez
 */

/*Class that createds and pints simulation resaults*/
public class SimResult implements MIPS {

    private final File fileResult;
    private String[][] cycleMatrix;

    private double CPI;

    public SimResult() {
        fileResult = new File("SimResult.txt");
    }

    public void createResults(String[][] s, double CPI) throws IOException {
        this.cycleMatrix = s;
        this.CPI = CPI;
        Statistics.calculatePerc();
        printResults();
    }

    private void printResults() throws IOException {
        FileWriter fw = new FileWriter(fileResult.getAbsoluteFile());
        try (BufferedWriter bw = new BufferedWriter(fw)) {
            int cyclesG = 0;
            int cyclesA = 0;
            int cyclesM = 0;
            double p = 0;
            String out = "";
            HashMap<MIPS.typeFU, ArrayList<FunctionalUnit>> fu = FUManager.getFU();
            ArrayList<FunctionalUnit> arr = new ArrayList<>();

            out = "------------EXECUTED INSTRUCTIONS---------------\r\n"
                    + Arrays.asList(Architecture.getListOfIntructions().toString()) + "\r\n\r\n";
            bw.write(out);

            out = "------------SIMULATION RESULTS---------------\r\n";
            bw.write(out);
            out = "\r\n\n";
            bw.write(out);
            out = "SCHEDULED BY " + Scheduler.getType();
            bw.write(out);
            out = "\r\n\n";
            bw.write(out);
            for (int i = 0; i < Architecture.getListOfIntructions().size(); i++) {
                out = Architecture.getListOfIntructions().get(i).getIDIns() + " ";
                bw.write(out);
            }
            out = "\r\n\n";
            bw.write(out);

            out = "------------% OF INSTRUCTIONS---------------\r\n";
            bw.write(out);
            p = Statistics.getPload();
            out = "% LOAD " + p + "\r\n";
            bw.write(out);
            out = "% STORE " + Statistics.getPstore() + "\r\n";
            bw.write(out);
            out = "% ALUIM " + Statistics.getPaluIm() + "\r\n";
            bw.write(out);
            out = "% ALU " + Statistics.getPalu() + "\r\n";
            bw.write(out);
            out = "% SHIFT " + Statistics.getPshift() + "\r\n";
            bw.write(out);
            out = "% mulDiv " + Statistics.getPmulDiv() + "\r\n";
            bw.write(out);
            out = "% JUMP " + Statistics.getPjump() + "\r\n";
            bw.write(out);
            out = "\r\n\n";
            bw.write(out);

            out = "------- USE (%) OF FUNCTIONAL UNITS------\r\n";
            bw.write(out);

            arr = fu.get(typeFU.GENERIC);
            out = "TYPE GENERIC\r\n";
            bw.write(out);
            for (int i = 0; i < arr.size(); i++) {
                // System.out.println(arr.get(i).cyclesUse);
                p = 100 * ((double) arr.get(i).getCyclesUse() / Pipeline.getMaxCycle());
                out = "UF " + i + " is used " + p + "\r\n";
                bw.write(out);
                cyclesG += arr.get(i).getCyclesUse();
            }

            p = 100 * ((double) cyclesG / Pipeline.getMaxCycle());
            out = "% TOTAL " + p + "\r\n";
            bw.write(out);

            out = "\r\n\n";
            bw.write(out);

            out = "TYPE ADD\r\n";
            bw.write(out);
            arr = fu.get(typeFU.ADD);
            System.out.println("ADD SIZE " + arr.size());
            for (int i = 0; i < arr.size(); i++) {
                //  System.out.println(arr.get(i).cyclesUse);
                p = 100 * ((double) arr.get(i).getCyclesUse() / Pipeline.getMaxCycle());
                out = "UF " + i + " is used " + p + "\r\n";
                bw.write(out);
                cyclesA += arr.get(i).getCyclesUse();
            }
            p = 100 * ((double) cyclesA / Pipeline.getMaxCycle());
            out = "% TOTAL " + p + "\r\n";
            bw.write(out);

            out = "\r\n\n";
            bw.write(out);

            out = "TYPE MULTIPLY\r\n";
            bw.write(out);
            arr = fu.get(typeFU.MULT);
            for (int i = 0; i < arr.size(); i++) {
                // System.out.println(arr.get(i).cyclesUse);
                p = 100 * ((double) arr.get(i).getCyclesUse() / Pipeline.getMaxCycle());
                out = "UF " + i + " is used " + p + "\r\n";
                bw.write(out);
                cyclesM += arr.get(i).getCyclesUse();
            }
            p = 100 * ((double) cyclesM / Pipeline.getMaxCycle());
            out = "% TOTAL " + p + "\r\n";
            bw.write(out);
            out = "\r\n\n";
            bw.write(out);

            out = "CPI= " + CPI + "\r\n";
            bw.write(out);
            out = "\r\n\n";
            bw.write(out);

            out = "BRANCH-TAKEN NUMBER = " + BranchManager.getnBrachTaken() + "\r\n";
            bw.write(out);
            out = "\r\n\n";
            bw.write(out);

            out = "MEMORY-ACCESS NUMBER = " + FuGeneric.getnAccessMemory() + "\r\n";
            bw.write(out);
            out = "\r\n\n";
            bw.write(out);

            int rows = cycleMatrix.length;
            int cols = cycleMatrix[0].length;

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (i == 0) {
                        //System.out.print(cycleMatrix[i][j] + "\t");
                        out = cycleMatrix[i][j] + "\t";
                        bw.write(out);
                    } else {
                        if (cycleMatrix[i][j] == null) {
                            // System.out.print("\t");
                            out = "\t";
                            bw.write(out);

                        } else {
                            // System.out.print(cycleMatrix[i][j] + "\t");
                            out = cycleMatrix[i][j] + "\t";
                            bw.write(out);
                        }
                    }
                }
                //System.out.print("\r\n");
                bw.write("\r\n");
            }

            //show the final value of registers
            out = "\r\n\n";
            bw.write(out);
            out = "FINAL VALUE OF REGISTERS";
            bw.write(out);
            out = "\r\n\n";
            bw.write(out);
            HashMap<String, Integer> reg = Architecture.getRegisters();
            for (int i = 0; i < Architecture.numberOfRegistersGP; i++) {
                int value = reg.get("r" + i);

                String a = Integer.toString(value, 10);
                out = "r" + i + " " + a + "\r\n";
                bw.write(out);
            }

            out = "\r\n\n";
            bw.write(out);

            out = "FINAL VALUE OF MEMORY";
            bw.write(out);
            out = "\r\n\n";
            bw.write(out);
            ArrayList<String> v = SourceFile.getVar();
            for (int i = 0; i < v.size(); i++) {
                Architecture.mem.getContent(v.get(i));
                out = "Position: " + v.get(i) + " " + Architecture.mem.getContent(v.get(i)) + "\r\n";
                bw.write(out);
                //  System.out.println(Architecture.mem.getContent(v.get(i)));
            }
        }
    }
}
