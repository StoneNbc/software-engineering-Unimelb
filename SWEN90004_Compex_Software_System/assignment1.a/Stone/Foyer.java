

public class Foyer {

    //inpatient is used to admite paitent
    //because only onew patient at the same time, it's not necessay to use "queue"
    private Patient inPatient;

    //outpatient is used to discharge paitent
    //because only onew patient at the same time, it's not necessay to use "queue"
    private Patient outPatient;

    //construtor
    public Foyer() {
        this.inPatient = null;
        this.outPatient = null;
        
    }

    //patient arrived 
    public synchronized void arriveAtED(Patient patient) throws InterruptedException {
        //if here has already been a patient admitting
        //because Foyer only can contain one newly admitted patient 
        while (inPatient != null) {
            wait();//thread wait until here is no admiting patient
        }

        //let this patient admite to ED
        inPatient = patient;
 
        System.out.println(patient + " admitted to ED.");
        notifyAll();//notify other thread 
    }

    //allocate patient to nurse
    public synchronized Patient allocatedPatient() throws InterruptedException{
        //if here is no patient, thread wiat
        while (inPatient == null ) {
            wait();
        }

        //if the current patient has been allocated 
        while (inPatient.allocated == true) {
            wait();
        }

        //change the flag if this patient is allocated
        inPatient.allocated =true;

        //creat a new patient to copy inpatient as return value
        Patient patient = inPatient;

        //release inpatient's value to let ED is ready to admite new patient
        inPatient = null;
        //
        notifyAll();

        //return this patient to nurse
        return patient;   
    }

    //patient enter Foyer from other places
    public synchronized void recievePatient(Patient patient) throws InterruptedException{
        //if hera has already been a patient preparing for leaving,this thread wait
        //because Foyer only can have one patient waiting to be discharged
        while (outPatient != null) {
            wait();
        }

        //let one patient enter foyer to discharge
        outPatient = patient;
        System.out.println(outPatient + " enters Foyers.");
        notifyAll();
    }

    //patient depart
    public synchronized void departFromED() throws InterruptedException {
        //if here is no patient preparing for leaving,this thread wait
        while (outPatient == null) {
            wait();
        }

        //let this patient discharge
        System.out.println(outPatient + " discharged from ED.");
        outPatient = null;
        
        notifyAll();
    }

    //tranfer patient to triage
    //patient leave foyer
    public synchronized void toTriage(Patient patient){
        //transfer this patient from foyer to triage
        System.out.println(patient + " leaves Foyer.");
        notifyAll();
    }

}
