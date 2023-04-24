package bmc.swe.carbonemissioncalculator;

public class Answer {
    private String answerQuestion;
    private String answerText;
    private String answerNumber;
    private String date;
    private String user;

    public Answer(String aQ, String aT, String aN, String date, String usr){
        this.answerQuestion = aQ;
        this.answerText = aT;
        this.date = date;
        this.user = usr;
        this.answerNumber = aN;
    }

    public String getAnswerText(){
        return this.answerText;
    }
    public String getAnswerQuestion(){return this.answerQuestion;}
    public String getDate(){ return this.date;}
    public String getAnswerNumber(){return this.answerNumber;}

    public String toString(){
        String str = "Question: " + this.answerQuestion + "\n Date: " + this.date + " response: " + this.answerText;
        return str;
    }
}
