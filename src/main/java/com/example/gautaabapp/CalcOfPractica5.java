package com.example.gautaabapp;

import java.util.ArrayList;

public class CalcOfPractica5 {

    public ArrayList calcOfxyz(double periodTSatellite, double excentrValue, double srAnomaliyaValue,
                               double voshodUzelValue, double argOfPericenter, double naklonValue){

        //Находим большую полуось
        double periodTSatellite_sec = 86400/periodTSatellite; //переводим в секунды,
        //periodTSatellite - вытаскиваем из JSON как пример "MEAN_MOTION":14.42011877 (погрешность - 0.05%).
        System.out.println("Период в секундах = " + periodTSatellite_sec);
        double mGravityConst = 398600.4415;
        double dvaTretih = 2.0/3.0;;
        System.out.println("Две третих = " + dvaTretih);
        double aBolPoluos = Math.pow(((periodTSatellite_sec*(Math.sqrt(mGravityConst)))/(2*Math.PI)), dvaTretih);
        System.out.println("Большая полуось = " + aBolPoluos);

        //Дальше находим оставшиеся 6 параметров
        double pFokalpar; // фокальный параметр
        pFokalpar = aBolPoluos*(1-(excentrValue*excentrValue)); //получим значение фокального параметра в км
        System.out.println("p = " + pFokalpar);

        double uArgShirot = srAnomaliyaValue + argOfPericenter;

        double rGeocentrRast; // геоцентрическое расстояние
        double srAnomaliyaRadian = Math.toRadians(srAnomaliyaValue); // Перевели в радиан значение средней аномалии
        rGeocentrRast = pFokalpar/(1+excentrValue*Math.cos(srAnomaliyaRadian));
        System.out.println("r = " + rGeocentrRast);

        //Расчет x, y, z
        double x, y, z;
        double voshodUzelRadian = Math.toRadians(voshodUzelValue);
        double uArgShirotRadian = Math.toRadians(uArgShirot);
        double naklonRadian = Math.toRadians(naklonValue);

        x = rGeocentrRast*((Math.cos(voshodUzelRadian))*(Math.cos(uArgShirotRadian)) -
                (Math.sin(voshodUzelRadian))*(Math.sin(uArgShirotRadian))*(Math.cos(naklonRadian)));
        y = rGeocentrRast*(Math.sin(voshodUzelRadian)*Math.cos(uArgShirotRadian) +
                Math.cos(voshodUzelRadian)*Math.sin(uArgShirotRadian)*Math.cos(naklonRadian));
        z = rGeocentrRast*(Math.sin(uArgShirotRadian))*(Math.sin(naklonRadian));

        System.out.println("x= "+ x + " y= "+ y + " z= " + z);

        //Оставляем число с 3 знаками после запятой
        x = (Double)Math.floor(x*100000)/100000.0;
        y = (Double)Math.floor(y*100000)/100000.0;
        z = (Double)Math.floor(z*100000)/100000.0;

        double gravityParameter = 398600.4415;

        //радиальный и трансверсиальные компоненты вектора скорости
        double radialComponent = (Math.sqrt(gravityParameter/pFokalpar))*excentrValue*Math.sin(srAnomaliyaRadian);
        double transversComponent = (Math.sqrt(gravityParameter/pFokalpar))*(1+(excentrValue*Math.cos(srAnomaliyaRadian)));
        System.out.println("v radial= "+ radialComponent + " v trans= " + transversComponent);

        //Расчет компонентов вектора скорости
        double vx, vy, vz;
        vx = radialComponent*(Math.cos(voshodUzelRadian)*Math.cos(uArgShirotRadian) -
                Math.sin(voshodUzelRadian)*Math.sin(uArgShirotRadian)*Math.cos(naklonRadian)) - transversComponent*
                (Math.cos(voshodUzelRadian)*Math.sin(uArgShirotRadian) +
                Math.sin(voshodUzelRadian)*Math.cos(uArgShirotRadian)*Math.cos(naklonRadian));

        vy = radialComponent*(Math.sin(voshodUzelRadian)*Math.cos(uArgShirotRadian) +
                Math.cos(voshodUzelRadian)*Math.sin(uArgShirotRadian)*Math.cos(naklonRadian)) -
                transversComponent*(Math.sin(voshodUzelRadian)*Math.sin(uArgShirotRadian) -
                        Math.cos(voshodUzelRadian)*Math.cos(uArgShirotRadian)*Math.cos(naklonRadian));

        vz = radialComponent*(Math.sin(uArgShirotRadian)*Math.sin(naklonRadian)) + transversComponent*
                (Math.cos(uArgShirotRadian)*Math.sin(naklonRadian));
        vx = (Double)Math.floor(vx*10000000)/10000000.0;
        vy = (Double)Math.floor(vy*10000000)/10000000.0;
        vz = (Double)Math.floor(vz*10000000)/10000000.0;




        ArrayList<Double> arrayOfxyz = new ArrayList<Double>();
        arrayOfxyz.add(x);
        arrayOfxyz.add(y);
        arrayOfxyz.add(z);
        arrayOfxyz.add(vx);
        arrayOfxyz.add(vy);
        arrayOfxyz.add(vz);
        arrayOfxyz.add(aBolPoluos);

        return arrayOfxyz;
    }



}
