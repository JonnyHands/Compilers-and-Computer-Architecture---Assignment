

import java.util.ArrayList;
import java.util.Arrays;
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
public class MyLexer implements Lexer {

    public String[] words = new String[]{"def", "if", "then", "else", "skip",
        "while", "do", "repeat", "until", "break", "continue"}; //sets a list of the keywords

    public String[] symbols = new String[]{";", "(", ")", "=", "==", "<", ">", "<=", ">=", ",", "{", "}", ":=", "+", "*", "-", "/"}; //sets a list of the symbols

    public String identifiers = "\\b[A-Za-z][A-Za-z0-9_]*\\b"; //sets a list of the identifiers using regex

    @Override
    public List<Token> lex(String input) throws LexicalException, Task1Exception {
        List<String> wordsList = Arrays.asList(words); //creates array of word
        List<String> symbolsList = Arrays.asList(symbols); //creates array of symbols
        List<String> identifiersList = Arrays.asList(identifiers); //creates array of identifiers
        ArrayList<Token> tokens = new ArrayList<>(); //creates arraylist of tokens
        String[] split = input.split("\\s+");
        for (int i = 0; i < split.length; i++) 
        {
            if (wordsList.contains(split[i])) //checks if keyword and returns if so
            { 
                tokens.add(getKeyword(split[i]));
            } 
            else if (symbolsList.contains(split[i])) //checks if symbol and returns if so
            { 
                tokens.add(getSymbol(split[i]));
            } 
            else if (isInt(split[i])) //checks if int and returns if so
            { 
                tokens.add(new T_Integer(Integer.parseInt(split[i])));
            } 
            else if (identifiersList.contains(split[i])) //checks if identifier and returns if so
            { 
                tokens.add(getIdentifier(split[i]));
            } 
            else //if doesnt match any of above throws a Lexical exception
            { 
                throw new LexicalException("");
            }
        }
        return tokens; //returns converted tokens at the end
    }

    private Token getKeyword(String keyword) throws LexicalException {
        if (null == keyword) 
        { //if null throws exception
            throw new LexicalException("");
        } 
        else 
        {
            switch (keyword) { //switch to check through all potential cases
                case "def":
                    return new T_Def();
                case "skip":
                    return new T_Skip();
                case "if":
                    return new T_If();
                case "then":
                    return new T_Then();
                case "else":
                    return new T_Else();
                case "while":
                    return new T_While();
                case "do":
                    return new T_Do();
                case "repeat":
                    return new T_Repeat();
                case "until":
                    return new T_Until();
                case "break":
                    return new T_Break();
                case "continue":
                    return new T_Continue();
                default:
                    throw new LexicalException("");  //if doesnt match throws exception
            }
        }
    }

    private Token getSymbol(String symbol) throws LexicalException {
        if (null == symbol) { //if null throws exception
            throw new LexicalException("");
        } else {
            switch (symbol) { //switch to check through all cases of symbols
                case ";":
                    return new T_Semicolon();
                case "(":
                    return new T_LeftBracket();
                case ")":
                    return new T_RightBracket();
                case "=":
                    return new T_EqualDefines();
                case "==":
                    return new T_Equal();
                case "<":
                    return new T_LessThan();
                case ">":
                    return new T_GreaterThan();
                case "<=":
                    return new T_LessEq();
                case ">=":
                    return new T_GreaterEq();
                case ",":
                    return new T_Comma();
                case "{":
                    return new T_LeftCurlyBracket();
                case "}":
                    return new T_RightCurlyBracket();
                default:
                    throw new LexicalException(""); //if doesnt match throws exception
            }
        }
    }

    private boolean isInt(String integer) throws LexicalException { //method to check if is an integer
        if ("\\d+".equals(integer))
        { //checks if string as an integer
            return true;
        } else 
        {
            return false;
        }
    }

    private Token getIdentifier(String identifier) throws LexicalException {
        if (identifier.matches(identifiers)) 
        { //checks if the token matches the identifiers list
            return new T_Identifier(identifier); //if so adds a new identifier token 
        } else 
        {
            throw new LexicalException(""); //if doesnt match throws exception
        }
    }
}

