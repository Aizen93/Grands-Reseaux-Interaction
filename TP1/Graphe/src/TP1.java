public class TP1 {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       if(args.length > 1){
            System.out.println("Erreur entrée, trop d'arguments");
       }else{
            String name = args[0];
            Parser p = new Parser();
            
            p.parser(name);
            p.printResult();
                      
            System.out.println("Memoire allouee : " + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()) / 1_048_576 + " Mo"); 
       }
    }
    
}
