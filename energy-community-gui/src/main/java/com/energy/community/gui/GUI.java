package com.energy.community.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.chart.*;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GUI extends Application {

	// HTTP-Client für API-Anfragen
	private final HttpClient httpClient = HttpClient.newHttpClient();
	// Textbereich zur Anzeige der Historie
	private final TextArea historyDisplay = new TextArea();

	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #1A1A2E;"); // Hintergrundfarbe setzen

		HBox topBar = createTopBar(); // obere Navigationsleiste erstellen
		root.setTop(topBar);

		VBox leftSidebar = createLeftSidebar(); // linke Seitenleiste erstellen
		root.setLeft(leftSidebar);

		VBox centerPanel = createTradingDashboard(); // Hauptinhalt (Trading Dashboard)
		root.setCenter(centerPanel);

		VBox rightSidebar = createRightSidebar(); // rechte Seitenleiste (Wetter, Kalender, Historie)
		root.setRight(rightSidebar);

		Scene scene = new Scene(root, 1200, 800);
		primaryStage.setTitle("Energy Community Trading Platform");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// Erstellt die obere Leiste mit Titel und Logout-Button
	private HBox createTopBar() {
		HBox topBar = new HBox(20);
		topBar.setPadding(new Insets(15));
		topBar.setStyle("-fx-background-color: #16213E;");

		Label title = new Label("Energy Community Platform");
		title.setFont(new Font("Arial", 24));
		title.setTextFill(Color.WHITE);

		Button logoutBtn = new Button("Logout");
		logoutBtn.setStyle("-fx-background-color: #E94560; -fx-text-fill: white;");

		topBar.getChildren().addAll(title, logoutBtn);
		topBar.setAlignment(Pos.CENTER_LEFT);
		HBox.setHgrow(title, Priority.ALWAYS);
		return topBar;
	}

	// Erstellt die linke Seitenleiste mit Zeitfeldern und Buttons für Datenabruf
	private VBox createLeftSidebar() {
		VBox sidebar = new VBox(20);
		sidebar.setPadding(new Insets(20));
		sidebar.setStyle("-fx-background-color: #0F3460;");
		sidebar.setPrefWidth(300);

		Label startLabel = new Label("Start:");
		startLabel.setStyle("-fx-text-fill: white;");
		TextField startField = new TextField("2024-01-01T00:00:00");
		Label endLabel = new Label("End:");
		endLabel.setStyle("-fx-text-fill: white;");
		TextField endField = new TextField("2024-01-01T05:00:00");

		Button getCurrentButton = new Button("Get Current");
		Button getHistoricalButton = new Button("Get Historical");

		// Aktuelle Energiedaten abrufen
		getCurrentButton.setOnAction(e -> fetchData("http://localhost:8080/energy/current"));

		// Historische Energiedaten abrufen
		getHistoricalButton.setOnAction(e -> {
			String url = String.format("http://localhost:8080/energy/historical?start=%s&end=%s",
				startField.getText(), endField.getText());
			fetchData(url);
		});

		sidebar.getChildren().addAll(startLabel, startField, endLabel, endField,
			getCurrentButton, getHistoricalButton);
		sidebar.setAlignment(Pos.TOP_CENTER);

		return sidebar;
	}

	// Hauptbereich: Dashboard zum Handeln mit Energie
	private VBox createTradingDashboard() {
		VBox dashboard = new VBox(20);
		dashboard.setPadding(new Insets(20));
		dashboard.setAlignment(Pos.CENTER);

		Label tradeLabel = new Label("Trading Dashboard");
		tradeLabel.setFont(new Font("Arial", 20));
		tradeLabel.setTextFill(Color.WHITE);

		// Animation oder GIF zur optischen Darstellung
		ImageView gifView = new ImageView();
		gifView.setFitWidth(400);
		gifView.setFitHeight(300);
		gifView.setPreserveRatio(true);
		gifView.setStyle("-fx-border-color: #ffae00; -fx-border-width: 2; -fx-border-radius: 5;");

		// GIF von lokalem Pfad laden
		Image gifImage = new Image("file:/Users/pavelpleshakov/Desktop/energy-community/energy-community-gui/src/main/java/com/energy/community/gui/anim.gif");
		gifView.setImage(gifImage);

		// Steuerungselemente für den Handel
		HBox tradeControls = new HBox(10);
		TextField amountField = new TextField();
		amountField.setPromptText("Amount (kWh)");
		amountField.setStyle("-fx-background-color: #16213E; -fx-text-fill: white;");

		ComboBox<String> tradeType = new ComboBox<>();
		tradeType.getItems().addAll("Sell to Community", "Sell to Exchange", "Buy from Exchange");
		tradeType.setStyle("-fx-background-color: #16213E;");

		Button tradeBtn = new Button("Execute Trade");
		tradeBtn.setStyle("-fx-background-color: #ffae00; -fx-text-fill: #1A1A2E;");

		tradeControls.getChildren().addAll(amountField, tradeType, tradeBtn);
		tradeControls.setAlignment(Pos.CENTER);

		// Tabelle für Handelsdaten
		TableView<String> tradesTable = new TableView<>();
		TableColumn<String, String> timeCol = new TableColumn<>("Time");
		TableColumn<String, String> typeCol = new TableColumn<>("Type");
		TableColumn<String, String> amountCol = new TableColumn<>("Amount");
		tradesTable.getColumns().addAll(timeCol, typeCol, amountCol);
		tradesTable.setStyle("-fx-background-color: #16213E;");

		dashboard.getChildren().addAll(tradeLabel, gifView, tradeControls, tradesTable);
		VBox.setVgrow(tradesTable, Priority.ALWAYS);
		return dashboard;
	}

	// Rechte Seitenleiste: Wetteranzeige, Kalender, Historie
	private VBox createRightSidebar() {
		VBox sidebar = new VBox(20);
		sidebar.setPadding(new Insets(20));
		sidebar.setStyle("-fx-background-color: #0F3460;");
		sidebar.setPrefWidth(300);

		VBox weatherBox = new VBox(10);
		weatherBox.setPadding(new Insets(10));
		weatherBox.setStyle("-fx-background-color: #16213E; -fx-border-radius: 10;");

		Label weatherLabel = new Label("Current Weather");
		weatherLabel.setTextFill(Color.WHITE);

		Label temp = new Label("23°C");
		temp.setFont(new Font("Arial", 24));
		temp.setTextFill(Color.WHITE);

		Label conditions = new Label("Sunny - Wind: 5 m/s");
		conditions.setTextFill(Color.WHITE);

		weatherBox.getChildren().addAll(weatherLabel, temp, conditions);

		DatePicker calendar = new DatePicker();
		calendar.setStyle("-fx-background-color: #16213E;");

		historyDisplay.setEditable(false);
		historyDisplay.setStyle("-fx-control-inner-background: #16213E; -fx-text-fill: white;");
		historyDisplay.setPrefHeight(300);

		sidebar.getChildren().addAll(weatherBox, calendar, historyDisplay);
		VBox.setVgrow(historyDisplay, Priority.ALWAYS);
		return sidebar;
	}


	// Holt Daten von einer API und zeigt sie im rechten Textbereich an
	private void fetchData(String url) {
		try {
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
			httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenAccept(response -> historyDisplay.setText(response))
				.exceptionally(e -> {
					historyDisplay.setText("Error: " + e.getMessage());
					return null;
				});
		} catch (Exception e) {
			historyDisplay.setText("Error: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		launch();
	}
}
