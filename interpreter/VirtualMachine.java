package interpreter;


import java.util.Stack;

import interpreter.RunTimeStack;
import interpreter.bytecode.*;


public class VirtualMachine {

    private RunTimeStack runStack;
    private Stack<Integer> returnAddrs; //can only hold Integer objects
    private Program program;
    private int pc;
    private boolean dumping;    //this dump flag uses boolean instead of Boolean, because we don't need the additional functionality of the Boolean class
    private Boolean isRunning; // NOTE: I did not make a getter for this function because it is internal to the VM and doesn't need to be accessed outside the VM

    protected VirtualMachine(Program program) {
        this.program = program;
    }

    public void attemp_halt() {
        isRunning = false;
    }

    void executeProgram() { //package private because it is only called by the interpreter which resides in the same package

        pc = 0;
        int top_runstack = 0;
        runStack = new RunTimeStack();
        returnAddrs = new Stack<Integer>();
        isRunning = true;
        try {


            while (isRunning) {
                ByteCode code = program.getCode(pc);
                if (code instanceof StoreCode && dumping && ((StoreCode) code).getArguments().size() == 2) {
                    top_runstack = runStack.peek(); //this is done before executing the store code, because we can't access the previous value on the runStack after its been popped
                }


                code.execute(this);

                if (dumping) {
                    String temp_tostring = code.toString();

                    if (!(code instanceof StoreCode) && !(code instanceof WriteCode) && !(code instanceof ReturnCode && ((ReturnCode) code).getArguments().size() == 1) && !(code instanceof CallCode) && !(code instanceof DumpCode)) { //if this runs, we don't require special modifications for printing the dump statements
                        System.out.println(temp_tostring);
                    } else if (code instanceof StoreCode && ((StoreCode) code).getArguments().size() == 2) {
                        System.out.println(temp_tostring + top_runstack);
                    } else if (code instanceof ReturnCode) {       //if this runs, the code is a ReturnCode and its size is not 1, so it is 2(and we print the return value(top of runtime stack))
                        System.out.println(code.toString() + runStack.peek());
                    } else if (code instanceof WriteCode) {    //if its a WRITE code, we print WRITE then the top of the stack (on a new line)
                        System.out.println(temp_tostring); //printing the "WRITE" label *****NOTE: since we execute first, the value would show up on the previous line*****
                    } else if (code instanceof CallCode) {
                        int arr_arguments[] = this.getArgs();
                        temp_tostring += "(";
                        for (int i = 0; i < arr_arguments.length; i++) {  //this loop goes through the arguments, appends them, and prints a comma after each one
                            temp_tostring += arr_arguments[i];
                            temp_tostring += ",";
                        }
                        if (temp_tostring.charAt(temp_tostring.length() - 1) == ',') {      //removing trailing comma
                            temp_tostring = temp_tostring.substring(0, temp_tostring.length() - 1);
                        }
                        temp_tostring += ")";

                        System.out.println(temp_tostring);
                    }

                    runStack.dump();    //dumping run stack
                }

                pc++;
            }
        } catch (Exception x) {
            System.out.println("Exception in vm.executeProgram");
        }
    }


    public void dumping_off() {          //turns dumping off
        dumping = false;
    }

    public void dumping_on() {           //turns dumping on
        dumping = true;
    }

    public void set_PC(int new_pc_val) {   //setter for PC
        pc = new_pc_val;
    }

    public int get_PC() {                  //getter for PC
        return pc;
    }

    public int popRunStack() {
        return runStack.pop();
    }

    public void mult_popRunStack(int num_to_pop) {   //this function allows you to pop multiple values off the run time stack(it doesn't do error checking because we handle that before its called)
        runStack.mult_pop(num_to_pop);
    }

    public int pop_returnAddrs() {
        return returnAddrs.pop();
    }

    public void push_returnAddrs(int new_returnAddrs) {
        returnAddrs.push(new_returnAddrs);
    }

    public void pushRunStack(int push_val) {
        runStack.push(push_val);
    }

    public int peekRunStack() {
        return runStack.peek();
    }

    public void newFrameAt(int offset_val) {
        runStack.newFrameAt(offset_val);
    }

    public void popFrame() {
        runStack.popFrame();
    }

    public int Store(int offset_val) {
        return runStack.store(offset_val);
    }

    public int Load(int offset) {
        return runStack.load(offset);
    }

    public int[] getArgs() { //helper function to return an array of the current runtime stack(this is used in printing the arguments in the dump statement for functions)
        return runStack.get_args();
    }


}
