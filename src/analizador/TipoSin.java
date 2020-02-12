/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador;

/**
 *
 * @author Guill
 */
public enum TipoSin {
    PACKAGED("((\\s)*)?((package)"
            + "(\\s)?)*\\s([A-Z]+)?[a-z]+([0-9]+)?(\\s)?((;)*)(\\s)*"),
    CLASS("((\\s)*)?((public|private))?(\\s)*((class))((\\s)*)([A-Z]+)?[a-z]+([0-9]+)?((\\s)*)"
            + "((extends)*)?((\\s)*)([A-Z]+)?[a-z]+([0-9]+)?((\\s)*)(\\{)?(\\s)*"),
    MAIN("((\\s)*)?(public)(\\s)*(static)(\\s)*(void)(\\s)*(([A-Z]+)?[a-z]+([0-9]+)*)(\\s)*(\\()(\\s)*(String)"
            + " ((\\[\\])|((\\[)(\\s)*(\\])))(\\s)*(([A-Z]+)?[a-z]+([0-9]+)*)(\\s)*(\\))(\\s)*(\\{)(\\s)*"),
    METHOD(	"(\\s)*(public)?(\\s)*(String|int|boolean|void)(\\s)*([A-Z]+)?[a-z]+([0-9]+)?(\\s)*"
            + "(\\()(\\s)*((int|String|boolean)(\\s)*([A-Z]+)?[a-z]+([0-9]+)?)?(\\s)*(\\))((\\s)*)?(\\{)?(\\s)*"),
    CHARSQC("(\\s)*((\\{)|(\\()|(\\[)|(\\])|(\\))|(\\}))(\\s)*"),
    RETURNST("(\\s)*(return)(\\s)*(([A-Z]+)?([a-z]+)?([0-9]+)?)(\\s)*(;)(\\s)*"),
    VARDEC("(\\s)*(int|boolean|String|double)(\\s)*((([A-Z]+)?[a-z]+([0-9]+)?(\\s)*(;))|"
            + "([A-Z]+)?[a-z]+([0-9]+)?((\\s)*)?([=])(\\s)*(([A-Z]+)?([a-z]+)?([0-9]+)?((\\s)*)?)(\\s)*(;))(\\s)*"),
    EXP("(\\s)*(\\w)+([+|-|=|*|/])+((\\w)+)?(((([+|-|*|/])+)?((\\w)+)?)+)?(\\s)*(;)"),
    PRINT("(\\s)*(System.out.println)(\\s)*(\\()((\\s)*(\\\"((\\s)*((.)*)?(\\s)*\\\"[+]?)*)(\\s)*(\\))(\\s)*(;)(\\s)*)")
    ;
    
    public final String patron;
    
     TipoSin(String s) {
        this.patron = s;
     }
}
