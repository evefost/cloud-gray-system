package com.xie.server.a.util;

public  class Circle {

        private static final double PI = 3.14f;
        private double mPointX, mPointY;
        private double mRadius;
        public Circle(double x, double y, double radius){
            mPointX = x;
            mPointY = y;
            mRadius = radius;
        }

        public Point computeCoordinates(double angle) {
            return new Point(mPointX+ (double)(mRadius * Math.cos(angle*Math.PI/180)),
                mPointY+ (double)(mRadius * Math.sin(angle*Math.PI/180)));
        }

    }
