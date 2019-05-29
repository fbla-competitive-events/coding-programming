package Login;

public enum Options {
    Admin, Student;

    private Options(){

    }

    public String value(){
        return name();
    }

    public static Options fromvalue(String opt){
        return valueOf(opt);
    }
}
