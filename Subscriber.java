import java.util.ArrayList;

class Plan
{
    int value;
    int duration;
    double local;
    double std;
    //constructor
    Plan(int value,double local,double std,int duration)
    {
        this.value  = value;
        this.local  = local;
        this.std  = std;
        this.duration  = duration;
    }
    //Get Value per day
    double getvalueDaily(int localUsage, int stdusage,boolean flag)
    {
        if(flag)
        {
            double localTariff = local==0 ? Subscriber.localPerMin : local;
            double stdTariff = std==0 ? Subscriber.stdPerMin : std;
            return (value + localUsage*localTariff + stdusage* stdTariff)/duration;
        }
        else
        {
            return (value + localUsage*local + stdusage* std)/duration;
        }
    }
}

class Subscriber
{
    //User usage requirements
    public static int localMts = 220;
    public static int stdMts = 150;
    //Constants
    public static final double localPerMin = 1.0;
    public static final double stdPerMin = 1.0;
    //Arraylist of instances of Plan
    private static ArrayList<Plan> instances = new ArrayList<Plan>();
    private static ArrayList<Plan> localInstances = new ArrayList<Plan>();
    private static ArrayList<Plan> stdInstances = new ArrayList<Plan>();

    public static void main(String args[])
    {
        //Creating Plans (value,local_tarriff_per_min,std_tariff_per_min,duration)
        //Plan A
        createPlan(30,0.8,0.9,30);
        createPlan(25,0.6,0.8,10);
        createPlan(40,0.75,0.75,20);
        createPlan(35,0.5,0.5,10);
        createPlan(50,0.2,0.2,10);
        // Plan B
        createPlan(22,0.4,0.0,30);
        createPlan(10,0.3,0.0,15);
        // Plan C
        createPlan(20,0.0,0.3,30);
        createPlan(10,0.0,0.2,30);
        createPlan(10,0.0,0.1,10);
        //Finding the best plan for given requirements
        //default pricing : No plans
        double min = (localMts * localPerMin + stdMts * stdPerMin)/30;
        String planName = "No plans Found";
        //Plan A
        for(Plan p : instances)
        {
            if(min>getValue(p))
            {
                min = getValue(p);
                planName = "Plan-"+p.value;
            }
            if(p.local==0)
            localInstances.add(p);
            if(p.std==0)
            stdInstances.add(p);;
        }
        //In case of Plan B and C
        for (Plan l : localInstances)
        {
            double localOnly = getValue(l,false);
            for (Plan s : stdInstances )
            {
                if(min>getValue(s,false)+localOnly)
                {
                    min = getValue(s,false)+localOnly;
                    planName = "Plan-"+s.value+" for local calls and Plan-"+l.value+" for STD";
                }
            }
        }
        System.out.println("Best Plan : " + planName);
    }
    //Creating Object and pushing to Arraylist
    public static void createPlan(int val, double loc, double std, int dur)
    {
        Plan p = new Plan(val,loc,std,dur);
        instances.add(p);
    }
    //overloaded method1
    public static double getValue(Plan p)
    {
        return p.getvalueDaily(localMts,stdMts,true);
    }
    //overloaded method2
    public static double getValue(Plan p,Boolean b)
    {
        return p.getvalueDaily(localMts,stdMts,b);
    }
}
