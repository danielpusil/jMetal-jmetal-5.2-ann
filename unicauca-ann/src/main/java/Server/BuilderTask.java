package Server;

import interfaces.Task;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuilderTask {

    private static final int MAXEXPERIMENT = 30;

    public ArrayList<Task> getTaks() {
        ArrayList<Task> tasksToMake = new ArrayList();
        ArrayList algorithms = getAlgorithms();
        ArrayList problems = getProblems();
        ArrayList typesValidations = getTypeValidacion();
        ArrayList typesSolucions = getSolutionRepresentation();

        int idTaks = 0;
        for (Object tipoSolucion : typesSolucions) {
            for (Object tipoValidacion : typesValidations) {
                for (Object algoritmo : algorithms) {
                    try {
                        ArrayList<ArrayList> parametersFile = readParameter(algoritmo + "parametros.txt");
                        for (int k = 0; k < parametersFile.size(); k++) {
                            for (int i = 1; i < MAXEXPERIMENT; i++) {//i values is the SEED
                                for (Object problema : problems) {
                                    Task tarea = new Task(i, -1, -1, -1, 0, 0, problema + "", algoritmo + "", k, tipoValidacion + "", Integer.parseInt(tipoSolucion + ""), idTaks);
                                    idTaks++;
//                                    if (!isMakeInFile(tarea)) {//only IF is Socke implemetation to chek if file contient that task
                                    tasksToMake.add(tarea);
//                                    }
                                }
                            }
                        }

                    } catch (IOException ex) {
                        Logger.getLogger(BuilderTask.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }


        System.out.println("Total  por hacer : " + tasksToMake.size());
        return tasksToMake;
    }

    public ArrayList getAlgorithms() {
        ArrayList algList = new ArrayList();
        algList.add("HS");
        algList.add("GHS");
        algList.add("IHS");
        algList.add("NGHS");
        algList.add("M_ELM");
        algList.add("PSO");
        algList.add("DEExp");
        algList.add("DEBin");
        algList.add("RWELM");
        // algList.add("cmaes");
        return algList;

    }

    public ArrayList getProblems() {
        ArrayList probList = new ArrayList<>();

        probList.add("Iris");
        probList.add("Banknote");
        probList.add("Blood");
//        probList.add("Cardiotocography");
//        probList.add("Car");
//        probList.add("Chart");
//        probList.add("Connectionist");
//        probList.add("Contraceptive");
//        probList.add("Dermatology");
//        probList.add("Diabetes");
//        probList.add("Ecoli");
//        probList.add("Fertility");
//        probList.add("Glass");
//        probList.add("Haberman");
//        probList.add("Hayes");
//        probList.add("Hill");
//        probList.add("Indian");
//        probList.add("Ionosphere");
//        probList.add("Leaf");
//        probList.add("Letter");
//        probList.add("Libras");
//        probList.add("Optdigits");
//        probList.add("Seeds");
//        probList.add("SPECTF");
//        probList.add("Vertebral2C");
//        probList.add("Vertebral3C");
//        probList.add("Pen");
//        probList.add("QSARBiodegradation");
//        probList.add("Wdbc");
//        probList.add("Yeast");
//        probList.add("Wine");
//        probList.add("Zoo");
        //Regression problemas
        probList.add("AutoMpg");
        probList.add("AutoPrice");
        probList.add("Cpu");
        probList.add("Sensory");
        probList.add("Servo");
//        probList.add("Veteran");
//        probList.add("Housing");
//        probList.add("BodyFat");

        return probList;
    }

    public ArrayList getSolutionRepresentation() {
        ArrayList typeSol = new ArrayList<>();
        //  typeSol.add(0);//vectoe=[peso,bias,peso,bias....] Esquema A or 0
        typeSol.add(1);//vector=[peso,peso,peso,bias.....] Esquema B or 1
        return typeSol;
    }

    public ArrayList getTypeValidacion() {
        ArrayList tipos = new ArrayList();
        // tipos.add("CV");
        tipos.add("TT");
        return tipos;
    }

    public String getSQLInsert(ArrayList<Task> lst) {
        String sqlMask = "INSERT INTO Task (seed, train, test, time, state, assigned, problemName, algorithm, configuration, typeValidation, typeSolution, id) VALUES ";
        String sqlInsert = sqlMask;

        int size = lst.size();
        int c = 0;
        int block = 0;
        int MAXBLOCK = 100;
        for (Task t : lst) {
            sqlInsert += "(" + t.getSeed() + "," + t.getTrain() + "," + t.getTest() + "," + t.getTime() + "," + t.getState() + "," + t.getAssigned() + ",'" + t.getProblemName() + "','" + t.getAlgorimo() + "'," + t.getConfiguracion() + ",'" + t.getTypeValidation() + "'," + t.getTypeSolution() + ",null" + ")";
            c++;
            if ((size - c) != 0 && block < MAXBLOCK) {
                sqlInsert += ",\n";
                block++;
            } else {
                if (size != c) {
                    sqlInsert += ";\n" + sqlMask;
                } else {
                    sqlInsert += ";\n";
                }
                block = 0;

            }
        }
        System.out.println("" + sqlInsert);
        return sqlInsert;
    }

    //read the configuration in parameters file
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

    //only for socktes implementation
    public void writeTask(ArrayList<Task> task) {

        try {
            String directorio = "";
            String SumaryNameFile = directorio + "Tasks.txt";

            File dir = new File(directorio);

            if (!dir.exists()) {
                dir.mkdirs();
            }
            BufferedWriter sumary;
            try {
                sumary = new BufferedWriter(new FileWriter(SumaryNameFile, true));
                for (Task t : task) {
                    sumary.newLine();
                    sumary.write(t.toString());
                }
                sumary.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//only for socktes implementation
    public boolean isMakeInFile(Task t) {

        Object resultForFile = t.getProblemName();
        String directorio = "Result\\" + t.getAlgorimo() + "Result\\s" + t.getTypeSolution() + "\\" + t.getTypeValidation() + "\\";
        String SumaryNameFile = directorio + "c" + t.getConfiguracion() + "_" + resultForFile + "_Results.txt";
        File dir = new File(directorio);
        ArrayList<Task> lst = new ArrayList<>();
        BufferedReader br = null;
        FileReader fr = null;

        ArrayList<ArrayList> parameters = new ArrayList<>();
        try {
            fr = new FileReader(SumaryNameFile);
        } catch (FileNotFoundException ex) {
            return false;
        }
        br = new BufferedReader(fr);
        String actualLine;

        boolean echaT = false;
        try {
            while ((actualLine = br.readLine()) != null) {
                echaT = false;
                if (actualLine.contains(",")) {//Is a line with parameters

                    Task echa = new Task(actualLine);
                    if (echa.getSeed() == t.getSeed() && echa.getConfiguracion() == t.getConfiguracion()) {
                        echaT = true;
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(BuilderTask.class.getName()).log(Level.SEVERE, null, ex);
        }

        return echaT;
    }

}
