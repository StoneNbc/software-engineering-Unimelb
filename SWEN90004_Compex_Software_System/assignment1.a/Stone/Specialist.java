
public class Specialist extends Thread {

    // specialist need to enter the treatment
    private Treatment treatment;

    // the patient who needs to be treated
    private Patient patient;

    //constructor
    public Specialist(Treatment treatment) {
        this.treatment = treatment;
    }

    // treat this patient
    public void treatPatient() throws InterruptedException{
        
        System.out.println(patient+ " treatment started.");
        sleep(Params.TREATMENT_TIME);
        treatment.treatPatient(patient);
        System.out.println(patient+ " treatment complete.");
        patient = null;
        
    }

    // repeat treat patients
    public void run(){
        try {
            
            while (!interrupted()) {
                System.out.println("Specialist enters treatment room.");
                //recieve a patient
                patient = treatment.allocatedPatient();

                //treat patient
                if (patient != null)
                    treatPatient();

                System.out.println("Specialist leaves treatment room.");
                //specialist leave the treatment
                sleep(Params.SPECIALIST_AWAY_TIME);
            }
        } catch (Exception e) {
            System.out.println("specialist run wrong." + e.getMessage());
        }
    }

}
