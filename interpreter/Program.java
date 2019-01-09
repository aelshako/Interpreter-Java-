package interpreter;

import java.util.ArrayList;

import interpreter.bytecode.*;

import java.util.HashMap;

public class Program {

    private ArrayList<ByteCode> program;

    public Program() {

        program = new ArrayList<ByteCode>();

    }

    protected ByteCode getCode(int pc) {

        return this.program.get(pc);

    }

    protected void addCode(ByteCode bc_post_initialization) {    //this helper function allows us to add fully INITILIAZED bytecodes to the Array List
        program.add(bc_post_initialization);
    }

    public int getSize() {

        return this.program.size();
    }

    /**
     * This function  goes through the program and resolves all addresses.
     * Previously all labels look like LABEL <<num>>>, but here they are converted into
     * correct addresses so the VirtualMachine knows what to set the Program Counter(PC)
     */
    public void resolveAddrs() {

        HashMap<String, Integer> label_to_line = new HashMap<>();  //this hash map stores the labels(keys) to the line numbers(values)

        for (int i = 0; i < this.program.size(); i++) {   //on each iteration, we loop through and get one element from the array list of initialized bytecodes
            ByteCode bc = program.get(i);   //retrieving the ith bytecode from the array list of bytecodes
            if (bc instanceof LabelCode) {    //is that bytecode an instance of LabelCode class?
                label_to_line.put(((LabelCode) bc).getArguments().get(0), i);  //adding first argument as key, and line number as value
            }
        }

        for (int i = 0; i < this.program.size(); i++) {
            ByteCode bc = program.get(i);
            if ((bc instanceof FalseBranchCode)) {
                int val = label_to_line.get(((FalseBranchCode) bc).getArguments().get(0));  //this holds the line value for the FalseBranchCode's destination address
                ((FalseBranchCode) bc).setDestination_addr(val);
            } else if (bc instanceof GotoCode) {
                int val = label_to_line.get(((GotoCode) bc).getArguments().get(0));        //this holds the line value for the GotoCode's destination address
                ((GotoCode) bc).setDestination_addr(val);
            } else if (bc instanceof CallCode) {
                int val = label_to_line.get(((CallCode) bc).getArguments().get(0));        //this holds the line value for the CallCode's destination address
                ((CallCode) bc).setDestination_addr(val);
            }

        }


    }


}
