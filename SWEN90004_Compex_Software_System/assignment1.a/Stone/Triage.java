
public class Triage {
    // the patient who is in triage now
    private Patient patient;
    
    // constructor
    public Triage() {
        this.patient = null;
    }

    // recieve patient from other places
    public synchronized void recievePatient(Patient patient) throws InterruptedException{
        // if here has been a patient now, wati until here is free
        while (this.patient != null) {
            wait();
        }

        // patient in 
        this.patient = patient;
        
        System.out.println( patient + " enters triage.");

        notifyAll();
    }

    //assess patien
    public boolean assessPatient(){

        
        // System.out.println(patient+" is assessing");
        if(patient.Severe()){
            //病人严重 patient is severe, return ture
            // System.out.println(patient+" is severe");
            return true;
        }
        else {
            // System.out.println(patient+" is not severe");
            // patient is not severe, return false
            return false;
            
        }
    }


    //transfer patient to Foyer
    public synchronized void toFoyer(){
        System.out.println(patient + " leaves Triage.");
        // patient leave triage
        this.patient = null;
        notifyAll();
    }

    //transfer patient to treatment
    public synchronized void toTreatment(){
        System.out.println(patient + " leaves Triage.");
        // patient leave here
        this.patient = null;
        notifyAll();
    }

}
