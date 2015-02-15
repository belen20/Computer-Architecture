//TFG 2013-2014
package architecture;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Belen Bermejo Gonzalez
 */
public class BranchManager {

    private static int PC;
    private static boolean doBranch = false; //it indicate if the brach is taked or not
    private static boolean manangmentFinish = false; //it indicate if the brach check is finish

    //Number of taken-branch
    private static int nBrachTaken;

    /*Function that decide if an instruction brach is taken or not.*/
    public static void branchManager(Instruction inst) {
        ArrayList<Object> result = new ArrayList<>();
        HashMap<String, Integer> branchPoints = Architecture.getBranchPoints();
        int s = Architecture.getListOfIntructions().size();
        int op0;
        int op1;
        int target;
        int branch;
        String[] op = inst.getListOperands();
        switch (inst.getOpcode()) {
            case "J":
                PC = branchPoints.get(op[0]) - 1;
                target = PC;
                branch = inst.getIDIns() - 1;
                if (ROB.isActive()) {
                    Pipeline.cleanInstructionCycles(branch, s);
                }
                doBranch = true;
                nBrachTaken++;
                break;
            case "BEQ":
                op0 = Architecture.getRegisters().get(op[0]);
                op1 = Architecture.getRegisters().get(op[1]);
                if (op0 == op1) {
                    PC = branchPoints.get(op[2]) - 1;
                    target = PC;
                    branch = inst.getIDIns() - 1;
                    if (ROB.isActive()) {
                        Pipeline.cleanInstructionCycles(branch, s);
                    }
                    doBranch = true;
                    nBrachTaken++;
                } else {
                    //  PC = Integer.MAX_VALUE;
                    doBranch = false;
                }
                break;
            case "BNE":
                op0 = Architecture.getRegisters().get(op[0]);
                op1 = Architecture.getRegisters().get(op[1]);
                System.out.println(op0 + " " + op1);
                if (op0 != op1) {
                    PC = branchPoints.get(op[2]) - 1;
                    System.out.println("PC " + PC);
                    target = PC;
                    branch = inst.getIDIns() - 1;
                    if (ROB.isActive()) {
                        Pipeline.cleanInstructionCycles(branch, s);
                    }
                    doBranch = true;
                    nBrachTaken++;
                } else {
                    //  PC = Integer.MAX_VALUE;
                    doBranch = false;
                }
                break;
            case "BLEZ":
                op0 = Architecture.getRegisters().get(op[0]);
                if (op0 <= 0) {
                    PC = branchPoints.get(op[1]) - 1;
                    target = PC;
                    branch = inst.getIDIns() - 1;
                    if (ROB.isActive()) {
                        Pipeline.cleanInstructionCycles(branch, s);
                    }
                    doBranch = true;
                    nBrachTaken++;
                } else {
                    // PC = Integer.MAX_VALUE;
                    doBranch = false;
                }
                break;
            case "BGTZ":
                op0 = Architecture.getRegisters().get(op[0]);
                if (op0 > 0) {
                    PC = branchPoints.get(op[1]) - 1;
                    target = PC;
                    branch = inst.getIDIns() - 1;
                    if (ROB.isActive()) {
                        Pipeline.cleanInstructionCycles(branch, s);
                    }
                    doBranch = true;
                    nBrachTaken++;
                } else {
                    // PC = Integer.MAX_VALUE;
                    doBranch = false;
                }
                break;
            case "BLTZ":
                op0 = Architecture.getRegisters().get(op[0]);
                if (op0 < 0) {
                    PC = branchPoints.get(op[1]) - 1;
                    target = PC;
                    branch = inst.getIDIns() - 1;
                    if (ROB.isActive()) {
                        Pipeline.cleanInstructionCycles(branch, s);
                    }
                    doBranch = true;
                    nBrachTaken++;
                } else {
                    // PC = Integer.MAX_VALUE;
                    doBranch = false;
                }
                break;
            case "BGEZ":
                op0 = Architecture.getRegisters().get(op[0]);
                if (op0 >= 0) {
                    PC = branchPoints.get(op[1]) - 1;
                    target = PC;
                    branch = inst.getIDIns() - 1;
                    if (ROB.isActive()) {
                        Pipeline.cleanInstructionCycles(branch, s);
                    }
                    doBranch = true;
                    nBrachTaken++;
                } else {
                    // PC = Integer.MAX_VALUE;
                    doBranch = false;
                }
                break;
        }
        manangmentFinish = true;
    }

    public static int getPC() {
        return PC;
    }

    public static boolean isDoBranch() {
        return doBranch;
    }

    public static boolean isManangmentFinish() {
        return manangmentFinish;
    }

    public static int getnBrachTaken() {
        return nBrachTaken;
    }
}
