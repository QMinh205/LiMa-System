package app;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.canvas.*;
import javafx.geometry.Point2D;

import java.io.IOException;
import java.util.*;

public class Game extends Application {

    private final String[] imageUrls = {
            "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/image1.jpg",
            "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/image2.jpg",
            "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/image3.jpg",
            "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/image4.jpg",
            "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/image5.jpg",
            "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/image6.jpg",
            "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/image7.jpg",
            "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/image8.jpg",
            "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/image9.jpg",
            "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/image10.jpg",
            "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/image11.jpg",
            "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/image12.jpg",
            "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/image13.jpg",
            "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/image14.jpg",
            "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/image15.jpg",
            "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/image16.jpg",
            "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/image17.jpg",
            "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/image18.jpg",
            "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/image19.jpg",
            "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/image20.jpg",
    };

    private String getRandomImageUrl() {
        Random random = new Random();
        int index = random.nextInt(imageUrls.length);
        return imageUrls[index];
    }

    private Stage primaryStage;
    private Scene userHomeScene;

    public Game(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void loadScene(String fxmlFile, String title, JFXButton button) {
        try {
            // load file fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // lấy stage hiện tại và đặt scene mới
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        // display intro
        primaryStage.setScene(createIntroScene());
        primaryStage.setTitle("Game");
        primaryStage.show();
    }

    // tạo stackpane cho intro
    public Scene createIntroScene() {
        StackPane root = new StackPane();

        // ảnh nền
        ImageView introImage = new ImageView(new Image("file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/game.jpeg")); // Replace with your image path
        introImage.setFitWidth(1500);
        introImage.setFitHeight(750);

        // return
        JFXButton backButton = new JFXButton("Return");
        backButton.getStyleClass().add("button-game");
        backButton.setPrefWidth(127);
        backButton.setPrefHeight(60);
        ButtonSoundUtil.addClickSound(backButton);
        backButton.setOnAction(event -> loadScene("User-Home.fxml", "Home", backButton));

// enter
        JFXButton enterButton = new JFXButton("Enter");
        enterButton.getStyleClass().add("button-game");
        enterButton.setPrefWidth(127);
        enterButton.setPrefHeight(60);
        ButtonSoundUtil.addClickSound(enterButton);
        enterButton.setOnAction(e -> primaryStage.setScene(createGameScene()));

// thêm các nút enter back
        StackPane.setAlignment(backButton, javafx.geometry.Pos.BOTTOM_LEFT);
        StackPane.setAlignment(enterButton, javafx.geometry.Pos.BOTTOM_RIGHT);

        root.getChildren().addAll(introImage, backButton, enterButton);
        Scene scene = new Scene(root, 1500, 750);

        // Liên kết file CSS
        scene.getStylesheets().add("file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/src/main/resources/app/style.css");
        return scene;

    }

    //tạo game scene
    private Scene createGameScene() {
        String randomImageUrl = getRandomImageUrl();
        GameBoard gameBoard = new GameBoard(randomImageUrl);
        Scene gameScene = gameBoard.getScene();

        // đặt lại kích thước stage cho khớp
        primaryStage.setWidth(1500);
        primaryStage.setHeight(750);
        return gameScene;
    }
}

class GameBoard {
    private final int GRID_SIZE = 3; // lưới 3x3
    private final int TILE_SIZE = 150; // kích thước mỗi mảnh
    private final Image image;
    private final ImagePiece[][] tiles;
    private final BorderPane root; // sử dụng borderpane để bố trí
    private int emptyX, emptyY; // vị trí ô trống
    private Canvas canvas; // canvas để vẽ lưới
    private MediaPlayer backgroundMusic;
    private String s="file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/sontung.png";
    private final Image victoryImage = new Image("file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/sontung1.png");

    public GameBoard(String imagePath) {
        this.image = new Image(imagePath);
        this.tiles = new ImagePiece[GRID_SIZE][GRID_SIZE];
        this.root = new BorderPane();

        initializeMusic();
        initializeTiles();
        shuffleTiles();

        // Khởi tạo các mảnh ảnh
        initializeTiles();

        // Xáo trộn các mảnh
        shuffleTiles();

        // Canvas để vẽ lưới
        canvas = new Canvas(GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawTiles(gc);

        // Sự kiện click chuột để di chuyển các mảnh
        canvas.setOnMousePressed(this::handleMousePress);

        // Thêm canvas vào trung tâm BorderPane
        root.setCenter(canvas);

        ImageView sideImage = new ImageView(s);
        sideImage.setPreserveRatio(true);
        sideImage.setFitWidth(550); // Đặt chiều rộng cho ảnh bên trái
        root.setLeft(sideImage); // Thêm ảnh vào bên trái

        // Thêm ảnh mẫu (thumbnail) và nút Shuffle vào giao diện
        addRightPanel();
    }

    // Phương thức khởi tạo nhạc nền
    private void initializeMusic() {
        try {
            String musicPath = "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/music1.mp3";
            Media music = new Media(musicPath);
            backgroundMusic = new MediaPlayer(music);
            backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE); // Lặp vô hạn
            backgroundMusic.setVolume(0.3);
            backgroundMusic.play(); // Bắt đầu phát nhạc
        } catch (Exception e) {
            System.out.println("Error initializing music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Dừng nhạc khi thoát GameBoard
    private void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    // Trả về scene cho trò chơi
    public Scene getScene() {
        Scene scene = new Scene(root, GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE);
        scene.getStylesheets().add("file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/src/main/resources/app/style.css");
        return scene;
    }

    private void addRightPanel() {
        // ảnh mẫu (thumbnail)
        ImageView thumbnail = new ImageView(image);
        thumbnail.setPreserveRatio(true);
        thumbnail.setFitWidth(GRID_SIZE * TILE_SIZE * 0.7); // Kích thước nhỏ hơn 30%

        // Ảnh nút nhạc (bật/tắt nhạc)
        Image musicOnImage = new Image("file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/sound.png");
        Image musicOffImage = new Image("file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/noSound.png");
        ImageView musicButton = new ImageView(musicOnImage);
        musicButton.setFitWidth(75);
        musicButton.setFitHeight(75);

        // Xử lý sự kiện khi click vào ảnh
        musicButton.setOnMouseClicked(e -> {
            if (backgroundMusic.getStatus() == MediaPlayer.Status.PLAYING) {
                backgroundMusic.pause();
                musicButton.setImage(musicOffImage); // Thay đổi ảnh khi nhạc tắt
            } else {
                backgroundMusic.play();
                musicButton.setImage(musicOnImage); // Thay đổi ảnh khi nhạc bật
            }
        });

        // nút shuffle
        JFXButton shuffleButton = new JFXButton("Shuffle");
        shuffleButton.getStyleClass().add("button-game");
        ButtonSoundUtil.addClickSound(shuffleButton);
        shuffleButton.setOnAction(e -> shuffleAndRedraw());

        // nút back
        JFXButton backButton = new JFXButton("Back");
        backButton.getStyleClass().add("button-game");
        ButtonSoundUtil.addClickSound(backButton);
        backButton.setOnAction(e -> backToIntro());

        Label titleLabel = new Label("BOOK RECOMMENDED TODAY");
        titleLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;-fx-font-family: 'Times New Roman'; -fx-text-fill: #333;");
        titleLabel.setAlignment(Pos.CENTER);

        // Sử dụng StackPane để căn chỉnh ảnh Back và Music
        StackPane topPanel = new StackPane(backButton,titleLabel,musicButton);
        StackPane.setAlignment(backButton, javafx.geometry.Pos.TOP_LEFT);
        StackPane.setAlignment(titleLabel, javafx.geometry.Pos.TOP_CENTER);
        StackPane.setAlignment(musicButton, javafx.geometry.Pos.TOP_RIGHT);

        // đặt vbox để nhét shuffleButton
        VBox rightPanel = new VBox(10, thumbnail, shuffleButton);
        rightPanel.setStyle("-fx-alignment: center; -fx-padding: 10;");

        root.setTop(topPanel); // Đặt leftPanel vào vị trí trên cùng
        BorderPane.setAlignment(rightPanel, javafx.geometry.Pos.CENTER_RIGHT); // Align to the right
        root.setRight(rightPanel); // đặt rightPanel vào BorderPane


        // If it's still not showing, you can try setting a specific width for the right panel
        rightPanel.setMinWidth(200); // You can adjust this width based on your design
    }

    private void backToIntro() {
        stopMusic();
        Stage stage = (Stage) root.getScene().getWindow(); // Lấy stage hiện tại
        Game game = new Game(stage); // Tạo lại đối tượng Game với stage hiện tại
        stage.setScene(game.createIntroScene()); // Chuyển đến giao diện intro
    }

    // xáo trộn các mảnh ảnh và vẽ lại
    private void shuffleAndRedraw() {
        shuffleTiles();
        drawTiles(canvas.getGraphicsContext2D());
    }

    // khởi tạo các mảnh ảnh và ô trống
    private void initializeTiles() {
        double pieceWidth = image.getWidth() / GRID_SIZE;
        double pieceHeight = image.getHeight() / GRID_SIZE;

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (i == GRID_SIZE - 1 && j == GRID_SIZE - 1) {
                    // Ô trống
                    tiles[i][j] = null;
                    emptyX = i;
                    emptyY = j;
                } else {
                    // Cắt mảnh ảnh
                    Image subImage = new WritableImage(image.getPixelReader(),
                            (int) (j * pieceWidth),
                            (int) (i * pieceHeight),
                            (int) pieceWidth,
                            (int) pieceHeight);
                    tiles[i][j] = new ImagePiece(subImage, i, j);
                }
            }
        }
    }

    // xáo trộn các mảnh ảnh
    private void shuffleTiles() {
        ArrayList<ImagePiece> pieces = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (tiles[i][j] != null) {
                    pieces.add(tiles[i][j]);
                }
            }
        }

        // lặp lại xáo trộn cho đến khi trạng thái có thể giải được
        do {
            Collections.shuffle(pieces);
        } while (!isSolvable(pieces));

        // sắp xếp lại các mảnh vào lưới
        int index = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (tiles[i][j] != null) {
                    tiles[i][j] = pieces.get(index++);
                }
            }
        }
    }

    // kiểm tra trạng thái có thể giải được
    private boolean isSolvable(ArrayList<ImagePiece> pieces) {
        int inversions = 0;
        for (int i = 0; i < pieces.size(); i++) {
            for (int j = i + 1; j < pieces.size(); j++) {
                if (pieces.get(i).getOriginalX() * GRID_SIZE + pieces.get(i).getOriginalY() >
                        pieces.get(j).getOriginalX() * GRID_SIZE + pieces.get(j).getOriginalY()) {
                    inversions++;
                }
            }
        }

        // lấy vị trí hàng của ô trống (từ dưới lên)
        int emptyRowFromBottom = GRID_SIZE - emptyX;

        // kiểm tra tính chẵn lẻ
        if (GRID_SIZE % 2 == 0) { // Lưới kích thước chẵn
            return (emptyRowFromBottom % 2 == 0) == (inversions % 2 != 0);
        } else { // Lưới kích thước lẻ
            return inversions % 2 == 0;
        }
    }

    // vẽ các mảnh ảnh
    private void drawTiles(GraphicsContext gc) {
        gc.clearRect(0, 0, GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE);

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                ImagePiece tile = tiles[i][j];
                if (tile != null) {
                    // vẽ ảnh
                    gc.drawImage(tile.getImage(), j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);

                    // vẽ viền đen xung quanh mảnh
                    gc.setStroke(javafx.scene.paint.Color.BLACK);
                    gc.setLineWidth(2);
                    gc.strokeRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    // sự kiện chuột để di chuyển mảnh ảnh
    private void handleMousePress(MouseEvent e) {
        int col = (int) (e.getX() / TILE_SIZE);
        int row = (int) (e.getY() / TILE_SIZE);

        // kiểm tra xem mảnh có kề với ô trống không
        if (isAdjacent(row, col)) {
            // Đổi chỗ mảnh và ô trống
            tiles[emptyX][emptyY] = tiles[row][col];
            tiles[row][col] = null;
            emptyX = row;
            emptyY = col;

            // vẽ lại lưới
            drawTiles(((Canvas) e.getSource()).getGraphicsContext2D());

            // kiểm tra trạng thái thắng
            if (isWin()) {
                drawVictoryScreen();
                //System.out.println("You win!");
                canvas.setOnMousePressed(null);
            }
        }
    }

    // kiểm tra xem mảnh có kề với ô trống không
    private boolean isAdjacent(int row, int col) {
        return (Math.abs(emptyX - row) == 1 && emptyY == col) ||
                (Math.abs(emptyY - col) == 1 && emptyX == row);
    }

    private void drawVictoryScreen() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Xóa toàn bộ canvas
        gc.drawImage(victoryImage, 0, 0, canvas.getWidth(), canvas.getHeight()); // Vẽ ảnh chiến thắng
    }

    // kiểm tra trạng thái thắng
    private boolean isWin() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (tiles[i][j] != null) {
                    if (tiles[i][j].getOriginalX() != i || tiles[i][j].getOriginalY() != j) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // class đại diện cho từng mảng ảnh
    private static class ImagePiece {
        private final Image image;
        private final int originalX, originalY;

        public ImagePiece(Image image, int originalX, int originalY) {
            this.image = image;
            this.originalX = originalX;
            this.originalY = originalY;
        }

        public Image getImage() {
            return image;
        }

        public int getOriginalX() {
            return originalX;
        }

        public int getOriginalY() {
            return originalY;
        }
    }
}