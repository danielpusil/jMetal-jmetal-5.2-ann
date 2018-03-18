package interfaces;

import java.io.Serializable;

public class Task implements Serializable {

    private int seed;
    private double train;
    private double test;
    private double time;
    private int state;
    private int assigned;
    private String problemName;
    private String algorithm;
    private int configuracion;
    private String typeValidation;
    private int typeSolution;
    private int id;

    public Task() {
        this.seed = -1;
        this.train = -1;
        this.test = -1;
        this.time = -1;
        this.state = -1;
        this.assigned = -1;
        this.problemName = "problemName";
        this.algorithm = "algorimo";
        this.configuracion = -1;
        this.typeValidation = "type";
        this.typeSolution = -1;
        this.id = -1;

    }

    public Task(int seed, double train, double test, double time, int state, int asiggned, String problemName, String algorimo, int config, String type, int typeSol, int id) {
        this.seed = seed;
        this.train = train;
        this.test = test;
        this.time = time;
        this.state = state;
        this.assigned = asiggned;
        this.problemName = problemName;
        this.algorithm = algorimo;
        this.configuracion = config;
        this.typeValidation = type;
        this.typeSolution = typeSol;
        this.id = id;
    }

    public Task(String task) {
        String[] values = task.split(",");
        this.seed = Integer.parseInt(values[0]);
        this.train = Double.parseDouble(values[1]);
        this.test = Double.parseDouble(values[2]);
        this.time = Double.parseDouble(values[3]);
        this.state = Integer.parseInt(values[4]);
        this.assigned = Integer.parseInt(values[5]);
        this.problemName = values[6];
        this.algorithm = values[7];
        this.configuracion = Integer.parseInt(values[8]);
        this.typeValidation = values[9];
        this.typeSolution = Integer.parseInt(values[10]);
        this.id = Integer.parseInt(values[11]);

    }

    @Override
    public String toString() {
        return "" + seed + "," + train + "," + test + "," + time + "," + state + "," + assigned + "," + problemName + "," + algorithm + "," + configuracion + "," + typeValidation + "," + this.typeSolution + "," + this.id;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public double getTrain() {
        return train;
    }

    public void setTrain(double train) {
        this.train = train;
    }

    public double getTest() {
        return test;
    }

    public void setTest(double test) {
        this.test = test;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getAssigned() {
        return assigned;
    }

    public void setAssigned(int assigned) {
        this.assigned = assigned;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public String getAlgorimo() {
        return algorithm;
    }

    public void setAlgorimo(String algorimo) {
        this.algorithm = algorimo;
    }

    public int getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(int configuracion) {
        this.configuracion = configuracion;
    }

    public String getTypeValidation() {
        return typeValidation;
    }

    public void setTypeValidation(String typeValidation) {
        this.typeValidation = typeValidation;
    }

    public int getTypeSolution() {
        return typeSolution;
    }

    public void setTypeSolution(int typeSolution) {
        this.typeSolution = typeSolution;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
