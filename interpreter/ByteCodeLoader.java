
package interpreter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;

import interpreter.bytecode.*;


public class ByteCodeLoader {

    private BufferedReader byteSource;


    public ByteCodeLoader(String file) throws IOException {
        this.byteSource = new BufferedReader(new FileReader(file));
    }

    /**
     * This function reads one line of source code at a time.
     * For each line it :
     * Tokenizes the string to break it into parts.
     * Grabs THE correct class name for the given ByteCode from CodeTable
     * Creates an instance of the ByteCode class name returned from code table.
     * Parses any additional arguments for the given ByteCode and sends them to
     * the newly created ByteCode instance via the init function.
     */
    public Program loadCodes() {

        Program prog = new Program();
        String line;
        try {
            while ((line = byteSource.readLine()) != null) { //read the line, and loop for as long as the line is not null
                StringTokenizer st = new StringTokenizer(line); //Making a tokenizer to operate on the line
                String bytecode_name = st.nextToken(" ");   //getting the bytecode(since first field is always the bytecode)
                String bytecode_classname = CodeTable.getClassName(bytecode_name);   //getting the correct class name from the CodeTable
                Class c = Class.forName("interpreter.bytecode." + bytecode_classname);
                ByteCode bc = (ByteCode) c.getDeclaredConstructor().newInstance();  //making instance of the proper subclass(ie. LitCode)
                ArrayList<String> args = new ArrayList<>(); //Array List of arguments(Strings)
                while (st.hasMoreTokens()) {      //while more tokens(arguments) exist
                    args.add(st.nextToken());
                }
                bc.init(args);  //passing in args to the init function
                prog.addCode(bc);   //adding the fully initialized code
            }

        } catch (Exception x) {    //if an exception occurs, we catch it and don't do anything (for now)

        }

        prog.resolveAddrs(); //request that all addresses be resolved

        return prog; //returning the program prog
    }


}


