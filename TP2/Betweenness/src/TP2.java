public class TP2 {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length < 1) System.out.println("Erreur entrée, pas d'arguments");
        else if(args.length == 1){
            String name = args[0];
            Parser p = new Parser();
            p.parser(name);
            p.printResult2();
            p.calculBetweenness();
       }else{
            String name = args[0];
            int[] sommet = new int[args.length-1];
            try{
                for(int i = 1; i < args.length; i++){
                    sommet[i-1] = Integer.parseInt(args[i]);
                }
            }catch(NumberFormatException e){
                System.out.println("Erreur arguments, entrez des numeros de sommets");
                System.exit(0);
            }
            Parser p = new Parser();           
            p.parser(name);
            p.printResult2();
            p.calculBetweenness2(sommet);
                      
            System.out.println("Memoire allouee : " + (Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory())+ " octets"); 
       }
    }
    
}
