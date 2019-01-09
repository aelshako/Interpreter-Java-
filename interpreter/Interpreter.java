package interpreter;

import java.io.*;

/**
 * <pre>
 *     Interpreter class runs the interpreter:
 *     1. Perform all initializations
 *     2. Load the bytecodes from file
 *     3. Run the virtual machine
 *
 *     THIS FILE CANNOT BE MODIFIED. DO NOT
 *     LET ANY EXCETIONS PROPAGATE TO THE
 *     INTERPRETER CLASS. ONLY EXCEPTION OT THIS RULE IS
 * 	   BYTECODELOADER CONSTRUCTOR WHICH IS
 *     ALREADY IMPLEMENTED.
 * </pre>
 */
public class Interpreter {

    private ByteCodeLoader bcl;

    public Interpreter(String codeFile) {
        try {
            CodeTable.init();
            bcl = new ByteCodeLoader(codeFile);
        } catch (IOException e) {
            System.out.println("**** " + e);
        }
    }

    void run() {
        Program program = bcl.loadCodes();    //loading the codes (recall that loadCodes also calls the resolve address)
        VirtualMachine vm = new VirtualMachine(program); //making a vm
        vm.executeProgram();    //executing the program
    }

    public static void main(String args[]) {

        if (args.length == 0) {
            System.out.println("***Incorrect usage, try: java interpreter.Interpreter <file>");
            System.exit(1);
        }
        (new Interpreter(args[0])).run();
    }
}