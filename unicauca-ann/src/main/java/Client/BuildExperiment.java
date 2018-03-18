package Client;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import co.edu.unicauca.problem.cross_validation.AbstractCrossValidationEvaluator;
import co.edu.unicauca.problem.training_testing.TrainingTestingEvaluator;
import co.edu.unicauca.util.sumary.*;
import co.edu.unicauca.util.sumary.SendFileEmail;
import co.edu.unicauca.util.sumary.SumaryFile;
import interfaces.Task;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.LocalOptimizer.OptSimulatedAnnealing;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DifferentialEvolutionBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.MemeticEDBuilder;
import org.uma.jmetal.algorithm.singleobjective.evolutionstrategy.CovarianceMatrixAdaptationEvolutionStrategy;
import org.uma.jmetal.algorithm.singleobjective.harmonySearch.*;
import org.uma.jmetal.algorithm.singleobjective.particleswarmoptimization.StandardPSO2007;
import org.uma.jmetal.algorithm.singleobjective.particleswarmoptimization.random_search.RandomSearch;
import org.uma.jmetal.algorithm.singleobjective.particleswarmoptimization.random_search.RandomSearchBuilder;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.comparator.FitnessNorma2Comparator;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.impl.JavaRandomGenerator;

public class BuildExperiment {

    private static final int DEFAULT_NUMBER_OF_EXPERIMENTS = 30;
    private int DEFAULT_EFOS = 300;//default for CrossValidation, need be *kfolders for TT
    private Task tarea;

    public void HS(String problemName, ArrayList parameters, String indicator) {

        Algorithm<DoubleSolution> algorithm = null;
        DoubleProblem problem = null;
        problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem((String) problemName);
        if (parameters != null) {
            algorithm = new HSBuilder((DoubleProblem) problem)
                    .setHMS(Integer.parseInt((String) parameters.get(0)))
                    .setHMCR(Double.parseDouble((String) parameters.get(1)))
                    .setPAR(Double.parseDouble((String) parameters.get(2)))
                    .setBW(Double.parseDouble((String) parameters.get(3)))
                    .setMaxEvaluations(DEFAULT_EFOS)
                    .build();

        } else {
            algorithm = new HSBuilder((DoubleProblem) problem)
                    .setHMS(10)
                    .setHMCR(0.9)
                    .setPAR(0.4)
                    .setBW(0.04)
                    .setMaxEvaluations(DEFAULT_EFOS)
                    .build();
        }
        if (tarea == null) {
            RunAlgorithm(algorithm, problem, indicator);
        } else {
            runAlgoritmo(algorithm, problem, tarea);
        }
    }

    public void GHS(String problemName, ArrayList parameters, String indicator) {
        Algorithm<DoubleSolution> algorithm = null;
        DoubleProblem problem = null;
        problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem((String) problemName);
        if (parameters != null) {//see parameters file
            algorithm = new GHSBuilder((DoubleProblem) problem)
                    .setHMS(Integer.parseInt((String) parameters.get(0)))
                    .setHMCR(Double.parseDouble((String) parameters.get(1)))
                    .setPARMIN(Double.parseDouble((String) parameters.get(2)))
                    .setPARMAX(Double.parseDouble((String) parameters.get(3)))
                    .setMaxEvaluations(DEFAULT_EFOS)
                    .build();
        } else {
            algorithm = new GHSBuilder((DoubleProblem) problem)
                    .setHMS(10)
                    .setHMCR(0.9)
                    .setPARMAX(0.001)
                    .setPARMIN(0.6)
                    .setMaxEvaluations(DEFAULT_EFOS)
                    .build();
        }
        if (tarea == null) {
            RunAlgorithm(algorithm, problem, indicator);
        } else {
            runAlgoritmo(algorithm, problem, tarea);
        }
    }

    public void NGHS(String problemName, ArrayList parameters, String indicator) {

        Algorithm<DoubleSolution> algorithm = null;
        DoubleProblem problem = null;
        problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem((String) problemName);
        if (parameters != null) {//see parameters file
            algorithm = new NGHSBuilder((DoubleProblem) problem)
                    .setHMS(Integer.parseInt((String) parameters.get(0)))
                    .setPM(Double.parseDouble((String) parameters.get(1)))
                    .setMaxEvaluations(DEFAULT_EFOS)
                    .build();
        } else {
            algorithm = new NGHSBuilder((DoubleProblem) problem)
                    .setHMS(10)
                    .setPM(0.01)
                    .build();
        }
        if (tarea == null) {
            RunAlgorithm(algorithm, problem, indicator);
        } else {

            runAlgoritmo(algorithm, problem, tarea);
        }
    }

    public void IHS(String problemName, ArrayList parameters, String indicator) {
        Algorithm<DoubleSolution> algorithm = null;
        DoubleProblem problem = null;
        problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem((String) problemName);
        if (parameters != null) {//see parameters file
            algorithm = new IHSBuilder((DoubleProblem) problem)
                    .setHMS(Integer.parseInt((String) parameters.get(0)))
                    .setHMCR(Double.parseDouble((String) parameters.get(1)))
                    .setPARMIN(Double.parseDouble((String) parameters.get(2)))
                    .setPARMAX(Double.parseDouble((String) parameters.get(3)))
                    .setBWMIN(Double.parseDouble((String) parameters.get(4)))
                    .setBWMAX(Double.parseDouble((String) parameters.get(5)))
                    .setMaxEvaluations(DEFAULT_EFOS)
                    .build();
        } else {
            algorithm = new GHSBuilder((DoubleProblem) problem)
                    .setHMS(10)
                    .setHMCR(0.9)
                    .setPARMAX(0.001)
                    .setPARMIN(0.6)
                    .setMaxEvaluations(DEFAULT_EFOS)
                    .build();
        }
        if (tarea == null) {
            RunAlgorithm(algorithm, problem, indicator);
        } else {

            runAlgoritmo(algorithm, problem, tarea);
        }
    }

    //build a DE with type BIN or EXP, need be specific in parametersFile.txt
    public void DE(String problemName, ArrayList parameters, String indicator) {
        DoubleProblem problem;
        Algorithm<DoubleSolution> algorithm;
        DifferentialEvolutionSelection selection;
        DifferentialEvolutionCrossover crossover;
        SolutionListEvaluator<DoubleSolution> evaluator;
        problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem((String) problemName);
        evaluator = new SequentialSolutionListEvaluator<>();

        if (parameters != null) {
            crossover = new DifferentialEvolutionCrossover(Double.parseDouble((String) parameters.get(1)), Double.parseDouble((String) parameters.get(2)), (String) parameters.get(3));
            selection = new DifferentialEvolutionSelection();
            algorithm = new DifferentialEvolutionBuilder(problem)
                    .setCrossover(crossover)
                    .setSelection(selection)
                    .setSolutionListEvaluator(evaluator)
                    .setMaxEvaluations(DEFAULT_EFOS)
                    .setPopulationSize(Integer.parseInt((String) parameters.get(0)))
                    .build();
        } else {
            crossover = new DifferentialEvolutionCrossover(0.5, 0.5, "rand/1/bin");
            selection = new DifferentialEvolutionSelection();
            algorithm = new DifferentialEvolutionBuilder(problem)
                    .setCrossover(crossover)
                    .setSelection(selection)
                    .setSolutionListEvaluator(evaluator)
                    .setMaxEvaluations(300)
                    .setPopulationSize(100)
                    .build();
        }
        if (tarea == null) {
            RunAlgorithm(algorithm, problem, indicator);
        } else {

            runAlgoritmo(algorithm, problem, tarea);
        }
    }

    public void PSO(String problemName, ArrayList parameters, String indicator) {
        Algorithm<DoubleSolution> algorithm = null;
        DoubleProblem problem = null;
        problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem((String) problemName);
        SolutionListEvaluator<DoubleSolution> evaluator;

        evaluator = new SequentialSolutionListEvaluator<DoubleSolution>();
        if (parameters != null) {
            algorithm = new StandardPSO2007(problem,
                    (int) 10 + (int) (2 * Math.sqrt(problem.getNumberOfVariables())),
                    11, Integer.parseInt((String) parameters.get(0)), evaluator, DEFAULT_EFOS);

        } else {
            algorithm = new StandardPSO2007(problem,
                    10 + (int) (2 * Math.sqrt(problem.getNumberOfVariables())),
                    80000, 3, evaluator);
        }
        if (tarea == null) {
            RunAlgorithm(algorithm, problem, indicator);
        } else {
            runAlgoritmo(algorithm, problem, tarea);
        }

    }

    public void CMAES(String problemName, ArrayList parameters, String indicator) {

        Algorithm<DoubleSolution> algorithm = null;
        DoubleProblem problem = null;
        problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem((String) problemName);

        if (parameters != null) {
            algorithm = new CovarianceMatrixAdaptationEvolutionStrategy.Builder(problem, Integer.parseInt((String) parameters.get(0)), Double.parseDouble((String) parameters.get(1)), DEFAULT_EFOS)
                    .build();
        } else {
            algorithm = new CovarianceMatrixAdaptationEvolutionStrategy.Builder(problem)
                    .build();
        }
        if (tarea == null) {
            RunAlgorithm(algorithm, problem, indicator);
        } else {

            runAlgoritmo(algorithm, problem, tarea);
        }
    }

    public void MA_ELM(String problemName, ArrayList parameters, String indicator) {
        DoubleProblem problem;
        Algorithm<DoubleSolution> algorithm;
        DifferentialEvolutionSelection selection;
        DifferentialEvolutionCrossover crossover;
        OptSimulatedAnnealing localSearch;
        SolutionListEvaluator<DoubleSolution> evaluator;
        problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem(problemName);

        evaluator = new SequentialSolutionListEvaluator<>();
        crossover = new DifferentialEvolutionCrossover(0.5, 0.5, "current-to-best/1");

        selection = new DifferentialEvolutionSelection();

        Comparator<DoubleSolution> comparator;
        comparator = new ObjectiveComparator<DoubleSolution>(0);
        if (parameters != null) {
            localSearch = new OptSimulatedAnnealing(-1, null, comparator, problem);
            algorithm = new MemeticEDBuilder((DoubleProblem) problem)
                    .setCrossover(crossover)
                    .setSelection(selection)
                    .setSolutionListEvaluator(evaluator)
                    .setMaxEvaluations(DEFAULT_EFOS)
                    .setPopulationSize(50)
                    .setLocalSearch(localSearch)
                    .build();
        } else {
            localSearch = new OptSimulatedAnnealing(10, null, comparator, problem);
            algorithm = new MemeticEDBuilder((DoubleProblem) problem)
                    .setCrossover(crossover)
                    .setSelection(selection)
                    .setSolutionListEvaluator(evaluator)
                    .setMaxEvaluations(DEFAULT_EFOS)
                    .setPopulationSize(50)
                    .setLocalSearch(localSearch)
                    .build();
        }
        if (tarea == null) {
            RunAlgorithm(algorithm, problem, indicator);
        } else {
            runAlgoritmo(algorithm, problem, tarea);
        }
    }

    public void RWELM(String problemName, ArrayList parameters, String indicator) {
        DoubleProblem problem;
        problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem(problemName);
        Comparator<DoubleSolution> comparator;
        comparator = new FitnessNorma2Comparator<>();
        RandomSearch RWELM = new RandomSearchBuilder(problem).setComparator(comparator).setMaxEvaluations(DEFAULT_EFOS).build();
        if (tarea == null) {
        } else {
            runAlgoritmo(RWELM, problem, tarea);
        }

    }

    public void RunAlgorithm(Algorithm<DoubleSolution> algorithm, DoubleProblem problem, String descriptor) {
        /*Aux to Experiments results*/
        List accurracyList = new ArrayList();
        List executionTimeList = new ArrayList();
        List<DoubleSolution> solutionList = new ArrayList<>();

        for (int i = 0; i < DEFAULT_NUMBER_OF_EXPERIMENTS; i++) {

            JMetalRandom.getInstance().setRandomGenerator(new JavaRandomGenerator());
            JMetalRandom.getInstance().setSeed(i);

            AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                    .execute();

            DoubleSolution solution = algorithm.getResult();

            System.out.println("Best Train " + solution.getObjective(0));
            long computingTime = algorithmRunner.getComputingTime();

            System.out.println("Total execution time: " + computingTime + "ms");
            double a = ((AbstractCrossValidationEvaluator) problem).test(solution);

            executionTimeList.add(computingTime);
            accurracyList.add((Object) a);
            solutionList.add(solution);
            System.out.println(" Accuracy " + a);
            System.gc();

        }

        try {
            SumaryFile.exportResult(algorithm, null, (AbstractCrossValidationEvaluator) problem, solutionList, accurracyList, executionTimeList, problem, descriptor);
        } catch (IOException ex) {
            System.out.println("No se puede Escribir los");
        }
        // evaluator.shutdown();
        /*Clear for next problem*/
        accurracyList.clear();
        executionTimeList.clear();
        solutionList.clear();

    }

    public Task runAlgoritmo(Algorithm algorithm, DoubleProblem problem, Task t) {
        double train;
        double test;

        JMetalRandom.getInstance().setRandomGenerator(new JavaRandomGenerator());
        JMetalRandom.getInstance().setSeed(t.getSeed());
        ((AbstractELMEvaluator) problem).setType_solution(t.getTypeSolution());

        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute();
        DoubleSolution solution = (DoubleSolution) algorithm.getResult();

        long computingTime = algorithmRunner.getComputingTime();
        train = solution.getObjective(0);
        if ("CV".equalsIgnoreCase(t.getTypeValidation())) {
            test = ((AbstractCrossValidationEvaluator) problem).test(solution);
        } else {
            test = ((TrainingTestingEvaluator) problem).test(solution);

        }
        t.setTime(computingTime);
        t.setTest(test);
        t.setTrain(train);
        if (t.getTrain() != -1.0) {
            t.setState(1);
        } else {
            t.setState(0);
        }
        System.out.println(" Accuracy " + test +" id "+t.getId());
        return t;
    }

    /*Util to send mail files results*/
    public void sendResult(String problemas) {
        comprimir();
        sendMail("Result ", "Result.zip", "Resultados para " + problemas);

    }

    public static void sendMail(String resultsName, String filename, String message) {
        SendFileEmail sender = new SendFileEmail();
        sender.send(resultsName, filename, message);
    }

    public void comprimir() {
        try {
            Zipper zp = new Zipper();
            zp.comprimir_carpeta("Result", "Result.zip");
        } catch (Exception ex) {
            Logger.getLogger(BuildExperiment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Task getTarea() {
        return tarea;
    }

    public void setTarea(Task tarea) {
        this.tarea = tarea;
    }

    public int getDEFAULT_EFOS() {
        return DEFAULT_EFOS;
    }

    public void setDEFAULT_EFOS(int DEFAULT_EFOS) {
        this.DEFAULT_EFOS = DEFAULT_EFOS;
    }

}