package cliente.db.implemente;

import interfaces.Task;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel Pusil<danielpusil@unicauca.edu.co>
 */
public class TaskDao {

    DataBaseConnection db = new DataBaseConnection();

    /* Method To Fetch The Task Records From Database */
    public ArrayList<Task> getTasksList() {
        ArrayList<Task> tasksList = new ArrayList<>();
        ResultSet resultSetObj = db.executeQuery("select * from Task");
        try {
            while (resultSetObj.next()) {
                Task taskObj = taskFromResulSet(resultSetObj);
                tasksList.add(taskObj);
            }
            DataBaseConnection.closeConnection();
        } catch (SQLException ex) {
        }
       // System.out.println("Total Alcanzados: " + tasksList.size());
        return tasksList;
    }

//    /* Method To Fetch The Task Records From Database */
    public ArrayList<Task> getTasksListToMake() {
        ArrayList<Task> tasksList = new ArrayList<>();
        //Control Concurrence
        ResultSet resultSetObj = db.executeQuery("SELECT * FROM Task WHERE id=(select min(id) from Task where test<0 and assigned<1) FOR UPDATE");
        // ResultSet resultSetObj = DataBaseConnection.executeQuery("SELECT * FROM Task WHERE id=(select min(id) from Task) ");

        int saveUpdate = 0;
        try {
            while (resultSetObj.next()) {
                String updateQuqery = "UPDATE Task SET assigned=? WHERE id=" + resultSetObj.getInt("id");
                db.prepareStatement(updateQuqery);
                db.getPstmt().setInt(1, 1);
                saveUpdate = db.getPstmt().executeUpdate();
                DataBaseConnection.connObj.commit();
                Task taskObj = taskFromResulSet(resultSetObj);
                tasksList.add(taskObj);
            }
            DataBaseConnection.closeConnection();
        } catch (SQLException ex) {
            try {
                DataBaseConnection.connObj.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(TaskDao.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        DataBaseConnection.closeConnection();
        //System.out.println("Total Alcanzados: " + tasksList.size());
        return tasksList;
    }

    /*Metho to Fech the Task */
    public Task getById(int taskId) {
        Task taskObj = null;
        String query = "select * from task  where id = " + taskId;
        ResultSet resultSetObj = db.executeQuery(query);

        if (resultSetObj != null) {
            try {
                resultSetObj.next();
            } catch (SQLException ex) {
            }
            taskObj = taskFromResulSet(resultSetObj);
        }
        DataBaseConnection.closeConnection();
        return taskObj;
    }

    /* Method Used To Save New Task Record In Database */
    public boolean saveTaskDetailsInDB(Task t) {
        boolean save = false;
        int saveResult = 0;

        String query = "INSERT INTO Task (seed, train, test, time, state, assigned, problemName, algorithm, configuration, typeValidation, typeSolution, id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NULL)";

        int pstmt = db.prepareStatement(query);
        if (pstmt != 0) {
            try {
                db.getPstmt().setInt(1, t.getSeed());
                db.getPstmt().setDouble(2, t.getTrain());
                db.getPstmt().setDouble(3, t.getTest());
                db.getPstmt().setDouble(4, t.getTime());
                db.getPstmt().setInt(5, t.getState());
                db.getPstmt().setInt(6, t.getAssigned());
                db.getPstmt().setString(7, t.getProblemName());
                db.getPstmt().setString(8, t.getAlgorimo());
                db.getPstmt().setInt(9, t.getConfiguracion());
                db.getPstmt().setString(10, t.getTypeValidation());
                db.getPstmt().setInt(11, t.getTypeSolution());
                saveResult = db.getPstmt().executeUpdate();

                DataBaseConnection.connObj.commit();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }

        }

        DataBaseConnection.closeConnection();
        save = saveResult != 0;
        System.out.println("Guardado: " + saveResult + " " + save);
        return save;
    }

    public boolean saveTaskOnDBToMake(ArrayList<Task> tasks) {

        boolean save = false;
        int saveResult = 0;
        for (Task t : tasks) {
            String query = "INSERT INTO Task (seed, train, test, time, state, assigned, problemName, algorithm, configuration, typeValidation, typeSolution, id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NULL)";

            int pstmt = db.prepareStatement(query);
            if (pstmt != 0) {
                try {
                    db.getPstmt().setInt(1, t.getSeed());
                    db.getPstmt().setDouble(2, t.getTrain());
                    db.getPstmt().setDouble(3, t.getTest());
                    db.getPstmt().setDouble(4, t.getTime());
                    db.getPstmt().setInt(5, t.getState());
                    db.getPstmt().setInt(6, t.getAssigned());
                    db.getPstmt().setString(7, t.getProblemName());
                    db.getPstmt().setString(8, t.getAlgorimo());
                    db.getPstmt().setInt(9, t.getConfiguracion());
                    db.getPstmt().setString(10, t.getTypeValidation());
                    db.getPstmt().setInt(11, t.getTypeSolution());
                    saveResult = db.getPstmt().executeUpdate();
                    DataBaseConnection.connObj.commit();

                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
                if (saveResult == 1) {
                    System.out.println("Guardado: " + saveResult);
                }
            }
        }
        db.closeConnection();

        return save;
    }

    /* Method Used To Edit Task Record In Database */
    public Task editTaskRecordInDB(int taskId) {
        Task taskEditRecord = null;
        System.out.println("editTaskRecordInDB() : Task Id: " + taskId);

        /* Setting The Particular Task Details In Session */
        String query = "select * from Task where id = " + taskId;
        try {
            ResultSet resultSetObj = db.executeQuery(query);
            if (resultSetObj != null) {
                resultSetObj.next();
                taskEditRecord = taskFromResulSet(resultSetObj);
            }

            System.out.println("task fetch:" + taskEditRecord);
        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
        DataBaseConnection.closeConnection();
        return taskEditRecord;
    }

    /* Method Used To Update Task Record In Database */
    public boolean updateTaskDetailsInDB(Task updateTaskObj) {
        int saveUpdate = 0;
        boolean modification = false;
        try {
            db.prepareStatement("UPDATE task SET train=?,test=?,\n"
                    + "time=?, state=? WHERE id=?");

            db.getPstmt().setDouble(1, updateTaskObj.getTrain());
            db.getPstmt().setDouble(2, updateTaskObj.getTest());
            db.getPstmt().setDouble(3, updateTaskObj.getTime());
            db.getPstmt().setInt(4, updateTaskObj.getState());
            db.getPstmt().setInt(5, updateTaskObj.getId());

            saveUpdate = db.getPstmt().executeUpdate();
            DataBaseConnection.connObj.commit();

            DataBaseConnection.closeConnection();
        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
        if (saveUpdate != 1) {
            modification = false;
        } else {
          // System.out.println("Save");
            modification = true;
        }

        return modification;
    }

    /* Method Used To Delete Task Record From Database */
    public boolean deleteTaskRecordInDB(int taskIdentification) {
        System.out.println("deleteTaskRecordInDB() : Task Id: " + taskIdentification);
        int result = -1;
        boolean modification = false;

        try {
            db.prepareStatement("delete from Task where id = " + taskIdentification);
            result = db.getPstmt().executeUpdate();
            DataBaseConnection.closeConnection();
        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
        if (result != 1) {
            modification = false;
        } else {
            modification = true;//then delete is ok
        }
        return modification;
    }

    /*Build a Obj from ResultSet*/
    private static Task taskFromResulSet(ResultSet resultSetObj) {
        Task taskObj = new Task();
        try {
            taskObj.setSeed(resultSetObj.getInt("seed"));
            taskObj.setTrain(resultSetObj.getDouble("train"));
            taskObj.setTest(resultSetObj.getDouble("test"));
            taskObj.setTime(resultSetObj.getDouble("time"));
            taskObj.setState(resultSetObj.getInt("state"));
            taskObj.setAssigned(resultSetObj.getInt("assigned"));
            taskObj.setProblemName(resultSetObj.getString("problemName"));
            taskObj.setAlgorimo(resultSetObj.getString("algorithm"));
            taskObj.setConfiguracion(resultSetObj.getInt("configuration"));
            taskObj.setTypeValidation(resultSetObj.getString("typeValidation"));
            taskObj.setTypeSolution((resultSetObj.getInt("typeSolution")));
            taskObj.setId(resultSetObj.getInt("id"));
        } catch (SQLException ex) {

        }
        return taskObj;
    }

}
