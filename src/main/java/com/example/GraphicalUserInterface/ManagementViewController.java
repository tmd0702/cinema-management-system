package com.example.GraphicalUserInterface;

import Database.Processor;

import java.lang.Math;
import Utils.ColumnType;
import Utils.Response;
import Utils.StatusCode;
import Utils.Utils;
import com.example.GraphicalUserInterface.ManagementMain;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManagementViewController implements Initializable {
    private ArrayList<Button> tabPanels;
    private Processor activeProcessor;
    private VBox currentActiveSortList;
    private String queryCondition, sortQuery;
    private ArrayList<String> queryConditionFormatStrings;
    private ArrayList<String> queryConditionValues;

    @FXML
    private HBox subTabPanelHBox;
    private Node currentActiveDataViewHeader, currentActiveSortButton;
    private Label cellOnClick;
    private int cellOnClickRowIndex, cellOnClickColumnIndex, rowPerPageNum, totalPageNum, totalRowNum, currentPage;
    private boolean isSearchFieldActive;
    private ArrayList<ArrayList<String>> data;
    @FXML
    private HBox menuBox, pageToolBar;
    @FXML
    private VBox managementContainerView;
    @FXML
    private AnchorPane managementPage;
    @FXML
    private StackPane managementContainerStackPane;
    @FXML
    private Button insertBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button refreshBtn;
    private ManagementMain main;
    private Button tabPanelOnClick;
    @FXML
    private AnchorPane dataViewContainer = new AnchorPane();
    @FXML
    private GridPane dataView = new GridPane();
    @FXML
    private Button signoutBtn = new Button("Sign out");
    private Button subTabPanelOnClick;
    @FXML
    private Button analyticsDashboardPanel = new Button("Dashboard");
    @FXML
    private Button itemTabPanel = new Button("Item");
    @FXML
    private Button seatTabPanel = new Button("Seat");
    @FXML
    private Button movieTabPanel = new Button("Movie");
    @FXML
    private Button accountTabPanel = new Button("Account");
    @FXML
    private Button theaterTabPanel = new Button("Theater");
    @FXML
    private Button paymentTabPanel = new Button("Payment");
    @FXML
    private Button promotionTabPanel = new Button("Promotion");
    @FXML
    private Button bookingTabPanel = new Button("Ticket");
    @FXML
    private Button scheduleTabPanel = new Button("Schedule");
    @FXML
    private Button pagingToolbarRefreshBtn;
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
    private HashMap<String, String> columnNameMapper;
    @FXML
    private ScrollPane dataViewScrollPane;

    public ManagementViewController() {
        columnNameMapper = new HashMap<String, String>();
        sortQuery = "";
        queryCondition = "";
        queryConditionValues = new ArrayList<String>();
        queryConditionFormatStrings = new ArrayList<String>();
        isSearchFieldActive = false;
        main = ManagementMain.getInstance();

        tabPanels = new ArrayList<Button>();
        dataView = new GridPane();
        tabPanels.add(analyticsDashboardPanel);
        tabPanels.add(movieTabPanel);
        tabPanels.add(accountTabPanel);
        tabPanels.add(theaterTabPanel);
        tabPanels.add(bookingTabPanel);
        tabPanels.add(promotionTabPanel);
        tabPanels.add(seatTabPanel);
        tabPanels.add(itemTabPanel);
        tabPanels.add(scheduleTabPanel);
        tabPanels.add(paymentTabPanel);

//        insertBtn = new ImageView(main.getImageManager().getInsertIconImage());
//        deleteBtn = new ImageView(main.getImageManager().getDeleteIconImage());
//        refreshBtn = new ImageView(main.getImageManager().getRefreshIconImage());
//        updateBtn = new ImageView(main.getImageManager().getUpdateIconImage());

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
//        accountManagementViewInit();
        menuBoxInit();
        Event.fireEvent(analyticsDashboardPanel, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                true, true, true, true, true, true, null));
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
        menuBoxButtonsStylingOnMouseEvent(nextBtn);
        menuBoxButtonsStylingOnMouseEvent(nextToTailBtn);
        menuBoxButtonsStylingOnMouseEvent(pagingToolbarRefreshBtn);
        pagingToolbarRefreshBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                reRenderPage(false);
            }
        });
        menuBoxButtonsStylingOnMouseEvent(backBtn);
        menuBoxButtonsStylingOnMouseEvent(backToHeadBtn);
    }
    public void changeSceneToInsertView() throws IOException {
        if (subTabPanelOnClick.getId().equals("seatMapSubTab")){
            System.out.println(ManagementMain.getInstance().getSeatBtnSelected());
            if(ManagementMain.getInstance().getSeatBtnSelected().size() <= 0)
            {
                Dialog<String> dialog = new Dialog<String>();
                //Setting the title
                dialog.setTitle("Error");
                ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                dialog.setContentText("Choose seat for insert");
                dialog.getDialogPane().getButtonTypes().add(type);
                dialog.showAndWait();
                managementContainerStackPane.setVisible(true);
                return;
            }
            ArrayList<Button> seatBtnSelected = ManagementMain.getInstance().getSeatBtnSelected();
            for(Button button: seatBtnSelected){
                if(button.getStyle() != "-fx-border-color: red; -fx-background-color: transparent;"){
                    Dialog<String> dialog = new Dialog<String>();
                    //Setting the title
                    dialog.setTitle("Error");
                    ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                    dialog.setContentText("Only choose seats have not existed");
                    dialog.getDialogPane().getButtonTypes().add(type);
                    dialog.showAndWait();
                    return;
                }
                if(seatBtnSelected.size() > 1){
                    Dialog<String> dialog = new Dialog<String>();
                    //Setting the title
                    dialog.setTitle("Error");
                    ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                    dialog.setContentText("once insert, only insert 1 seat");
                    dialog.getDialogPane().getButtonTypes().add(type);
                    dialog.showAndWait();
                    return;
                }
            }
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("add-seat-form.fxml")));
            return;
        }
        managementContainerStackPane.setVisible(false);
        if (subTabPanelOnClick.getId().equals("userInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("add-account-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("movieInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("add-movie-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("promotionInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("add-promotion-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("cinemaInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("add-cinema-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("itemInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("add-item-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("screenRoomInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("add-screen-room-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("showTimeInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("add-showtime-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("scheduleInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("add-schedule-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("itemCategoryInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("add-item-category-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("seatCategoryInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("add-seat-category-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("userCategoryInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("add-user-category-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("paymentMethodInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("add-payment-method-form.fxml")));
        } else if(subTabPanelOnClick.getId().equals("bookingTicketInfoSubTab")){
            Main otherMain = Main.getInstance();
            otherMain.setSignedInUser(main.getSignedInUser());
            otherMain.start(new Stage());
            Event.fireEvent(subTabPanelOnClick, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                    true, true, true, true, true, true, null));
        } else if(subTabPanelOnClick.getId().equals("bookingItemInfoSubTab")){
            Main otherMain = Main.getInstance();
            otherMain.setSignedInUser(main.getSignedInUser());
            otherMain.start(new Stage());
            Event.fireEvent(subTabPanelOnClick, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                    true, true, true, true, true, true, null));
        } else if(subTabPanelOnClick.getId().equals("seatPriceInfoSubTab")){
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("add-seat-category-price-form.fxml")));
        } else if(subTabPanelOnClick.getId().equals("itemPriceInfoSubTab")){
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("add-item-category-price-form.fxml")));
        } else {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("add-account-form.fxml")));
        }
    }
    public void changeSceneToUpdateView() throws IOException {
        if (subTabPanelOnClick.getId().equals("seatMapSubTab")) {

            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("update-seat-form.fxml")));
        }
        managementContainerStackPane.setVisible(false);
        if (subTabPanelOnClick.getId().equals("userInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("update-account-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("movieInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("update-movie-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("promotionInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("update-promotion-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("cinemaInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("update-cinema-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("itemInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("update-item-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("screenRoomInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("update-screen-room-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("showTimeInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("update-showtime-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("scheduleInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("update-schedule-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("itemCategoryInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("update-item-category-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("seatCategoryInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("update-seat-category-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("userCategoryInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("update-user-category-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("paymentMethodInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("update-payment-method-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("seatPriceInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("update-seat-category-price-form.fxml")));
        } else if (subTabPanelOnClick.getId().equals("itemPriceInfoSubTab")) {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("update-item-category-price-form.fxml")));
        }
        else {
        try {
            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("update-account-form.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    }
    public String normColumnNameToElementID(String columnName) {
        String[] columnNameElements = columnName.split("\\.");
        System.out.println(columnNameElements.toString());
        String normColumnName;
        if (columnNameElements[0].equals(activeProcessor.getDefaultDatabaseTable())) {
            normColumnName = columnNameElements[1];
        } else if(columnNameElements.length == 1){
            normColumnName = columnNameElements[0];
        } else {
            if (columnNameElements[0].charAt(columnNameElements.length - 1) == 's') {
                normColumnName = columnNameElements[0].substring(0, columnNameElements[0].length() - 1) + "_" + columnNameElements[1];
            } else {
                normColumnName = columnNameElements[0] + "_" + columnNameElements[1];
            }
        }
        return normColumnName;
    }
    public void menuBoxInit() {
        insertBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    System.out.println("insert btn clicked");
                    changeSceneToInsertView();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        });

        updateBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (cellOnClick != null) {
                    try {
                        changeSceneToUpdateView();
                        int rowIndex = GridPane.getRowIndex(cellOnClick);
                        for (int i=0;i < data.get(0).size(); ++i) {
                            String columnName = data.get(0).get(i);
                            System.out.println("#" + Utils.toCamelCase(normColumnNameToElementID(columnName)) + "Field");
                            Node node = main.getNodeById("#" + Utils.toCamelCase(normColumnNameToElementID(columnName)) + "Field");
                            if (node instanceof TextField) {
                                ((TextField) node).setText(data.get(rowIndex).get(i));
                            } else if (node instanceof DatePicker) {
                                ((DatePicker) node).setValue(LocalDate.parse(data.get(rowIndex).get(i).substring(0, 10), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                            } else if (node instanceof ComboBox) {
                                ((ComboBox) node).setValue(data.get(rowIndex).get(i));
                            }
                        }
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }else if (subTabPanelOnClick.getId().equals("seatMapSubTab") && main.getSeatBtnSelected().size() > 0) {
                    try {
                        if(ManagementMain.getInstance().getSeatBtnSelected().size() != 1 || !ManagementMain.getInstance().getSeatBtnSelected().get(0).getStyle().equals("-fx-border-color: red; -fx-background-color: transparent;")){
                            Dialog<String> dialog = new Dialog<String>();
                            //Setting the title
                            dialog.setTitle("Error");
                            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                            dialog.setContentText("Only choose seats have not existed");
                            dialog.getDialogPane().getButtonTypes().add(type);
                            dialog.showAndWait();
                            managementContainerStackPane.setVisible(true);
                        }   else {
                            changeSceneToUpdateView();
                            ((TextField) main.getNodeById("#idField")).setText(main.getSeatIdSelected().get(0));
                            ArrayList<ArrayList<String>> seatInfo = main.getProcessorManager().getSeatManagementProcessor().getData(0, 1, String.format("SEATS.ID = '%s'", main.getSeatIdSelected().get(0)), "").getData();
                            ((TextField) main.getNodeById("#nameField")).setText(Utils.getRowValueByColumnName(2, "SEATS.NAME", seatInfo));
                            ((ComboBox) main.getNodeById("#seatCategoryCategoryField")).setValue(Utils.getRowValueByColumnName(2, "SEAT_CATEGORY.CATEGORY", seatInfo));
                            ((ComboBox) main.getNodeById("#statusField")).setValue(Utils.getRowValueByColumnName(2, "SEATS.STATUS", seatInfo));
                        }
                        } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

//        deleteBtn.setPrefHeight(20);
//        deleteBtn.setPrefWidth(20);

        deleteBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (cellOnClick != null) {
                    deleteConfirmationAlert("Are you sure to delete this record?");
                } else if (subTabPanelOnClick.getId().equals("seatMapSubTab") && main.getSeatBtnSelected().size() > 0) {
                    deleteConfirmationAlert("Are you sure to delete this seat?");
                }
            }
        });

//        refreshBtn.setPrefHeight(20);
//        refreshBtn.setPrefWidth(20);

        refreshBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                reRenderPage(false);
            }
        });

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        menuBox.getChildren().add(region);
        menuBox.getChildren().add(signoutBtn);
//        signoutBtn.setText("Signout");
        signoutBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent)  {
                try {
                    main.changeScene("admin-login-form.fxml");
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        });

        menuBox.setSpacing(10);
        menuBoxButtonsStylingOnMouseEvent(insertBtn);
        menuBoxButtonsStylingOnMouseEvent(deleteBtn);
        menuBoxButtonsStylingOnMouseEvent(updateBtn);
        menuBoxButtonsStylingOnMouseEvent(refreshBtn);
    }
    public void menuBoxButtonsStylingOnMouseEvent(Button btn) {
        btn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                btn.getChildrenUnmodifiable().get(0).setStyle("-fx-font-family: FontAwesome; -fx-font-size: 20.0; -fx-fill: WHITE;");
                btn.setStyle("-fx-background-color: #314090;");
            }
        });
        btn.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                btn.getChildrenUnmodifiable().get(0).setStyle("-fx-font-family: FontAwesome; -fx-font-size: 20.0; -fx-fill: #314090;");
                btn.setStyle("-fx-background-color: transparent;");
            }
        });
    }
    public String getRecordIdByRowIndex() {
        int onClickedCellRowIndex = GridPane.getRowIndex(cellOnClick);
        String id = data.get(onClickedCellRowIndex).get(0);
        return id;

    }

    public void handleUpdateRecordRequest() {
        String recordId = getRecordIdByRowIndex();
        String queryCondition = String.format("ID = '%s'", recordId);
    }
    public void  handleDeleteRecordRequest() {

        if (subTabPanelOnClick.getId().equals("bookingTicketInfoSubTab")) {
            int onClickedCellRowIndex = GridPane.getRowIndex(cellOnClick);
            String paymentId = data.get(onClickedCellRowIndex).get(0);
            String ticketId = data.get(onClickedCellRowIndex).get(1);
            String queryCondition = String.format("PAYMENT_ID = '%s' AND TICKET_ID = '%s'", paymentId, ticketId);
            Response response = activeProcessor.deleteData(queryCondition, true);
            StatusCode status = response.getStatusCode();
            if (status == StatusCode.OK) {
                System.out.println("delete success");
                reRenderPage(false);
            }
        } else if (subTabPanelOnClick.getId().equals("bookingItemInfoSubTab")) {
            int onClickedCellRowIndex = GridPane.getRowIndex(cellOnClick);
            String paymentId = data.get(onClickedCellRowIndex).get(0);
            String itemId = data.get(onClickedCellRowIndex).get(1);
            String queryCondition = String.format("PAYMENT_ID = '%s' AND ITEM_ID = '%s'", paymentId, itemId);
            Response response = activeProcessor.deleteData(queryCondition, true);
            StatusCode status = response.getStatusCode();
            if (status == StatusCode.OK) {
                System.out.println("delete success");
                reRenderPage(false);
            }
        } else if(subTabPanelOnClick.getId().equals("seatMapSubTab")){
            for(Button button : ManagementMain.getInstance().getSeatBtnSelected() ) {
                button.setStyle("-fx-border-color: green; -fx-background-color: transparent;");
                button.setOnAction(e -> {
                    if (button.getStyle().equals("-fx-border-color: green; -fx-background-color: transparent;")) {
                        button.setStyle("-fx-border-color: red; -fx-background-color: transparent;");
                        ManagementMain.getInstance().getSeatIdSelected().add(button.getId());
                        ManagementMain.getInstance().getSeatBtnSelected().add(button);
                    } else {
                        button.setStyle("-fx-border-color: green; -fx-background-color: transparent;");
                        ManagementMain.getInstance().getSeatIdSelected().remove(button.getId());
                        ManagementMain.getInstance().getSeatBtnSelected().remove(button);
                    }
                    System.out.println(ManagementMain.getInstance().getSeatIdSelected());
                    System.out.println(ManagementMain.getInstance().getSeatBtnSelected());
                    ManagementMain.getInstance().getSeatMapController().getSeatGrid().requestFocus();
                });
            }
            ArrayList<String> deleteFailedSeatId = new ArrayList<String>();
            for(String id : ManagementMain.getInstance().getSeatIdSelected() ){
                String queryCondition = String.format("ID = '%s'", id);
                Response response = activeProcessor.deleteData(queryCondition, true);
                StatusCode status = response.getStatusCode();
                if (status == StatusCode.OK) {
                    System.out.println("delete success");
                    reRenderPage(false);
                } else {
                    deleteFailedSeatId.add(id);
//                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
////        alert.setTitle("Confirmation");
//                    alert.setContentText("Can not delete seat id: " + id);//"Are you sure to delete this record?");
//                    Optional<ButtonType> result = alert.showAndWait();
                }
            }
            if (deleteFailedSeatId.size() > 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setContentText("Can not delete seat id: " + deleteFailedSeatId.toString());//"Are you sure to delete this record?");
                Optional<ButtonType> result = alert.showAndWait();
            }
        } else {
            String recordId = getRecordIdByRowIndex();
            String idColumn = String.format("%s.ID", activeProcessor.getDefaultDatabaseTable());
            String queryCondition = String.format("%s = '%s'", idColumn, recordId);
            Response response = activeProcessor.deleteData(queryCondition, true);
            StatusCode status = response.getStatusCode();
            if (status == StatusCode.OK) {
                System.out.println("delete success");
                reRenderPage(false);
            } else {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Confirmation");
                alert.setContentText("Can not delete " + idColumn + " = " + recordId);//"Are you sure to delete this record?");
                Optional<ButtonType> result = alert.showAndWait();
            }
        }

    }
    public void deleteConfirmationAlert(String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Confirmation");
        alert.setContentText(contentText);//"Are you sure to delete this record?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            handleDeleteRecordRequest();
        } else {
            System.out.println("cancel");
        }
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
    @FXML
    public void cancelInsertBtnOnClick() {
//        DialogPane cancelConfirmation = new DialogPane();
        System.out.println("cancel");
//        try {
//            System.out.println("insert btn clicked");
//            accountManagementView.getChildren().clear();
//            accountManagementView.getChildren().add(FXMLLoader.load(getClass().getResource("add-account-form.fxml")));
//        } catch (IOException e) {
//            System.out.println(e);
//        }
    }
    @FXML
    public void saveInsertBtnOnClick() {
        System.out.println("save");
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
    public String constructSortQuery(String sortColumn, String sortType) {
        String sortQuery = sortColumn == ""? "" : String.format("%s %s", sortColumn, sortType);
        return sortQuery;
    }
    public String constructQueryCondition(ArrayList<String> columnNames) {
        String queryCondition = "";
        System.out.println(queryConditionFormatStrings);
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
    public void sortPanelMouseEventListener(Node sortPanel, String currentSortColumn, String currentSortType) {
        sortPanel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                dataViewContainer.getChildren().remove(sortPanel.getParent());
                sortQuery = constructSortQuery(currentSortColumn, currentSortType);
                reRenderPage(false);
            }
        });
        sortPanel.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                sortPanel.setStyle("-fx-background-color: #cccccc");
            }
        });
        sortPanel.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                sortPanel.setStyle("-fx-background-color: white");
            }
        });
    }
    public boolean inHierarchy(Node node, Node potentialHierarchyElement) {
        if (potentialHierarchyElement == null) {
            return true;
        }
        while (node != null) {
            if (node == potentialHierarchyElement) {
                return true;
            }
            node = node.getParent();
        }
        return false;
    }

    public void offFocusSortList(Node node) {
        main.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> {
            if (!inHierarchy(evt.getPickResult().getIntersectedNode(), node)) {
                if (currentActiveDataViewHeader != null) {
                    currentActiveDataViewHeader.setStyle("-fx-border-style: solid none solid solid;-fx-border-color: #cccccc;-fx-background-color: white;");
                    currentActiveDataViewHeader = null;
                }
                dataViewContainer.getChildren().remove(node);
            }
        });
    }
    public void activateBtn() {
        insertBtn.setDisable(false);
        deleteBtn.setDisable(false);
        updateBtn.setDisable(false);
    }
    public void constructSortList(Region sortButton) {
        sortButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            int columnIndex = GridPane.getColumnIndex(sortButton.getParent());
            int rowIndex = GridPane.getRowIndex(sortButton.getParent());
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (currentActiveSortList != null) {
                    dataViewContainer.getChildren().remove(currentActiveSortList);
                }

                Label sortDescLabel = new Label("Descending");
                Label sortAscLabel = new Label("Ascending");

                HBox sortDescPanel = new HBox(sortDescLabel);
                sortDescPanel.setPadding(new Insets(10, 10, 10, 10));
                HBox sortAscPanel = new HBox(sortAscLabel);
                sortAscPanel.setPadding(new Insets(10, 10, 10, 10));
                VBox sortList = new VBox(sortAscPanel, sortDescPanel);
                sortList.setStyle("-fx-background-color: white;-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.5, 0.0, 0.0);");
                Bounds cellBounds = dataView.getCellBounds(columnIndex, rowIndex);
                System.out.println(cellBounds);
                if (columnIndex == data.get(0).size()) {
                    sortList.setLayoutX(cellBounds.getMaxX() - 150);
                } else {
                    sortList.setLayoutX(cellBounds.getMaxX() - sortButton.getWidth());
                }

                sortList.setLayoutY(cellBounds.getMaxY());
                sortList.setPrefWidth(150);
                sortPanelMouseEventListener(sortDescPanel, data.get(0).get(columnIndex - 1), "DESC");
                sortPanelMouseEventListener(sortAscPanel, data.get(0).get(columnIndex - 1), "ASC");
                currentActiveSortList = sortList;
                Timeline sortListTimeline = new Timeline(new KeyFrame(Duration.seconds(0.2), ae -> {
                    dataViewContainer.getChildren().add(sortList);
                }));
                sortListTimeline.play();
                offFocusSortList(sortList);


            }
        });
    }
    public void sortButtonMouseEventListener(Node node) {
        node.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (currentActiveSortButton != node) {
                    node.setStyle("-fx-border-style: none none none solid;-fx-fill: transparent; -fx-border-color: transparent; -fx-border-width: 2;");
                }
            }
        });
        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (currentActiveSortButton != node) {
                    node.setStyle("-fx-border-style: none none none solid; -fx-border-color: #cccccc; -fx-border-width: 0 0 0 2;");
                }
            }
        });
    }
    public boolean sortButtonOnClickedDetect(Node node, double x, double y) {
        Bounds dataViewHeaderBounds = dataView.getCellBounds(GridPane.getColumnIndex(node), GridPane.getRowIndex(node));
        System.out.println(dataViewHeaderBounds);
        if (x >= dataViewHeaderBounds.getMaxX() - 20 + 110 && x <= dataViewHeaderBounds.getMaxX() + 110 && y <= dataViewHeaderBounds.getMaxY() + 46 && y >= dataViewHeaderBounds.getMinY() + 46) {
            return true;
        }
        return false;
    }
    public void dataViewHeaderNodeMouseEventListener(Node node) {
        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                node.setStyle("-fx-border-style: solid none solid solid;-fx-border-color: #cccccc;-fx-background-color: #e7e9f3;");
            }
        });
        node.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (node != currentActiveDataViewHeader) {
                    node.setStyle("-fx-border-style: solid none solid solid;-fx-border-color: #cccccc;-fx-background-color: white;");
                }
            }
        });
        node.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Node currentFocusedSortButton = ((HBox) node).getChildren().get(1);
                if (currentActiveDataViewHeader != null && currentActiveDataViewHeader != node) {
                    currentActiveDataViewHeader.setStyle("-fx-border-style: solid none solid solid;-fx-border-color: #cccccc;-fx-background-color: white;");
                }
                if (sortButtonOnClickedDetect(node, mouseEvent.getSceneX(), mouseEvent.getSceneY())) {
                    if (currentActiveSortButton != null && currentFocusedSortButton != currentActiveSortButton) {
                        currentActiveSortButton.setStyle("-fx-border-style: none none none solid; -fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0 0 0 2;");
                    }
                    currentFocusedSortButton.setStyle("-fx-border-style: none none none solid; -fx-background-color: rgba(0, 0, 255, 0.1); -fx-border-color: #cccccc; -fx-border-width: 0 0 0 2;");
                    currentActiveSortButton = currentFocusedSortButton;
                }
                currentActiveDataViewHeader = node;
            }
        });
    }
    public ArrayList<String> generateColumnNameView(ArrayList<String> columnNames) {
        ArrayList<String> columnNamesView = new ArrayList<>();
        for (String columnName : columnNames) {
            String columnNameView = "";
            String []columnNameSplitted = columnName.split("\\.");
            if (columnNameSplitted.length == 1) {
                columnNameView = Utils.toTitleCase(columnNameSplitted[0]);
            } else {
                for (int j=0;j<columnNameSplitted.length;++j) {
                    String element = columnNameSplitted[j];
                    if (element.toLowerCase().charAt(element.length() - 1) == 's') {
                        element = element.substring(0, element.length() - 1);
                    }
                    columnNameView += Utils.toTitleCase(element);
                    if (j < columnNameSplitted.length - 1) {
                        columnNameView += " ";
                    }
                }
            }
            columnNamesView.add(columnNameView);
        }
        return columnNamesView;
    }
    public void renderTableOutline(ArrayList<ArrayList<String>> data) {
        ArrayList<String> columnNames = data.get(0);
        ArrayList<String> columnTypes = data.get(1);
        ArrayList<String> columnNamesView = generateColumnNameView(columnNames);
        for (int i = 0; i <= 1; ++i) {
            for (int j = 0; j <= columnNames.size(); ++j) {
                if (i == 0) {
                    HBox headerGroup = new HBox();
                    headerGroup.setMaxWidth(Double.MAX_VALUE);
                    headerGroup.setMaxHeight(Double.MAX_VALUE);
                    Label label = new Label();
                    if (j > 0) {
                        label.setText(columnNamesView.get(j - 1));
                        dataViewHeaderNodeMouseEventListener(headerGroup);
                    } else {
                        label.setPrefWidth(40);
                        label.setStyle("-fx-border-style: solid none solid solid;-fx-border-color: transparent;");
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
                                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.4), ae -> {
                                    getNodeByPosition(1, 1).requestFocus();
                                }));
                                timeline.play();
                                isSearchFieldActive = !isSearchFieldActive;
                            }
                        });
                    }
                    label.setTextAlignment(TextAlignment.CENTER);
                    label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    GridPane.setFillWidth(headerGroup, true);
                    GridPane.setFillHeight(headerGroup, true);
                    label.setPadding(new Insets(10, 10, 10, 10));
                    headerGroup.setStyle("-fx-border-style: solid none solid solid;-fx-border-color: #cccccc;-fx-background-color: white;");
                    headerGroup.getChildren().add(label);
                    if (j > 0) {
                        Region sortButton = new Region();
                        sortButton.setPrefWidth(20);
                        sortButton.setPrefHeight(40);
                        sortButton.setStyle("-fx-border-style: none none none solid;-fx-fill: transparent; -fx-border-color: transparent; -fx-border-width: 2;");
                        sortButtonMouseEventListener(sortButton);
//                        constructSortList(sortButton);

                        headerGroup.getChildren().add(sortButton);
                        main.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> {
                            if (!inHierarchy(evt.getPickResult().getIntersectedNode(), sortButton)) {
                                currentActiveSortButton = null;
                                sortButton.setStyle("-fx-border-style: none none none solid;-fx-fill: transparent; -fx-border-color: transparent; -fx-border-width: 2;");
                            }
                        });
                    }
                    headerGroup.setHgrow(label, Priority.ALWAYS);
                    dataView.add(headerGroup, j, i);
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

                            ((DatePicker) searchInputField).setPrefWidth(((HBox)dataView.getChildren().get(j)).getPrefWidth());
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
    public void sortListInit() {
        for (Node node : dataView.getChildren()) {
            int rowIndex = GridPane.getRowIndex(node), columnIndex = GridPane.getColumnIndex(node);
            if (rowIndex == 0) {
                if (node instanceof HBox && columnIndex > 0) {
                    constructSortList((Region)((HBox) node).getChildren().get(1));
                }
            } else if (GridPane.getRowIndex(node) > 0) {
                break;
            }
        }
    }
    public void disableForms() {
        managementPage.getChildren().get(0).setVisible(true);
        if (managementPage.getChildren().size() > 2) {
            managementPage.getChildren().remove(2);
        }
    }
    public void resetQueryStatementValues() {
        sortQuery = "";
        queryCondition = "";
        queryConditionValues = new ArrayList<String>();
        queryConditionFormatStrings = new ArrayList<String>();
    }
    public void reRenderPage(boolean isInit) {
        cellOnClick = null;
        totalRowNum = activeProcessor.countData(queryCondition);
        System.out.println(queryCondition);
        System.out.println(Math.ceilDiv(totalRowNum, rowPerPageNum) + " " + totalRowNum + " " + rowPerPageNum);
        setTotalPageNum(Math.max(1, Math.ceilDiv(totalRowNum, rowPerPageNum)));
        setCurrentPage(Math.min(currentPage, totalPageNum));
        if (isInit) resetQueryStatementValues();
        data = activeProcessor.getData((currentPage - 1) * rowPerPageNum, rowPerPageNum, queryCondition, sortQuery).getData();
        ArrayList<String> columnNames = data.get(0);
        if (isInit) {
            disableForms();
            setCurrentPage(1);
            dataView.getChildren().clear();
            renderTableOutline(data);
            dataViewContainer.getChildren().remove(dataView);
            dataViewContainer.getChildren().add(dataView);
            dataViewScrollPane.setContent(dataViewContainer);
        } else {
            for (int i=dataView.getChildren().size()-1 ;i >= 0; --i) {
                if (GridPane.getRowIndex(dataView.getChildren().get(i)) > 1) {
                    dataView.getChildren().remove(i);
                }
            }
        }
        for (int i=2;i<data.size(); ++i) {
            for (int j=0;j<=columnNames.size();++j) {
                Label label = new Label();
                label.setTextAlignment(TextAlignment.CENTER);
                label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                if (j > 0) label.setText(data.get(i).get(j - 1));

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

        sortListInit();
    }
    public void insertRows(int count) {
        for (Node child : dataView.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(child);
            if (rowIndex > 0) {
                GridPane.setRowIndex(child, rowIndex == null ? count : count + rowIndex);
            }
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
    public void managementTabPaneInit() {
        for (Button tabPanel : tabPanels) {
            tabPanel.setStyle("-fx-pref-width: 110; -fx-pref-height: 48; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: transparent;-fx-font-family: Georgia");
            tabPanelMouseEventListener(tabPanel);

            tabPane.getChildren().add(tabPanel);
        }

    }
    public void setSubTabPanelStyle(Node subTabPanel) {

        subTabPanel.setStyle("-fx-background-color: #dedede; -fx-font-weight: bold;-fx-pref-height: 40px;");
        subTabPanel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (subTabPanelOnClick != null) {
                    subTabPanelOnClick.setStyle("-fx-background-color: #dedede; -fx-font-weight: bold;-fx-pref-height: 40px;");
                }
                subTabPanel.setStyle("-fx-background-color: white; -fx-font-weight: bold;-fx-pref-height: 40px;");
                subTabPanelOnClick = (Button)subTabPanel;
                activateBtn();
                pageToolBar.setPrefHeight(29);
                pageToolBar.setVisible(true);
                dataViewScrollPane.setContent(dataView);
                dataViewScrollPane.setPrefHeight(527);
                if (subTabPanel.getId().equals("userInfoSubTab")) {
                    activeProcessor = main.getProcessorManager().getAccountManagementProcessor();
//                    insertBtn.setDisable(true);
                    reRenderPage(true);
                } else if (subTabPanel.getId().equals("userCategoryInfoSubTab")) {
                    activeProcessor = main.getProcessorManager().getUserCategoryManagementProcessor();
                    reRenderPage(true);
                } else if (subTabPanel.getId().equals("movieInfoSubTab")) {
                    activeProcessor = main.getProcessorManager().getMovieManagementProcessor();
                    reRenderPage(true);
                } else if (subTabPanel.getId().equals("movieGenreInfoSubTab")) {
                    activeProcessor = main.getProcessorManager().getMovieGenreManagementProcessor();
                    reRenderPage(true);
                } else if (subTabPanel.getId().equals("promotionInfoSubTab")) {
                    activeProcessor = main.getProcessorManager().getPromotionManagementProcessor();
                    reRenderPage(true);
                } else if (subTabPanel.getId().equals("seatInfoSubTab")) {
                    activeProcessor = main.getProcessorManager().getSeatManagementProcessor();
                    updateBtn.setDisable(true);
                    insertBtn.setDisable(true);
                    reRenderPage(true);
                } else if (subTabPanel.getId().equals("seatPriceInfoSubTab")) {
                    activeProcessor = main.getProcessorManager().getSeatPriceManagementProcessor();
                    reRenderPage(true);
                } else if (subTabPanel.getId().equals("seatCategoryInfoSubTab")) {
                    activeProcessor = main.getProcessorManager().getSeatCategoryManagementProcessor();
                    reRenderPage(true);
                } else if (subTabPanel.getId().equals("cinemaInfoSubTab")) {
                    activeProcessor = main.getProcessorManager().getCinemaManagementProcessor();
                    reRenderPage(true);
                } else if (subTabPanel.getId().equals("screenRoomInfoSubTab")) {
                    activeProcessor = main.getProcessorManager().getScreenRoomManagementProcessor();
                    reRenderPage(true);
                } else if (subTabPanel.getId().equals("itemInfoSubTab")) {
                    activeProcessor = main.getProcessorManager().getItemManagementProcessor();
                    reRenderPage(true);
                } else if (subTabPanel.getId().equals("itemCategoryInfoSubTab")) {
                    activeProcessor = main.getProcessorManager().getItemCategoryManagementProcessor();
                    reRenderPage(true);
                } else if (subTabPanel.getId().equals("itemPriceInfoSubTab")) {
                    activeProcessor = main.getProcessorManager().getItemPriceManagementProcessor();
                    reRenderPage(true);
                } else if (subTabPanel.getId().equals("seatPriceInfoSubTab")) {
                    activeProcessor = main.getProcessorManager().getSeatPriceManagementProcessor();
                    reRenderPage(true);
                } else if (subTabPanel.getId().equals("showTimeInfoSubTab")) {
                    activeProcessor = main.getProcessorManager().getShowTimeManagementProcessor();
                    reRenderPage(true);
                } else if (subTabPanel.getId().equals("scheduleInfoSubTab")) {
                    activeProcessor = main.getProcessorManager().getScheduleManagementProcessor();
                    reRenderPage(true);
                } else if (subTabPanel.getId().equals("bookingTicketInfoSubTab")) {
                    activeProcessor = main.getProcessorManager().getBookingTicketManagementProcessor();
                    updateBtn.setDisable(true);
                    reRenderPage(true);
                } else if (subTabPanel.getId().equals("bookingItemInfoSubTab")) {
                    activeProcessor = main.getProcessorManager().getBookingItemManagementProcessor();
                    updateBtn.setDisable(true);
                    reRenderPage(true);
                } else if (subTabPanel.getId().equals("paymentInfoSubTab")) {

                    activeProcessor = main.getProcessorManager().getPaymentManagementProcessor();
                    insertBtn.setDisable(true);
                    deleteBtn.setDisable(true);
                    updateBtn.setDisable(true);
                    reRenderPage(true);
                } else if (subTabPanel.getId().equals("paymentMethodInfoSubTab")) {
                    activeProcessor = main.getProcessorManager().getPaymentMethodManagementProcessor();
                    reRenderPage(true);
                } else if(subTabPanel.getId().equals("seatMapSubTab")){
                    activeProcessor = main.getProcessorManager().getSeatManagementProcessor();
                    try {
                        pageToolBar.setVisible(false);
                        dataViewScrollPane.setContent(FXMLLoader.load(getClass().getResource("seat-map.fxml")));
                        dataViewScrollPane.setPrefHeight(556);
                        pageToolBar.setPrefHeight(0);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
//                    managementContainerStackPane.setVisible(false);
//
//                        try {
//                            managementPage.getChildren().add(FXMLLoader.load(getClass().getResource("seat-map.fxml")));
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }

                }

            }
        });
    }
    public void tabPanelMouseEventListener(Button tabPanel) {
        tabPanel.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (tabPanel != tabPanelOnClick) tabPanel.setStyle("-fx-pref-width: 110; -fx-pref-height: 48; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: #293263;-fx-font-family: Georgia");
            }
        });
        tabPanel.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (tabPanel != tabPanelOnClick) tabPanel.setStyle("-fx-pref-width: 110; -fx-pref-height: 48; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: transparent;-fx-font-family: Georgia");
            }
        });
        tabPanel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (tabPanelOnClick != null) {
                    tabPanelOnClick.setStyle("-fx-pref-width: 110; -fx-pref-height: 48; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: transparent;-fx-font-family: Georgia");
                }
                tabPanel.setStyle("-fx-pref-width: 110; -fx-pref-height: 48; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: rgba(0, 0, 255, 0.3);-fx-font-family: Georgia");
                managementContainerView.getChildren().remove(subTabPanelHBox);
                subTabPanelHBox = new HBox();
                subTabPanelHBox.setStyle("-fx-background-color: #dddddd;-fx-pref-height: 60px;");
                subTabPanelHBox.setAlignment(Pos.BOTTOM_LEFT);
                managementContainerView.getChildren().add(0, subTabPanelHBox);
                tabPanelOnClick = tabPanel;
                if (tabPanelOnClick != analyticsDashboardPanel) {
                    managementContainerStackPane.getChildren().remove(main.getNodeById("#analyticsDashboardScrollPane"));
                }
                if (tabPanelOnClick == accountTabPanel) {
                    Button userInfoSubTab = new Button("User");
                    userInfoSubTab.setId("userInfoSubTab");
                    Button userCategoryInfoSubTab = new Button("User Category");
                    userCategoryInfoSubTab.setId("userCategoryInfoSubTab");
                    subTabPanelHBox.getChildren().add(userInfoSubTab);
                    subTabPanelHBox.getChildren().add(userCategoryInfoSubTab);
                } else if (tabPanelOnClick == analyticsDashboardPanel) {
                    try {
                        managementContainerStackPane.getChildren().add(FXMLLoader.load(getClass().getResource("analytics-dashboard.fxml")));
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                } else if (tabPanelOnClick == movieTabPanel) {
                    Button movieInfoSubTab = new Button("Movie");
                    movieInfoSubTab.setId("movieInfoSubTab");
                    Button movieGenreInfoSubTab = new Button("Movie Genre");
                    movieGenreInfoSubTab.setId("movieGenreInfoSubTab");
                    subTabPanelHBox.getChildren().add(movieInfoSubTab);
                    subTabPanelHBox.getChildren().add(movieGenreInfoSubTab);
                } else if (tabPanelOnClick == promotionTabPanel) {
                    Button promotionInfoSubTab = new Button("Promotion");
                    promotionInfoSubTab.setId("promotionInfoSubTab");
                    subTabPanelHBox.getChildren().add(promotionInfoSubTab);
                } else if (tabPanelOnClick == theaterTabPanel) {
                    Button cinemaInfoSubTab = new Button("Cinema");
                    cinemaInfoSubTab.setId("cinemaInfoSubTab");
                    Button screenRoomInfoSubTab = new Button("Screen Room");
                    screenRoomInfoSubTab.setId("screenRoomInfoSubTab");
                    subTabPanelHBox.getChildren().add(cinemaInfoSubTab);
                    subTabPanelHBox.getChildren().add(screenRoomInfoSubTab);
                } else if (tabPanelOnClick == itemTabPanel) {
                    Button itemInfoSubTab = new Button("Item");
                    itemInfoSubTab.setId("itemInfoSubTab");
                    Button itemCategoryInfoSubTab = new Button("Item Category");
                    itemCategoryInfoSubTab.setId("itemCategoryInfoSubTab");
                    Button itemPriceSubTab = new Button("Item price");
                    itemPriceSubTab.setId("itemPriceInfoSubTab");
                    subTabPanelHBox.getChildren().add(itemInfoSubTab);
                    subTabPanelHBox.getChildren().add(itemCategoryInfoSubTab);
                    subTabPanelHBox.getChildren().add(itemPriceSubTab);
                } else if (tabPanelOnClick == seatTabPanel) {
                    Button seatInfoSubTab = new Button("Seat");
                    seatInfoSubTab.setId("seatInfoSubTab");
                    Button seatCategoryInfoSubTab = new Button("Seat Category");
                    seatCategoryInfoSubTab.setId("seatCategoryInfoSubTab");
                    Button seatPriceInfoSubTab = new Button("Seat Price");
                    seatPriceInfoSubTab.setId("seatPriceInfoSubTab");
                    Button seatMapSubTab = new Button("Seat Map");
                    seatMapSubTab.setId("seatMapSubTab");
                    subTabPanelHBox.getChildren().add(seatInfoSubTab);
                    subTabPanelHBox.getChildren().add(seatCategoryInfoSubTab);
                    subTabPanelHBox.getChildren().add(seatPriceInfoSubTab);
                    subTabPanelHBox.getChildren().add(seatMapSubTab);
                } else if (tabPanelOnClick == scheduleTabPanel) {
                    Button scheduleInfoSubTab = new Button("Schedule");
                    scheduleInfoSubTab.setId("scheduleInfoSubTab");
                    Button showTimeInfoSubTab = new Button("Showtime");
                    showTimeInfoSubTab.setId("showTimeInfoSubTab");
                    subTabPanelHBox.getChildren().add(scheduleInfoSubTab);
                    subTabPanelHBox.getChildren().add(showTimeInfoSubTab);
                } else if (tabPanelOnClick == bookingTabPanel) {
                    Button bookingTicketInfoSubTab = new Button("Booking Ticket");
                    bookingTicketInfoSubTab.setId("bookingTicketInfoSubTab");
                    Button bookingItemInfoSubTab = new Button("Booking Item");
                    bookingItemInfoSubTab.setId("bookingItemInfoSubTab");
                    subTabPanelHBox.getChildren().add(bookingTicketInfoSubTab);
                    subTabPanelHBox.getChildren().add(bookingItemInfoSubTab);
                } else if (tabPanelOnClick == paymentTabPanel) {
                    Button paymentInfoSubTab = new Button("Payment");
                    paymentInfoSubTab.setId("paymentInfoSubTab");
                    Button paymentMethodInfoSubTab = new Button("Payment Method");
                    paymentMethodInfoSubTab.setId("paymentMethodInfoSubTab");
                    subTabPanelHBox.getChildren().add(paymentInfoSubTab);
                    subTabPanelHBox.getChildren().add(paymentMethodInfoSubTab);
                }
                for (Node subTab : subTabPanelHBox.getChildren()) {
                    setSubTabPanelStyle(subTab);
                }
                if (tabPanelOnClick != analyticsDashboardPanel) {
                    Event.fireEvent(subTabPanelHBox.getChildren().get(0), new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                            0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                            true, true, true, true, true, true, null));
                }
//                else {
//                    activeProcessor = main.getProcessorManager().getMovieManagementProcessor();
//                    reRenderPage(true);
//                }
            }
        });
    }
}
