//TFG 2013-2014
package genCode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 *
 * @author Belen Bermejo Gonzalez
 */

/*Class that implements random source code*/
public class GenCode implements GenCodeConfig {

    public enum Instruction {

        LOAD, STORE, ALUIM, ALU, SHIFT, JUMP
    }

    //Atributes of source code
    private static ArrayList<String> variables;
    private ArrayList<String> nameVar;
    private String generatedInstructions[];
    private int index; //index of array generatedInstructions[]
    private File file;
    private int probOpcode; //prob of opcode
    private int probOperand; //prob of operand
    private int probBrachPoint;
    private String ins = "AND r21,r15,r16"; //Default value    
    private String opcode;
    private String[] operand = new String[3];
    private Collection<String> labelBranchList = new ArrayList<>();
    private Collection<Integer> idBranchPointIns = new ArrayList<>();

    /*Construct the object*/
    public GenCode() {
        double percent = P_LOAD + P_STORE + P_ALUIM + P_ALU + P_SHIFT + P_JUMP;
        if (percent == 100) {
            generatedInstructions = new String[nLines];
            index = 0;
            genCode();
            genBrachPoints();
            genFile();
        } else {
            System.out.println("Error en la asignación de porcentajes");
        }
    }

    /*Method to decide what instruction must to be generated*/
    private void genCode() {
        genVariables();
        int acc;
        int prob;
        for (int i = 0; i < nLines; i++, index++) {
            acc = 0;
            prob = genRandom(0, 100);
            if (prob < (acc += P_LOAD)) {
                genInstruction(Instruction.LOAD);
            } else if (prob < (acc += P_STORE)) {
                genInstruction(Instruction.STORE);
            } else if (prob < (acc += P_ALUIM)) {
                genInstruction(Instruction.ALUIM);
            } else if (prob < (acc += P_ALU)) {
                genInstruction(Instruction.ALU);
            } else if (prob < (acc += P_SHIFT)) {
                genInstruction(Instruction.SHIFT);

            } else if (prob < (acc += P_JUMP)) {
                genInstruction(Instruction.JUMP);
            }
        }
    }

    /*Method to generate random brachPoints*/
    private void genBrachPoints() {
        int indexToLabel = 0;
        Object[] toArray = labelBranchList.toArray();
        //Number of instructions that are a potencial brach point
        int nBrachPoints = labelBranchList.size();
        while (idBranchPointIns.size() != nBrachPoints) {
            probBrachPoint = genRandom(0, generatedInstructions.length);
            if (!idBranchPointIns.contains(probBrachPoint)) {
                idBranchPointIns.add(probBrachPoint);
            }
        }
        Object[] toArray1 = idBranchPointIns.toArray();
        for (int i = 0; i < toArray1.length; i++, indexToLabel++) {
            int indextoArrayIns = (int) toArray1[i];
            generatedInstructions[indextoArrayIns] = toArray[indexToLabel] + ":" + generatedInstructions[indextoArrayIns];
        }
    }


    /*Method that generate a String compose by OPCODE OPERANDS, depending on 
     instruction type calculated before*/
    public void genInstruction(Instruction ins) {
        switch (ins) {
            case LOAD:
                generatedInstructions[index] = genMemIns(true);
                break;
            case STORE:
                generatedInstructions[index] = genMemIns(false);
                break;
            case ALUIM:
                generatedInstructions[index] = genAluIns(true);
                break;
            case ALU:
                generatedInstructions[index] = genAluIns(false);
                break;
            case SHIFT:
                generatedInstructions[index] = genShiftIns();
                break;
            case JUMP:
                generatedInstructions[index] = genJumpIns();
                break;
            default:
                System.out.println("Enumerate Error");
                break;
        }
    }

    /*Method that generate LOAD and STORE instructions. Both have the same format (OPCODE ri,@memory)*/
    public String genMemIns(boolean type) {
        if (type) {
            probOpcode = (int) genRandom(0, load.length);
            opcode = load[probOpcode];
        } else {
            probOpcode = (int) genRandom(0, store.length);
            opcode = store[probOpcode];
        }
        probOperand = (int) genRandom(0, 32);
        operand[0] = "r" + probOperand;
        probOperand = (int) genRandom(0, nameVar.size());
        operand[1] = nameVar.get(probOperand);
        ins = opcode + " " + operand[0] + "," + operand[1];
        System.out.flush();
        return ins;
    }

    /*Method that generate ALU instructions*/
    public String genAluIns(boolean immediate) {
        if (immediate) {
            probOpcode = (int) genRandom(0, aluIm.length);
            opcode = aluIm[probOpcode];
        } else {
            probOpcode = (int) genRandom(0, alu.length);
            opcode = alu[probOpcode];
        }
        probOperand = (int) genRandom(0, 32);
        operand[0] = "r" + probOperand;
        probOperand = (int) genRandom(0, 32);
        operand[1] = "r" + probOperand;
        if (immediate == true) {
            /*The immediate operand->16 bits. 2^16 -1 */
            int nBits = (int) genRandom(0, 16);
            operand[2] = "" + genBinaryNumber(nBits);
        } else {
            probOperand = (int) genRandom(0, 32);
            operand[2] = "r" + probOperand;
        }
        ins = opcode + " " + operand[0] + "," + operand[1] + "," + operand[2];
        System.out.flush();
        return ins;
    }


    /*Method that generate shift instructions*/
    public String genShiftIns() {
        probOpcode = (int) genRandom(0, shift.length);
        opcode = shift[probOpcode];
        probOperand = (int) genRandom(0, 32);
        operand[0] = "r" + probOperand;
        probOperand = (int) genRandom(0, 32);
        operand[1] = "r" + probOperand;
        probOperand = (int) genRandom(0, 32);
        operand[2] = "r" + probOperand;
        ins = opcode + " " + operand[0] + "," + operand[1] + "," + operand[2];
        System.out.flush();
        return ins;
    }

    /*Method that generate jump instructions*/
    public String genJumpIns() {
        probOpcode = (int) genRandom(0, jump.length);
        opcode = jump[probOpcode];
        if (opcode.equals("J")) {
            probOperand = (int) genRandom(0, label.length);
            operand[0] = label[probOperand];
            if (!labelBranchList.contains(operand[0])) {
                labelBranchList.add(operand[0]);
            }
            ins = opcode + " " + operand[0];
        } else if (opcode.equals("BEQ") || opcode.equals("BNE")) {
            probOperand = (int) genRandom(0, 32);
            operand[0] = "r" + probOperand;
            probOperand = (int) genRandom(0, 32);
            operand[1] = "r" + probOperand;
            probOperand = (int) genRandom(0, label.length);
            operand[2] = label[probOperand];
            if (!labelBranchList.contains(operand[2])) {
                labelBranchList.add(operand[2]);
            }
            ins = opcode + " " + operand[0] + "," + operand[1] + "," + operand[2];
        } else if (opcode.equals("BLEZ") || opcode.equals("BGTZ") || opcode.equals("BLTZ")
                || opcode.equals("BGEZ")) {
            probOperand = (int) genRandom(0, 32);
            operand[0] = "r" + probOperand;
            probOperand = (int) genRandom(0, label.length);
            operand[1] = label[probOperand];
            if (!labelBranchList.contains(operand[1])) {
                labelBranchList.add(operand[1]);
            }
            ins = opcode + " " + operand[0] + "," + operand[1];
        }
        System.out.flush();
        return ins;
    }

    /*Method that place random variables*/
    public void genVariables() {
        int nVar = (int) genRandom(1, N_VARIABLES + 1);
        variables = new ArrayList(nVar);
        nameVar = new ArrayList(nVar);
        int index;
        String type;
        String name;
        String val;
        String var;
        for (int i = 0; i < nVar; i++) {
            index = (int) genRandom(0, typeVar.length);
            type = typeVar[index];
            name = "var" + i;
            index = (int) genRandom(0, value.length);
            val = value[index];
            var = name + "_ ." + type + " " + val;
            variables.add(var);
            nameVar.add(name);
        }
    }

    /*Function that return a random number contents in [min,max)*/
    public int genRandom(int min, int max) {
        return (int) (min + (Math.random() * (max - min)));
    }

    /*Method that print random code in a file*/
    private void genFile() {
        file = new File("code.txt");
        try {
            try (BufferedWriter output = new BufferedWriter(new FileWriter(file))) {
                output.write("/////////////////////////////////////////////\r\n");
                output.write("/Description: \r\n");
                output.write("/\tGenerated program \r\n");
                output.write("/Name:  \r\n");
                output.write("/\tBelén Bermejo \r\n");
                output.write("/////////////////////////////////////////////\r\n");
                output.write("/Main program  \r\n");
                output.write("/Variables for main  \r\n");
                output.write("\t.data \r\n");
                for (int i = 0; i < variables.size(); i++) {
                    String s = variables.get(i) + "\r\n";
                    output.write(s);
                }
                output.write("/Main body  \r\n");
                output.write("\t.text \r\n");
                for (int i = 0; i < generatedInstructions.length; i++) {
                    output.write(generatedInstructions[i] + "\r\n");
                    output.flush();
                    System.out.flush();
                }
                output.close();
            }

        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    /*Function that generate a random binary string depending on bits number*/
    public String genBinaryNumber(int nBits) {
        String number = "";
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            int x = 0;
            if (r.nextBoolean()) {
                x = 1;
            }
            number += x;
        }
        return number;
    }

    public File getFile() {
        return file;
    }
}
