package interpreter.bytecode;

import interpreter.VirtualMachine;

import java.util.ArrayList;

public class HaltCode extends ByteCode {
    private ArrayList<String> arguments;

    public void init(ArrayList<String> args) {
        arguments = args;
    }

    public void execute(VirtualMachine vm) {
        vm.attemp_halt(); //attempts to halt(the attempt_halt function in the VM sets isRunning to false)
    }

    public ArrayList<String> getArguments() {
        return arguments;
    }

    public String toString() {
        return "HALT";
    }

}
