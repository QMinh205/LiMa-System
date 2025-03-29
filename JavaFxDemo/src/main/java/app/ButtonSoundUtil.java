package app;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class ButtonSoundUtil {
    private static final MediaPlayer CLICK_SOUND;

    static {
        // Chỉ tạo một lần MediaPlayer cho âm thanh
        String soundPath = "file:/E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/assets/mouseClick.wav";
        Media sound = new Media(soundPath);
        CLICK_SOUND = new MediaPlayer(sound);
    }

    public static void addClickSound(Button button) {
        // Thêm sự kiện khi nhấn nút, không tải lại âm thanh
        button.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            CLICK_SOUND.seek(javafx.util.Duration.ZERO); // Đảm bảo âm thanh bắt đầu từ đầu
            CLICK_SOUND.play(); // Phát âm thanh
        });
    }
}
