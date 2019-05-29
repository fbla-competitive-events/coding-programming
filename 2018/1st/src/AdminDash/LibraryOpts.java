package AdminDash;

public enum LibraryOpts {
    Title,Author,Serial,Available,Comments,Ownerid,Hold,Outdate;

    private LibraryOpts(){

    }

    public String value(){ return name();}

    public static LibraryOpts fromvalue(String opt){return valueOf(opt);}
}
