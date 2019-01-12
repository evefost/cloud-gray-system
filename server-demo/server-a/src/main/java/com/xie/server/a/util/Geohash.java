package com.xie.server.a.util;

import com.alibaba.fastjson.JSON;
import java.util.BitSet;
import java.util.HashMap;

/**
 * 可以看出，当geohash base32编码长度为8时，精度在19米左右，
 * 而当编码长度为9时，精度在2米左右，编码长度需要根据数据情况进行选择。
 */

public class Geohash {

    /**
     * 米/度
     */
    public static final double lonUnit=0.00001141;

    /**
     * 米/度
     */
    public static final double latUnit=0.00000899;
    /**
     * 赤道半径(单位m)
     */
    private static final double EARTH_RADIUS = 6371000;
    private static int numbits = 6 * 5;

    final static char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    final static HashMap<Character, Integer> lookup = new HashMap<Character, Integer>();

    static {
        int i = 0;
        for (char c : digits)
            lookup.put(c, i++);
    }

    public static void main(String[] args) throws Exception {

        System.out.println(new Geohash().encode(125, 45));
        System.out.println(JSON.toJSON(new Geohash().decode(new Geohash().encode(125, 45))));
        System.out.println(new Geohash().encode(125.5, 45));

    }

    public double[] decode(String geohash) {
        StringBuilder buffer = new StringBuilder();
        for (char c : geohash.toCharArray()) {

            int i = lookup.get(c) + 32;
            buffer.append(Integer.toString(i, 2).substring(1));
        }

        BitSet lonset = new BitSet();
        BitSet latset = new BitSet();

        //even bits  
        int j = 0;
        for (int i = 0; i < numbits * 2; i += 2) {
            boolean isSet = false;
            if (i < buffer.length())
                isSet = buffer.charAt(i) == '1';
            lonset.set(j++, isSet);
        }

        //odd bits  
        j = 0;
        for (int i = 1; i < numbits * 2; i += 2) {
            boolean isSet = false;
            if (i < buffer.length())
                isSet = buffer.charAt(i) == '1';
            latset.set(j++, isSet);
        }

        double lon = decode(lonset, -180, 180);
        double lat = decode(latset, -90, 90);

        return new double[]{lat, lon};
    }

    private double decode(BitSet bs, double floor, double ceiling) {
        double mid = 0;
        for (int i = 0; i < bs.length(); i++) {
            mid = (floor + ceiling) / 2;
            if (bs.get(i))
                floor = mid;
            else
                ceiling = mid;
        }
        return mid;
    }


    public String encode(double lon,double lat) {
        BitSet latbits = getBits(lat, -90, 90);
        BitSet lonbits = getBits(lon, -180, 180);
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < numbits; i++) {
            buffer.append((lonbits.get(i)) ? '1' : '0');
            buffer.append((latbits.get(i)) ? '1' : '0');
        }
        return base32(Long.parseLong(buffer.toString(), 2));
    }

    private BitSet getBits(double lat, double floor, double ceiling) {
        BitSet buffer = new BitSet(numbits);
        for (int i = 0; i < numbits; i++) {
            double mid = (floor + ceiling) / 2;
            if (lat >= mid) {
                buffer.set(i);
                floor = mid;
            } else {
                ceiling = mid;
            }
        }
        return buffer;
    }

    public static String base32(long i) {
        char[] buf = new char[65];
        int charPos = 64;
        boolean negative = (i < 0);
        if (!negative)
            i = -i;
        while (i <= -32) {
            buf[charPos--] = digits[(int) (-(i % 32))];
            i /= 32;
        }
        buf[charPos] = digits[(int) (-i)];

        if (negative)
            buf[--charPos] = '-';
        return new String(buf, charPos, (65 - charPos));
    }



    /**
     * 转化为弧度(rad)
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double getDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }










}
