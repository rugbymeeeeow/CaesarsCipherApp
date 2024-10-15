package com.example.caesarscipher;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CaesarsCipherApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Caesar Cipher Application");
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        TextArea inputText = new TextArea();
        inputText.setPromptText("Введите текст...");
        GridPane.setConstraints(inputText, 0, 0);

        TextField shiftField = new TextField();
        shiftField.setPromptText("Сдвиг");
        GridPane.setConstraints(shiftField, 1, 0);

        ChoiceBox<String> languageChoice = new ChoiceBox<>();
        languageChoice.getItems().addAll("Русский", "Английский");
        languageChoice.setValue("Русский");
        GridPane.setConstraints(languageChoice, 2, 0);

        Button encryptButton = new Button("Зашифровать");
        GridPane.setConstraints(encryptButton, 0, 1);
        encryptButton.setOnAction(e -> {
            String text = inputText.getText();
            int shift = Integer.parseInt(shiftField.getText());
            String language = languageChoice.getValue();
            CaesarsCipher cipher = new CaesarsCipher(text, shift);
            String result = (language.equals("Русский")) ? cipher.cipherRus() : cipher.cipherEng();
            inputText.setText(result);
        });

        Button openFileButton = new Button("Открыть файл");
        GridPane.setConstraints(openFileButton, 0, 2);
        openFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Выберите файл");
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    String text = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8);
                    inputText.setText(text);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Button saveFileButton = new Button("Сохранить файл");
        GridPane.setConstraints(saveFileButton, 1, 2);
        saveFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Сохранить файл");
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                try {
                    Files.write(Paths.get(file.getAbsolutePath()), inputText.getText().getBytes(StandardCharsets.UTF_8));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Button decryptButton = new Button("Расшифровать");
        GridPane.setConstraints(decryptButton, 1, 1);
        decryptButton.setOnAction(e -> {
            String text = inputText.getText();
            int shift = Integer.parseInt(shiftField.getText());
            String language = languageChoice.getValue();
            CaesarsCipher cipher = new CaesarsCipher(text, shift);
            String result = (language.equals("Русский")) ? cipher.decipherRus() : cipher.decipherEng();
            inputText.setText(result);
        });

        grid.getChildren().addAll(inputText, shiftField, languageChoice, encryptButton, openFileButton, saveFileButton, decryptButton);

        Scene scene = new Scene(grid, 600, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    static class CaesarsCipher {

        private String input;
        private int shift;

        public CaesarsCipher(String input
                , int shift) {
            this.input = input;
            this.shift = shift % 26;
        }

        public String cipherEng() {
            return processText('a', 26);
        }

        public String decipherEng() {
            return processText('a', -26);
        }

        public String cipherRus() {
            return processText('а', 33);
        }

        public String decipherRus() {
            return processText('а', -33);
        }

        private String processText(char baseChar, int alphabetSize) {
            StringBuilder result = new StringBuilder(input);
            for (int i = 0; i < input.length(); i++) {
                char currentChar = input.charAt(i);
                if (Character.isLetter(currentChar)) {
                    char offsetChar = (char) ((currentChar - baseChar + shift + alphabetSize) % alphabetSize + baseChar);
                    result.setCharAt(i, offsetChar);
                }
            }
            return result.toString();
        }
    }
}