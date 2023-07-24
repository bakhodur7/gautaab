package com.example.gautaabapp;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONParse {

    /**
     * Метод для получения данных по указанной ссылке
     *
     * @param url - ссылка в виде объекта URL (Uniform Resource Locator)
     * @return содержимое страницы на указанной ссылке в @param url
     */
    public static String parseUrl(URL url) {
        if (url == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        // открываем соедиение к указанному URL
        // помощью конструкции try-with-resources
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {

            String inputLine;
            // построчно считываем результат в объект StringBuilder
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
                //System.out.println(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    String OBJECT_NAME = "KAZEOSAT 1";
    String OBJECT_ID = "2014-024A";
    String EPOCH = "2023-06-16T02:00:11.389824";
    double MEAN_MOTION = 14.42007367;
    double ECCENTRICITY = 0.0001173;
    double INCLINATION = 98.4292;
    double RA_OF_ASC_NODE = 243.8116;
    double ARG_OF_PERICENTER = 96.7421;
    double MEAN_ANOMALY = 263.3901;
    double EPHEMERIS_TYPE = 0;
    String CLASSIFICATION_TYPE = "U";
    long NORAD_CAT_ID = 39731;
    long ELEMENT_SET_NO = 999;
    long REV_AT_EPOCH = 48049;
    double BSTAR = 9.0364e-5;
    double MEAN_MOTION_DOT = 2.53e-6;
    double MEAN_MOTION_DDOT = 0;
    public static JSONArray jsonArrayKazSatellites = new JSONArray();

    // парсим некоторые данные о КА
    public static void parseCurrentSatelliteJson(String resultJson) {

        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(resultJson);

            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;

                //Это все объекты, которые мы извлекаем из массива Json

                String OBJECT_NAME = (String) jsonObject.get("OBJECT_NAME");
                String OBJECT_ID = (String) jsonObject.get("OBJECT_ID");
                String EPOCH = (String) jsonObject.get("EPOCH");
                double MEAN_MOTION = (double) jsonObject.get("MEAN_MOTION");
                double ECCENTRICITY = (double) jsonObject.get("ECCENTRICITY");
                double INCLINATION = ((Number) jsonObject.get("INCLINATION")).doubleValue();
                double RA_OF_ASC_NODE = (double) jsonObject.get("RA_OF_ASC_NODE");
                double ARG_OF_PERICENTER = (double) jsonObject.get("ARG_OF_PERICENTER");
                double MEAN_ANOMALY = (double) jsonObject.get("MEAN_ANOMALY");
                long EPHEMERIS_TYPE = (long) jsonObject.get("EPHEMERIS_TYPE");
                String CLASSIFICATION_TYPE = (String) jsonObject.get("CLASSIFICATION_TYPE");
                long NORAD_CAT_ID = (long) jsonObject.get("NORAD_CAT_ID");
                long ELEMENT_SET_NO = (long) jsonObject.get("ELEMENT_SET_NO");
                long REV_AT_EPOCH = (long) jsonObject.get("REV_AT_EPOCH");

                double BSTAR = ((Number) jsonObject.get("BSTAR")).doubleValue();
                double MEAN_MOTION_DOT = (double) jsonObject.get("MEAN_MOTION_DOT");
                double MEAN_MOTION_DDOT = ((Number) jsonObject.get("MEAN_MOTION_DDOT")).doubleValue();



                if(OBJECT_NAME.contains("KAZ")){
                    jsonArrayKazSatellites.add(obj);
                    System.out.println("Название КА= " + OBJECT_NAME + " Средняя аномалия= " + MEAN_MOTION +
                            " OBJECT_ID = " + OBJECT_ID);
                    System.out.println("Эпоха = " + EPOCH);
                    System.out.println("Эксцентриситет = " + ECCENTRICITY + "\n" + "Наклонение = " );
                }
            }

//            System.out.println("Полученный JSON:\n" + resultJson);
//            // парсим полученный JSON и печатаем его на экран
//            JSONUtils.parseCurrentWeatherJson(resultJson);

//            // формируем новый JSON объект из нужных нам погодных данных
//            String json = JSONUtils.buildWeatherJson();
//            System.out.println("Созданный нами JSON:\n" + json);
        }catch (ParseException e){
            e.printStackTrace();
        }

    }
    public JSONArray getJSONArray(){
        return jsonArrayKazSatellites;
    }

    // формируем новый JSON объект из нужных нам погодных данных
//    public static String buildWeatherJson() {
//        // для простоты примера просто хардкодим нужные данные в методе
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("name", "Лондон");
//        jsonObject.put("main", "Солнечно");
//        jsonObject.put("description", "Мороз трескучий, На небе ни единой тучи");
//
//        return jsonObject.toJSONString();
//    }

    // создаем объект URL из указанной в параметре строки
    public static URL createUrl(String link) {
        try {
            return new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
