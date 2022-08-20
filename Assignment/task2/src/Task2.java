
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 198397
 */
interface Parser {

    public Block parse(List<Token> input) throws SyntaxException,
            Task2Exception;
}

class SyntaxException extends Exception {

    public String msg;

    public SyntaxException(String _msg) {
        msg = _msg;
    }
}

class Task2Exception extends Exception {

    public String msg;

    public Task2Exception(String _msg) {
        msg = _msg;
    }
}

public class Task2 {

    public static Parser create() {
        Parser parser = new Parser() {
            List<Exp> exps = new ArrayList<Exp>();

            @Override
            public Block parse(List<Token> input) throws SyntaxException, Task2Exception {
                if (input.get(0) instanceof T_LeftCurlyBracket) 
                { // start of block {
                    try {input.remove(0);} 
                    catch (Exception e) {
                        throw new Task2Exception("");}
                    exps.add((Exp) new BlockExp(parse(input)));
                } 
                else if (input.get(0) instanceof T_RightCurlyBracket) //potential end of block}
                { 
                    input.remove(0);
                } 
                else
                { //ENE
                    Token t = input.get(0);
                    if (input.get(0) instanceof T_Integer) 
                    { //E check for int
                        input.remove(0);
                        exps.add((Exp) new IntLiteral(((T_Integer) t).n));
                        parse(input);
                    }
                    if (input.get(0) instanceof T_Skip) 
                    { //E check for skip
                        input.remove(0);
                        exps.add((Exp) new Skip());
                        parse(input);
                    }
                    if (input.get(0) instanceof T_Semicolon) 
                    { //E check for block (E;)
                        input.remove(0);
                        parse(input);
                    }
                    if (input.get(0) instanceof T_Integer) 
                    { // final INT ->
                        input.remove(0);
                        exps.add((Exp) new IntLiteral(((T_Integer) t).n));
                        parse(input);
                    }
                    else 
                    {
                        throw new SyntaxException("");
                    }
                }
                return new Block(exps);
            }
        };
        return parser;
    }

}
