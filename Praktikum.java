import java.io.*;
public class Praktikum{
    // Anfang Attribute
    static final char EOF=(char)255;
    // Zeiger auf das aktuelle Eingabezeichen
    static int pointer = 0;
    // Zeiger auf das Ende der Eingabe
    static int maxPointer = 0;
    // Eingabe zeichenweise abgelegt
    static char input[];
    // Ende Attribute

    public static void main(String args[]){
        // Anlegen des Arrays f�r den zu parsenden Ausdruck
        input = new char[256];
        
        // Einlesen der Datei und Aufruf des Parsers    
        if (readInput("Input.txt"))
        if (expression(0) && inputEmpty())
            System.out.println("Korrekter Ausdruck");
        else
            System.out.println("Fehler im Ausdruck"); 
    }//main

    //Grammatik
    
    //Olcayto Gutt
    //expression -> term rightExpression
    static boolean expression(int t){
        ausgabe("expression -> ", t);
        return term(t+1) && rightExpression(t+1);
    }

    //Melih Canitez
    //rightExpression -> '+' term RightExpression | '-' term righExpression | leeres Wort
    static boolean rightExpression(int t){
        ausgabe("rightExpression -> ", t+1);
        char[] matchSet = {'+', '-'};
        if (match(matchSet, t))
            return term(t+1) && rightExpression(t+1);
        
        ausgabe("Epsilon", t);
        return true;
    }

    //Leon Kühne
    //term -> operator rightTerm
    static boolean term(int t){
        ausgabe("term -> ", t);
        return operator(t+1) && rightTerm(t+1);
    }

    //Olcayto Gutt
    //rightTerm -> '*' operator rightTerm|'/' operator rightTerm| leeres Wort
    static boolean rightTerm(int t) {
        ausgabe("rightTerm -> ", t);
        char[] matchSet = {'*', '/'};
        if (match(matchSet,t+1))
            return operator(t+1) && rightTerm(t+1);
        
        ausgabe("Epsilon", t);
        return true;
    }

    //Vorlage
    static boolean digit(int t){
        char [] matchSet = {'1','2','3','4','5','6','7','8','9','0'};
        ausgabe("digit->",t);     //Syntaxbaum ausgeben
        if (match(matchSet,t+1)){   //digit->'1'|'2'...|'9'|'0'
            return true;                // korrekte Ableitung der Regel m�glich
        }else{
            syntaxError("Ziffer erwartet"); // korrekte Ableitung der Regel  
            return false;                   // nicht m�glich
        }
    }//digit
    
    //Leon Kühne, Olcayto Gutt und Melih Canitez
    static boolean operator(int t) {
        char[] openMatchSet = {'('};
        char[] closedMatchSet = {')'};

        // operator -> (expression)
        ausgabe("Operator -> ", t); //Syntaxbaum ausgabe
        if(match(openMatchSet, t+1)){
            if(expression(t+1)){  //Guckt ob in der Klammer eine Expression ist
                if(match(closedMatchSet, t+1)){
                    return true;
                }
                else{
                    syntaxError(") wird erwartet");
                    return false;
                }
            }
            else{
                syntaxError("Expression wurde erwartet");
                return false;
            }
        }
        else{ // Operator -> num
            return num(t+1);
        }
    }

    //Vorlage
    // num -> digit num | digit
    static boolean num(int t){
        char [] digitSet = {'1','2','3','4','5','6','7','8','9','0'};
        ausgabe("num->", t);          //Syntaxbaum ausgeben
        if (lookAhead(digitSet))
            return digit(t+1)&& num(t+1);   //num->digit num
        else 
            return digit(t+1);          //num->digit   
    }//num

    //Hilfsmethoden(Vorlage)
    //Vorlage
    static boolean match(char [] matchSet,int t){
        for (int i=0;i<matchSet.length;i++)
        if (input[pointer]==matchSet[i]){
            ausgabe(" match: "+input[pointer],t);
            pointer++;  //Eingabepointer auf das n�chste Zeichen setzen 
            return true;
        }
        return false;
    }//match
    
    //Vorlage
    static boolean lookAhead(char [] aheadSet){
        for (int i=0;i<aheadSet.length;i++)
        if (input[pointer+1]==aheadSet[i])
            return true;
        return false;
    }//lookAhead

    //Vorlage
    static boolean readInput(String name){
        int c=0;
        try{
        FileReader f = new FileReader(name);
        for(int i=0;i<256;i++){
            c = f.read();
            if (c== -1){
            maxPointer=i;
            input[i]=EOF;
            break;
            }else
            input[i]=(char)c;
        } 
        }
        catch(Exception e){
        System.out.println("Fehler beim Dateizugriff: "+name);
        return false;
        }
        return true;  
    }//readInput

    //Vorlage
    static boolean inputEmpty(){
        if (pointer==maxPointer){
        ausgabe("Eingabe leer!",0);
        return true;
        }else{
        syntaxError("Eingabe bei Ende des Parserdurchlaufs nicht leer");
        return false;
        }
    }//inputEmpty

    //Vorlage
    static void ausgabe(String s, int t){
        for(int i=0;i<t;i++)
        System.out.print("  ");
        System.out.println(s);
    }//ausgabe

    //Vorlage
    static void syntaxError(String s){
        System.out.println("Syntax Fehler beim "+(pointer+1)+" Zeichen: "
                +input[pointer]);
        System.out.println(s);  
    }//syntaxError
}