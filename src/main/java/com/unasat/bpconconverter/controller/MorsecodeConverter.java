package com.unasat.bpconconverter.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MorsecodeConverter {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}