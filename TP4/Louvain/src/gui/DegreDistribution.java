
package gui;

/**
 *
 * @author Oussama
 */
public class DegreDistribution {
    private final int degre;
    private final int total_node;

    /**
     *
     * @param degre
     * @param total_node
     */
    public DegreDistribution(int degre, int total_node) {
        this.degre = degre;
        this.total_node = total_node;
    }

    public int getDegre() {
        return degre;
    }

    public int getTotal_node() {
        return total_node;
    }
    
}
