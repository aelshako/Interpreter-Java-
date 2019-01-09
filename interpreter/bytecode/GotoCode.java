package interpreter.bytecode;

import interpreter.VirtualMachine;

import java.util.ArrayList;

public class GotoCode extends ByteCode {
    private ArrayList<String> arguments;
    private int destination_addr; // NOTE: this variable doesn't have an accessor, because it only needs to be accessed in this class


    public void init(ArrayList<String> args) {
        arguments = args;
    }

    public void execute(VirtualMachine vm) {
        vm.set_PC(destination_addr - 1); //we jump to the proper line number(offset by 1 because we want to execute/display the label as well)
    }

    public ArrayList<String> getArguments() {
        return arguments;
    }

    public void setDestination_addr(int dest) {
        destination_addr = dest;
    }

    public String toString() {
        return ( "GOTO" + " " + arguments.get(0));
    }
}
