package Client;

import interfaces.Task;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Daniel Pusil<danielpusil@unicauca.edu.co>
 */
public class CommandProcessor {

    ArrayList algorithmsLst = new ArrayList();
    ArrayList problemsLst = new ArrayList();
    ArrayList parametersLst = new ArrayList();
    Task taskToBuild = null;

    public void execute(String[] args) throws IOException {

        //the following lines are examples of use of the command processor 
        //syntax -p [Problems] -A [Algorithms] -T [algorithmParameters.txt]
//         args = "-p Iris Leaf -A DEbin -T DEbinparametros.txt".split(" ");
//         args = "-p Iris -A DEexp -T DEexpparametros.txt".split(" ");
//         args = "-p Iris -A ghs -T ghsparametros.txt".split(" ");
//         args = "-p Iris -A ihs -T ihsparametros.txt".split(" ");
//         args = "-p Iris -A nghs -T nghsparametros.txt".split(" ");
//         args = "-p Iris -A m_elm -T m_elmparametros.txt".split(" ");
//         args = "-p Iris -A pso -T psoparametros.txt".split(" ");
//         args = "-p Iris Leaf -A DEbin -T DEbinparametros.txt".split(" ");
//        args = "-p Iris -A hs -T hsparametros.txt".split(" ");
//        args = "-p Banknote -A pso -T psoparametros.txt".split(" ");
//        args = "-p Iris -A cmaes -T cmaesparametros.txt".split(" ");
        if (args.length > 0) {
            readComand(args);
        }

        String paquete = "co.edu.unicauca.problem.cross_validation.";
        BuildExperiment exp = new BuildExperiment();

        if (taskToBuild != null) {//set Package
            if (taskToBuild.getTypeValidation().equalsIgnoreCase("CV")) {
                paquete = "co.edu.unicauca.problem.cross_validation.";
            } else {
                paquete = "co.edu.unicauca.problem.training_testing.";
                exp.setDEFAULT_EFOS(exp.getDEFAULT_EFOS() * 10);//By deafult, *10 because be 10 folders and MAXEFOs=300 
            }
        }
        ArrayList parameters = null;

        for (int i = 1; i < problemsLst.size(); i++) {
            for (int j = 1; j < algorithmsLst.size(); j++) {
                if (parametersLst.size() > 0) {//see parameters file
                    ArrayList<ArrayList> parametersFile = readParameter((String) algorithmsLst.get(j) + "parametros.txt");
                    exp.setTarea(taskToBuild);
                    if (taskToBuild != null) {
                        parameters = parametersFile.get(taskToBuild.getConfiguracion());
                        builderAlgorithm(exp, paquete, i, j, taskToBuild.getConfiguracion(), parameters);
                    } else {
                        for (int k = 0; k < parametersFile.size(); k++) {
                            parameters = parametersFile.get(k);

                            builderAlgorithm(exp, paquete, i, j, k, parameters);
                        }
                    }
                    exp.setTarea(null);
                }
                System.gc();
            }
        }
        System.gc();

    }

    public void builderAlgorithm(BuildExperiment exp, String paquete, int i, int j, int k, ArrayList parameters) {
        String indicator = "c";//Indicate the configuration Line in the file parameter.txt
        if ("pso".equalsIgnoreCase(algorithmsLst.get(j) + "")) {
            exp.PSO(paquete + problemsLst.get(i), parameters, indicator + k);
        } else if ("hs".equalsIgnoreCase(algorithmsLst.get(j) + "")) {
            exp.HS(paquete + problemsLst.get(i), parameters, indicator + k);
        } else if ("ghs".equalsIgnoreCase(algorithmsLst.get(j) + "")) {
            exp.GHS(paquete + problemsLst.get(i), parameters, indicator + k);
        } else if ("ihs".equalsIgnoreCase(algorithmsLst.get(j) + "")) {
            exp.IHS(paquete + problemsLst.get(i), parameters, indicator + k);
        } else if ("nghs".equalsIgnoreCase(algorithmsLst.get(j) + "")) {
            exp.NGHS(paquete + problemsLst.get(i), parameters, indicator + k);
        } else if ("DEexp".equalsIgnoreCase(algorithmsLst.get(j) + "") || "Debin".equalsIgnoreCase(algorithmsLst.get(j) + "")) {
            exp.DE(paquete + problemsLst.get(i), parameters, algorithmsLst.get(j) + indicator + k);
        } else if ("cmaes".equalsIgnoreCase(algorithmsLst.get(j) + "")) {
            exp.CMAES(paquete + problemsLst.get(i), parameters, indicator + k);
        } else if ("m_elm".equalsIgnoreCase(algorithmsLst.get(j) + "")) {
            exp.MA_ELM(paquete + problemsLst.get(i), parameters, indicator + k);
        } else if ("RWELM".equalsIgnoreCase(algorithmsLst.get(j) + "")) {
            exp.RWELM(paquete + problemsLst.get(i), parameters, indicator + k);
        } else {
            System.out.println("Algorithms no Found " + taskToBuild);
        }
    }

    public void readComand(String[] args) {//Change the implement to a MAP<Key,Value>
        boolean algoritmo = false;
        boolean parametro = false;
        boolean problema = false;
        for (String p : args) {
            if (p.contains("-")) {
                if ("-p".equalsIgnoreCase(p)) {//indicate the problem name
                    problema = true;
                    algoritmo = false;
                    parametro = false;
                } else if ("-a".equalsIgnoreCase(p)) {//indicate algorithm 
                    problema = false;
                    algoritmo = true;
                    parametro = false;
                } else {
                    problema = false;
                    algoritmo = false;
                    parametro = true;
                }
            }
            if (algoritmo) {
                algorithmsLst.add(p);
            } else if (problema) {
                problemsLst.add(p);
            } else if (parametro) {
                parametersLst.add(p);
            }
        }
    }

    public ArrayList<ArrayList> readParameter(String fileName) throws FileNotFoundException, IOException {
        BufferedReader br = null;
        FileReader fr = null;

        ArrayList<ArrayList> parameters = new ArrayList<>();
        fr = new FileReader(fileName);
        br = new BufferedReader(fr);
        String actualLine;

        while ((actualLine = br.readLine()) != null) {
            String[] aux = actualLine.split(" ");
            if (!aux[0].contains("#")) {//Is a line with parameters
                ArrayList lineParametros = new ArrayList<>();
                for (String value : aux) {
                    lineParametros.add(value);
                }
                parameters.add(lineParametros);
            }
            aux = null;
        }

        return parameters;
    }

    public Task getTarea() {
        return taskToBuild;
    }

    public void setTarea(Task tarea) {
        this.taskToBuild = tarea;
    }

}
