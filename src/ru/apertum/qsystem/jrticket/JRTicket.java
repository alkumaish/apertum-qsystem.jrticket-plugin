/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsystem.jrticket;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.view.JasperViewer;
import ru.apertum.qsystem.client.common.WelcomeParams;

/**
 *
 * @author Evgeniy Egorov
 */
public class JRTicket {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     * @throws net.sf.jasperreports.engine.JRException
     * @throws java.awt.print.PrinterException
     */
    public static void main(String[] args) throws FileNotFoundException, JRException, IOException, PrinterException {
        final File f_temp = new File("jrt_template/ticket.jasper");
        final InputStream inStr;
        inStr = new FileInputStream(f_temp);

        final HashMap<String, Object> parameters = new HashMap();
        /*
        
         customer, FWelcome.caption
        
        
         */
        parameters.put("caption", "caption");
        parameters.put("promo_text", "promoText");
        parameters.put("bottom_text", "bottomText");
        parameters.put("customer_number", 555);
        parameters.put("customer_prefix", "F");
        parameters.put("customer_priority", 1);
        parameters.put("customer_input_data", "Input_data");
        parameters.put("customer_id", 777L);
        parameters.put("service_description", "Service Description");
        parameters.put("service_input_caption", "Input caption");
        parameters.put("service_name", "Service Name");
        parameters.put("service_pre_info_text", "Pre Info Print Text");
        parameters.put("service_ticket_text", "Ticket Text");
        parameters.put("adv_customer_input_data", "Adv Input Data");
        parameters.put("adv_customer_id", 775533L);
        parameters.put("adv_customer_time", new Date());
        parameters.put("service_description", "Description");
        parameters.put("service_input_caption", "Input caption");
        parameters.put("service_name", "Service Name");
        parameters.put("service_pre_info_text", "Pre Info Print Text");
        parameters.put("service_ticket_text", "TicketText");
        parameters.put("service", null);
        parameters.put("customer", null);

        final JasperPrint jasperPrint = JasperFillManager.fillReport(inStr, parameters);//это используя уже откампиленный

        //Image image = JasperPrintManager.printPageToImage(jasperPrint, 0, 1.0f);
        //ImageIO.write((RenderedImage) image, "jpg", new File("img.jpg"));
        //PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();//вообще нет настроек, то печать как А4
        //  printRequestAttributeSet.add(OrientationRequested.LANDSCAPE);
        //  MediaSizeName mediaSizeName = MediaSize.findMedia(210, 148, MediaPrintableArea.MM);        printRequestAttributeSet.add(mediaSizeName);
        //    printRequestAttributeSet.add(MediaSizeName.ISO_A7);//Формат А5 большое поле слева - 67 мм.
        //printRequestAttributeSet.add(MediaSizeName.EXECUTIVE);
        //printRequestAttributeSet.add(new MediaPrintableArea( 0,   0,  50,  50,  MediaPrintableArea.MM));//210*210(210x100) поля минимум; Напечатал обрезанный до 50*50(50X50) талон в левом верхнем углу листа без полей.
        JRPrintServiceExporter exporter = new JRPrintServiceExporter();

/////////////////////////////////////////////        
/////////////////////////////////////////////        
/////////////////////////////////////////////        
        /* Create an array of PrintServices */
        /*
         final PrintService[] ps = PrintServiceLookup.lookupPrintServices(null, null);
         for (PrintService p : ps) {
         System.out.println("ps=" + p.getName());
         }
         PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
         if (!"".equals(WelcomeParams.getInstance().printerName)) {
         System.out.println("?=" + WelcomeParams.getInstance().printerName);
         for (PrintService p : ps) {
         if (p.getName().equalsIgnoreCase(WelcomeParams.getInstance().printerName)) {
         System.out.println("!=" + p.getName());
         printService = p;
         break;
         }
         }
         }
         System.out.println(">> " + printService.getName());
         exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printService);
         */
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE,
                WelcomeParams.getInstance().printService == null ? PrintServiceLookup.lookupDefaultPrintService() : WelcomeParams.getInstance().printService);
        //exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, PrintServiceLookup.lookupDefaultPrintService().getAttributes());
        ///////////////////////////////////////////////       
        ///////////////////////////////////////////////       
        ///////////////////////////////////////////////
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        // We set the selected service and pass it as a paramenter 
        // это экспериментаторское exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, WelcomeParams.getInstance().printAttributeSet);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
        exporter.exportReport();

        JasperViewer.viewReport(jasperPrint);

    }

}
