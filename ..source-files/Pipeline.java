//TFG 2013-2014
package architecture;

import functionalUnit.FUManager;
import functionalUnit.FunctionalUnit;
import java.io.IOException;
import java.util.ArrayList;
import files.ConfigFile;
import files.SimResult;

/**
 *
 * @author Belen Bermejo Gonzalez
 */

/*Class to implements the pipelined execution*/
public class Pipeline implements MIPS {

    public enum pipelineStages {

        FETCH, DECODE, EXECUTION, MEMORY, WRITE, COMMIT
    }

    private static ArrayList<Instruction> listOfIntructions = new ArrayList<>();
    private String[][] cycleMatrix;
    private static int maxCycle;
    private int cycle = 0;
    private static int pc = 0; //program counter
    private static boolean finish = false;
    private final int SCALAB = ConfigFile.getScalability();

    //Stage latency
    private final int FETCH = ConfigFile.getFetchCycles();
    private final int DECODE = ConfigFile.getDecodeCycles();
    private final int EXECUTE = ConfigFile.getExecuteCycles();
    private final int MEMORY = ConfigFile.getMemoryCycles();
    private final int WRITE = ConfigFile.getWriteCycles();
    private final int COMMIT = ConfigFile.getCommitCycles();

    /*How many cicles are been executen from one stage*/
    private int counterF = 0;
    private int counterD = 0;
    private int counterE = 0;
    private int counterM = 0;
    private int counterW = 0;
    private int counterC = 0;

    /*Buffers between stages*/
    private static ArrayList<Instruction> decodeList = new ArrayList();
    private static ArrayList<Instruction> executeList = new ArrayList();
    private static ArrayList<Instruction> memoryList = new ArrayList();
    private static ArrayList<Instruction> writeList = new ArrayList();
    private static ArrayList<Instruction> commitList = new ArrayList();
    private boolean d = false;
    private static boolean branchOK = true; /*= false*/

    //Functional units
    int generic = ConfigFile.getnFUGeneric();
    int add = ConfigFile.getnFUAdd();
    int mult = ConfigFile.getnFUMult();
    private FUManager manag;

    //Execution states
    public static ArrayList<State> stateList;

    //Simulation results
    SimResult sr = new SimResult();

    public void initPipe() {
        listOfIntructions = Architecture.getListOfIntructions();
        manag = new FUManager(generic, add, mult);
        stateList = new ArrayList<>();
    }

    public void executePipe() throws IOException {
        initPipe();
        while (!finish) {
            cycle++;
            if (ROB.isActive()) {
                commit();
            }
            write();
            memory();
            execute();
            decode();
            fetch();
        }
        generateTimeGraphic();
        double CPI = (double) maxCycle / listOfIntructions.size();
        sr.createResults(cycleMatrix, CPI);

    }

    public void fetch() {
        if (counterF == 0) {
            for (int i = 0; i < SCALAB; i++, pc++) {
                //if ROB is full, the execution is stoped
                if (ROB.isActive() && Architecture.getRob().isFull()) {                    
                    break;
                }                
                //if brach is not resoult and ROB isn't active, fetch is stoped
                if (!branchOK && !ROB.isActive()) {
                    break;
                }
                counterF = FETCH;
                if (pc < listOfIntructions.size()) {
                    if (pc >= 0) {                        
                        if (ROB.isActive()) {
                            Architecture.rob.goInside(listOfIntructions.get(pc));
                            listOfIntructions.get(pc).setState(instState.THROW); //esta lanzada
                        }                        
                        if (pc > 0) {
                            if (listOfIntructions.get(pc - 1).isFlagDecode()) {//si aún la anterior espera señal      
                                d = true;
                                break;
                            }
                        }                        
                        //if ROB isn't active, brach will be managed in fetch
                        if (listOfIntructions.get(pc).isIsJump() && !ROB.isActive()) {
                            BranchManager.branchManager(listOfIntructions.get(pc));
                            if (BranchManager.isDoBranch() == true) {
                                pc = BranchManager.getPC();
                            }
                        }
                    }
    
                    listOfIntructions.get(pc).setFetch(cycle + FETCH - 1);
                    
                    listOfIntructions.get(pc).setSfetch(cycle); //cycle start of fetch
                    System.out.println("INICIO fetch " + listOfIntructions.get(pc).getIDIns() + " ciclo "
                            + listOfIntructions.get(pc).getSfetch());

                    System.out.println("fetch " + listOfIntructions.get(pc).getIDIns() + " ciclo "
                            + listOfIntructions.get(pc).getFetch());
                }
            }
        }
        counterF--;
    }

    public void decode() {
        int index;
        int counterDecodeIssue = SCALAB;
        decodeList = getList(pipelineStages.DECODE);
        if (counterD > 0) {
            counterD--;
        }
        if (counterD == 0) {
            for (Instruction inst : decodeList) {                
                if (inst.isRAW()) {
                    break;
                }
                counterD = DECODE;
                index = inst.getIDIns() - 1;                
                
                //if the ins has RAW dependence
                if (listOfIntructions.get(index).isFlagDecode() == true) {
                    int stalls = listOfIntructions.get(index).getCycleDecode();               
                    listOfIntructions.get(index).setSdecode(stalls - DECODE + 1);
                    listOfIntructions.get(index).setDecode(stalls);
                    listOfIntructions.get(index).setFlagDecode(false);
                } else {                
                    listOfIntructions.get(index).setSdecode(cycle);
                    listOfIntructions.get(index).setDecode(cycle + DECODE - 1); 
                }

                System.out.println("INICIO decode " + listOfIntructions.get(index).getIDIns() + " ciclo "
                        + listOfIntructions.get(index).getSdecode());

                System.out.println("decode " + listOfIntructions.get(index).getIDIns()
                        + " ciclo " + listOfIntructions.get(index).getDecode());
                
                /*we determine at what UF beglong one instruction*/
                typeFU t = manag.whatUF(inst);
                inst.setFU(t);                
                counterDecodeIssue--;

                if (counterDecodeIssue == 0) {
                    break;
                }
            }
        }
    }

    public void execute() {
        executeList = getList(pipelineStages.EXECUTION);
        int index;
        int counterExecuteIssue = SCALAB;
        if (counterE > 0) {
            counterE--;
        }
        if (counterE == 0) {
            for (Instruction inst : executeList) {
                counterE = EXECUTE;
                index = inst.getIDIns() - 1; 
                
                /*Entrance to fU*/
                ArrayList<FunctionalUnit> UfList = new ArrayList<>();
                UfList = manag.get(inst.getFU());
                
                //if all FU are non-active 
                if (!UfList.isEmpty()) {                    
                    listOfIntructions.get(index).setExec(cycle + UfList.get(0).getLatency() - 1);                  
                    listOfIntructions.get(index).setState(instState.EXECUTING);
                    UfList.get(0).execute(inst);
                    UfList.get(0).addUse(UfList.get(0).getLatency());       

                    listOfIntructions.get(index).setSexec(cycle);

                    System.out.println("INICIO exec " + listOfIntructions.get(index).getIDIns() + " ciclo "
                            + listOfIntructions.get(index).getSexec());

                    System.out.println("exec  " + listOfIntructions.get(index).getIDIns()
                            + " ciclo " + listOfIntructions.get(index).getExec());
                }
                
                counterExecuteIssue--;
                if (counterExecuteIssue == 0) {
                    break;
                }
            }
        }
        //restore the state of FU
        manag.updateUf();
    }

    public void memory() {
        memoryList = getList(pipelineStages.MEMORY);
        int index;
        int counterMemoryIssue = SCALAB;
        if (counterM > 0) {
            counterM--;
        }
        if (counterM == 0) {
            for (Instruction inst : memoryList) {                
                counterM = MEMORY;
                index = inst.getIDIns() - 1; 
                listOfIntructions.get(index).setMem(cycle + MEMORY - 1); 

                listOfIntructions.get(index).setSmem(cycle);
                System.out.println("INICIO memory " + listOfIntructions.get(index).getIDIns() + " ciclo "
                        + listOfIntructions.get(index).getSmem());

                System.out.println("memory " + listOfIntructions.get(index).getIDIns()
                        + " ciclo " + listOfIntructions.get(index).getMem());
                counterMemoryIssue--;
                if (counterMemoryIssue == 0) {
                    break;
                }
            }
        }
    }

    public void write() {
        writeList = getList(pipelineStages.WRITE);
        int index;
        int counterWriteIssue = SCALAB;
        if (counterW > 0) {
            counterW--;
        }
        if (counterW == 0) {
            for (Instruction inst : writeList) {
                counterW = WRITE;
                index = inst.getIDIns() - 1; 
                listOfIntructions.get(index).setWr(cycle + WRITE - 1); 
                listOfIntructions.get(index).setState(instState.FINISH);
                listOfIntructions.get(index).setSwr(cycle);

                System.out.println("INICIO write " + listOfIntructions.get(index).getIDIns() + " ciclo "
                        + listOfIntructions.get(index).getSwr());

                System.out.println("write " + listOfIntructions.get(index).getIDIns()
                        + " ciclo " + listOfIntructions.get(index).getWr());
                
                /*if the instruction is a master of dependence, it has to signal the slaves instructions
                in ordre to realitse decode*/
                if (inst.isIsSource()) {
                    System.out.println("soy origen " + inst.getIDIns());
                    ArrayList<Integer> col = inst.getSignal();
                    for (int i = 0; i < col.size(); i++) {
                        int signal = col.get(i) - 1;
                        System.out.println("SOY: " + (signal + 1) + "\tRaw: " + listOfIntructions.get(signal).isRAW());
                        listOfIntructions.get(signal).setRAW(false);
                        if (listOfIntructions.get(signal).getFetch() > 0) {
                            if (ROB.isActive()) {
                                listOfIntructions.get(signal).setCycleDecode(cycle + WRITE - 1 + DECODE + COMMIT);
                            } else {
                                listOfIntructions.get(signal).setCycleDecode(cycle + WRITE - 1 + DECODE);
                            }
                        }
                    }
                }
                
                if (!ROB.isActive()) {
                    if (listOfIntructions.get(index).getIDIns() == listOfIntructions.size()) {
                        finish = true;
                        maxCycle = listOfIntructions.get(index).getWr(); //útimo ciclo de ejecución
                        System.out.println("MAX CYCLE: "+maxCycle);
                        //System.out.println("MAX CICLE " + maxCycle);
                    }
                }
                counterWriteIssue--;
                if (counterWriteIssue == 0) {
                    break;
                }
            }
        }
    }
    
    public void commit() {
        if (counterC > 0) {
            counterC--;
        }
        if (counterC == 0) {
            //pasar la lista a la ROB
            commitList = getList(pipelineStages.COMMIT);
            Architecture.getRob().doCommit(commitList, listOfIntructions);
        }
    }

    public ArrayList getList(pipelineStages stage) {
        ArrayList<Instruction> list = new ArrayList<>();
        switch (stage) {
            case DECODE:
                for (int i = 0; i < listOfIntructions.size(); i++) {
                    if (listOfIntructions.get(i).getFetch() < cycle && listOfIntructions.get(i).getFetch() > 0
                            && listOfIntructions.get(i).getDecode() == 0) {
                        list.add(listOfIntructions.get(i));
                    }
                }
                break;
            case EXECUTION:
                for (int i = 0; i < listOfIntructions.size(); i++) {
                    if (listOfIntructions.get(i).getDecode() < cycle && listOfIntructions.get(i).getDecode() > 0
                            && listOfIntructions.get(i).getExec() == 0) {
                        list.add(listOfIntructions.get(i));
                    }
                }
                break;
            case MEMORY:
                for (int i = 0; i < listOfIntructions.size(); i++) {
                    if (listOfIntructions.get(i).getExec() < cycle && listOfIntructions.get(i).getExec() > 0
                            && listOfIntructions.get(i).getMem() == 0) {
                        list.add(listOfIntructions.get(i));
                    }
                }
                break;
            case WRITE:
                for (int i = 0; i < listOfIntructions.size(); i++) {
                    if (listOfIntructions.get(i).getMem() < cycle && listOfIntructions.get(i).getMem() > 0
                            && listOfIntructions.get(i).getWr() == 0) {
                        list.add(listOfIntructions.get(i));
                    }
                }
                break;
            case COMMIT:
                for (int i = 0; i < listOfIntructions.size(); i++) {
                    if (listOfIntructions.get(i).getWr() < cycle && listOfIntructions.get(i).getWr() > 0
                            && listOfIntructions.get(i).getCommit() == 0
                            && Architecture.getRob().containsIns(listOfIntructions.get(i))) {
                        list.add(listOfIntructions.get(i));
                    }
                }
                break;
            default:
                System.out.println("non-define stage");
                break;
        }
        return list;

    }

    /*Method that clean the instruction cycles when a brach has been executed*/
    public static void cleanInstructionCycles(int start, int end) {
        System.out.println("limpia de "+start +" hasta "+end);
        for (int i = start; i <= end; i++) {
            listOfIntructions.get(i).setFetch(0);
            listOfIntructions.get(i).setDecode(0);
            listOfIntructions.get(i).setExec(0);
            listOfIntructions.get(i).setMem(0);
            listOfIntructions.get(i).setWr(0);
            if (ROB.isActive()) {
                listOfIntructions.get(i).setCommit(0);
            }
        }
    }

    /*Method that remove instructions of pipeline stages*/
    public static void cleanList(Instruction inst) {
        if (decodeList.contains(inst)) {
            System.out.println("decode");
            decodeList.remove(inst);
        }
        if (executeList.contains(inst)) {
            System.out.println("execute");
            executeList.remove(inst);
        }
        if (memoryList.contains(inst)) {
            System.out.println("memory");
            memoryList.remove(inst);
        }
        if (writeList.contains(inst)) {
            System.out.println("write");
            writeList.remove(inst);
        }
        if (commitList.contains(inst)) {
            System.out.println("commit");
            commitList.remove(inst);
        }
    }


    private void generateTimeGraphic() throws IOException {
        int filas = listOfIntructions.size() + 1;
        int columnas = maxCycle + 1;

        cycleMatrix = new String[filas][columnas];
        cycleMatrix[0][0] = "instruction";
        for (int i = 1; i <= columnas - 1; i++) {
            cycleMatrix[0][i] = "" + i;
        }
        String ins;
        for (int i = 0; i < filas - 1; i++) {
            ins = listOfIntructions.get(i).getOpcode() + " " + listOfIntructions.get(i).getOperands();
            cycleMatrix[i + 1][0] = ins;
        }

        for (int i = 0; i < listOfIntructions.size(); i++) {
            for (int j = listOfIntructions.get(i).getSfetch(); j <= listOfIntructions.get(i).getFetch(); j++) {
                cycleMatrix[i + 1][j] = "F";
            }
            for (int j = listOfIntructions.get(i).getSdecode(); j <= listOfIntructions.get(i).getDecode(); j++) {
                cycleMatrix[i + 1][j] = "D";
            }

            for (int j = listOfIntructions.get(i).getSexec(); j <= listOfIntructions.get(i).getExec(); j++) {
                cycleMatrix[i + 1][j] = "E";
            }

            for (int j = listOfIntructions.get(i).getSmem(); j <= listOfIntructions.get(i).getMem(); j++) {
                cycleMatrix[i + 1][j] = "M";
            }

            for (int j = listOfIntructions.get(i).getSwr(); j <= listOfIntructions.get(i).getWr(); j++) {
                cycleMatrix[i + 1][j] = "W";
            }
            for (int j = listOfIntructions.get(i).getSfetch(); j <= listOfIntructions.get(i).getWr(); j++) {
                if (cycleMatrix[i + 1][j] == null) {
                    cycleMatrix[i + 1][j] = "S";
                }
            }
            if (ROB.isActive()) {
                for (int j = listOfIntructions.get(i).getScommit(); j <= listOfIntructions.get(i).getCommit(); j++) {
                    cycleMatrix[i + 1][j] = "C";
                }
                //print 'S' to indicate stalls
                for (int j = listOfIntructions.get(i).getSfetch(); j <= listOfIntructions.get(i).getCommit(); j++) {
                    if (cycleMatrix[i + 1][j] == null) {
                        cycleMatrix[i + 1][j] = "S";
                    }
                }
            }
        }
    }

    
    public static void setPC(int value) {
        Pipeline.pc = value;
    }

    public static void setFinish(boolean finish) {
        Pipeline.finish = finish;
    }
    public static ArrayList<State> getStateList() {
        return stateList;
    }

    public static int getMaxCycle() {
        return maxCycle;
    }

    public static void setMaxCycle(int mc) {
        Pipeline.maxCycle = mc;
    }
}
