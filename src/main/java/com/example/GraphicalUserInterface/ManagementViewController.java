package com.example.GraphicalUserInterface;

import Utils.ColumnType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ManagementViewController implements Initializable {
    private ArrayList<Button> tabPanels;
    private String queryCondition;
    private ArrayList<String> queryConditionFormatStrings;
    private ArrayList<String> queryConditionValues;
    private Label cellOnClick;
    private int cellOnClickRowIndex, cellOnClickColumnIndex, rowPerPageNum, totalPageNum, totalRowNum, currentPage;
    private boolean isSearchFieldActive;
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
        queryCondition = "";
        queryConditionValues = new ArrayList<String>();
        queryConditionFormatStrings = new ArrayList<String>();
        isSearchFieldActive = false;
        main = ManagementMain.getInstance();

        tabPanels = new ArrayList<Button>();
        dataView = new GridPane();
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
    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
        totalPageNumLabel.setText("of " + totalPageNum);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        managementTabPaneInit();
        logoImageViewInit();
        pagingToolbarInit();
        accountManagementViewInit();
        menuBoxInit();
    }
    public void pagingToolbarInit() {
//        numRowPerPageInputField.setTextFormatter(new TextFormatter(new NumberStringConverter()));

//        numRowPerPageInputField.textProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("textfield changed from " + oldValue + " to " + newValue);
//        });
        numRowPerPageInputField.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                try {
                    if (newPropertyValue) {
                        System.out.println("Textfield on focus");
                    } else if (Integer.parseInt(numRowPerPageInputField.getText()) != rowPerPageNum) {
                        setRowPerPageNum(Integer.parseInt(numRowPerPageInputField.getText()));
                        reRenderPage(false);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
        pageInputField.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                try {
                    if (newPropertyValue) {
                        System.out.println("Textfield on focus");
                    } else if (Integer.parseInt(pageInputField.getText()) != currentPage){
                        setCurrentPage(Integer.parseInt(pageInputField.getText()));
                        reRenderPage(false);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
        setRowPerPageNum(11);
        setTotalPageNum(Math.ceilDiv(totalRowNum, rowPerPageNum));
        setCurrentPage(1);
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
            try {
                Label cell = (Label) (dataView.getChildren().get(rowIndex * colNum + i));
                cell.setStyle(style);
            } catch(Exception e) {
                System.out.print(e);
            }
        }
    }
    public Node getNodeByPosition(int rowIndex, int columnIndex) {
        Node res = null;
        for (Node node : dataView.getChildren()) {
            if (GridPane.getRowIndex(node) == rowIndex && GridPane.getColumnIndex(node) == columnIndex) {
                res = node;
                break;
            }
        }
        return res;
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
                        if (cellOnClickRowIndex % 2 == 0) {
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
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        pageInputField.setText(Integer.toString(currentPage));
    }
    public void setRowPerPageNum(int rowPerPageNum) {
        this.rowPerPageNum = rowPerPageNum;
        numRowPerPageInputField.setText(Integer.toString(rowPerPageNum));
    }
    public void prepareQueryConditions(ColumnType columnType) {
        if (columnType == ColumnType.VARCHAR || columnType == ColumnType.DATE || columnType == ColumnType.TIMESTAMP) {
            queryConditionFormatStrings.add("%s LIKE '%s'");
        } else if (columnType == ColumnType.INTEGER || columnType == ColumnType.FLOAT || columnType == ColumnType.DOUBLE) {
            queryConditionFormatStrings.add("%s = %s");
        }
        queryConditionValues.add("");
    }
    public void setConditionValue(int i, String newValue, ColumnType columnType) {
           if (columnType == ColumnType.VARCHAR || columnType == ColumnType.DATE || columnType == ColumnType.TIMESTAMP) {
               queryConditionValues.set(i, "%" + newValue + "%");
           } else if (columnType == ColumnType.INTEGER || columnType == ColumnType.FLOAT || columnType == ColumnType.DOUBLE) {
               queryConditionValues.set(i, newValue);
           }
    }
    public String constructQueryCondition(ArrayList<String> columnNames) {
        String queryCondition = "";
        for (int i=0;i<queryConditionFormatStrings.size(); ++i) {
            if (queryConditionValues.get(i).equals("")|| queryConditionValues.get(i).equals("%%")) continue;

            if (queryCondition == "") {
                queryCondition += String.format(queryConditionFormatStrings.get(i), columnNames.get(i), queryConditionValues.get(i));
            } else {
                queryCondition += " AND " + String.format(queryConditionFormatStrings.get(i), columnNames.get(i), queryConditionValues.get(i));
            }
        }
        return queryCondition;
    }
    public void renderTableOutline(ArrayList<ArrayList<String>> data) {
        ArrayList<String> columnNames = data.get(0);
        ArrayList<String> columnTypes = data.get(1);
        for (int i = 0; i <= 1; ++i) {
            for (int j = 0; j <= columnNames.size(); ++j) {
                if (i == 0) {
                    Label label = new Label();
                    if (j > 0) {
                        label.setText(columnNames.get(j - 1));
                    } else {
                        label.setPrefWidth(40);
                        label.setStyle("-fx-border-style: solid none solid solid;-fx-border-color: black;");
                        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                ObservableList<Node> childrens = dataView.getChildren();
                                int prefHeight = isSearchFieldActive == true? 0 : 40;
                                for (Node node : childrens) {
                                    if(dataView.getRowIndex(node) == 1) {
                                        node.setVisible(!isSearchFieldActive);
                                        node.setStyle(String.format("-fx-pref-height: %d;", prefHeight));
                                    } else if (dataView.getRowIndex(node) > 1) {
                                        break;
                                    }
                                }
//                                getNodeByPosition(1, 1).requestFocus();
                                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.3), ae -> {
                                    getNodeByPosition(1, 1).requestFocus();
                                }));
                                timeline.play();


                                isSearchFieldActive = !isSearchFieldActive;
                            }
                        });
                    }
                    label.setTextAlignment(TextAlignment.CENTER);
                    label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    GridPane.setFillWidth(label, true);
                    GridPane.setFillHeight(label, true);
                    label.setPadding(new Insets(10, 10, 10, 10));
                    label.setStyle("-fx-border-style: solid none solid solid;-fx-border-color: black;");
                    dataView.add(label, j, i);

                } else {
                    if (j == 0) {
                        Label label = new Label();
//                        label.setStyle("-fx-border-style: solid none solid solid;-fx-border-color: black;");
                        label.setPrefHeight(0);
                        dataView.add(label, j, i);
                    } else {
                        int finalJ = j;
                        Node searchInputField;
                        ColumnType currentColumnType = ColumnType.getByDescription(columnTypes.get(j - 1));
                        prepareQueryConditions(currentColumnType);
                        if (currentColumnType == ColumnType.VARCHAR || currentColumnType == ColumnType.INTEGER || currentColumnType == ColumnType.DOUBLE) {
                            searchInputField = new TextField();
                            ((TextField) searchInputField).setMinHeight(0);
                            ((TextField) searchInputField).setPrefHeight(0);
                            ((TextField) searchInputField).setPrefWidth(40);
                            ((TextField) searchInputField).textProperty().addListener((observable, oldValue, newValue) -> {
                                System.out.println("textfield changed from " + oldValue + " to " + newValue + " " + columnNames.get(GridPane.getColumnIndex(searchInputField) - 1));
//                                queryCondition = String.format("%s LIKE '%s'", columnNames.get(GridPane.getColumnIndex(searchInputField) - 1), "%" + newValue + "%");
                                setConditionValue(finalJ - 1, newValue.toString(), currentColumnType);
                                queryCondition = constructQueryCondition(columnNames);
                                reRenderPage(false);
                            });
                        } else if (currentColumnType == ColumnType.DATE || currentColumnType == ColumnType.TIMESTAMP) {
                            searchInputField = new DatePicker();
                            ((DatePicker) searchInputField).setMinHeight(0);
                            ((DatePicker) searchInputField).setPrefHeight(0);
                               
                            ((DatePicker) searchInputField).setPrefWidth(((Label)dataView.getChildren().get(j)).getPrefWidth());
                            ((DatePicker) searchInputField).valueProperty().addListener((observable, oldValue, newValue) -> {
                                System.out.println("datepicker changed from " + oldValue + " to " + newValue + " " + columnNames.get(GridPane.getColumnIndex(searchInputField) - 1));

                                if (newValue == null) {
                                    setConditionValue(finalJ - 1, "", currentColumnType);
                                } else {
                                    setConditionValue(finalJ - 1, newValue.toString(), currentColumnType);
                                }

                                queryCondition = constructQueryCondition(columnNames);     
                                reRenderPage(false);
                            });
                        } else {
                            searchInputField = null;
                        }
                        searchInputField.setVisible(false);
                        searchInputField.setStyle("-fx-pref-height: 0;");

                        dataView.add(searchInputField, j, i);
                    }

                }
            }
        }
    }
    public void reRenderPage(boolean isInit) {
        for (int i=dataView.getChildren().size()-1 ;i >= 0; --i) {
            if (GridPane.getRowIndex(dataView.getChildren().get(i)) > 1) {
                dataView.getChildren().remove(i);
            }
        }

        cellOnClick = null;
        totalRowNum = main.getAccountManagementProcessor().count(queryCondition);
        setTotalPageNum(Math.max(1, Math.ceilDiv(totalRowNum, rowPerPageNum)));
        setCurrentPage(Math.min(currentPage, totalPageNum));
        ArrayList<ArrayList<String>> result = main.getAccountManagementProcessor().select((currentPage - 1) * rowPerPageNum, rowPerPageNum, queryCondition);
        ArrayList<String> columnNames = result.get(0);
        if (isInit) renderTableOutline(result);
//        dataView.setVgap(10);
//        dataView.setHgap(10);
        for (int i=2;i<result.size(); ++i) {
            for (int j=0;j<=columnNames.size();++j) {
                Label label = new Label();
                if (j > 0) label.setText(result.get(i).get(j - 1));
                label.setTextAlignment(TextAlignment.CENTER);
                label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                GridPane.setFillWidth(label, true);
                GridPane.setFillHeight(label, true);
                label.setPadding(new Insets(10, 10, 10, 10));
                label.setStyle("-fx-border-color: transparent;");

                dataView.add(label, j, i);
                label.setWrapText(true);
                if (i % 2 == 0) {
                    label.setStyle(label.getStyle() + "-fx-background-color: #fafafa;"); //#e7e9f3
                }
                if (j == 0) {
//                        label.setStyle(label.getStyle() + "-fx-border-style: none solid none none;-fx-border-color: black;");
                }
                cellMouseEventListener(label, i, j, columnNames.size() + 1);
            }
        }
        dataViewScrollPane.setContent(dataView);
    }
    public void insertRows(int count) {
        for (Node child : dataView.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(child);
            System.out.print(rowIndex);
            if (rowIndex > 0) {
                GridPane.setRowIndex(child, rowIndex == null ? count : count + rowIndex);
            }
            System.out.println(GridPane.getRowIndex(child));
        }
    }
    @FXML
    public void onPageInputFieldEnterKeyPress() {
        try {
            setCurrentPage(Math.max(Math.min(Integer.parseInt(pageInputField.getText()), totalPageNum), 1));
            reRenderPage(false);
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    @FXML
    public void onNumRowPerPageInputFieldEnterKeyPress() {
        try {
            setRowPerPageNum(Integer.parseInt(numRowPerPageInputField.getText()));
            setTotalPageNum(Math.ceilDiv(totalRowNum, rowPerPageNum));
            setCurrentPage(Math.min(currentPage, totalPageNum));
            reRenderPage(false);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    @FXML
    public void nextBtnOnClick() {
        setCurrentPage(Math.min(currentPage + 1, totalPageNum));
        pageInputField.setText(Integer.toString(currentPage));
        reRenderPage(false);
    }
    @FXML
    public void backBtnOnClick() {
        setCurrentPage(Math.max(currentPage - 1, 1));
        pageInputField.setText(Integer.toString(currentPage));
        reRenderPage(false);
    }
    @FXML
    public void backToHeadBtnOnClick() {
        setCurrentPage(1);
        pageInputField.setText(Integer.toString(currentPage));
        reRenderPage(false);
    }
    @FXML
    public void nextToTailBtnOnClick() {
        setCurrentPage(totalPageNum);
        pageInputField.setText(Integer.toString(currentPage));
        reRenderPage(false);
    }
    public void accountManagementViewInit() {
        reRenderPage(true);
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
