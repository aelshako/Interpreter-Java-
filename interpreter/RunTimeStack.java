package interpreter;

import java.util.ArrayList;
import java.util.Stack;

public class RunTimeStack {

    private ArrayList<Integer> runTimeStack;
    private Stack<Integer> framePointer;

    public RunTimeStack() {
        runTimeStack = new ArrayList<>();
        framePointer = new Stack<>();
        // Add initial Frame Pointer, main is the entry
        // point of our language, so its frame pointer is 0.
        framePointer.add(0);
    }


     void dump() {

        String str = "";    //this variable will eventually hold the dumped runtime stack

        for (int i = 0; i < framePointer.size(); i++) { //looping for the entire frame pointer stack
            str += "["; //starting a frame
            if (i == framePointer.size() - 1) {        //Do we have the last (top-most) element?
                for (int j = framePointer.peek(); j < (runTimeStack.size()); j++) { //loop from the first element in the frame to the last
                    str += runTimeStack.get(j);  //appending element
                    str += ",";                 //appending comma
                }

            } else {      //not top most element
                int dist = framePointer.get(i + 1) - framePointer.get(i); //this is used to tell us how far to go on the runtimestack( how many element are in the frame, so we can print them all)

                for (int m = framePointer.get(i); m < framePointer.get(i) + dist; m++) { //looping for the size of the current frame
                    str += runTimeStack.get(m); //appending the current element in the run time stack
                    str += ",";                 //appending comma
                }

            }

            if (str.charAt(str.length() - 1) == ',') {  //if the last element is a comma
                str = str.substring(0, str.length() - 1);  //reduce the size of the string by 1(essentially removes the comma)

            }
            str += "] ";    //we append closing brace with a space at the end of the outer-loop iteration(essentially, at the end of the frame)
        }

        System.out.println(str);    //printing the final string

    }


     int peek() {
        return runTimeStack.get(runTimeStack.size() - 1);
    }

     int pop() {
        int size_of_curr_frame = runTimeStack.size() - framePointer.peek();
        int top_stack = 0;
        if (size_of_curr_frame >= 1) {  //verifying that we have a frame big enough to pop(size >=1)
            top_stack = runTimeStack.get(runTimeStack.size() - 1);
            runTimeStack.remove(runTimeStack.size() - 1);
        }
        return top_stack;    //unwraps on its own
    }

     void mult_pop(int num_to_pop) {  //helper function to pop multiple elements from the run time stack(no error checking, because error checking is done before calling this function)
        for (int i = 0; i < num_to_pop; i++) {
            this.pop();
        }
    }

     int push(int push_val) {
        runTimeStack.add(push_val);
        return push_val;
    }


     void newFrameAt(int offset) { //offset denotes how many frames to go down from the top of the run time stack to assign a new frame
        framePointer.push(runTimeStack.size() - offset);
    }

     void popFrame() {    //removes the top frame of the run time stack
        int num_to_pop = runTimeStack.size() - framePointer.peek(); //number of elements in current frame
        int return_val = runTimeStack.get(runTimeStack.size() - 1);

        this.mult_pop(num_to_pop);  //emptying the frame
        this.framePointer.pop();
        runTimeStack.add(return_val); //pushing the return value onto the run time stack
    }

     int store(int offset) {  //offset is from the bottom of the run time stack frame
        int val_to_store = runTimeStack.get(runTimeStack.size() - 1);

        runTimeStack.remove(runTimeStack.size() - 1); //pop top of stack
        runTimeStack.set(offset + framePointer.peek(), val_to_store); //replace element at proper index

        return val_to_store;
    }

     int load(int offset) {    //offset is showing the offset from the start of the frame (bottom of frame/stack)
        int load_val = runTimeStack.get(framePointer.peek()) + offset;

        runTimeStack.add(load_val);
        return load_val;
    }

     Integer push(Integer val) {
        runTimeStack.add(val);
        return val;
    }

     int[] get_args() {        //this is a helper function to return an array holding the current arguments(used for getting the arguments in the CALL dumping line)
        final int num_args = (runTimeStack.size() - framePointer.peek());
        int[] arr = new int[num_args];

        for (int i = framePointer.peek(); i < runTimeStack.size(); i++) { //go through top frame on run time stack
            arr[i - framePointer.peek()] = runTimeStack.get(i); //set each element in the array
        }
        return arr;
    }
}
