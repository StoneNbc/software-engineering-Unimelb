
public class Nurse extends Thread {
    //id
    private int id;

    //initialize the foyer triage treatment
    private Foyer foyer;
    private Triage triage;
    private Treatment treatment;

    // initialize orderlies allocator
    private Orderlies orderlies;
    private Patient patient;


    //construtor
    public Nurse(int id, Foyer foyer, Triage triage, Orderlies orderlies, Treatment treatment) {
        this.id = id;
        this.foyer = foyer;
        this.triage = triage;
        this.treatment = treatment;
        this.orderlies = orderlies;

    }

    //check if this nurse is allocated
    public boolean IsAllocated(){
        if (patient == null)
            return false;
        else
            return true;
    }

    //alocate a patient for this nurse 
    public void allocatePatient() throws InterruptedException{
        //take a patient from the foyer
        this.patient = foyer.allocatedPatient();

        if (patient != null) {
            System.out.println(patient+" allocated to Nurse  "+ this.id +".");
        }
    }

    //let current paitent leave ED and free this nurse
    public void releasePatient() throws InterruptedException{
        System.out.println("Nurse "+this.id + " release " + patient + ".");
        //release the patient param
        this.patient = null;

        //call the departfromEd function, because this action is happened at foyer
        foyer.departFromED();
        
    }

    //request for 3 orderlies when transfering patients
    public void requestOrderlies() throws InterruptedException{      
        //call the request function in orderlies class  
        orderlies.requestOrderlies();
        System.out.println("Nurse "+this.id+ " recruits 3 oderlies ("+ orderlies.getAvailableOrderlies()+ " free)." );
    }

    //release 3 orderlies after transfering patients
    public void releaseOrderlies() throws InterruptedException{
        //call the relese function in orderlies class
        orderlies.releaseOrderlies();
        System.out.println("Nurse "+this.id+ " releases 3 oderlies ("+ orderlies.getAvailableOrderlies()+ " free)." );
    }
    
    //transfer patient from foyer to triage
    public void FoyertoTriage() throws InterruptedException{
        //request orderliews
        requestOrderlies();

        //call the leaving function because this action should be execute at foyer
        foyer.toTriage(patient);

        //use sleep() to simulate the transfer time
        sleep(Params.TRANSFER_TIME);

        //call the recieve function because this action should be execute at triage
        triage.recievePatient(patient);

        //release orderlies
        releaseOrderlies();
    }

    //transfer patient from triage to foyer
    public void TriagetoFoyer() throws InterruptedException{
        //request orderliews
        requestOrderlies();

        //call the leaving function because this action should be execute at triage
        triage.toFoyer();

        //use sleep() to simulate the transfer time
        sleep(Params.TRANSFER_TIME);

        //call the recieve function because this action should be execute at foyer
        foyer.recievePatient(patient);

        //release orderlies
        releaseOrderlies();
    }

    //assess patient if is severe
    public boolean assessPatient() throws InterruptedException{
        //use sleep() to simulate the severe time
        sleep(Params.TRIAGE_TIME);
        return triage.assessPatient();
    }

    //transfer patient from triage to treatment
    public void TriagetoTreatment() throws InterruptedException{
        //request orderliews
        requestOrderlies();

        //call the leaving function because this action should be execute at triage
        triage.toTreatment();

        //use sleep() to simulate the transfer time
        sleep(Params.TRANSFER_TIME);

        //call the recieve function because this action should be execute at treatment
        treatment.recievePatient(patient);

        //release orderlies
        releaseOrderlies();
    }

    //transfer patient from treatment to foyer
    public void TreatmenttoFoyer() throws InterruptedException{
        //request orderliews
        requestOrderlies();

        //call the leaving function because this action should be execute at treatment
        treatment.toFoyer(patient);

        //use sleep() to simulate the transfer time
        sleep(Params.TRANSFER_TIME);

        //call the recieve function because this action should be execute at foyer
        foyer.recievePatient(patient);

        //release orderlies
        releaseOrderlies();
    }

    // repeat the process of curing patients
    public void run(){

        try {
            
            while (!interrupted()) {
                // //teat part 1
                // allocatePatient();
                // foyer.recievePatient(patient);
                // releasePatient();

                // //test part 2
                // allocatePatient();
                // totriage();
                // TriagetoFoyer();
                // releasePatient();

                // allocate a patient at first
                allocatePatient();

                //transfer patient to triage
                FoyertoTriage();

                //assess the patient
                if ( assessPatient()){
                    //if patient is severe, transfer to treatment
                    TriagetoTreatment();

                    //transfer patient to Foyer aftering treatment
                    TreatmenttoFoyer();

                }else{
                    // patient is not severe, transfer to foyer
                    TriagetoFoyer();
                }

                // paitent discharge
                releasePatient();

            }
        } catch (Exception e) {
            System.out.println(this.id + " Nurse runs wrong" + e.getMessage());
        }
    }



}
