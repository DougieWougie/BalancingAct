package eu.lynxworks.balancingact;

/*  Singleton class used to store any value that needs to be shared between activities and
    fragments.
 */

public class Global {
    private static Global globalInstance;

    private User user;

    private Global(){}

    public void setUser(User user)  { this.user = user; }
    public User getUser()           { return this.user; }

    public static synchronized Global getGlobalInstance(){
        if (globalInstance==null){
            globalInstance = new Global();
        }
        return globalInstance;
    }
}