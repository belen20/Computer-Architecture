//TFG 2013-2014
package functionalUnit;

import architecture.Instruction;

/**
 *
 * @author Belen Bermejo Gonzalez
 */
//Super class
public abstract class FunctionalUnit {

    //Atributes of all FU
    protected int ID;
    protected int latency;
    protected boolean active;
    protected boolean execFinish;
    protected int cyclesUse;

    public FunctionalUnit(int ID, int latency) {
        this.ID = ID;
        this.latency = latency;
    }

    public abstract void execute(Instruction in);

    public boolean isExecFinish() {
        return execFinish;
    }

    public void addUse(int cycles) {
        cyclesUse += cycles;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public int getLatency() {
        return latency;
    }

    public int getCyclesUse() {
        return cyclesUse;
    }

    
}
