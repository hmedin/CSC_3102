/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package heap;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Heap {

    public static void main(String[] args) throws FileNotFoundException {
        //used to denote the max heap size and num of instructions. 
        //if all the instrcutions are IN then the size of the heap would be the same number
        int maxHeapSize = 0;
        int min=0;
        File input = new File("inputFile.txt");
        Scanner in = new Scanner(input);
        if (in.hasNextLine()) {
            maxHeapSize = Integer.parseInt(in.nextLine());
        }
        System.out.println(maxHeapSize);
        MinHeap heap = new MinHeap(maxHeapSize);
        while (maxHeapSize > 0 && in.hasNextLine()) {
            String instructions = in.nextLine();
            String[] commands = instructions.split(" ");
            if (commands[0].equals("IN")) {
                heap.insert(Integer.parseInt(commands[1]));
            } else if (commands[0].equals("DK")) {
                heap.dereaseKey(Integer.parseInt(commands[1]), Integer.parseInt(commands[2]));
            } else if (commands[0].equals("EM")) {
                min = heap.extractMin();
            }
            maxHeapSize--;
        }
        System.out.println(min);

    }

}
