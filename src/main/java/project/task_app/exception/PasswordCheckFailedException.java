package project.task_app.exception;

public class PasswordCheckFailedException extends RuntimeException {
    public PasswordCheckFailedException(String message) {
        super(message);

    }
}
