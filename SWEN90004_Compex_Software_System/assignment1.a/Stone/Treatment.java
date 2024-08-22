
public class Treatment {
    // the patient who is in triage now
    private Patient patient;
    
    // constructor
    public Treatment() {
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
        
        System.out.println( patient + " enters Treatment room.");

        notifyAll();
    }

    //allocate patient to specialist
    public synchronized Patient allocatedPatient() throws InterruptedException{
        // if here is no patient or this patient has been treated, wait
        while (patient == null || patient.treated == true) {
            wait();
        }

        // Patient treatingpatient = patient;
        notifyAll();
        return patient;   
    }

    // treat this patient
    public synchronized void treatPatient(Patient patient){
        //patient's treated param set true 
        patient.treated = true;

        // //patient leave after treating
        // this.patient = null;
        notifyAll();
    }


    //transfer patient to Foyer
    public synchronized void toFoyer(Patient patient) throws InterruptedException{
        // if patient haven't been treated, wait
        while (patient.treated == false) {
            wait();
        }
        
        System.out.println(patient + " leaves Treatment room.");
        // patient leave after treating
        this.patient = null;
        notifyAll();
    }

}
