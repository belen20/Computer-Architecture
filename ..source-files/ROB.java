//TFG 2013-2014
package architecture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import files.ConfigFile;


/**
 *
 * @author Belen Bermejo Gonzalez
 */

/*Class that implements ROB structure in order to re-order the instructions*/
public class ROB implements MIPS { 

    private static boolean active;
    private final int bufferSize = ConfigFile.getSizeROB(); 
    private int latencyCommit = ConfigFile.getCommitCycles();
    private  int SCALAB_COMMIT = ConfigFile.getRetirROB();

    private HashMap<Integer, Instruction> rob = new HashMap<>();

    public ROB(boolean active) {
        ROB.active = active;      
    }

    /*Method that put an instruction in ROB*/
    public void goInside(Instruction inst) {
        int key = inst.getIDIns(); //clave al hash
        if (rob.size() != bufferSize) {         
            rob.put(key, inst);
        } else {
            System.out.println("Put ROB error");
        }
    }

    public boolean isFull() {
        boolean full = false;
        if (rob.size() == bufferSize) {
            full = true;
        }
        return full;
    }

    /*Function that return true if one instruction is in ROB*/
    public boolean containsIns(Instruction inst) {
        return rob.containsValue(inst);
    }

    public void doCommit(ArrayList<Instruction> commitList, ArrayList<Instruction> instList) {       
        int commit = 0;
        for (int i = 0; i < SCALAB_COMMIT; i++) {
            for (Instruction ins : commitList) {          
                if (ins.getIDIns() == 1) {                   
                    commit = ins.getWr() + latencyCommit;                    
                    ins.setCommit(commit);
                    int inicio=ins.getWr()+1;
                    ins.setScommit(inicio);
                    System.out.println("INICIO Commit " + ins.getIDIns() + " ciclo " + inicio);
                    System.out.println("Commit " + ins.getIDIns() + " ciclo " + ins.getCommit());
                } else {
                    int inicio1=ins.getWr()+1;
                    ins.setScommit(inicio1);
                    if (previousCommit(ins, instList)) { 
                        ins.setCommit(ins.getWr() + latencyCommit);
                        System.out.println("INICIO Commit " + ins.getIDIns() + " ciclo " + inicio1);
                        System.out.println("Commit " + ins.getIDIns() + " ciclo " + ins.getCommit());
                    }
                }
                if (ins.getIDIns() == instList.size()) {                    
                    Pipeline.setFinish(true);
                    Pipeline.setMaxCycle(ins.getCommit());
                    System.out.println("CICLO MAXIMO ROB "+ins.getCommit());
                }
            }
        }
    }

    /*Fuction that return true is all of previous instruccions has done commit*/
    private boolean previousCommit(Instruction ins, ArrayList<Instruction> inst) {
        boolean OK = true;
        int index = ins.getIDIns() - 1; 
        for (int i = index; i <= 0; i--) {
            if (inst.get(index).getCommit() == 0) {
                OK = false;
            }
        }
        return OK;
    }
    
    /*Method that clean ROB dur to and speculated instruction*/
    public void cleanRobSpeculation(Instruction specRespon) {
        int insSpecID = specRespon.getIDIns(); 
        Iterator<Integer> keySetIterator = rob.keySet().iterator();
        while (keySetIterator.hasNext()) {
            Integer key = keySetIterator.next();
            Instruction inst = rob.get(key);
            if (inst.getSpeculate().contains(insSpecID)) {
                rob.remove(key);
            }
        }
    }

    public static boolean isActive() {
        return active;
    }
}
