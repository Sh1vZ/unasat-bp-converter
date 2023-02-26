package com.unasat.bpconconverter.controller;

import com.unasat.bpconconverter.audioplayer.MorseCodePlayer;
import com.unasat.bpconconverter.constant.Constants;
import com.unasat.bpconconverter.constant.MorseCodeMap;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import javax.sound.sampled.LineUnavailableException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            if (isMorse(input)) {
                convertMorseToWords(input);
            } else {
                convertWordsToMorse(input);
            }
        } catch (Exception e) {
            setOutputText("");
            showErrorMsg(e.getMessage());
        }
    }

    @FXML
    protected void swapInputValues() {
        String temp = getInputText();
        setInputText(getOutputText());
        setOutputText(temp);
        convertTextToMorse();
    }

    private Thread audioThread = null;

    @FXML
    protected void playMorseAudio() {
        if (MorseCodePlayer.isPlaying) {
            MorseCodePlayer.stop();
            audioThread.interrupt();
            audioThread = null;
            return;
        }
        audioThread = new Thread(() -> {
            try {
                String input = getInputText();
                Boolean isMorse = isMorse(input);
                MorseCodePlayer.play(isMorse ? getInputText() : getOutputText());
            } catch (LineUnavailableException | InterruptedException e) {
//                e.printStackTrace();
            }
        });
        audioThread.start();
    }

    private Boolean isMorse(String input) {
        Pattern morsePattern = Pattern.compile(Constants.morseCodepattern);
        String formattedInput = input.replace("/", " ");
        Matcher matcher = morsePattern.matcher(formattedInput);
        return matcher.find();
    }

    private String convertMorseToSentence(String morseSentence) {
        String[] morseLetters = morseSentence.split(" ");
        ArrayList<String> morseOutput = new ArrayList<>();
        for (String morseLetter : morseLetters) {
            morseOutput.add(morse2abc(morseLetter).toString());
        }
        return String.join("", morseOutput);
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

    private void convertMorseToWords(String morseSentence) {
        String sentence = convertMorseToSentence(morseSentence);
        setOutputText(sentence);
    }

    private Character morse2abc(String morseletter) {
        try {
            HashMap<Character, String> morseMap = MorseCodeMap.morseCodeMap;
            Character key = null;
            for (Map.Entry<Character, String> entry : morseMap.entrySet()) {
                if (entry.getValue().equals(morseletter)) {
                    key = entry.getKey();
                }
            }
            if (key == null) throw new Exception("Could not convert " + morseletter);
            return key;
        } catch (Exception e) {
            showErrorMsg(e.getMessage());
            return '#';
        }
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