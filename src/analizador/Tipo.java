
package analizador;
/**
 *
 * @author Guill
 */

public enum Tipo {
    SYSTEM_RESERVED("(class|public|static|void|extends|return|if|while|"
            + "System.out.println|lenght|case|break|this|do|package|"
            + "new|main|true|false|int|boolean|String|float)"),
    
    STRING("\"((.)+)?\""),
    INTEGER("^\\d+$"),
    EXPRESSION("(\\w)+([+|-|=|*|/])+((\\w)+)?(((([+|-|*|/])+)?((\\w)+)?)+)?"),
    IDENTIFIERS("([A-Z]+)?[a-z]+([0-9]+)?"),
    SIMBOLS("\\p{Punct}*")
    ;
    

    public final String patron;

    Tipo(String s) {
        this.patron = s;

    }
   
}
