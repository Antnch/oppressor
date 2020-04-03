public class Test {
    static class CanvasClient {
        public CanvasClient(CanvasHandler handler) {
        }

        public void open(String token, String board) {

        }
    }

    interface CanvasConnection {

    }

    interface CanvasHandler {
        void onOpened(CanvasConnection connection);
    }

    interface Variables {
        <T> T get(String key, Class<T> valueClass);
    }

    enum ScenarioContinuation {
        RESUME,
        PAUSE,
        ABORT
    }

    interface ScenarioTask {
        ScenarioContinuation run(Variables variables);
    }

    interface CanvasScenarioTask extends ScenarioTask {

    }

    interface Scenario {
        void resume(Canvas canvas); // Перейти к следующей задаче
    }

    interface Canvas {

    }

    static class OpenCanvasTask implements ScenarioTask {

        @Override
        public ScenarioContinuation run(Variables variables) {
            return ScenarioContinuation.ABORT;
        }
    }

    enum TaskState {
        SUCCESS,
        FAIL,
        WAITING,
        NOT_STARTED
    }

    static class CreateStickerTask implements CanvasScenarioTask {
        public CreateStickerTask() {
            this.setFailTimeout(30000);
        }

        @Override
        public void run(Variables variables, Promise<TaskState> promise) {
            Canvas canvas = variables.get(Canvas.class);
            canvas.createSticker("test", new StickerCallback() {
                void complete(Sticker sticker) {
                    if (promise.isResolved()) {
                        promise.resolve(TaskState.SUCCESS);
                    }
                }
                void failure() {
                    promise.error(TaskState.FAIL);
                }
            });
        }
    }

    static final CREATE_STICKER_SCENARIO = Arrays.asList(
            new OpenCanvasTask(),
      new CreateStickerTask()
  );


    public static void threadMain(Variables variables) {
        String token = variables.get("token", String.class);
        String board = variables.get("board", String.class);

        Scenario scenario = scenarioFactory.create(CREATE_STICKER_SCENARIO);

        CanvasHandler handler = new CanvasHandler() {
            @Override
            public void onOpened(CanvasConnection connection) {
                scenario.resume(connection);
            }

            @Override
            public void onMessage(CanvasConnection connection, CanvasMessage message) {
                if (message instanceof StickerResponse) {
                    scenario.???
                }
            }

            @Override
            public void onClosed(CanvasConnection connection) {
                if (iWantToContinueScenario) {
                    reconnect();
                } else {
                    scenario.abort();
                }
            }
        };
        CanvasClient client = new CanvasClient(handler);
        client.open(token, board);

//        client.join();
        scenario.join();
    }
}


