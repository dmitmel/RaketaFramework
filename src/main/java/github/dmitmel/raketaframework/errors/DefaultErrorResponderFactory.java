package github.dmitmel.raketaframework.errors;

public class DefaultErrorResponderFactory {
    private DefaultErrorResponderFactory() {
        throw new RuntimeException("Can\'t create instance of DefaultErrorResponderFactory");
    }
    

    public static ErrorResponder makeResponderForError(HTTPError target) {
        return target::toString;
    }
}
