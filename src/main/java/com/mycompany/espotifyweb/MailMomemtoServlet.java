package com.mycompany.espotifyweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import servicios.DataAlbum;
import servicios.DataCliente;
import servicios.DataTema;
import org.json.JSONException;
import org.json.JSONObject;
import servicios.IPublicador;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import servicios.DataSus;
import servicios.DataUsuario;



@WebServlet(name = "MailMomemtoServlet", urlPatterns = {"/MailMomemtoServlet"})
public class MailMomemtoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Mail momento POST-----");
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();


        HttpSession session = request.getSession();
        String nickname = (String) session.getAttribute("nickname");

        String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        System.out.println("Body:" + body);
       
       
        String idSus = null;
        String pecio = "5 peso'";
        LocalDate fechaFin = LocalDate.now();
       
        
        //String susAct = null;
        
        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        try {
            JSONObject jsonObject = new JSONObject(body);
           

          
            idSus = jsonObject.getString("idSus");
            
            //susAct = jsonObject.getString("susAct");
            
            
        } catch (JSONException e) {
            out.println("{\"success\": false, \"error\": \"Error al procesar la solicitud.\"}");
            return; // Salir si hay un error al procesar el JSON
        }
        System.out.println("\n-----Pasado la construccion del json-----");
        
        DataUsuario user = publicador.retornarCliente(nickname);
        
        DataSus dtaSus = publicador.obtenerDataSuscripcionPorID(Integer.valueOf(idSus));
        
        String mail = user.getCorreo();
        
        String nombre = user.getNombre();
        
        String apellido = user.getApellido();
        
        LocalDate today = LocalDate.now();
        
        if("Anual".equals(dtaSus.getTipoSus())){
             fechaFin.plusYears(1);
        }else if("Mensual".equals(dtaSus.getTipoSus())){
            fechaFin.plusMonths(1);
        }else{
            fechaFin.plusWeeks(1);
        }
        
       
            ///////
          final String username = "andresferreira05@gmail.com";
        final String password = "fvly hipq jwom ppqw";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
       
        Session sessionG = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(sessionG);
            message.setFrom(new InternetAddress("andresferreira05@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("perepupengue@gmail.com")
            );
            message.setSubject("[Esporify Grupo-7] [" +String.valueOf(today) + "]");
            message.setText("Estimado/a ," + nombre +" "+ apellido + ". Su suscpripcion en Espotify del Equipo 7 a sido aprobada y se encuentra en " + dtaSus.getEstado() + "."
                    + "\n\n "
                     + "\n\n Detalles de la suscrpcion"
                     + "\n\n Tipo:"
                     + "\n\n" + dtaSus.getTipoSus() + ": " + pecio 
                     + "\n\n Fecha inicio:"
                     + "\n\n" + dtaSus.getUltiFechaHabi()
                     + "\n\n Fecha fin:"
                     +"\n\n" + String.valueOf(fechaFin)
                     +"\n\n"
                     +"\n\n Gracias por preferirnos,"
                     +"\n\n Saludos."
                     +"\n\n Espotify del grupo-7."
                    );
                    
                    
                    
                    
                    
                    

            Transport.send(message);

            System.out.println("Done");
            }catch (MessagingException e) {
                    e.printStackTrace();
                }
        
        
        ///////
        
        
    }

}
