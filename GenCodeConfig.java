//TFG 2013-2014

package genCode;

/**
 *
 * @author Belen Bermejo Gonzalez
 */

/*Class to containt the random code options*/
public interface GenCodeConfig {

    final int nLines = GenCodeFile.getnLines();
    
    /*Probability of each kind of instructions*/    
    final double P_LOAD = GenCodeFile.getPload();
    final double P_STORE = GenCodeFile.getPstore();
    final double P_ALUIM = GenCodeFile.getPaluim();
    final double P_ALU = GenCodeFile.getPalu();
    final double P_SHIFT = GenCodeFile.getPshift();
    final double P_JUMP = GenCodeFile.getPjump();

    /*Number of branch points*/
    final int N_JUMPS_POINTS = GenCodeFile.getnJumpsPoints();

    /*Number of variables*/
    final int N_VARIABLES = GenCodeFile.getnVariables();

    /*Opcode instructions*/
    String load[] = {"LB", "LH", "LW"};
    String store[] = {"SB", "SH", "SW"};
    String aluIm[] = {"ADDI","ANDI", "ORI", "XORI"};
    String alu[] = {"ADD", "SUB", "SLT", "AND", "OR", "XOR", "NOR"};
    String shift[] = {"SLLV", "SRLV"};    
    String jump[] = {"J", "BEQ", "BNE", "BLEZ", "BGTZ", "BLTZ", "BGEZ"}; 

    /*Variable types*/
    String typeVar[] = {"word", "byte", "half"};

    /*Variable values*/
    String value[] = {"0b01110", "-25", "0xA2B", "0%234"};
    
    /*Labels*/
    String label[] = {"loop1", "loop2", "loop3", "loop4"};

}
