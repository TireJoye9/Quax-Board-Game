package swe2.co.sweprojectsht;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Window;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.fail;

class FxTestUtils {

    static void initJavaFx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {
            // JavaFX already started
        }
    }

    static void runOnFxThreadAndWait(FxRunnable action) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Holder<Throwable> errorHolder = new Holder<>();

        Platform.runLater(() -> {
            try {
                action.run();
            } catch (Throwable t) {
                errorHolder.value = t;
            } finally {
                latch.countDown();
            }
        });

        boolean completed = latch.await(5, TimeUnit.SECONDS);
        if (!completed) {
            fail("Timed out waiting for JavaFX action");
        }

        if (errorHolder.value != null) {
            throw new RuntimeException(errorHolder.value);
        }
    }

    static <T> T callOnFxThreadAndWait(FxSupplier<T> supplier) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Holder<T> resultHolder = new Holder<>();
        Holder<Throwable> errorHolder = new Holder<>();

        Platform.runLater(() -> {
            try {
                resultHolder.value = supplier.get();
            } catch (Throwable t) {
                errorHolder.value = t;
            } finally {
                latch.countDown();
            }
        });

        boolean completed = latch.await(5, TimeUnit.SECONDS);
        if (!completed) {
            fail("Timed out waiting for JavaFX action");
        }

        if (errorHolder.value != null) {
            throw new RuntimeException(errorHolder.value);
        }

        return resultHolder.value;
    }

    static DialogPane findShowingDialogPaneByHeader(String headerText) {
        for (Window window : Window.getWindows()) {
            if (!window.isShowing()) {
                continue;
            }

            Scene scene = window.getScene();
            if (scene == null) {
                continue;
            }

            if (scene.getRoot() instanceof DialogPane dialogPane) {
                if (headerText.equals(dialogPane.getHeaderText())) {
                    return dialogPane;
                }
            }
        }
        return null;
    }

    static DialogPane findShowingDialogPaneByTitle(String title) {
        for (Window window : Window.getWindows()) {
            if (!window.isShowing()) {
                continue;
            }

            Scene scene = window.getScene();
            if (scene == null) {
                continue;
            }

            if (scene.getRoot() instanceof DialogPane dialogPane) {
                if (dialogPane.getScene() != null &&
                        dialogPane.getScene().getWindow() != null &&
                        title.equals(dialogPane.getScene().getWindow().getScene().getWindow().getClass().getSimpleName())) {
                    return dialogPane;
                }
            }
        }
        return null;
    }

    static void clickDialogButton(DialogPane dialogPane, String buttonText) {
        for (ButtonType buttonType : dialogPane.getButtonTypes()) {
            if (buttonText.equals(buttonType.getText())) {
                Node button = dialogPane.lookupButton(buttonType);
                ((ButtonBase) button).fire();
                return;
            }
        }
        fail("Could not find dialog button with text: " + buttonText);
    }

    @FunctionalInterface
    interface FxRunnable {
        void run() throws Exception;
    }

    @FunctionalInterface
    interface FxSupplier<T> {
        T get() throws Exception;
    }

    static class Holder<T> {
        T value;
    }
}
