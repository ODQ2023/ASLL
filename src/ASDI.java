import java.util.List;

public class ASDI implements Parser{
    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;


    public ASDI(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }

    @Override
    public boolean parse() {
        Q();
        if(preanalisis.tipo == TipoToken.EOF && !hayErrores){
            System.out.println("Consulta correcta");
            return  true;
        }else {
            System.out.println("Se encontraron errores");
        }
        return false;
    }

    // Q -> select D from T
    private void Q(){
        match(TipoToken.SELECT);
        D();
        match(TipoToken.FROM);
        T();
    }

    // D -> distinct P | P
    private void D(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.DISTINCT){
            match(TipoToken.DISTINCT);
            P();
        }
        else if (preanalisis.tipo == TipoToken.ASTERISCO
                || preanalisis.tipo == TipoToken.IDENTIFICADOR
                || preanalisis.tipo == TipoToken.ID) {
            P();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba 'distinct' or '*' or 'identificador'");
        }
    }
    //T->T2T1
    private void T(){
        if(hayErrores)
            return;
        if((preanalisis.tipo == TipoToken.ID)||(preanalisis.tipo == TipoToken.IDENTIFICADOR)){
            T2();
            T1();
        }
        else {
            hayErrores=true;
            System.out.println("Se esperaba un identificador,coma o id");
        }

    }
    //T1->,T|Ɛ
    private void T1(){
        if(preanalisis.tipo == TipoToken.COMA) {
            match(TipoToken.COMA);
            T();
        }
    }
    //T2->idT3
    private void T2(){
        if (hayErrores)
            return;
        if (preanalisis.tipo == TipoToken.ID){
            match(TipoToken.ID);
            T3();
        }
        else if (preanalisis.tipo == TipoToken.IDENTIFICADOR){
            match(TipoToken.IDENTIFICADOR);
            T3();
        }
        else {
            hayErrores = true;
            System.out.println("Se esperaba Identificador");
        }
    }

    private void T3(){
        if (preanalisis.tipo == TipoToken.IDENTIFICADOR){
            match(TipoToken.IDENTIFICADOR);
        }
        if (preanalisis.tipo == TipoToken.ID){
            match(TipoToken.ID);
        }
    }

    // P -> * | A
    private void P(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.ASTERISCO){
            match(TipoToken.ASTERISCO);
        }
        else if(preanalisis.tipo == TipoToken.IDENTIFICADOR
                || preanalisis.tipo == TipoToken.ID){
            A();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '*' or 'identificador'");
        }
    }

    // A -> A2 A1
    private void A(){
        if(hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.ID){
            A2();
            A1();
        }
        else if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
            A2();
            A1();
        }
        else {
            hayErrores=true;
            System.out.println("Se esperaba un identificador,coma o id");
        }
    }

    // A2 -> id A3
    private void A2(){
        if(hayErrores)
            return;
        if (preanalisis.tipo == TipoToken.ID) {
            match(TipoToken.ID);
            A3();
        } else{
            hayErrores = true;
            System.out.println("Se esperaba un 'identificador'");
        }
    }

    // A1 -> ,A | Ɛ
    private void A1(){
        if (preanalisis.tipo==TipoToken.COMA){
            match(TipoToken.COMA);
            A();
        }
    }

    // A3 -> . id | Ɛ
    private void A3(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.PUNTO){
            match(TipoToken.PUNTO);
            if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
            match(TipoToken.IDENTIFICADOR);}
            if(preanalisis.tipo == TipoToken.ID){
                match(TipoToken.ID);}
        }
    }


    private void match(TipoToken tt){
        if(preanalisis.tipo == tt){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            hayErrores = true;
            System.out.println(preanalisis.tipo + ": No coincide");
        }

    }

}
