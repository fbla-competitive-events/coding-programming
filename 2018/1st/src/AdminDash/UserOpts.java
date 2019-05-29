package AdminDash;

public enum UserOpts {
    All,Firstname,Lastname,UserID,Username,Password,Usertype,Homeroom,Comments,Fines,Books,Bookcount;

    private UserOpts(){

    }

    public String value(){ return name();}

    public static UserOpts fromvalue(String opt){return valueOf(opt);}
}
