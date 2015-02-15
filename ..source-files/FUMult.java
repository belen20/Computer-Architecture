//TFG 2013-2014
package functionalUnit;

import architecture.Instruction;
import architecture.MIPS;
import architecture.Operations;

/**
 *
 * @author Belen Bermejo Gonzalez
 */

/*Class that implement multiply FU*/
public class FuMult extends FunctionalUnit implements MIPS {

    public FuMult(int ID, int latency) {
        super(ID, latency);
    }

    @Override
    public void execute(Instruction in) {
        active = false;
        switch (in.getOpcode()) {
            case "SLLV":
                Operations.SLLV(in);
                break;
            case "SRLV":
                Operations.SRLV(in);
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
