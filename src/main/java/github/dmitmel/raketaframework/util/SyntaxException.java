package github.dmitmel.raketaframework.util;

public class SyntaxException extends RuntimeException {
    public final String description;
    public final CharSequence line;
    public final int index;
    
    public SyntaxException(String description, CharSequence line, int index) {
        this.description = description;
        this.line = line;
        this.index = index;
    }
    
    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(description);
        
        if (index >= 0) {
            builder.append(" near index ");
            builder.append(index);
        }
        
        builder.append('\n').append(line);
        
        if (index >= 0) {
            builder.append('\n');
            for (int i = 0; i < index; i++) builder.append(' ');
            builder.append('^');
        }
        
        return builder.toString();
    }
}
