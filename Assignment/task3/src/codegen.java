
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
class Task3 {

    public static Codegen createCodegen() throws CodegenException {
        return new My_Solution_Named_Whatever();
    }
}

class My_Solution_Named_Whatever implements Codegen {

    String assemblyCommands = "";
    
    int ifLabel = 0;
    
    int whileLabel = 0;
    
    int repeatLabel = 0;
    

    @Override
    public String codegen(Program program) throws CodegenException {
        assemblyCommands = "";
        for (Declaration dec : program.decls) 
        {
            codegenDeclarations(dec);
        }
        return assemblyCommands;
    }

    public void codegenDeclarations(Declaration declaration) throws CodegenException {
        assemblyCommands += "\n" + ".data";
        int sizeOfArguments = (2 + declaration.numOfArgs) * 4;
        assemblyCommands += "\n" + ".text";
        assemblyCommands += "\n" + declaration.id + "_entry:";
        assemblyCommands = assemblyCommands + "\n\t" + "move $fp $sp"
                + "\n\t" + "sw $ra 0($sp)"
                + "\n\t" + "addiu $sp $sp -4";
        codegenExpressions((Exp) declaration.body);
        assemblyCommands = assemblyCommands + "\n\t" + "lw $ra 4($sp)"
                + "\n\t" + "addiu $sp $sp " + sizeOfArguments
                + "\n\t" + "lw $fp 0($sp)"
                + "\n\t" + "li $v0, 10"
                + "\n\t" + "syscall";
    
}
public void codegenExpressions(Exp expression) throws CodegenException {
        if(expression instanceof IntLiteral) 
        {
            assemblyCommands = assemblyCommands + "\n\t" + "li $a0 " + ((IntLiteral) expression).n;
        }
        else if(expression instanceof Variable) 
        {
            int offset = 4*((Variable) expression).x;
            assemblyCommands = assemblyCommands + "\n\t" + "lw $a0 " + offset + "($fp)";
        }
        else if(expression instanceof If) 
        {
            ifLabel++;
            String elseBranch = "else_0" + String.valueOf(ifLabel);
            String thenBranch = "then_0" + String.valueOf(ifLabel);
            String exitLabel = "exit_0" + String.valueOf(ifLabel);
            codegenExpressions(((If) expression).l);
            assemblyCommands = assemblyCommands + "\n\t" + "sw $a0 0($sp)"
                                  + "\n\t" + "addiu $sp $sp -4";
            codegenExpressions(((If) expression).r);
            assemblyCommands = assemblyCommands + "\n\t" + "lw $t1 4($sp)"
                                  + "\n\t" + "addiu $sp $sp 4";
            codegenSymbols(((If) expression).comp);
            assemblyCommands += thenBranch;
            assemblyCommands = assemblyCommands + "\n" + elseBranch + ":";
            codegenExpressions(((If) expression).elseBody);
            assemblyCommands = assemblyCommands + "\n\t" + "b " + exitLabel
                                  + "\n" + thenBranch + ":";
            codegenExpressions(((If) expression).thenBody);
            assemblyCommands = assemblyCommands + "\n" + exitLabel + ":";
        }
        else if(expression instanceof Binexp) 
        {
            codegenExpressions(((Binexp) expression).l);
            assemblyCommands = assemblyCommands + "\n\t" + "sw $a0 0($sp)"
                                  + "\n\t" + "addiu $sp $sp -4";
            codegenExpressions(((Binexp) expression).r);
            assemblyCommands = assemblyCommands + "\n\t" + "lw $t1 4($sp)"
                                  + "\n\t" + "addiu $sp $sp 4";
            codegenBinaryOperator(((Binexp) expression).binop);
        }
        else if(expression instanceof Invoke) //couldn't do this
        {
            throw new CodegenException("");
        }
        else if(expression instanceof Skip) 
        {
            assemblyCommands += "\n\t nop";
        }
        else if(expression instanceof While) 
        {
            whileLabel++;
            String loop = "loop_0" + String.valueOf(whileLabel);
            String loopBody = "loopBody_0" + String.valueOf(whileLabel);
            String loopExit = "loopExit_0" + String.valueOf(whileLabel);
            assemblyCommands = assemblyCommands + "\n" + loop + ":";
            assemblyCommands += "\n\t" + "la $t2, " + loopExit;
            assemblyCommands += "\n\t" + "sw $t2, 0($sp)";
            assemblyCommands += "\n\t" + "addiu $sp $sp -4";
            assemblyCommands += "\n\t" + "la $t3, " + loop;
            assemblyCommands += "\n\t" + "sw $t3, 0($sp)";
            assemblyCommands += "\n\t" + "addiu $sp $sp -4";
            codegenExpressions(((While) expression).l);
            assemblyCommands = assemblyCommands + "\n\t" + "sw $a0 0($sp)"
                                  + "\n\t" + "addiu $sp $sp -4";
            codegenExpressions(((While) expression).r);
            assemblyCommands = assemblyCommands + "\n\t" + "lw $t1 4($sp)"
                                  + "\n\t" + "addiu $sp $sp 4";
            codegenSymbols(((While) expression).comp);
            assemblyCommands += loopBody;
            assemblyCommands += "\n\t" + "j " + loopExit;
            assemblyCommands += "\n" + loopBody + ":";
            codegenExpressions(((While) expression).body);
            assemblyCommands += "\n\t" + "j " + loop;
            assemblyCommands = assemblyCommands + "\n" + loopExit + ":";
            assemblyCommands = assemblyCommands + "\n\t" + "lw $t2 4($sp)";
            assemblyCommands = assemblyCommands + "\n\t" + "addiu $sp $sp 4";
            assemblyCommands = assemblyCommands + "\n\t" + "lw $t3 4($sp)";
            assemblyCommands = assemblyCommands + "\n\t" + "addiu $sp $sp 4";
        }
        else if(expression instanceof RepeatUntil) 
        {
            repeatLabel++;
            String repeat = "repeat_0" + String.valueOf(repeatLabel);
            String repeatExit = "repeatExit_0" + String.valueOf(repeatLabel);
            assemblyCommands = assemblyCommands + "\n" + repeat + ":";
            assemblyCommands += "\n\t" + "la $t2, " + repeatExit;
            assemblyCommands += "\n\t" + "sw $t2, 0($sp)";
            assemblyCommands += "\n\t" + "addiu $sp $sp -4";
            assemblyCommands += "\n\t" + "la $t3, " + repeat;
            assemblyCommands += "\n\t" + "sw $t3, 0($sp)";
            assemblyCommands += "\n\t" + "addiu $sp $sp -4";
            codegenExpressions(((While) expression).body);
            codegenExpressions(((While) expression).l);
            assemblyCommands = assemblyCommands + "\n\t" + "sw $a0 0($sp)"
                                  + "\n\t" + "addiu $sp $sp -4";
            codegenExpressions(((While) expression).r);
            assemblyCommands = assemblyCommands + "\n\t" + "lw $t1 4($sp)"
                                  + "\n\t" + "addiu $sp $sp 4";
            codegenSymbols(((While) expression).comp);
            assemblyCommands += repeatExit;
            assemblyCommands += "\n\t" + "j " + repeat;
            assemblyCommands = assemblyCommands + "\n" + repeatExit + ":";
            assemblyCommands = assemblyCommands + "\n\t" + "lw $t2 4($sp)";
            assemblyCommands = assemblyCommands + "\n\t" + "addiu $sp $sp 4";
            assemblyCommands = assemblyCommands + "\n\t" + "lw $t3 4($sp)";
            assemblyCommands = assemblyCommands + "\n\t" + "addiu $sp $sp 4";
        } 
        else if(expression instanceof Assign) 
        {
            int offset = 4*((Assign) expression).x;
            codegenExpressions(((Assign) expression).e);
            assemblyCommands = assemblyCommands + "\n\t" + "sw $a0 " + offset + "($fp)";
        } 
        else if(expression instanceof Break) 
        {            
            assemblyCommands = assemblyCommands + "\n\t" + "jr $t2";
        } 
        else if(expression instanceof Continue) 
        {
            assemblyCommands = assemblyCommands + "\n\t" + "jr $t3";
        } 
        else 
        {
            throw new CodegenException("");
        }
    }
    
    public void codegenSymbols (Comp symbol) { //matches symbols i.e: '= , <, <= , >, >=' accordingly and implements their functions
        if(symbol instanceof Equals) 
        {
            assemblyCommands += "\n\t" + "beq $a0 $t1 ";
        }
        else if(symbol instanceof Less) 
        {
            assemblyCommands += "\n\t" + "blt $t1 $a0 ";
        }
        else if(symbol instanceof LessEq)
        {
            assemblyCommands += "\n\t" + "ble $t1 $a0 ";
        }
        else if(symbol instanceof Greater)
        {
            assemblyCommands += "\n\t" + "bgt $t1 $a0 ";
        }
        else if(symbol instanceof GreaterEq) 
        {
            assemblyCommands += "\n\t" + "bge $t1 $a0 ";
        }
    }

    public void codegenBinaryOperator(Binop binaryOperator) { //matches binary operators i.e: '+,-,*,/' accordingly and implements their functions
        if(binaryOperator instanceof Plus) 
        {
            assemblyCommands += "\n\t" + "add $a0 $t1 $a0";
        } 
        else if(binaryOperator instanceof Minus)
        {
            assemblyCommands += "\n\t" + "sub $a0 $t1 $a0";
        }
        else if(binaryOperator instanceof Times)
        {
            assemblyCommands += "\n\t" + "mul $a0 $t1 $a0";
        } 
        else if(binaryOperator instanceof Div)
        {
            assemblyCommands = assemblyCommands + "\n\t" + "div $t1, $a0" 
                                  + "\n\t" + "mflo $a0";
        }
    }

}