package com.energy.community;

import javafx.application.Application;
import javafx.application.Platform;
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

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class GUI extends Application {

	private final HttpClient httpClient = HttpClient.newHttpClient();
	private final TextArea historyDisplay = new TextArea();

	private final Label communityProducedLabel = new Label();
	private final Label communityUsedLabel = new Label();
	private final Label gridUsedLabel = new Label();

	private final Label gridPortionLabel = new Label();
	private final Label communityDepletedLabel = new Label();

	private final Label temp = new Label("...");
	private final Label conditions = new Label("...");

	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #1A1A2E;");

		root.setTop(createTopBar());
		root.setLeft(createLeftSidebar());
		root.setCenter(createCenterGIF());
		root.setRight(createRightSidebar());

		Scene scene = new Scene(root, 1200, 800);
		primaryStage.setTitle("Energy Community Platform");
		primaryStage.setScene(scene);
		primaryStage.show();

		loadStats();
		loadWeather();
	}

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

	private VBox createLeftSidebar() {
		VBox sidebar = new VBox(15);
		sidebar.setPadding(new Insets(20));
		sidebar.setStyle("-fx-background-color: #0F3460;");
		sidebar.setPrefWidth(320);
		sidebar.setAlignment(Pos.TOP_LEFT);

		Label startLabel = new Label("Start:");
		startLabel.setStyle("-fx-text-fill: white;");
		DatePicker startDatePicker = new DatePicker();
		TextField startTimeField = new TextField("00:00:00");

		Label endLabel = new Label("End:");
		endLabel.setStyle("-fx-text-fill: white;");
		DatePicker endDatePicker = new DatePicker();
		TextField endTimeField = new TextField("18:00:00");

		Button getCurrentButton = new Button("Get Current");
		Button getHistoricalButton = new Button("Get Historical");

		getCurrentButton.setOnAction(e -> fetchData("http://localhost:8081/energy/current"));

		getHistoricalButton.setOnAction(e -> {
			String startDate = startDatePicker.getValue() != null ? startDatePicker.getValue().toString() : "";
			String endDate = endDatePicker.getValue() != null ? endDatePicker.getValue().toString() : "";
			String start = startDate + "T" + startTimeField.getText();
			String end = endDate + "T" + endTimeField.getText();
			String url = String.format("http://localhost:8081/energy/historical?start=%s&end=%s", start, end);
			fetchData(url);
		});

		Label title = new Label("Energy Stats");
		title.setFont(new Font("Arial", 18));
		title.setTextFill(Color.WHITE);

		communityProducedLabel.setTextFill(Color.WHITE);
		communityUsedLabel.setTextFill(Color.WHITE);
		gridUsedLabel.setTextFill(Color.WHITE);
		gridPortionLabel.setTextFill(Color.WHITE);
		communityDepletedLabel.setTextFill(Color.WHITE);

		Button refreshButton = new Button("Refresh");
		refreshButton.setStyle("-fx-background-color: #ffae00; -fx-text-fill: #1A1A2E;");
		refreshButton.setOnAction(e -> loadStats());

		sidebar.getChildren().addAll(
			startLabel, startDatePicker, startTimeField,
			endLabel, endDatePicker, endTimeField,
			getCurrentButton, getHistoricalButton,
			new Separator(),
			title,
			communityProducedLabel,
			communityUsedLabel,
			gridUsedLabel,
			gridPortionLabel,
			communityDepletedLabel,
			refreshButton
		);

		return sidebar;
	}

	private StackPane createCenterGIF() {
		ImageView gifView = new ImageView();
		gifView.setFitWidth(400);
		gifView.setFitHeight(300);
		gifView.setPreserveRatio(true);
		gifView.setStyle("-fx-border-color: #ffae00; -fx-border-width: 2; -fx-border-radius: 5;");

		Image gifImage = new Image(getClass().getResourceAsStream("/images/anim.gif"));
		gifView.setImage(gifImage);

		StackPane centerPane = new StackPane(gifView);
		centerPane.setAlignment(Pos.CENTER);
		centerPane.setStyle("-fx-background-color: #16213E;");
		return centerPane;
	}

	private VBox createRightSidebar() {
		VBox sidebar = new VBox(20);
		sidebar.setPadding(new Insets(20));
		sidebar.setStyle("-fx-background-color: #0F3460;");
		sidebar.setPrefWidth(300);

		VBox weatherBox = new VBox(10);
		weatherBox.setPadding(new Insets(10));
		weatherBox.setStyle("-fx-background-color: #16213E; -fx-border-radius: 10;");

		Label weatherLabel = new Label("Current Weather in Wien AT");
		weatherLabel.setTextFill(Color.WHITE);

		temp.setFont(new Font("Arial", 24));
		temp.setTextFill(Color.WHITE);

		conditions.setTextFill(Color.WHITE);

		Button refreshWeatherButton = new Button("Refresh Weather");
		refreshWeatherButton.setOnAction(e -> loadWeather());

		weatherBox.getChildren().addAll(weatherLabel, temp, conditions, refreshWeatherButton);

		historyDisplay.setEditable(false);
		historyDisplay.setStyle("-fx-control-inner-background: #16213E; -fx-text-fill: white;");
		historyDisplay.setPrefHeight(300);

		sidebar.getChildren().addAll(weatherBox, historyDisplay);
		VBox.setVgrow(historyDisplay, Priority.ALWAYS);
		return sidebar;
	}

	private void fetchData(String url) {
		try {
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
			httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenAccept(response -> {
					try {
						JSONArray jsonArray = new JSONArray(response);
						StringBuilder formatted = new StringBuilder();
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject obj = jsonArray.getJSONObject(i);
							String hour = obj.getString("hour").substring(0, 16);

							formatted.append("Time: ").append(hour).append("\n")
								.append("Community Produced: ").append(String.format("%.2f", obj.getDouble("communityProduced"))).append(" kWh\n")
								.append("Community Used:     ").append(String.format("%.2f", obj.getDouble("communityUsed"))).append(" kWh\n")
								.append("Grid Used:          ").append(String.format("%.2f", obj.getDouble("gridUsed"))).append(" kWh\n\n");
						}
						Platform.runLater(() -> historyDisplay.setText(formatted.toString()));
					} catch (Exception parseEx) {
						Platform.runLater(() -> historyDisplay.setText("Failed to parse response.\n\n" + response));
					}
				})
				.exceptionally(e -> {
					Platform.runLater(() -> historyDisplay.setText("Error: " + e.getMessage()));
					return null;
				});
		} catch (Exception e) {
			historyDisplay.setText("Error: " + e.getMessage());
		}
	}

	private void loadStats() {
		String statsUrl = "http://localhost:8081/api/usage/summary";
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(statsUrl)).build();

		httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			.thenApply(HttpResponse::body)
			.thenAccept(response -> {
				try {
					JSONObject obj = new JSONObject(response);

					Platform.runLater(() -> {
						communityProducedLabel.setText("Community produced: " +
							String.format("%.3f", obj.getDouble("totalProduced")) + " kWh");

						communityUsedLabel.setText("Community used: " +
							String.format("%.3f", obj.getDouble("totalUsed")) + " kWh");

						gridUsedLabel.setText("Grid used: " +
							String.format("%.3f", obj.getDouble("totalGrid")) + " kWh");

						gridPortionLabel.setText("Grid Portion: " +
							String.format("%.2f", obj.optDouble("gridPortion", 0.0) * 100) + " %");

						communityDepletedLabel.setText("Community Depleted: " +
							String.format("%.2f", obj.optDouble("communityDepleted", 0.0) * 100) + " %");
					});

				} catch (Exception ex) {
					System.out.println("Failed to parse summary JSON: " + response);
				}
			})
			.exceptionally(e -> {
				System.out.println("Failed to load stats: " + e.getMessage());
				return null;
			});
	}

	private void loadWeather() {
		String weatherUrl = "http://api.weatherapi.com/v1/current.json?key=713084ecdf1a4ac1b5b100419252206&q=Vienna&aqi=no";
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(weatherUrl)).build();

		httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			.thenApply(HttpResponse::body)
			.thenAccept(response -> {
				try {
					JSONObject obj = new JSONObject(response);
					JSONObject current = obj.getJSONObject("current");
					JSONObject condition = current.getJSONObject("condition");

					String temperature = current.getDouble("temp_c") + "°C";
					String weatherText = condition.getString("text");
					double wind = current.getDouble("wind_kph");

					Platform.runLater(() -> {
						temp.setText(temperature);
						conditions.setText(weatherText + " - Wind: " + wind + " km/h");
					});
				} catch (Exception e) {
					Platform.runLater(() -> {
						temp.setText("Error");
						conditions.setText("Failed to parse weather.");
					});
				}
			})
			.exceptionally(e -> {
				Platform.runLater(() -> {
					temp.setText("Error");
					conditions.setText("Could not fetch weather.");
				});
				return null;
			});
	}

	public static void main(String[] args) {
		launch();
	}
}
