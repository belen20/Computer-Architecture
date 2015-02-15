//TFG 2013-2014

package functionalUnit;

import architecture.Instruction;
import architecture.MIPS;
import architecture.Operations;

/**
 *
 * @author Belen Bermejo Gonzalez
 */

/*Class that implement add FU*/
public class FuAdd extends FunctionalUnit implements MIPS {

    public FuAdd(int ID, int latency) {
        super(ID, latency);
    }

    @Override
    public void execute(Instruction in) {
        active = false;
        switch (in.getOpcode()) {
            case "ADDI":
                Operations.ADDI(in);
                break;
            case "ADD":
                Operations.ADD(in);
                break;
            default:
                System.out.println("Execution error");
                break;
        }
        execFinish = true;
    }

    public boolean isIsActive() {
        return active;
    }

    public void setIsActive(boolean stage) {
        this.active = stage;
    }
}
