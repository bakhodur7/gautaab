package com.example.gautaabapp;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;
import java.util.ResourceBundle;
import com.example.gautaabapp.JSONParse;
import javafx.util.Duration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import static com.example.gautaabapp.JSONParse.jsonArrayKazSatellites;
import static java.lang.String.format;

public class Controller implements Initializable {
    @FXML
    private Label xLabel, yLabel, zLabel, vxLabel, vyLabel, vzLabel, warningLabel, bolPoluosValue;
    @FXML
    private TextField periodField, excentrField, naklonField, voshodUzelField, argPericentraField, srAnomaliyaField;
    @FXML
    private Button calculateButton, download_button;

    double periodValue, excentrValue, naklonValue, voshodUzelValue, argPericentraValue, srAnomaliyaValue;


    public void submit(ActionEvent event){

        calculateButton.arm();
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(e -> calculateButton.disarm());

        pause.setOnFinished(e -> {
            calculateButton.disarm();
            calculateButton.fire();
        });

        String periodString = periodField.getText();
        String excentrString = excentrField.getText();
        String naklonString = naklonField.getText();
        String voshodUzelString = voshodUzelField.getText();
        String argPericentraString = argPericentraField.getText();
        String srAnomaliyaString = srAnomaliyaField.getText();
        try{
            // Здесь значения в градусах (или км) дробные, как 286.0. То есть здесь начальные вводные значения
            periodValue = Double.parseDouble(periodString);
            excentrValue = Double.parseDouble(excentrString);
            naklonValue = Double.parseDouble(naklonString);
            voshodUzelValue = Double.parseDouble(voshodUzelString);
            argPericentraValue = Double.parseDouble(argPericentraString);
            srAnomaliyaValue = Double.parseDouble(srAnomaliyaString);

            warningLabel.setText("");
        } catch (NumberFormatException e){
            if (periodString == null || periodField.getText().trim().isEmpty() ||
                    excentrString == null || excentrField.getText().trim().isEmpty() ||
                    naklonString == null || naklonField.getText().trim().isEmpty() ||
                    voshodUzelString == null || voshodUzelField.getText().trim().isEmpty() ||
                    argPericentraString == null || argPericentraField.getText().trim().isEmpty() ||
                    srAnomaliyaString == null || srAnomaliyaField.getText().trim().isEmpty()) {
                warningLabel.setText("Заполните все поля, нецелые числа вводим только через точку: 560.98");
            }else{
                warningLabel.setText("Вводите только число, нецелые числа вводим только через точку: 560.98");
            }

        }catch (Exception e){
            warningLabel.setText(String.valueOf(e));
        }

        //Начинаем расчеты, сначала определим аргумент широты
//        double uArgShirot; // аргумент широты
//        uArgShirot = srAnomaliyaValue + argPericentraValue;
//
////        Проверить соответствует ли аргумент широты реальному, если нет, то отнять 180 или 360
////        if (uArgShirot>360){
////            uArgShirot = uArgShirot - 360;
////            uArgShirot = 360 - uArgShirot;
////        }
//        System.out.println("u = " + uArgShirot);

        //Вызываем метод для расчета x, y, z
        CalcOfPractica5 calcOfPractica5 = new CalcOfPractica5();


        ArrayList<Double> arrayOfGetxyz = new ArrayList<Double>();
        arrayOfGetxyz = calcOfPractica5.calcOfxyz(periodValue, excentrValue, srAnomaliyaValue,
                voshodUzelValue, argPericentraValue, naklonValue);

        xLabel.setText(String.valueOf(arrayOfGetxyz.get(0)));
        yLabel.setText(String.valueOf(arrayOfGetxyz.get(1)));
        zLabel.setText(String.valueOf(arrayOfGetxyz.get(2)));
        vxLabel.setText(String.valueOf(arrayOfGetxyz.get(3)));
        vyLabel.setText(String.valueOf(arrayOfGetxyz.get(4)));
        vzLabel.setText(String.valueOf(arrayOfGetxyz.get(5)));
        bolPoluosValue.setText(String.valueOf(arrayOfGetxyz.get(6)));


    }

    @FXML
    private Label longitudeValue, latitudeValue, altitudeAboveSeaValue;
    @FXML
    private ComboBox<String> nameOfLocation;

    @FXML
    DatePicker datePickerMainWindow;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        nameOfLocation.setItems(FXCollections.observableArrayList("Астана", "Алматы", "Байконур", "Туркестан"));
        nameOfLocation.setOnAction(this::choiseLocation);

        nameOfSatellite.setItems(FXCollections.observableArrayList("KAZEOSAT 1", "KAZEOSAT 2", "KAZSTSAT"));
        nameOfSatellite.setOnAction(this::choiseSatellite);

    }


    @FXML
    Label showDownloadResultLabel, showTLEAgeLabel, dataUpload, lastUploadDate;
    //кнопка загрузить новые ТЛЕ элементы
    public void downloadJSON_button(ActionEvent event) throws InterruptedException {


        String SATELLITE_URL = "https://celestrak.org/NORAD/elements/gp.php?GROUP=resource&FORMAT=json-pretty";
        // создаем URL из строки
        URL url = JSONParse.createUrl(SATELLITE_URL);

        // загружаем Json в виде Java строки
        String resultJson = JSONParse.parseUrl(url);

        JSONParse kazThreeSatellites = new JSONParse();
        kazThreeSatellites.parseCurrentSatelliteJson(resultJson);
        System.out.println(kazThreeSatellites.getJSONArray());



        //Write JSON file
        try (FileWriter file = new FileWriter("kazSatellitesList.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(kazThreeSatellites.getJSONArray().toJSONString());
            file.flush();
            dataUpload.setText("Данные успешно загружены");

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4), e ->{
                dataUpload.setText(null);
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void choiseLocation(ActionEvent event){
        String getValueOfComboBox = nameOfLocation.getSelectionModel().getSelectedItem().toString();
        System.out.println(getValueOfComboBox);

        if(getValueOfComboBox.equals("Астана")){
            longitudeValue.setText("71°26′45″ в.д.");
            latitudeValue.setText("51°10′48″ с.ш.");
            altitudeAboveSeaValue.setText("358 м");
            datePickerMainWindow.setValue(LocalDate.now());
        }else if(getValueOfComboBox.equals("Алматы")){
            longitudeValue.setText("76°55′42″ в.д.");
            latitudeValue.setText("43°15′24″ с.ш.");
            altitudeAboveSeaValue.setText("787 м");
            datePickerMainWindow.setValue(LocalDate.now());
        }else if(getValueOfComboBox.equals("Байконур")){
            longitudeValue.setText("63°19′00″ в.д.");
            latitudeValue.setText("45°37′00″ с.ш.");
            altitudeAboveSeaValue.setText("91 м");
            ZoneId  timezone = ZoneId.of("Asia/Qyzylorda");
            datePickerMainWindow.setValue(LocalDate.now(timezone));
        }else if(getValueOfComboBox.equals("Туркестан")){
            longitudeValue.setText("68°15′06″ в.д.");
            latitudeValue.setText("43°17′50″ с.ш.");
            altitudeAboveSeaValue.setText("214 м");
            datePickerMainWindow.setValue(LocalDate.now());
        }
    }

    @FXML
    private ComboBox<String> nameOfSatellite;

    @FXML
    private Label labelNameOfSatellite, labelNameOfSatelliteMain, norad_cat_id_label, object_id_label, epoch_label, bstar_label;

    @FXML
    private Label eccentricity_label, inclination_label, ra_of_ascnode_label, arg_of_pericenter_label, mean_anomaly_label;

    @FXML
    private Label xLabel1, yLabel1, zLabel1, vxLabel1, vyLabel1, vzLabel1, bol_poluos_label;

    public void choiseSatellite(ActionEvent event){

        String getValueOfComboBox = nameOfSatellite.getSelectionModel().getSelectedItem().toString();
        JSONParse satelliteInfo = new JSONParse();
        //Перевод из экспоненциальной формы записи типа 9.053E-4 в десятичную дробную как 0.0009053
        DecimalFormat formatterDoubleToDecimal = new DecimalFormat("0.0############");
        formatterDoubleToDecimal.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));

        //Парсим данные из файла JSON
        JSONArray jsonArrayOfKazSatellites = new JSONArray();

        try {
            jsonArrayOfKazSatellites = (JSONArray) new JSONParser().parse(new FileReader("kazSatellitesList.json"));

            System.out.println("в трай проверка 0 index = "+ jsonArrayOfKazSatellites.get(0));
            System.out.println("в трай проверка 1 index = "+ jsonArrayOfKazSatellites.get(1));
            System.out.println("в трай проверка 2 index = "+ jsonArrayOfKazSatellites.get(2));

        }catch (Exception e){
            e.printStackTrace();
        }

        CalcOfPractica5 keplerWindow = new CalcOfPractica5();




        if(getValueOfComboBox.equals("KAZEOSAT 1")){

            JSONObject choisenSatellite = (JSONObject) jsonArrayOfKazSatellites.get(0);

            String OBJECT_NAME = (String)choisenSatellite.get("OBJECT_NAME");
            long NORAD_CAT_ID = (long) choisenSatellite.get("NORAD_CAT_ID");
            String OBJECT_ID = (String) choisenSatellite.get("OBJECT_ID");
            String EPOCH = (String) choisenSatellite.get("EPOCH");
            double BSTAR = ((Number) choisenSatellite.get("BSTAR")).doubleValue();
            double MEAN_MOTION = (double) choisenSatellite.get("MEAN_MOTION");

            double ECCENTRICITY = (double) choisenSatellite.get("ECCENTRICITY");
            double INCLINATION = (double)  choisenSatellite.get("INCLINATION");
            double RA_OF_ASC_NODE = (double)  choisenSatellite.get("RA_OF_ASC_NODE");

            double ARG_OF_PERICENTER = (double)  choisenSatellite.get("ARG_OF_PERICENTER");
            double MEAN_ANOMALY = (double)  choisenSatellite.get("MEAN_ANOMALY");


            String ECCENTRICITY_toDec = formatterDoubleToDecimal.format(ECCENTRICITY);
            String MEAN_MOTION_toDec = formatterDoubleToDecimal.format(MEAN_MOTION);
            String BSTAR_toDec = formatterDoubleToDecimal.format(BSTAR);

            System.out.println("Название КА= " + OBJECT_NAME  + "\n" + "Эксцентриситет = " + ECCENTRICITY_toDec);


            //отображение СВЕЖЕСТИ загруженных данных
            String dayOfDownloadString = EPOCH.substring(0, 10);
            LocalDate dayOdDownload = LocalDate.parse(dayOfDownloadString);
            LocalDate today = LocalDate.now();
            System.out.println("День загрузки: " + dayOfDownloadString);
            String timeOfDownload = EPOCH.substring(11, 19);
            System.out.println("Время загрузки: " + timeOfDownload);
            double days = dayOdDownload.until(today, ChronoUnit.DAYS);
            lastUploadDate.setText( days + " дня назад.");
            if(days>2.9){
                String backgroundCss = String.format("-fx-text-fill: red;");
                lastUploadDate.setStyle(backgroundCss);
            }else{
                String backgroundCss = String.format("-fx-text-fill: white;");
                lastUploadDate.setStyle(backgroundCss);
            }



            labelNameOfSatellite.setText(OBJECT_NAME);
            labelNameOfSatelliteMain.setText(OBJECT_NAME);
            norad_cat_id_label.setText(Long.toString(NORAD_CAT_ID));
            object_id_label.setText(OBJECT_ID);
            epoch_label.setText(EPOCH);
            bstar_label.setText(Double.toString(BSTAR));

            eccentricity_label.setText(ECCENTRICITY_toDec);
            inclination_label.setText(Double.toString(INCLINATION));
            ra_of_ascnode_label.setText(Double.toString(RA_OF_ASC_NODE));
            arg_of_pericenter_label.setText(Double.toString(ARG_OF_PERICENTER));
            mean_anomaly_label.setText(Double.toString(MEAN_ANOMALY));

            //Для окна Кеплеровы элементы вычисляем xyz, Vx, Vy, Vz и большую полуось


            ArrayList<Double> arrayOfGetxyz_KeplerWindow = new ArrayList<Double>();
            arrayOfGetxyz_KeplerWindow = keplerWindow.calcOfxyz(MEAN_MOTION, ECCENTRICITY, MEAN_ANOMALY,
                    RA_OF_ASC_NODE, ARG_OF_PERICENTER, INCLINATION);

            xLabel1.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(0)));
            yLabel1.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(1)));
            zLabel1.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(2)));
            vxLabel1.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(3)));
            vyLabel1.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(4)));
            vzLabel1.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(5)));
            bol_poluos_label.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(6)));




        }else if(getValueOfComboBox.equals("KAZEOSAT 2")){
            System.out.println(jsonArrayOfKazSatellites.get(1));
            JSONObject choisenSatellite = (JSONObject) jsonArrayOfKazSatellites.get(1);
            String OBJECT_NAME = (String)choisenSatellite.get("OBJECT_NAME");
            long NORAD_CAT_ID = (long) choisenSatellite.get("NORAD_CAT_ID");
            String OBJECT_ID = (String) choisenSatellite.get("OBJECT_ID");
            String EPOCH = (String) choisenSatellite.get("EPOCH");
            double BSTAR = ((Number) choisenSatellite.get("BSTAR")).doubleValue();

            double ECCENTRICITY = (double) choisenSatellite.get("ECCENTRICITY");
            double INCLINATION = (double)  choisenSatellite.get("INCLINATION");
            double RA_OF_ASC_NODE = (double)  choisenSatellite.get("RA_OF_ASC_NODE");

            double ARG_OF_PERICENTER = (double)  choisenSatellite.get("ARG_OF_PERICENTER");
            double MEAN_ANOMALY = (double)  choisenSatellite.get("MEAN_ANOMALY");
            double MEAN_MOTION = (double) choisenSatellite.get("MEAN_MOTION");

            String ECCENTRICITY_toDec = formatterDoubleToDecimal.format(ECCENTRICITY);


            System.out.println("Название КА= " + OBJECT_NAME  + "\n" + "Эксцентриситет = " + ECCENTRICITY);

            //отображение СВЕЖЕСТИ загруженных данных
            String dayOfDownloadString = EPOCH.substring(0, 10);
            LocalDate dayOdDownload = LocalDate.parse(dayOfDownloadString);
            LocalDate today = LocalDate.now();
            System.out.println("День загрузки: " + dayOfDownloadString);
            String timeOfDownload = EPOCH.substring(11, 19);
            System.out.println("Время загрузки: " + timeOfDownload);
            double days = dayOdDownload.until(today, ChronoUnit.DAYS);
            lastUploadDate.setText( days + " дня назад.");
            if(days>2.9){
                String backgroundCss = String.format("-fx-text-fill: red;");
                lastUploadDate.setStyle(backgroundCss);
            }else{
                String backgroundCss = String.format("-fx-text-fill: white;");
                lastUploadDate.setStyle(backgroundCss);
            }

            labelNameOfSatellite.setText(OBJECT_NAME);
            labelNameOfSatelliteMain.setText(OBJECT_NAME);
            norad_cat_id_label.setText(Long.toString(NORAD_CAT_ID));
            object_id_label.setText(OBJECT_ID);
            epoch_label.setText(EPOCH);
            bstar_label.setText(Double.toString(BSTAR));

            eccentricity_label.setText(ECCENTRICITY_toDec);
            inclination_label.setText(Double.toString(INCLINATION));
            ra_of_ascnode_label.setText(Double.toString(RA_OF_ASC_NODE));
            arg_of_pericenter_label.setText(Double.toString(ARG_OF_PERICENTER));
            mean_anomaly_label.setText(Double.toString(MEAN_ANOMALY));

            //Для окна Кеплеровы элементы вычисляем xyz, Vx, Vy, Vz и большую полуось


            ArrayList<Double> arrayOfGetxyz_KeplerWindow = new ArrayList<Double>();
            arrayOfGetxyz_KeplerWindow = keplerWindow.calcOfxyz(MEAN_MOTION, ECCENTRICITY, MEAN_ANOMALY,
                    RA_OF_ASC_NODE, ARG_OF_PERICENTER, INCLINATION);

            xLabel1.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(0)));
            yLabel1.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(1)));
            zLabel1.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(2)));
            vxLabel1.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(3)));
            vyLabel1.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(4)));
            vzLabel1.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(5)));
            bol_poluos_label.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(6)));

        }else if(getValueOfComboBox.equals("KAZSTSAT")){
            System.out.println(jsonArrayOfKazSatellites.get(2));

            JSONObject choisenSatellite = (JSONObject) jsonArrayOfKazSatellites.get(2);
            String OBJECT_NAME = (String)choisenSatellite.get("OBJECT_NAME");
            long NORAD_CAT_ID = (long) choisenSatellite.get("NORAD_CAT_ID");
            String OBJECT_ID = (String) choisenSatellite.get("OBJECT_ID");
            String EPOCH = (String) choisenSatellite.get("EPOCH");
            double BSTAR = ((Number) choisenSatellite.get("BSTAR")).doubleValue();

            double ECCENTRICITY = (double) choisenSatellite.get("ECCENTRICITY");
            double INCLINATION = (double)  choisenSatellite.get("INCLINATION");
            double RA_OF_ASC_NODE = (double)  choisenSatellite.get("RA_OF_ASC_NODE");

            double ARG_OF_PERICENTER = (double)  choisenSatellite.get("ARG_OF_PERICENTER");
            double MEAN_ANOMALY = (double)  choisenSatellite.get("MEAN_ANOMALY");
            double MEAN_MOTION = (double) choisenSatellite.get("MEAN_MOTION");

            String ECCENTRICITY_toDec = formatterDoubleToDecimal.format(ECCENTRICITY);

            System.out.println("Название КА= " + OBJECT_NAME  + "\n" + "Эксцентриситет = " + ECCENTRICITY);

            //отображение СВЕЖЕСТИ загруженных данных
            String dayOfDownloadString = EPOCH.substring(0, 10);
            LocalDate dayOdDownload = LocalDate.parse(dayOfDownloadString);
            LocalDate today = LocalDate.now();
            System.out.println("День загрузки: " + dayOfDownloadString);
            String timeOfDownload = EPOCH.substring(11, 19);
            System.out.println("Время загрузки: " + timeOfDownload);
            double days = dayOdDownload.until(today, ChronoUnit.DAYS);
            lastUploadDate.setText( days + " дня назад.");

            if(days>2.9){
                String backgroundCss = String.format("-fx-text-fill: red;");
                lastUploadDate.setStyle(backgroundCss);
            }else{
                String backgroundCss = String.format("-fx-text-fill: white;");
                lastUploadDate.setStyle(backgroundCss);
            }

            labelNameOfSatellite.setText(OBJECT_NAME);
            labelNameOfSatelliteMain.setText(OBJECT_NAME);
            norad_cat_id_label.setText(Long.toString(NORAD_CAT_ID));
            object_id_label.setText(OBJECT_ID);
            epoch_label.setText(EPOCH);
            bstar_label.setText(Double.toString(BSTAR));

            eccentricity_label.setText(ECCENTRICITY_toDec);
            inclination_label.setText(Double.toString(INCLINATION));
            ra_of_ascnode_label.setText(Double.toString(RA_OF_ASC_NODE));
            arg_of_pericenter_label.setText(Double.toString(ARG_OF_PERICENTER));
            mean_anomaly_label.setText(Double.toString(MEAN_ANOMALY));

            //Для окна Кеплеровы элементы вычисляем xyz, Vx, Vy, Vz и большую полуось


            ArrayList<Double> arrayOfGetxyz_KeplerWindow = new ArrayList<Double>();
            arrayOfGetxyz_KeplerWindow = keplerWindow.calcOfxyz(MEAN_MOTION, ECCENTRICITY, MEAN_ANOMALY,
                    RA_OF_ASC_NODE, ARG_OF_PERICENTER, INCLINATION);

            xLabel1.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(0)));
            yLabel1.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(1)));
            zLabel1.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(2)));
            vxLabel1.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(3)));
            vyLabel1.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(4)));
            vzLabel1.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(5)));
            bol_poluos_label.setText(String.valueOf(arrayOfGetxyz_KeplerWindow.get(6)));
        }
    }

    @FXML
    private DateTimePickerConverter dateTimePickerConverter;

    @FXML
    private DatePicker dateTimePicker;



    @FXML
    private Label resultLabel;


}