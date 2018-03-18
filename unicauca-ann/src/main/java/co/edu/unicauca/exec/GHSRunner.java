package co.edu.unicauca.exec;

import static co.edu.unicauca.exec.CmaesRunner.MAXEFOS;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import co.edu.unicauca.problem.cross_validation.AbstractCrossValidationEvaluator;
import co.edu.unicauca.problem.training_testing.TrainingTestingEvaluator;
import co.edu.unicauca.util.sumary.SumaryFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.harmonySearch.GHSBuilder;
import org.uma.jmetal.algorithm.singleobjective.harmonySearch.HSBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Daniel Pusil <danielpusil@unicauca.edu.co>
 */
public class GHSRunner {

    private static final int DEFAULT_NUMBER_OF_EXPERIMENTS = 5;//
    private static int MAXEFOS = 3000;
    //Example HS runner and Export results file

    public static void main(String[] args) throws Exception {
        ejecutar(args);
    }

    public static void ejecutar(String[] args) throws IOException {

        System.out.println(" GHS Is run");
        List problemsNames = new ArrayList();
        problemsNames.add("co.edu.unicauca.problem.training_testing.Iris");
        // problemsNames.add("co.edu.unicauca.problem.training_testing.Banknote");

        // problemsNames.add("co.edu.unicauca.problem.cross_validation.AutoMpg");
        //  problemsNames.add("co.edu.unicauca.problem.cross_validation.Hill");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.Iris");
        //  problemsNames.add("co.edu.unicauca.problem.cross_validation.Dermatology");
        /* problemsNames.add("co.edu.unicauca.problem.cross_validation.Blood");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Cardiotocography");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Car");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Chart");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Connectionist");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Contraceptive");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Dermatology");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Diabetes");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Ecoli");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Fertility");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Glass");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Haberman");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Hayes");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Hill");
    
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Indian");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Ionosphere");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Leaf");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Letter");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Libras");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Optdigits");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Seeds");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.SPECTF");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Shuttle");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Vertebral2C");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Vertebral3C");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Pen");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.QSARBiodegradation");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Wdbc");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Yeast");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Wine");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Zoo");
         */

 /*Aux to Experiments results*/
        List accurracyList = new ArrayList();
        List executionTimeList = new ArrayList();
        List<DoubleSolution> solutionList = new ArrayList<>();

        Algorithm<DoubleSolution> algorithm = null;
        DoubleProblem problem = null;

        for (int j = 0; j < problemsNames.size(); j++) {
            System.out.println("Experiment for "+problemsNames.get(j));
            for (int i = 0; i < DEFAULT_NUMBER_OF_EXPERIMENTS; i++) {
                // set a SEED
                JMetalRandom randomGenerator = JMetalRandom.getInstance();
                randomGenerator.setSeed(i);

                //Load a problem by Name
                problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem((String) problemsNames.get(j));

                //Set solution representation scheme
                //vector=[weight,bias,weight,bias....] schema A or 0
                //vector=[weight,weight,weight,bias,bias.....] scheme B or 1
                ((AbstractELMEvaluator) problem).setType_solution(1);

                if (!((String) problemsNames.get(j)).contains("training_testing")) {
                    MAXEFOS = 3000 / 10;//only 300 EFOS, because count 10 Efos for 1 evaluation of objective funtion that make 10 ELM models
                }
                //Configure thw]e parameters for HS
                algorithm = new GHSBuilder((DoubleProblem) problem)
                        .setHMS(10)
                        .setHMCR(Double.parseDouble(0.85 + ""))
                        .setPARMIN(Double.parseDouble(0.3 + ""))
                        .setPARMAX(Double.parseDouble(0.85 + ""))
                        .setMaxEvaluations(MAXEFOS)
                        .build();

                //start algorithm
                AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                        .execute();

                DoubleSolution solution = algorithm.getResult();

                //print solution
                System.out.println("Best Train " + solution.getObjective(0));
                long computingTime = algorithmRunner.getComputingTime();

                double a = -1;
                if (((String) problemsNames.get(j)).contains("training_testing")) {
                    a = ((TrainingTestingEvaluator) problem).test(solution);
                } else {
                    a = ((AbstractCrossValidationEvaluator) problem).test(solution);
                }

                executionTimeList.add(computingTime);
                accurracyList.add((Object) a);
                solutionList.add(solution);
                System.out.println(" Accuracy " + a);
                System.out.println("Total execution time: " + computingTime + "ms");

                System.gc();
            }
            //export in result file
            SumaryFile.exportResult(algorithm, null, (AbstractELMEvaluator) problem, solutionList, accurracyList, executionTimeList, problem, null);
            /*Clear for next problem*/
            accurracyList.clear();
            executionTimeList.clear();
            solutionList.clear();
        }
    }
}
