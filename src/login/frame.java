/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;
//import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Properties;//properties are just key value store
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.PasswordAuthentication;

import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
//import java.awt.Toolkit;
import java.io.FileNotFoundException;
import static login.OTP.F;
import static login.OTP.Verify;
import static login.OTP.encode;
import static login.OTP.sendmail;

/**
 *
 * @author Dev Patel
 */

class OTP {
	public static String encode(String input) throws NoSuchAlgorithmException, IOException{
		//Converting input passed(here date and time) into SHA-256 encoding
		byte[] encodedMessage = MessageDigest.getInstance("SHA-256").digest(input.getBytes(StandardCharsets.UTF_8));
		//Encoded message is in byte array,so that byte needs to converted to hexadecimal for hash of length 64
		StringBuilder sb = new StringBuilder();
        for (byte b : encodedMessage) {
            sb.append(String.format("%02x", b));  
        }
        File file=new File("hashFile.txt");		//Reading the file
		if (file.exists() && file.isFile())		//Deleting the file if already exists
		  {
		  file.delete();
		  }
		file.createNewFile();
		//Writing the  hash Code into the file
		if(file.length()==0) {
			FileWriter writer=new FileWriter("hashFile.txt");
			writer.write(sb.toString());
			writer.close();
		}
        return sb.toString();
	}


	//In this method we are converting from 64 bit hash code to 4 digit OTP using ^ operator we are taking 8 consecutive character from front and 8 from back
	public static String F(String hashString) {
		String otp="";
		int i,j,front=0,back=0;
		for(i=0,j=hashString.length()-1;i<=j;i=i+8,j=j-8)
		{
			int iter = i+8;
			//take 8 consecutive elements from the left hand side
			while(iter>i)
			{
				front = front^(hashString.charAt(iter)+hashString.charAt(iter-1));
				iter=iter-2;
			}
			iter = j-8;
			//take 8 consecutive elements from the right hand side
			while(iter<j)
			{
				back = back^(hashString.charAt(iter)+hashString.charAt(iter+1));
				iter=iter+2;
			}
			//generate OTP by concatenating the string
			otp = otp + ((front^back)%10);
		}
		return otp;

	}
	
	
	//Function to verify the OTP with user entry
	public static boolean Verify(String hashCode, String otp) {
		return F(hashCode).equals(otp)?true:false;
	}


	//Method for sending OTP to recepient's email.
	public static void sendmail(String recepient, String otp) throws Exception {
		System.out.println("Preparing to send email");
		Properties properties =new Properties();
		properties.put("mail.smtp.auth","true");//authentication required here as using gmail smtp
		properties.put("mail.smtp.starttls.enable","true"); //tls encryption
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587"); //587 port for gmail
		
		String myAccountEmail = " ";//senders email address
		String password = " ";//app password set up for this in your google account
		String OTP_here= otp;
		
		//logging in using session
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(myAccountEmail,password);
			}
		});
		
		Message message = prepareMessage(session,myAccountEmail,recepient,OTP_here);
		
		
		
		Transport.send(message);//Using transport layer to send the message object.
		System.out.println("Email sent successfully");
		JOptionPane.showMessageDialog(null," OTP Sent to your email "," Login Form ",JOptionPane.NO_OPTION);
	}
	
	private static Message prepareMessage(Session session,String myAccountEmail,String recepient, String OTP_here) throws MessagingException {
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(myAccountEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
			message.setSubject("HERE'S YOUR OTP:");
			message.setText(OTP_here);//Passing OTP over here as body text.
			return message;
		} catch (AddressException ex) {
			Logger.getLogger(OTP.class.getName()).log(Level.SEVERE,null, ex);//Handling exception.
		} 
		return null;
	}
	
	public static void main(String[] args) throws InterruptedException {
    }
}

public class frame extends javax.swing.JFrame {

    /**
     * Creates new form frame
     */
    public frame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        popupMenu1 = new java.awt.PopupMenu();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jreset = new javax.swing.JButton();
        jname = new javax.swing.JTextField();
        jemail = new javax.swing.JTextField();
        jgenerate = new javax.swing.JButton();
        jverify = new javax.swing.JButton();
        jotpenter = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jexit = new javax.swing.JButton();

        jDialog1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog1.setTitle("Are You Sure ??");
        jDialog1.setBackground(new java.awt.Color(153, 153, 255));

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        popupMenu1.setLabel("popupMenu1");
        popupMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupMenu1ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 255));

        jLabel1.setBackground(new java.awt.Color(51, 255, 204));
        jLabel1.setFont(new java.awt.Font("Tw Cen MT", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 153));
        jLabel1.setText("OTP login using Cryptography");

        jSeparator1.setBackground(new java.awt.Color(51, 51, 255));

        jSeparator2.setBackground(new java.awt.Color(51, 0, 255));
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Enter your Name :");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Enter Your Email :");

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("Enter OTP :");

        jreset.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        jreset.setText("RESET");
        jreset.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jreset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jresetActionPerformed(evt);
            }
        });

        jname.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        jname.setForeground(new java.awt.Color(0, 102, 153));
        jname.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jnameActionPerformed(evt);
            }
        });

        jemail.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        jemail.setForeground(new java.awt.Color(0, 102, 153));
        jemail.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jemail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jemailActionPerformed(evt);
            }
        });

        jgenerate.setBackground(new java.awt.Color(204, 204, 255));
        jgenerate.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        jgenerate.setForeground(new java.awt.Color(102, 0, 102));
        jgenerate.setText("Generate OTP");
        jgenerate.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 153), 3, true));
        jgenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jgenerateActionPerformed(evt);
            }
        });

        jverify.setBackground(new java.awt.Color(204, 204, 255));
        jverify.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        jverify.setForeground(new java.awt.Color(102, 0, 102));
        jverify.setText("VERIFY");
        jverify.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 204), 4, true));
        jverify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jverifyActionPerformed(evt);
            }
        });

        jotpenter.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        jotpenter.setForeground(new java.awt.Color(255, 51, 0));
        jotpenter.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 4, true));
        jotpenter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jotpenterActionPerformed(evt);
            }
        });

        jLabel5.setBackground(java.awt.Color.lightGray);
        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 51, 51));
        jLabel5.setText("!! ATTENTION !!    ");

        jLabel6.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        jLabel6.setText("Please Enable Access For ");

        jLabel7.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        jLabel7.setText("Less Secure Apps From Manage Account");

        jLabel8.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        jLabel8.setText("Security -> Access for less srcure Apps -> ON");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(98, Short.MAX_VALUE))
        );

        jexit.setBackground(new java.awt.Color(255, 51, 0));
        jexit.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        jexit.setText("EXIT!");
        jexit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jexitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(224, 224, 224))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jreset, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(73, 73, 73)
                                .addComponent(jemail, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jotpenter, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jgenerate, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jexit, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jverify, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(41, 41, 41)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jname, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jreset, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jname, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jemail, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jgenerate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42)
                                .addComponent(jotpenter, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jverify, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                                .addComponent(jexit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22))))
                    .addComponent(jSeparator2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jotpenterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jotpenterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jotpenterActionPerformed

    private void jnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jnameActionPerformed

    private void jemailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jemailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jemailActionPerformed

    private void jexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jexitActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null," Are You Sure ? "," Login Form ",JOptionPane.QUESTION_MESSAGE);
        System.exit(0);
    }//GEN-LAST:event_jexitActionPerformed

    private void jresetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jresetActionPerformed
        // TODO add your handling code here:
        jname.setText(null);
        jemail.setText(null);
        jotpenter.setText(null);
    }//GEN-LAST:event_jresetActionPerformed

    private void jgenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jgenerateActionPerformed
        // TODO add your handling code here:
        String name = jname.getText();
        String recepient = jemail.getText();
        if (recepient.contains(".com") || recepient.contains("in") || recepient.contains("@")) {
           
			SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ssddMMyyyy");
			Date date=new Date();
			String stringDate=dateFormat.format(date);
                       
            try {
                sendmail(recepient,F(encode(stringDate)));
            } catch (Exception ex) {
                Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
            }
                        
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid Email-id", "OTP Login", JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_jgenerateActionPerformed

    private void jverifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jverifyActionPerformed
        // TODO add your handling code here:
        String otpenter = jotpenter.getText();        
        String H = null;
        try {
            H = new String(Files.readAllBytes(Paths.get("hashFile.txt")));
        } catch (IOException ex) {
            Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
        }
			int attempt = 3;
			//Giving user three attempts to make for correct OTP
			while(attempt>0)
			{
				String otp = otpenter;
				if(Verify(H, otp)==true) {
					
					PrintWriter writer = null;
                                    try {
                                        writer = new PrintWriter("hashFile.txt"); //Make the text file clear after the use
                                    } catch (FileNotFoundException ex) {
                                        Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
                                    }
					writer.print("");
					writer.close();
                                         Success jsucc = new Success();
                                         jsucc.setVisible(true);
					break;
			}
			else {
			JOptionPane.showMessageDialog(null," WRONG OTP  "," Login Form ",JOptionPane.INFORMATION_MESSAGE);
                        attempt--;  
                        break;     
			}
			
                        
			}
			if(attempt==0){
                 JOptionPane.showMessageDialog(null," You have exceeded the limit "," Login Form ",JOptionPane.INFORMATION_MESSAGE);
		  SystemExit();
                        }         
       
    }//GEN-LAST:event_jverifyActionPerformed

    private void popupMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupMenu1ActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null," You have exceeded the limit "," Login Form ",JOptionPane.INFORMATION_MESSAGE);
		 
    }//GEN-LAST:event_popupMenu1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jemail;
    private javax.swing.JButton jexit;
    private javax.swing.JButton jgenerate;
    private javax.swing.JTextField jname;
    private javax.swing.JTextField jotpenter;
    private javax.swing.JButton jreset;
    private javax.swing.JButton jverify;
    private java.awt.PopupMenu popupMenu1;
    // End of variables declaration//GEN-END:variables

    private void SystemExit() {
        WindowEvent winclose = new WindowEvent(this, WindowEvent.WINDOW_CLOSED);
    }
}
