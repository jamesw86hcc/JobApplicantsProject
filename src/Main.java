import java.nio.file.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

//JavaDocs
/*
This assignment produces a list of emails given a list of applicants formatted in CSV.
The intent is to show mastery of reading in files and writing output to files.

Date: March 3rd, 2023
@author James Williams
 */

public class Main {
    public static void main(String[] args) {
        try {
            Scanner readFile = new Scanner(Paths.get("applicants.csv"));
            ArrayList<String[]> applicants = getFileData(readFile);
            Formatter output = new Formatter("emails.txt");

            for (String[] applicant : applicants){

                try {
                    String email = composeEmail(applicant);

                    output.format("%s\n\n", email);

                } catch (Exception e)
                {
                    System.err.println(e);
                }
            }

            output.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<String[]> getFileData(Scanner input){

        ArrayList<String[]> applicantList = new ArrayList<>();

        input.nextLine(); //discard first row
        while (input.hasNextLine())
        {
            String[] applicant = new String[7];

            applicant = input.nextLine().split(",");

            for (int i = 0; i<7; i++)
            {
                applicant[i] = applicant[i].trim();
            }

            applicantList.add(applicant);
        }

        return applicantList;
    }
    public static String composeEmail(String[] applicant) throws Exception{
        StringBuilder sb = new StringBuilder();

        //ID can be ignored
        //Title, i = 1
        //First name, i = 2
        //Last name, i = 3
        String name;

        if (applicant[1].isEmpty())
        {
            name = applicant[2] + " " + applicant[3];
        } else {
            name = applicant[1] + " " + applicant[3];
        }

        if (!name.matches("^[\\sa-zA-Z]*"))
        {
            System.err.println("Name must be alphanumeric. " + name + " is therefore unacceptable.");
            //return "Applicant #" + applicant[0] + ": Invalid Name";
            throw new RuntimeException("Applicant #" + applicant[0] + ": Invalid Name");
        }
        //email, i = 4
        if (!applicant[4].contains("@"))
        {
            System.err.println("Applicant #" + applicant[0] + " has an invalid email address: \'"+
                    applicant[4] + "\' .");
            throw new RuntimeException("Applicant #" + applicant[0] + ": Invalid Email");
        }

        String email = applicant[4];



        //Position, i = 5
        //Status, i = 6
        StringBuilder paragraph = new StringBuilder();

        paragraph.append("We thank you for your interest in our company. Having received your application for the position of ");
        paragraph.append(applicant[5]);
        paragraph.append(", ");

        if (applicant[6].equals("Hire"))
        {
            paragraph.append("we would like to offer you a position at our company. Please reach out to us at this email if there are any further questions about how to proceed.");
        } else if (applicant[6].equals("Interview"))
        {
            paragraph.append("we ask that you first sit for an interview with us. Attached should be a link for scheduling.");
        } else if (applicant[6].equals("Reject"))
        {
            paragraph.append("we unfortunately do not think you are a good match for our company. We wish you the best in your future endeavours.");
        } else
        {
            System.err.println("Unknown hiring status \'" + applicant[6] + "\'. Check the file.");
        }

        sb.append("--------------------------------------------------------------\n");
        sb.append("To: " + email + "\n");
        sb.append("From: humanresources@gizmogroupllc.com\n");
        sb.append("Subject: Your Gizmo Group Application\n");
        sb.append("--------------------------------------------------------------\n\n");
        sb.append("Dear " + name + ",\n\n");
        sb.append(paragraph);
        sb.append("\nThank you for your consideration.");

        return sb.toString();
    }
}