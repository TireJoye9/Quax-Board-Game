package swe2.co.sweprojectsht;

import javafx.application.Platform;
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