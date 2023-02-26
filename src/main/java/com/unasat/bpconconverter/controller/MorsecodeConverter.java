package com.unasat.bpconconverter.controller;

import com.unasat.bpconconverter.constant.MorseCodeMap;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MorsecodeConverter {
    @FXML
    private TextArea inputText;

    @FXML
    private TextArea outputText;
    @FXML
    private Label errorLabel;

//    @FXML
//    private VBox morseSection;
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), morseSection);
//        fadeIn.setFromValue(0);
//        fadeIn.setToValue(1);
//        fadeIn.play();
//    }

    @FXML
    protected void clearFields() {
        setInputText("");
        setOutputText("");
        clearErrorMsg();
    }

    @FXML
    protected void convertTextToMorse() {
        try {
            clearErrorMsg();
            String input = getInputText();
            if (input.length() == 0) throw new Exception("Please fill in a value");
            convertWordsToMorse(input);
        } catch (Exception e) {
            showErrorMsg(e.getMessage());
        }
    }

    private String convertSentenceToMorse(String sentence) {
        ArrayList<String> morseOutput = new ArrayList<>();
        for (char c : sentence.toUpperCase().toCharArray()) {
            morseOutput.add(abc2morse(c));
        }
        return String.join(" ", morseOutput);
    }

    private void convertWordsToMorse(String words) {
        String morse = convertSentenceToMorse(words);
        setOutputText(morse);
    }

    private String abc2morse(char letter) {
        try {
            HashMap<Character, String> morseMap = MorseCodeMap.morseCodeMap;
            if (!morseMap.containsKey(letter)) throw new Exception("Letter " + letter + " cannot be converted");
            return morseMap.get(letter);
        } catch (Exception e) {
            showErrorMsg(e.getMessage());
            return "#";
        }
    }

    public String getInputText() {
        return this.inputText.getText();
    }

    public void setInputText(String string) {
        this.inputText.setText(string);
    }

    public void setOutputText(String string) {
        this.outputText.setText(string);
    }

    public String getOutputText() {
        return this.outputText.getText();
    }

    private void showErrorMsg(String msg) {
        errorLabel.setText(msg);
    }

    private void clearErrorMsg() {
        errorLabel.setText("");
    }

}