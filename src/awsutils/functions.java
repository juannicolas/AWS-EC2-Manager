/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package awsutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;
/**
 *
 * @author jdelacruz
 */
public class functions {
    
    private static final File path = new File("/usr/local/aws/bin/aws");
    
    public static final String status = new String("ec2 describe-instances --instance-ids");
    public static final String start = new String("ec2 start-instances --instance-ids");
    public static final String stop = new String("ec2 stop-instances --instance-ids");
    public static final String reboot = new String("ec2 reboot-instances --instance-ids");
    
    
    public static void main(String[] args) {
        //checkLibs();
    }
    
    public static void checkLibs() {
        boolean exists = path.exists();
        
        if (!(exists)) {
            JOptionPane.showMessageDialog(null,
                    "AWS libs not found. Please download libraries and try again.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    
    public static String exec(String cmd, String instance) {
        String runCmd = new String();
        if (cmd == status) {
            runCmd = path + " " 
                    + cmd 
                    + " " 
                    + instance 
                    + " " 
                    + "--query Reservations[0].Instances[0].{EC2ID:InstanceId,Name:Tags[0].Value,State:State.Name}";
        }else if (cmd == start) {
            runCmd = path + " " 
                    + cmd 
                    + " " 
                    + instance 
                    + " " 
                    + "--query StartingInstances[0].{EC2ID:InstanceId,CurrentState:CurrentState.Name,PreviousState:PreviousState.Name}";
        }else if (cmd == stop) {
            runCmd = path + " " 
                    + cmd 
                    + " " 
                    + instance 
                    + " " 
                    + "--query StoppingInstances[0].{EC2ID:InstanceId,CurrentState:CurrentState.Name,PreviousState:PreviousState.Name}";
        }else if (cmd == reboot) {
            runCmd = path + " " 
                    + cmd 
                    + " " 
                    + instance;
        }
        
        try {
            Process proc = Runtime.getRuntime().exec(runCmd);
            
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader stdErr = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            
            String result = "";
            String line;
            while (( line = stdIn.readLine()) != null ) {
                result += line + "\n";
                //System.out.println(line);
            }
            while (( line = stdErr.readLine()) != null ) {
                result += line + "\n";
                //System.err.println(line);
            }
            return result;
        } catch (IOException e) {
            return e.getMessage();
        }
    }
    
}
