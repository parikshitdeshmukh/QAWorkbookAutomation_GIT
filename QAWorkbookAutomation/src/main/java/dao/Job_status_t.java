package dao;

/**
 * Created by SunilDeP on 5/24/2017.
 */
public class Job_status_t {

    public Job_status_t(int job_id, String staging_DB, String production_DB, String job_name) {
        this.job_id = job_id;
        Staging_DB = staging_DB;
        Production_DB = production_DB;
        this.job_name = job_name.replaceAll("[^a-zA-Z0-9]", "");
    }

    int job_id;
    String Staging_DB;
    String Production_DB;
    String job_name;

    public int getJob_id() {
        return job_id;
    }

    public void setJob_id(int job_id) {
        this.job_id = job_id;
    }

    public String getStaging_DB() {
        return Staging_DB;
    }

    public void setStaging_DB(String staging_DB) {
        Staging_DB = staging_DB;
    }

    public String getProduction_DB() {
        return Production_DB;
    }

    public void setProduction_DB(String production_DB) {
        Production_DB = production_DB;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }
}
