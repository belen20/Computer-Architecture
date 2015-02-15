//TFG 2013-2014
package functionalUnit;

import java.util.ArrayList;
import java.util.HashMap;
import files.ConfigFile;
import architecture.Instruction;
import architecture.MIPS;

/**
 *
 * @author Belen Bermejo Gonzalez
 */
public class FUManager extends HashMap implements MIPS {

    /*Structures to place FU*/
    private ArrayList genericFU;
    private ArrayList addFU;
    private ArrayList multFU;

    /*Data structure that store all of functional units*/
    private static HashMap<typeFU, ArrayList<FunctionalUnit>> FU = new HashMap<>();

    /*Set number of FU*/
    private int generic = ConfigFile.getnFUGeneric();
    private int add = ConfigFile.getnFUAdd();
    private int mult = ConfigFile.getnFUMult();

    /*Construct the FU structure*/
    public FUManager(int nGeneric, int nAdd, int nMult) {
        if (nGeneric != 0) {
            generic = nGeneric;
            genericFU = new ArrayList<>(nGeneric);
            //el ID de la UF es i
            for (int i = 0; i < nGeneric; i++) {
                FuGeneric gen = new FuGeneric(i, ConfigFile.getLatencyGeneric());
                gen.setIsActive(true);
                genericFU.add(gen);
            }
            FU.put(typeFU.GENERIC, genericFU);
        }

        if (nAdd != 0) {
            add = nAdd;
            addFU = new ArrayList<>(nAdd);
            for (int i = nGeneric; i < nAdd + nGeneric; i++) {
                FuAdd ad = new FuAdd(i, ConfigFile.getLatencyAdd());
                ad.setIsActive(true);
                addFU.add(ad);
            }
            FU.put(typeFU.ADD, addFU);
        }

        if (nMult != 0) {
            mult = nMult;
            multFU = new ArrayList<>(nMult);
            for (int i = nAdd; i < nMult + nAdd; i++) {
                FuMult m = new FuMult(i, ConfigFile.getLatencyMult());
                m.setIsActive(true);
                multFU.add(m);
            }
            FU.put(typeFU.MULT, multFU);
        }

    }
    
    /*Funtions that return what type of FU corresponds to one intruction*/
    public typeFU whatUF(Instruction inst) {
        boolean asig = false;
        typeFU type = null;
        if (add == 0 && mult == 0) {            
            type = typeFU.GENERIC;
            asig = true;
        } else {
            if (add > 0 && !asig) {
                switch (inst.getOpcode()) {
                    case "ADDI":
                    case "ADDIU":
                    case "ADD":
                    case "ADDU":
                        type = typeFU.ADD;
                        asig = true;
                        break;
                }
            }
            if (mult > 0 && !asig) {
                switch (inst.getOpcode()) {
                    case "MULT":
                    case "MULTU":
                        type = typeFU.MULT;
                        asig = true;
                        break;
                }
            }
            if (generic > 0 && !asig) {                
                asig = true;
                type = typeFU.GENERIC;
            }
        }
        return type;
    }

    /*Function that return the set of FU for one instruction type*/
    public ArrayList get(typeFU T) {
        ArrayList<FunctionalUnit> valueParent = new ArrayList<>(); 
        ArrayList<FunctionalUnit> filtered = new ArrayList<>();
        valueParent = FU.get(T);
        for (int i = 0; i < valueParent.size(); i++) {
            if (valueParent.get(i).isActive() == true) {
                filtered.add(valueParent.get(i));
            }
        }        
        return filtered;
    }

    /*Method that update the state (active / non-active) of FU*/
    public void updateUf() {
        ArrayList<FunctionalUnit> updateList = new ArrayList<>();
        updateList = FU.get(typeFU.GENERIC);
        for (int i = 0; i < updateList.size(); i++) {
            if (updateList.get(i).isExecFinish() == true) {
                updateList.get(i).setActive(true);                 
            }
        }
        updateList = FU.get(typeFU.ADD);
        for (int i = 0; i < updateList.size(); i++) {
            if (updateList.get(i).isExecFinish() == true) {
                updateList.get(i).setActive(true); 
            }
        }
        updateList = FU.get(typeFU.MULT);
        for (int i = 0; i < updateList.size(); i++) {
            if (updateList.get(i).isExecFinish() == true) {
                updateList.get(i).setActive(true); 
            }
        }
    }

    public static HashMap<typeFU, ArrayList<FunctionalUnit>> getFU() {
        return FU;
    }

}
