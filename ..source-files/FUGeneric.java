//TFG 2013-2014
package functionalUnit;

import java.util.ArrayList;
import java.util.HashMap;
import architecture.Architecture;
import architecture.BranchManager;
import architecture.Instruction;
import architecture.MIPS;
import architecture.Memory;
import architecture.Operations;
import architecture.Pipeline;
import architecture.State;

/**
 *
 * @author Belen Bermejo Gonzalez
 */

/*Class that implement all generic operations*/
public class FuGeneric extends FunctionalUnit implements MIPS {

    private static int nAccessMemory;

    public FuGeneric(int ID, int latency) {
        super(ID, latency);
    }

    
    @Override
    public void execute(Instruction in) {        
        active = false; 

        switch (in.getOpcode()) {
            case "BEQ":
            case "BNE":
            case "J":
            case "BLEZ":
            case "BGTZ":
            case "BLTZ":
            case "BGEZ":
                //Add speculate instruction
                setInsSpeculation(in);
                //Save brach state
                State s = new State(in.getIDIns(), Architecture.getMem(), Architecture.getRegisters());
               
                BranchManager.branchManager(in);
                if (BranchManager.isDoBranch() == true) { //if brach is correct                   
                    Pipeline.setPC(BranchManager.getPC());
                    for (int i = 0; i < Architecture.getListOfIntructions().size(); i++) {
                        if (Architecture.getListOfIntructions().get(i).containsInsSpec(in.getIDIns())) {                          
                            int start = Architecture.getListOfIntructions().get(i).getIDIns() - 1;
                            Pipeline.cleanInstructionCycles(start, start);
                            Pipeline.cleanList(in);
                        }
                    }
                   //restore state
                    restoreState(in);                   
                }

                break;
            case "LB":
                Operations.LB(in);
                nAccessMemory++;
                break;
            case "LH":
                Operations.LH(in);
                nAccessMemory++;
                break;
            case "LW":
                Operations.LW(in);
                nAccessMemory++;
                break;
            case "SB":
                Operations.SB(in);
                nAccessMemory++;
                break;
            case "SH":
                Operations.SH(in);
                nAccessMemory++;
                break;
            case "SW":
                Operations.SW(in);
                nAccessMemory++;
                break;
            case "ADDI":
                Operations.ADDI(in);
                break;
            case "ANDI":
                Operations.ANDI(in);
                break;
            case "ORI":
                Operations.ORI(in);
                break;
            case "XORI":
                Operations.XORI(in);
                break;
            case "ADD":
                Operations.ADD(in);
                break;
            case "SUB":
                Operations.SUB(in);
                break;
            case "AND":
                Operations.AND(in);
                break;
            case "OR":
                Operations.OR(in);
                break;
            case "XOR":
                Operations.XOR(in);
                break;
            case "NOR":
                Operations.NOR(in);
                break;
            case "SLLV":
                Operations.SLLV(in);
                break;
            case "SRLV":
                Operations.SRLV(in);
                break;
            default:
                System.out.println("Execution error");
                break;
        }
        execFinish = true; 
    }

    /*Method that restore the processor state*/
    public void restoreState(Instruction in) {
        int idIns = in.getIDIns(); 
        ArrayList<State> stateList = Pipeline.getStateList();
        for (int i = 0; i < stateList.size(); i++) {
            if (stateList.get(i).getIDSpecIns() == idIns) {                
                Memory mem = stateList.get(i).getMem();
                HashMap<String, Integer> registers = stateList.get(i).getRegisters();
                Architecture.registers = registers;
                Architecture.mem = mem;
            }
        }
    }

    /*Method that add speculate instructions*/
    private void setInsSpeculation(Instruction in) {        
        int index = in.getIDIns();    
        for (int i = index; i <= Architecture.getListOfIntructions().size() - 1; i++) {
            Architecture.getListOfIntructions().get(i).addSpeculation(in);            
        }
    }

    public boolean isIsActive() {
        return active;
    }

    public static int getnAccessMemory() {
        return nAccessMemory;
    }

    public void setIsActive(boolean stage) {
        this.active = stage;
    }
}
