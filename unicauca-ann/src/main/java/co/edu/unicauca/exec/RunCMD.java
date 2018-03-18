package co.edu.unicauca.exec.cross_validation.harmony;

import Client.CommandProcessor;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Daniel Pusil<danielpusil@unicauca.edu.co>
 *
 */
public class RunCMD {

    public static void main(String[] args) {

        CommandProcessor exp = new CommandProcessor();
        JMetalRandom.getInstance().setRandomGenerator(null);//necesaria para ejecutar los experimentos

        try {
            //args = "-p Iris -A ihs -T ihsparametros.txt".split(" ");//syntays Example 
            exp.execute(args);
            
        } catch (IOException ex) {
            Logger.getLogger(RunCMD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
