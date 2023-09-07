package com.graphapp;

import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import java.util.ArrayList;

class graphVertex2D extends Ellipse {
    private ArrayList<graphVertex2D> neighbours = new ArrayList<>();
    private boolean Colorable = true;
    private int colorId = 0;


    public graphVertex2D(ArrayList<graphVertex2D> aNeighbours) {
        super();
        if (aNeighbours != null) {
            this.neighbours = aNeighbours;
        }
    }


    @Override
    public String toString() {
        for (int i = 0; i < neighbours.size(); i++) {
            System.out.println(neighbours.get(i).getId());
        }
        return super.getCenterX() + " " + super.getCenterY();
    }


    public void addNeighbour(graphVertex2D new_element) {
        if (!neighbours.contains(new_element)) {
            neighbours.add(new_element);
        }
    }


    public ArrayList<graphVertex2D> getNeighbours() {
        return neighbours;
    }


    public void setColorable(boolean isColorable){
        this.Colorable = isColorable;
    }


    public boolean isColorable() {
        return Colorable;
    }


    public void setColorId(int colorId) {
        this.colorId = colorId;
    }


    public int getColorId() {
        return colorId;
    }
}