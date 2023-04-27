package com.example.GraphicalUserInterface;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.converter.NumberStringConverter;
import org.controlsfx.control.spreadsheet.Grid;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ManagementViewController implements Initializable {
    private ArrayList<Button> tabPanels;
    private Label cellOnClick;
    private int cellOnClickRowIndex, cellOnClickColumnIndex;
    @FXML
    private HBox menuBox;
    @FXML
    private VBox accountManagementView;
    @FXML
    private ImageView insertBtn;
    @FXML
    private ImageView updateBtn;
    @FXML
    private ImageView deleteBtn;
    @FXML
    private ImageView refreshBtn;
    private ManagementMain main;
    private Button tabPanelOnClick;
    @FXML
    private GridPane dataView = new GridPane();
    @FXML
    private Button homeTabPanel = new Button("Home");
    @FXML
    private Button movieTabPanel = new Button("Movie");
    @FXML
    private Button accountTabPanel = new Button("Account");
    @FXML
    private Button theaterTabPanel = new Button("Theater");
    @FXML
    private Button promotionTabPanel = new Button("Promotion");
    @FXML
    private Button revenueTabPanel = new Button("Revenue");
    @FXML
    private ImageView pagingToolbarRefreshBtn;
    @FXML
    private Button nextBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button backToHeadBtn;
    @FXML
    private Button nextToTailBtn;
    @FXML
    private Label totalPageNumLabel;
    @FXML
    private TextField pageInputField;
    @FXML
    private TextField numRowPerPageInputField;
    @FXML
    private ImageView logoImageView;
    @FXML
    private VBox tabPane;
    @FXML
    private ScrollPane dataViewScrollPane;
    public ManagementViewController() {
        main = ManagementMain.getInstance();
        tabPanels = new ArrayList<Button>();
        tabPanels.add(homeTabPanel);
        tabPanels.add(movieTabPanel);
        tabPanels.add(accountTabPanel);
        tabPanels.add(theaterTabPanel);
        tabPanels.add(revenueTabPanel);
        tabPanels.add(promotionTabPanel);
        insertBtn = new ImageView(main.getImageManager().getInsertIconImage());
        deleteBtn = new ImageView(main.getImageManager().getDeleteIconImage());
        refreshBtn = new ImageView(main.getImageManager().getRefreshIconImage());
        updateBtn = new ImageView(main.getImageManager().getUpdateIconImage());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        managementTabPaneInit();
        logoImageViewInit();
        accountManagementViewInit();

    }
    public void pagingToolbarInit(ArrayList<ArrayList<String>> data) {
        int totalRowNum = data.size() - 1;
        int rowPerPageNum = 11;
        int totalPageNum = Math.ceilDiv(totalRowNum, rowPerPageNum);

        numRowPerPageInputField.setTextFormatter(new TextFormatter(new NumberStringConverter()));
        numRowPerPageInputField.setText("11");
        totalPageNumLabel.setText("of " + totalPageNum);
        pageInputField.setText("1");

        pagingToolbarRefreshBtn.setImage(main.getImageManager().getRefreshIconImage());
    }
    public void menuBoxInit() {
        insertBtn.setFitHeight(20);
        insertBtn.setFitWidth(20);
        updateBtn.setFitHeight(20);
        updateBtn.setFitWidth(20);
        deleteBtn.setFitHeight(20);
        deleteBtn.setFitWidth(20);
        refreshBtn.setFitHeight(20);
        refreshBtn.setFitWidth(20);
        menuBox.getChildren().add(insertBtn);
        menuBox.getChildren().add(updateBtn);
        menuBox.getChildren().add(deleteBtn);
        menuBox.getChildren().add(refreshBtn);
        menuBox.setSpacing(20);

    }
    public void logoImageViewInit() {
        String imageSource = "https://docs.google.com/uc?id=1F2pXOLfvuynr9JcURTR5Syg7N1YdPJXK";
        Image logo = new Image(imageSource);
        if (logo.isError()) {
            System.out.println("Error loading image from " + imageSource);
        } else {
            logoImageView.setImage(logo);
        }
    }
    public void gridPaneChangeRowStyle(int colNum, int rowIndex, String style) {
        for (int i=0;i<colNum;++i) {
            Label cell = (Label)(dataView.getChildren().get(rowIndex * colNum + i));
            cell.setStyle(style);
        }
    }
    public void cellMouseEventListener(Label cell, int rowIndex, int columnIndex, int columnNumber) {
        cell.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (cellOnClick != null && cellOnClickRowIndex != rowIndex || cellOnClick == null) {
                    gridPaneChangeRowStyle(columnNumber, rowIndex, "-fx-border-color: transparent; -fx-background-color: #e7e9f3;");
                }
            }
        });
        cell.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (cellOnClick != null && cellOnClickRowIndex != rowIndex || cellOnClick == null) {
                    if (rowIndex % 2 == 0) {
                        gridPaneChangeRowStyle(columnNumber, rowIndex, "-fx-border-color: transparent; -fx-background-color: #fafafa;");
                    } else {
                        gridPaneChangeRowStyle(columnNumber, rowIndex, "-fx-border-color: transparent;");
                    }
                }
            }
        });
        cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (cellOnClick != null && (cellOnClickRowIndex != rowIndex || cellOnClickColumnIndex != columnIndex) || cellOnClick == null) {
                    if (cellOnClick != null) {
                        if (rowIndex % 2 == 0) {
                            gridPaneChangeRowStyle(columnNumber, cellOnClickRowIndex, "-fx-border-color: transparent; -fx-background-color: #fafafa;");
                        } else {
                            gridPaneChangeRowStyle(columnNumber, cellOnClickRowIndex, "-fx-border-color: transparent;");
                        }
                    }
                    gridPaneChangeRowStyle(columnNumber, rowIndex, "-fx-border-color: transparent; -fx-background-color: rgba(0, 0, 255, 0.1);");
                }
                cell.setStyle(cell.getStyle() + "-fx-border-color: #5d6cbc;");
                cellOnClick = cell;
                cellOnClickRowIndex = rowIndex;
                cellOnClickColumnIndex = columnIndex;
            }
        });
    }
    public void accountManagementViewInit() {
        ArrayList<ArrayList<String>> result = main.getAccountProcessor().select("USERS");
        ArrayList<String> columnNames = result.get(0);
//        dataView.setVgap(10);
//        dataView.setHgap(10);
        for (int i=0;i<result.size(); ++i) {
            for (int j=0;j<columnNames.size();++j) {
                Label label = new Label(result.get(i).get(j));
//                if (j != 0) label.setText(result.get(i).get(j - 1));

                label.setTextAlignment(TextAlignment.CENTER);
                label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                GridPane.setFillWidth(label, true);
                GridPane.setFillHeight(label, true);
                label.setPadding(new Insets(10, 10, 10, 10));
                label.setStyle("-fx-border-color: transparent;");
                dataView.add(label, j, i);
                if (i > 0) {
                    label.setWrapText(true);
                    if (i % 2 == 0) {
                        label.setStyle(label.getStyle() + "-fx-background-color: #fafafa;"); //#e7e9f3
                    }
                    cellMouseEventListener(label, i, j, columnNames.size());
                } else {
                    label.setStyle("-fx-border-style: solid none solid solid;-fx-border-color: black;");
                }
            }
        }
        dataViewScrollPane.setContent(dataView);
        menuBoxInit();
        pagingToolbarInit(result);
    }
    public void managementTabPaneInit() {
        for (Button tabPanel : tabPanels) {
            tabPanel.setStyle("-fx-pref-width: 110; -fx-pref-height: 48; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: transparent;");
            tabPanelMouseEventListener(tabPanel);

            tabPane.getChildren().add(tabPanel);
        }
        Event.fireEvent(accountTabPanel, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                true, true, true, true, true, true, null));
    }
    public void tabPanelMouseEventListener(Button tabPanel) {
        tabPanel.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (tabPanel != tabPanelOnClick) tabPanel.setStyle("-fx-pref-width: 110; -fx-pref-height: 48; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: #700000;");
            }
        });
        tabPanel.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (tabPanel != tabPanelOnClick) tabPanel.setStyle("-fx-pref-width: 110; -fx-pref-height: 48; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: transparent;");
            }
        });
        tabPanel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (tabPanelOnClick != null) {
                    tabPanelOnClick.setStyle("-fx-pref-width: 110; -fx-pref-height: 48; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: transparent;");
                }
                tabPanel.setStyle("-fx-pref-width: 110; -fx-pref-height: 48; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: #A10000;");
                tabPanelOnClick = tabPanel;
            }
        });
    }
}
