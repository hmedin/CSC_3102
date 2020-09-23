/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package heap;

/**
 *
 * @author horaciomedina
 */
public class MinHeap {

    int[] array;
    int arraySize;
    int heapSize;

    public MinHeap(int arraySize) {
        heapSize = 0;
        array = new int[arraySize];

    }

    public boolean isFull() {
        return heapSize == array.length;
    }

    public boolean isEmpty() {
        return heapSize == 0;
    }

    public int extractMin() {
        if (isEmpty()) {
            System.out.print("EMPTY HEAP ");
            return -1;
        }
        heapSize--;
        int min = array[0];
        array[0] = array[heapSize];

        sinkDown(array, 0, heapSize);
        return min;
    }

    public int dereaseKey(int pos, int newkey) {
//change a key value residing at a specific position pos in the array
//return the new position of that key in the array
        if (pos >= heapSize) {
            System.out.println("KEY DOES NOT EXIST IN HEAP");
            return -1;
        }
        array[pos] = newkey;
        floatUp(array, pos, heapSize);
        while(array[pos]!=newkey){
            pos=(pos - 1) / 3;
        }
        return pos;
    }

    public void insert(int element) {
        if (isFull()) {
            System.out.println("HEAP FULL; ELEMENT " + element + " NOT ADDED");
            return;
        }
        array[heapSize] = element;
        floatUp(array, heapSize, heapSize++);
    }

    private void sinkDown(int numbers[], int root, int bottom) {
        /* sinkDown is  "heapify(u)" in lectures. */
        int temp, minChild, otherChild, thirdChild;

        //the block below finds the minimum child
        minChild = root * 3 + 1;
        if (minChild > bottom) {
            return;
        }
        if (minChild < bottom) {
            //prevents 
            if (minChild + 2 <= bottom) {
                otherChild = minChild + 1;
                thirdChild = minChild + 2;
                if (numbers[otherChild] < numbers[minChild] && numbers[otherChild] < numbers[thirdChild]) {
                    minChild = otherChild;
                } else if (numbers[thirdChild] < numbers[minChild] && numbers[thirdChild] < numbers[otherChild]) {
                    minChild = thirdChild;
                }
            } else if (minChild + 1 <= bottom) {
                otherChild = minChild + 1;
                if (numbers[otherChild] < numbers[minChild]) {
                    minChild = otherChild;
                }
            }
        }
        if (numbers[root] <= numbers[minChild]) {
            return;
            /*done - go no further*/
        }
        temp = numbers[root];
        numbers[root] = numbers[minChild];
        numbers[minChild] = temp;
        sinkDown(numbers, minChild, bottom);
    }

    private void floatUp(int array[], int pos, int nodeCount) {
//opposite version of sinkDown
        int temp, parent;
        //the block below finds the minimum child
        parent = (pos - 1) / 3;
        if (array[pos] >= array[parent]) {
            return;
            /*done - go no further*/
        }
        temp = array[pos];
        array[pos] = array[parent];
        array[parent] = temp;
        floatUp(array, parent, nodeCount);

    }

    public void printArray() {
        if (isEmpty()) {
            System.out.println("EMPTY HEAP NO ARRAY PRINTED");
            return;
        }
        for (int i = 0; i < heapSize; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println("");
    }

}
