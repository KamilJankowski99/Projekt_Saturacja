package saturacja;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

//javafx wymaga klasy rozszerzajacej klase Application 
public class Main extends Application {

    //suwaczki dla przestrzeni barw HSL i RGB
    final Slider hueSlider = new Slider(-360, 360, 0);
    final Slider saturationSlider = new Slider(-1, 1, 0);
    final Slider lightnessSlider = new Slider(-1, 1, 0);
    final Slider redSlider = new Slider(-255, 255, 0);
    final Slider greenSlider = new Slider(-255, 255, 0);
    final Slider blueSlider = new Slider(-255, 255, 0);

    //obrazek do obrobki - na razie statycznie
    Image image = new Image(getClass().getResourceAsStream("rainbow.jpg"));
    Image imageOriginal = new Image(getClass().getResourceAsStream("rainbow.jpg"));

    final FileChooser fileChooser = new FileChooser();
    final Button openButton = new Button("Otwarcie obrazka");
    final Button saveButton = new Button("Zapisanie obrazka");
    private Desktop desktop = Desktop.getDesktop();

    //labelki identyfikujace obrazki
    final Label flagOriginal = new Label("Oryginalny obrazek:");
    final Label flagChange = new Label("Obrazek ze zmianami w przestrzeni barw HSL lub RGB:");

    //labalki z nazwami suwakow
    final Label hueCaption = new Label("(HSL)Barwa:");
    final Label saturationCaption = new Label("(HSL)Saturacja:");
    final Label lightnessCaption = new Label("(HSL)Jasnosc:");
    final Label redCaption = new Label("(RGB)Czerwony:");
    final Label greenCaption = new Label("(RGB)Zielony:");
    final Label blueCaption = new Label("(RGB)Niebieski:");

    //odczyt wartosci suwaczka pokazany w GUI
    final Label hueValue = new Label(
            Double.toString(hueSlider.getValue()));
    final Label saturationValue = new Label(
            Double.toString(saturationSlider.getValue()));
    final Label lightnessValue = new Label(
            Double.toString(lightnessSlider.getValue()));
    final Label redValue = new Label(
            Double.toString(redSlider.getValue()));
    final Label greenValue = new Label(
            Double.toString(greenSlider.getValue()));
    final Label blueValue = new Label(
            Double.toString(blueSlider.getValue()));

    //kolor dla tekstu
    final static Color textColor = Color.BLACK;

    //efekt kolorystyczny - na razie gotowiec, do zastapienia wlasnym
    final static ColorAdjust colorEffect = new ColorAdjust();
    final static JOCLImageEffectsRGB joclEffectRGB = new JOCLImageEffectsRGB();
    final static JOCLImageEffectsHSL joclEffectHSL = new JOCLImageEffectsHSL();

    @Override

    public void start(Stage stage) {
        //setup sceny, dodanie obrazkow itd
        Group root = new Group();
        Scene scene = new Scene(root, image.getWidth() * 2 + 30, image.getHeight() + 150);
        stage.setScene(scene);
        stage.setTitle("Systemy Rozproszone - program nr 2, Kamil Jankowski");
        scene.setFill(Color.RED);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);

        ImageView imageview = new ImageView(image);
        ImageView imageviewOriginal = new ImageView(imageOriginal);

        imageview.setEffect(colorEffect);

        GridPane.setConstraints(imageview, 0, 1);
        GridPane.setConstraints(imageviewOriginal, 3, 1);
        GridPane.setConstraints(flagChange, 0, 0);
        grid.getChildren().add(flagChange);
        flagChange.setTextFill(textColor);
        GridPane.setConstraints(flagOriginal, 3, 0);
        grid.getChildren().add(flagOriginal);
        flagOriginal.setTextFill(textColor);
        GridPane.setColumnSpan(imageview, 3);
        GridPane.setColumnSpan(imageviewOriginal, 3);
        grid.getChildren().add(imageview);
        grid.getChildren().add(imageviewOriginal);
        scene.setRoot(grid);

        //rozmieszczenie labelek, suwaczkow i wartosci 
        GridPane.setConstraints(hueCaption, 0, 2);
        GridPane.setConstraints(hueSlider, 1, 2);
        GridPane.setConstraints(hueValue, 2, 2);
        GridPane.setConstraints(saturationCaption, 0, 3);
        GridPane.setConstraints(saturationSlider, 1, 3);
        GridPane.setConstraints(saturationValue, 2, 3);
        GridPane.setConstraints(lightnessCaption, 0, 4);
        GridPane.setConstraints(lightnessSlider, 1, 4);
        GridPane.setConstraints(lightnessValue, 2, 4);
        GridPane.setConstraints(redCaption, 3, 2);
        GridPane.setConstraints(redSlider, 4, 2);
        GridPane.setConstraints(redValue, 5, 2);
        GridPane.setConstraints(greenCaption, 3, 3);
        GridPane.setConstraints(greenSlider, 4, 3);
        GridPane.setConstraints(greenValue, 5, 3);
        GridPane.setConstraints(blueCaption, 3, 4);
        GridPane.setConstraints(blueSlider, 4, 4);
        GridPane.setConstraints(blueValue, 5, 4);
        GridPane.setConstraints(openButton, 5, 0);
        GridPane.setConstraints(saveButton, 2, 0);

        //dodanie elementow GUI do siatki
        grid.getChildren().add(hueCaption);
        grid.getChildren().add(hueSlider);
        grid.getChildren().add(hueValue);
        grid.getChildren().add(saturationCaption);
        grid.getChildren().add(saturationSlider);
        grid.getChildren().add(saturationValue);
        grid.getChildren().add(lightnessCaption);
        grid.getChildren().add(lightnessSlider);
        grid.getChildren().add(lightnessValue);
        grid.getChildren().add(redCaption);
        grid.getChildren().add(redSlider);
        grid.getChildren().add(redValue);
        grid.getChildren().add(greenCaption);
        grid.getChildren().add(greenSlider);
        grid.getChildren().add(greenValue);
        grid.getChildren().add(blueCaption);
        grid.getChildren().add(blueSlider);
        grid.getChildren().add(blueValue);
        grid.getChildren().add(openButton);
        grid.getChildren().add(saveButton);

        //kolorki tekstu na wczesniej ustawiony default
        hueCaption.setTextFill(textColor);
        hueValue.setTextFill(textColor);
        saturationCaption.setTextFill(textColor);
        saturationValue.setTextFill(textColor);
        lightnessCaption.setTextFill(textColor);
        lightnessValue.setTextFill(textColor);
        redCaption.setTextFill(textColor);
        redValue.setTextFill(textColor);
        greenCaption.setTextFill(textColor);
        greenValue.setTextFill(textColor);
        blueCaption.setTextFill(textColor);
        blueValue.setTextFill(textColor);

        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {

                    imageview.setImage(new Image("file:" + file.getAbsolutePath()));
                    imageviewOriginal.setImage(new Image("file:" + file.getAbsolutePath()));
                }
            }
        });
        saveButton.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Image");
                File file = fileChooser.showSaveDialog(stage);
                if (file != null) {
                    try {
                        BufferedImage outputImage = SwingFXUtils.fromFXImage(image, null);
                        int imageSizeX = outputImage.getWidth();
                        int imageSizeY = outputImage.getHeight();
                        int[] pixels = outputImage.getRGB(0, 0, imageSizeX, imageSizeY, null, 0, imageSizeX);
                        BufferedImage bufferedImageNoAlphaChannel = new BufferedImage(imageSizeX, imageSizeY, BufferedImage.OPAQUE);
                        bufferedImageNoAlphaChannel.setRGB(0, 0, imageSizeX, imageSizeY, pixels, 0, imageSizeX);
                        ImageIO.write(bufferedImageNoAlphaChannel, "jpg", file);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });

        //sledzenie zmian suwaczkow, aktualizacja wyswietlanej wartosci i aplikowanie efektu
        hueSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                float deltaHue = new_val.floatValue() - old_val.floatValue();
                image = joclEffectHSL.changeImageComponents(deltaHue, 0, 0, image);
                imageview.setImage(image);
                hueValue.setText(String.format("%.2f", new_val));
            }
        });

        saturationSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                float deltaSaturation = new_val.floatValue() - old_val.floatValue();
                image = joclEffectHSL.changeImageComponents(0, deltaSaturation, 0, image);
                imageview.setImage(image);
                saturationValue.setText(String.format("%.2f", new_val));
            }
        });

        lightnessSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                float deltaLightness = new_val.floatValue() - old_val.floatValue();
                image = joclEffectHSL.changeImageComponents(0, 0, deltaLightness, image);
                imageview.setImage(image);
                lightnessValue.setText(String.format("%.2f", new_val));
            }
        });

        redSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                float deltaRed = new_val.intValue() - old_val.intValue();
                image = joclEffectRGB.changeImageComponents(deltaRed, 0, 0, image);
                imageview.setImage(image);
                redValue.setText(String.format("%.2f", new_val));
            }
        });

        greenSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                float deltaGreen = new_val.intValue() - old_val.intValue();
                image = joclEffectRGB.changeImageComponents(0, deltaGreen, 0, image);
                imageview.setImage(image);
                greenValue.setText(String.format("%.2f", new_val));
            }
        });

        blueSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                float deltaBlue = new_val.intValue() - old_val.intValue();
                image = joclEffectRGB.changeImageComponents(0, 0, deltaBlue, image);
                imageview.setImage(image);
                blueValue.setText(String.format("%.2f", new_val));
            }
        });

        //show must go on ;)
        stage.show();
    }

    // javafx nie uzywa funkcji main, ale trzeba ja dodac ze wzgledow formalnych
    public static void main(String[] args) {
        launch(args);
    }
}
