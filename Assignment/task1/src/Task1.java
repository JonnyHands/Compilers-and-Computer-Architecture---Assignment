/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 198397
 */

import java.util.List;

class LexicalException extends Exception {

    public String msg;

    public LexicalException(String _msg) {
        msg = _msg;
    }
}

class Task1Exception extends Exception {

    public String msg;

    public Task1Exception(String _msg) {
        msg = _msg;
    }
}

interface Lexer {

    public List<Token> lex(String input) throws LexicalException, Task1Exception;
}

class Task1 {

    public static MyLexer create() {
        return new MyLexer();

    }
}
