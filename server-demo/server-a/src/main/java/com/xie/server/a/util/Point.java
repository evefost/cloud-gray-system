package com.xie.server.a.util;

public class Point {

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
}