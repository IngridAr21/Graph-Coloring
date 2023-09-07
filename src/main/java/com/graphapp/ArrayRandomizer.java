package com.graphapp;

import java.util.Random;

public class ArrayRandomizer {

    /**
     *
     * @param length Length of the array
     * @return Returns an array containing integers from 0 to length - 1 in a random order;
     */
    public int[] getRandomArray(int length){

        int[] array = new int[length];

        for(int i = 0; i < length; i++){
            array[i] = i;
        }

        Random rand = new Random(System.nanoTime());

        for(int i = array.length - 1; i > 0 ; i--){
            int newIndex = rand.nextInt(length);
            int temp = array[newIndex];
            array[newIndex] = array[i];
            array[i] = temp;
        }
        return array;
    }
}